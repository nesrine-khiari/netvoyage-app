package tn.fst.spring.netvoyage.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl; // e.g., http://your-platform.com

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInvitationEmail(String toEmail, String token, String fromEmail, String nomInvite) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Invitation à rejoindre notre plateforme");
            message.setText(
                    "Bonjour " + nomInvite + ",\n\n" +
                            "Vous avez été invité à rejoindre notre plateforme.\n" +
                            "Cliquez sur le lien suivant pour accepter l'invitation :\n" +
                            baseUrl + "/api/invitations/accept?token=" + token + "\n\n" +
                            "Cordialement,\nL'équipe de la plateforme"
            );

            mailSender.send(message);
            log.info("Email envoyé à: {}", toEmail);
        } catch (Exception e) {
            log.error("Échec d'envoi d'email à: {}", toEmail, e);
            throw new RuntimeException("Échec d'envoi email", e);
        }
    }
}
