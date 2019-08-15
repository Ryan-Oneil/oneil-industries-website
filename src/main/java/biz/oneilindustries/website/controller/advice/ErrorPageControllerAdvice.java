package biz.oneilindustries.website.controller.advice;

import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.exception.TooManyLoginAttempts;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ErrorPageControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public RedirectView handleNoHandlerFoundException(NoHandlerFoundException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("page",ex.getRequestURL());

        return new RedirectView("/pageNotFound");
    }

    @ExceptionHandler(TooManyLoginAttempts.class)
    public RedirectView tooManyLoginAttemptsException(TooManyLoginAttempts ex) {
        return new RedirectView("/");
    }

    @ExceptionHandler(TokenException.class)
    public RedirectView tokenException(TokenException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("error",ex.getMessage());

        return new RedirectView("/userError");
    }

    @ExceptionHandler(ServiceProfileException.class)
    public RedirectView serverProfileException(ServiceProfileException ex, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("error", ex.getMessage());

        return new RedirectView("/userError");
    }
}
