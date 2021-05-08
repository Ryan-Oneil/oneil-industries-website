package biz.oneilenterprise.website.eventlisteners;

import biz.oneilenterprise.website.entity.User;
import biz.oneilenterprise.website.service.EmailSender;
import biz.oneilenterprise.website.service.UserService;
import java.util.UUID;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String RECEIVER_EMAIL = "blackielifegfgaming@gmail.com";

    private final UserService service;
    private final EmailSender emailSender;

    public RegistrationListener(UserService service, EmailSender emailSender) {
        this.service = service;
        this.emailSender = emailSender;
    }

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
                = event.getAppUrl() + "/confirmEmail/" + token;

        String message = "You have successfully registered your account. Note that an admin will still need to manually approve your account, Please confirm your email with this link to complete registration";

        emailSender.sendSimpleEmail(recipientAddress,subject,message + " " + confirmationUrl,"noreply@oneilenterprise.com", null);
        emailSender.sendSimpleEmail(RECEIVER_EMAIL, "New User",user.getUsername() + " has registered to Oneil Industries","noreply@oneilenterprise.com", null);
    }
}