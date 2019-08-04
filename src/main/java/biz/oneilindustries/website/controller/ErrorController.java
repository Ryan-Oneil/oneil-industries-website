package biz.oneilindustries.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/pageNotFound")
    public String showErrorPage() {
        return "error/notfound";
    }

    @GetMapping("/error/unauthorised")
    public String showUnauthorisedPage() {
        return "error/unauthorised";
    }

    @GetMapping("/error/userError")
    public String showUserErrorPage() {
        return "error/userError";
    }
}
