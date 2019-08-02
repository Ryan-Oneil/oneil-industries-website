package biz.oneilindustries.website.controller;

import biz.oneilindustries.website.entity.User;
import biz.oneilindustries.website.service.UserService;
import biz.oneilindustries.website.validation.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {

    //Add regex to check username is clean
    private final UserService userService;

    private static final String LOGIN_PAGE = "login";

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {

        model.addAttribute("LoginForm", new LoginForm());

        return LOGIN_PAGE;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("LoginForm") @Valid LoginForm loginForm, BindingResult bindingResult) {

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
        userService.registerUser(loginForm);

        return "redirect:/" + LOGIN_PAGE;
    }
}
