package fr.sd.reservcreneaux.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class MailService {

    @Inject
    Mailer mailer;

    public void sendEmail(String to, String subject, String body) {
        try {
            mailer.send(Mail.withHtml(to, subject, body));
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

    }
}