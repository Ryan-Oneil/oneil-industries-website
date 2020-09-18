package biz.oneilindustries.website.controller;

import static biz.oneilindustries.AppConfig.FRONT_END_URL;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.eventlisteners.OnRegistrationCompleteEvent;
import biz.oneilindustries.website.service.EmailSender;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.LoginForm;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailSender emailSender;

    @Autowired
    public AuthController(UserService userService, ApplicationEventPublisher eventPublisher, EmailSender emailSender) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.emailSender = emailSender;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid LoginForm loginForm, HttpServletRequest request) {
        User newUser = userService.registerUser(loginForm);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
            (newUser, request.getLocale(), FRONT_END_URL));

        return ResponseEntity.ok("A confirmation email has been sent. You will need to confirm it before you can login");
    }

    @PostMapping("/registrationConfirm/{token}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String token) {
        userService.confirmUserRegistration(token);

        return ResponseEntity.ok("Account has been successfully verified!");
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<String> sendResetToken(@PathVariable String email) {
        User user = userService.getUserByEmail(email);

        //Will generate a new token or send a previously generated one if exists and not expired
        String token = userService.generateResetToken(user);
        emailSender.sendSimpleEmail(user.getEmail(),"Password Reset","Reset Password Link " + FRONT_END_URL + "/changePassword/" + token,"Oneil-Industries",null);

        return ResponseEntity.ok("Password reset email has been sent");
    }

    @PostMapping("/newPassword/{token}")
    public ResponseEntity<String> setNewPassword(@PathVariable String token, @RequestBody String password) {
        userService.resetUserPassword(token, password);

        return ResponseEntity.ok("Password has been changed");
    }
}
