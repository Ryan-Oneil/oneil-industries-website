package biz.oneilenterprise.website.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailSender {

    @Value("${mail.enabled:true}")
    private Boolean sendEmail;
    private final JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("processExecutor")
    public void sendSimpleEmail(String recipientAddress, String subject, String message, String sender, String replyTo) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(StringUtils.trimAllWhitespace(sender));

        if (replyTo != null) {
            email.setReplyTo(replyTo);
        }
        if (sendEmail) {
            mailSender.send(email);
        }
    }
}
