package biz.oneilenterprise.website.controller;

import biz.oneilenterprise.website.service.UserService;
import biz.oneilenterprise.website.validation.LoginForm;
import javax.validation.Valid;
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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid LoginForm loginForm) {
        userService.registerUser(loginForm);

        return ResponseEntity.ok("A confirmation email has been sent. You will need to confirm it before you can login");
    }

    @PostMapping("/registrationConfirm/{token}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String token) {
        userService.confirmUserRegistration(token);

        return ResponseEntity.ok("Account has been successfully verified!");
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<String> sendResetToken(@PathVariable String email) {
        userService.generateResetToken(email);

        return ResponseEntity.ok("Password reset email has been sent");
    }

    @PostMapping("/newPassword/{token}")
    public ResponseEntity<String> setNewPassword(@PathVariable String token, @RequestBody String password) {
        userService.resetUserPassword(token, password);

        return ResponseEntity.ok("Password has been changed");
    }
}
