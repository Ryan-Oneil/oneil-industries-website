package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.entity.VerificationToken;
import biz.oneilindustries.website.eventlisteners.OnRegistrationCompleteEvent;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

@Controller
public class LoginController {

    //Add regex to check username is clean
    private final UserService userService;

    private static final String LOGIN_PAGE = "login";

    private final ApplicationEventPublisher eventPublisher;


    private final MessageSource messageSource;

    @Autowired
    public LoginController(UserService userService, ApplicationEventPublisher eventPublisher, @Qualifier("customMessageSource") MessageSource messageSource) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messageSource = messageSource;
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
    public ModelAndView confirmRegistration(WebRequest request, @RequestParam("token") String token, @PathVariable String tokens, RedirectAttributes redir) {

        ModelAndView redirect = new ModelAndView("redirect:/login");

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            redirect.setViewName("redirect:/error/userError");
            redir.addFlashAttribute("error", message);
            return redirect;
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
            redirect.setViewName("redirect:/error/userError");
            redir.addFlashAttribute("error", messageValue);
            return redirect;
        }

        user.setEnabled(1);
        userService.saveUser(user);

        return redirect;
    }
}
