package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.eventlisteners.OnRegistrationCompleteEvent;
import biz.oneilindustries.website.service.EmailSender;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.LoginForm;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class LoginController {

    //Add regex to check username is clean
    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    private final EmailSender emailSender;

    @Autowired
    public LoginController(UserService userService, ApplicationEventPublisher eventPublisher, EmailSender emailSender) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.emailSender = emailSender;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@ModelAttribute("LoginForm") @Valid LoginForm loginForm, HttpServletRequest request) {

        User user = userService.getUser(loginForm.getName());

        if (user != null) {
            return ResponseEntity.badRequest().body("An account with this username already exists");
        }

        user = userService.getUserByEmail(loginForm.getEmail());

        if (user != null) {
            return ResponseEntity.badRequest().body("An account with this email already exists");
        }

        User newUser = userService.registerUser(loginForm);

        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
            (newUser, request.getLocale(), appUrl));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm{token}")
    public ResponseEntity confirmRegistration(@PathVariable("token") String token) {

        VerificationToken verificationToken = userService.getToken(token);

        User user = verificationToken.getUser();

        userService.deleteVerificationToken(verificationToken);

        user.setEnabled(1);
        userService.saveUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity sendResetToken(@RequestParam String email) {

        User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid Email");
        }
        String token = UUID.randomUUID().toString();

        userService.generateResetToken(user,token);
        emailSender.sendSimpleEmail(user.getEmail(),"Password Reset","Reset Password Link " + " http://oneilindustries.biz" + "/changePassword?token=" + token,"Oneil-Industries",null);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/newPassword")
    public ResponseEntity setNewPassword(@RequestParam("token") String token, @RequestParam String password) {

        User user = userService.getResetToken(token).getUsername();

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        userService.deletePasswordResetToken(userService.getResetToken(token));
        userService.changeUserPassword(user, password);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
