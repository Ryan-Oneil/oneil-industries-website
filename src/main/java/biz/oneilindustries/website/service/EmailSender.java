package biz.oneilindustries.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String recipientAddress, String subject, String message, String sender, String replyTo) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(sender);

        if (replyTo != null) {
            email.setReplyTo(replyTo);
        }
        mailSender.send(email);
    }
}
