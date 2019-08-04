package biz.oneilindustries.website.controller.advice;

import biz.oneilindustries.website.exception.TooManyLoginAttempts;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ErrorPageControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public RedirectView handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new RedirectView("/pageNotFound");
    }

    @ExceptionHandler(TooManyLoginAttempts.class)
    public RedirectView TooManyLoginAttemptsException(TooManyLoginAttempts ex) {
        return new RedirectView("/");
    }
}
