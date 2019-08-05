package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.eventlisteners.OnRegistrationCompleteEvent;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.service.EmailSender;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class LoginController {

    //Add regex to check username is clean
    private final UserService userService;

    private static final String LOGIN_PAGE = "login";

    private final ApplicationEventPublisher eventPublisher;

    private final MessageSource messageSource;

    private final EmailSender emailSender;

    @Autowired
    public LoginController(UserService userService, ApplicationEventPublisher eventPublisher, @Qualifier("customMessageSource") MessageSource messageSource, EmailSender emailSender) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
        this.emailSender = emailSender;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {

        model.addAttribute("LoginForm", new LoginForm());

        return LOGIN_PAGE;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("LoginForm") @Valid LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {

        User user = userService.getUser(loginForm.getName());

        if (user != null) {
            bindingResult.rejectValue("name", "name.exists","An account with this username already exists");
        }

        user = userService.getUserByEmail(loginForm.getEmail());

        if (user != null) {
            bindingResult.rejectValue("email", "email.exists","An account with this email already exists");
        }

        if (bindingResult.hasErrors()) {
            return LOGIN_PAGE;
        }

        User newUser = userService.registerUser(loginForm);

        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (newUser, request.getLocale(), appUrl));

        return "redirect:/" + LOGIN_PAGE;
    }

    @GetMapping("/registrationConfirm{tokens}")
    public ModelAndView confirmRegistration(@RequestParam("token") String token, @PathVariable String tokens, RedirectAttributes redir) {

        ModelAndView redirect = new ModelAndView("redirect:/login");

        VerificationToken verificationToken = userService.getToken(token);

        User user = verificationToken.getUser();

        user.setEnabled(1);
        userService.saveUser(user);

        return redirect;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotpassword";
    }

    @PostMapping("/forgotPassword")
    public ModelAndView sendResetToken(@RequestParam String email, HttpServletRequest request) {

        User user = userService.getUserByEmail(email);

        ModelAndView modelAndView = new ModelAndView("/forgotpassword");

        if (user == null) {
            modelAndView.addObject("message","Invalid Email");
            return modelAndView;
        }

        String token = UUID.randomUUID().toString();

        userService.generateResetToken(user,token);

        emailSender.sendSimpleEmail(user.getEmail(),"Password Reset","Reset Password Link " + request.getLocalName() + "/changePassword?token=" + token,"Oneil-Industries");

        modelAndView.addObject("message","Successfully sent reset email");

        return modelAndView;
    }

    @GetMapping("/changePassword")
    public String confirmNewPassword(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);

        return "changePassword";
    }

    @PostMapping("/newPassword")
    public String setNewPassword(@RequestParam("token") String token, @RequestParam String password) {

        User user = userService.getResetToken(token).getUsername();

        if (user == null) {
            throw new TokenException("Token is not associated with any user");
        }

        userService.changeUserPassword(user, password);

        return "redirect:/login";
    }
}
