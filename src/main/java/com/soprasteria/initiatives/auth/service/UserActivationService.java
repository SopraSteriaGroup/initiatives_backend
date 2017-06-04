package com.soprasteria.initiatives.auth.service;

import com.mongodb.MongoException;
import com.soprasteria.initiatives.auth.config.AuthenticatedUser;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.repository.UserRepository;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;

/**
 * Service gérant les users
 *
 * @author rjansem
 * @author cegiraud
 */
@Service
public class UserActivationService {

    private final UserRepository userRepository;

    private final AuthorityService authorityService;

    private final JavaMailSender javaMailSender;

    private TokenService tokenService;

    public UserActivationService(UserRepository userRepository,
                                 AuthorityService authorityService,
                                 JavaMailSender javaMailSender,
                                 TokenService tokenService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.javaMailSender = javaMailSender;
        this.tokenService = tokenService;
    }

    /**
     * Effectue une souscription et envoie un email à l'utilisateur
     *
     * @param accessToken : accessToken
     * @param ssoProvider : ssoProvider
     * @param email       : l'email
     */
    @Transactional
    public Mono<OAuth2AccessToken> souscrire(String accessToken, SSOProvider ssoProvider, String email) {
        return tokenService.callSSOProvider(accessToken, ssoProvider)
                .map(user -> {
                    user.setUsername(email);
                    user.setTemporaryCode(RandomStringUtils.randomAlphanumeric(5));
                    return user;
                })
                .flatMap(user -> checkUsernameAvailable(user).then(Mono.just(user)))
                .flatMap(user -> userRepository.findByIdSSO(user.getIdSSO())
                        .map(user1 -> {
                            user.setId(user1.getId());
                            return user;
                        })
                        .defaultIfEmpty(user))
                .flatMap(userRepository::save)
                .flatMap(user -> sendMail(user).then(Mono.just(user)))
                .flatMap(user -> tokenService.authorize(user.getUsername()));
    }


    /**
     * Effectue la réccupération d'un user et demande un jeton oauth
     *
     * @param accessToken : accessToken
     * @param ssoProvider : ssoProvider
     */
    @Transactional
    public Mono<OAuth2AccessToken> authorize(String accessToken, SSOProvider ssoProvider) {
        Mono<User> errorMono = Mono.error(new MongoException("Unable to retrieve user"));
        return tokenService.callSSOProvider(accessToken, ssoProvider)
                .map(User::getIdSSO)
                .flatMap(userRepository::findByIdSSO)
                .switchIfEmpty(errorMono)
                .map(User::getUsername)
                .flatMap(tokenService::authorize);
    }

    private Mono<Void> sendMail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("test@soprasteria.com");
            messageHelper.setTo(user.getUsername());
            messageHelper.setSubject("Votre code de validation");
            messageHelper.setText("Veuillez trouver ici votre code pour vous connecter : " + user.getTemporaryCode());
        };
        return Mono.fromRunnable(() -> javaMailSender.send(messagePreparator))
                .onErrorResume(e -> Mono.error(new IllegalStateException("Erreur lors de l'envoi du mail", e)));
    }

    @Transactional
    public Mono<Void> activate(String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            String errorMsg = String.format("Unable to retrieve current user : %s", authenticatedUser.getUsername());
            return userRepository.findByUsernameAndTemporaryCode(authenticatedUser.getUsername(), uuid)
                    .flatMap(user -> {
                        user.setTemporaryCode(null);
                        return authorityService.findDefaultOrCreate().map(authority -> {
                            user.getAuthorities().add(authority);
                            return user;
                        });
                    })
                    .switchIfEmpty(Mono.error(new MongoException(errorMsg)))
                    .flatMap(userRepository::save)
                    .then();
        }
        return Mono.error(new IllegalStateException("Wrong principal type"));
    }

    private Mono<Void> checkUsernameAvailable(User user) {
        Mono<Void> error = Mono.error(new ValidationException(String.format("Username '%s' is already used", user.getUsername())));
        return userRepository.findByUsernameIgnoreCase(user.getUsername())
                .filter(userFound -> !userFound.getIdSSO().equals(user.getIdSSO()))
                .flatMap(x -> error);
    }
}
