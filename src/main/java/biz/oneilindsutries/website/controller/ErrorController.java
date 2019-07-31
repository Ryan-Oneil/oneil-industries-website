package biz.oneilindsutries.website.controller;

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
}
