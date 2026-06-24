package br.com.riverfy.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotification implements Notification {

    private final Logger logger = LoggerFactory.getLogger(EmailNotification.class);

    private final JavaMailSender mailSender;

    public EmailNotification(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String destination, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("riverfy.app@gmail.com");
        email.setTo(destination);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
        logger.info("E-mail enviado com sucesso para: " + destination);
    }
}
