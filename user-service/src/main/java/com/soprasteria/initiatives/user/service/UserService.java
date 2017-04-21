package com.soprasteria.initiatives.user.service;


import com.soprasteria.initiatives.commons.api.AuthenticatedUser;
import com.soprasteria.initiatives.user.domain.User;
import com.soprasteria.initiatives.user.repository.UserRepository;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * Service gérant les users
 *
 * @author rjansem
 * @author cegiraud
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    public UserService(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    /**
     * Effectue une souscription et envoie un email à l'utilisateur
     *
     * @param user : utilisateur a enregistrer
     */
    @Transactional
    public void souscrire(User user) {
        UUID uuid = UUID.randomUUID();
        user.setCodeTemporaire(uuid.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            user.setUsername(authenticatedUser.getUsername());
            user.setFirstName(authenticatedUser.getFirstName());
            user.setLastName(authenticatedUser.getLastName());
            userRepository.findByUsername(authenticatedUser.getUsername())
                    .ifPresent(u -> user.setId(u.getId()));
        }
        userRepository.save(user);
        sendMail(user);
    }

    private void sendMail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("test@soprasteria.com");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Votre code de validation");
            messageHelper.setText("Veuillez trouver ici votre code pour vous connecter : " + user.getCodeTemporaire());
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new RuntimeException("Erreur lors de l'envoi du mail", e);
        }

    }

    @Transactional(readOnly = true)
    public boolean exist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            return userRepository.existsByUsername(authenticatedUser.getUsername());
        }
        return false;
    }

    @Transactional
    public void activate(String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            User user = userRepository.findByUsernameAndCodeTemporaire(authenticatedUser.getUsername(), uuid.toString())
                    .orElseThrow(EntityNotFoundException::new);
            user.setCodeTemporaire(null);
            userRepository.save(user);
        }
    }
}
