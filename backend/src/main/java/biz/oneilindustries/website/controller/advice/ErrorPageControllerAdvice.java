package biz.oneilindustries.website.controller.advice;

import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.exception.TooManyLoginAttempts;
import biz.oneilindustries.website.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorPageControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getRequestURL() + " Not Found");
    }

    @ExceptionHandler(TooManyLoginAttempts.class)
    public ResponseEntity tooManyLoginAttemptsException(TooManyLoginAttempts ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Too many login attempts");
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity tokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceProfileException.class)
    public ResponseEntity serverProfileException(ServiceProfileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity userException(UserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
