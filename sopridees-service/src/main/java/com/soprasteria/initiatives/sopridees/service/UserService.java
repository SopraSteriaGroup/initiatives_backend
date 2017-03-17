package com.soprasteria.initiatives.sopridees.service;

import com.soprasteria.initiatives.sopridees.domain.Utilisateur;
import com.soprasteria.initiatives.sopridees.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service gérant les users
 *
 * @author rjansem
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final JavaMailSender javaMailSender;

    @Autowired
    public UserService(UtilisateurRepository utilisateurRepository, JavaMailSender javaMailSender) {
        this.utilisateurRepository = utilisateurRepository;
        this.javaMailSender = javaMailSender;
    }

    /**
     * Effectue une souscription et envoie un email à l'utilisateur
     * @param utilisateur : utilisateur a enregistrer
     */
    public void souscrire(Utilisateur utilisateur) {
        UUID uuid = UUID.randomUUID();
        utilisateur.setCodeTemporaire(uuid.toString());
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
