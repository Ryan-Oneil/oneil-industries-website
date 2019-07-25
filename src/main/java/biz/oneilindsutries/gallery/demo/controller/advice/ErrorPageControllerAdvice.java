package biz.oneilindsutries.gallery.demo.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ErrorPageControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public RedirectView handleNoHandlerFoundException(NoHandlerFoundException ex) {
        RedirectView redirect = new RedirectView("error");

        return redirect;
    }


}
