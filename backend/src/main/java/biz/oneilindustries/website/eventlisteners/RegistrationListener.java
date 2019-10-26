package biz.oneilindustries.website.eventlisteners;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.EmailSender;
import biz.oneilindustries.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String RECIEVER_EMAIL = "blackielifegfgaming@gmail.com";

    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("customMessageSource")
    private MessageSource messages;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        emailSender.sendSimpleEmail(recipientAddress,subject,message + " http://oneilindustries.biz" + confirmationUrl,"Oneil_Industries", null);
        emailSender.sendSimpleEmail(RECIEVER_EMAIL, "New User","A new user has registered to Oneil Industries","Oneil_Industries", null);
    }
}