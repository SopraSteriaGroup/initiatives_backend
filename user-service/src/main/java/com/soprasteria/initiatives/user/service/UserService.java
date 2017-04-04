package com.soprasteria.initiatives.user.service;


import com.soprasteria.initiatives.commons.api.AuthenticatedUser;
import com.soprasteria.initiatives.user.domain.Utilisateur;
import com.soprasteria.initiatives.user.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service gérant les users
 *
 * @author rjansem
 * @author cegiraud
 */
@Service
public class UserService {

    private final UtilisateurRepository utilisateurRepository;

    private final JavaMailSender javaMailSender;

    public UserService(UtilisateurRepository utilisateurRepository, JavaMailSender javaMailSender) {
        this.utilisateurRepository = utilisateurRepository;
        this.javaMailSender = javaMailSender;
    }

    /**
     * Effectue une souscription et envoie un email à l'utilisateur
     *
     * @param utilisateur : utilisateur a enregistrer
     */
    public void souscrire(Utilisateur utilisateur) {
        UUID uuid = UUID.randomUUID();
        utilisateur.setCodeTemporaire(uuid.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
            utilisateur.setUsername(user.getUsername());
            utilisateur.setFirstName(user.getFirstName());
            utilisateur.setLastName(user.getLastName());
        }
        utilisateurRepository.save(utilisateur);
        sendMail(utilisateur);
    }

    private void sendMail(Utilisateur utilisateur) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("test@soprasteria.com");
            messageHelper.setTo(utilisateur.getEmail());
            messageHelper.setSubject("Votre code de validation");
            messageHelper.setText("Veuillez trouver ici votre code pour vous connecter : " + utilisateur.getCodeTemporaire());
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new RuntimeException("Erreur lors de l'envoi du mail", e);
        }

    }

}
