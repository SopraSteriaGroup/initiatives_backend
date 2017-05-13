package com.soprasteria.initiatives.auth.service;

import com.mongodb.MongoException;
import com.soprasteria.initiatives.auth.config.AuthenticatedUser;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

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

    public UserActivationService(UserRepository userRepository, AuthorityService authorityService, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.javaMailSender = javaMailSender;
    }

    /**
     * Effectue une souscription et envoie un email à l'utilisateur
     *
     * @param user : utilisateur a enregistrer
     */
    @Transactional
    public Mono<Void> souscrire(User user) {
        UUID uuid = UUID.randomUUID();
        user.setTemporaryCode(uuid.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            user.setUsername(authenticatedUser.getUsername());
            user.setFirstName(authenticatedUser.getFirstName());
            user.setLastName(authenticatedUser.getLastName());
            return userRepository.findByUsernameIgnoreCase(authenticatedUser.getUsername())
                    .map(u -> {
                        user.setId(u.getId());
                        return user;
                    })
                    .then(userRepository.save(user))
                    .then(sendMail(user));
        }
        return Mono.error(new IllegalStateException("Wrong principal type"));
    }

    private Mono<Void> sendMail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("test@soprasteria.com");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Votre code de validation");
            messageHelper.setText("Veuillez trouver ici votre code pour vous connecter : " + user.getTemporaryCode());
        };
        return Mono.fromRunnable(() -> javaMailSender.send(messagePreparator))
                .onErrorResume(e -> Mono.error(new IllegalStateException("Erreur lors de l'envoi du mail", e)));
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> exist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            return userRepository.findByUsernameIgnoreCase(authenticatedUser.getUsername())
                    .hasElement();
        }
        return Mono.error(new IllegalStateException("Wrong principal type"));
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
}
