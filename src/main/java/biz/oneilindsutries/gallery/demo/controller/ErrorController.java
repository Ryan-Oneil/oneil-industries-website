package biz.oneilindsutries.gallery.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping(name = "/error")
    public String showErrorPage() {
        return "error";
    }
}
