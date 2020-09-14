package biz.oneilindustries.website.controller.advice;

import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.exception.MediaApprovalException;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import biz.oneilindustries.website.exception.ServiceProfileException;
import biz.oneilindustries.website.exception.TokenException;
import biz.oneilindustries.website.exception.TooManyLoginAttempts;
import biz.oneilindustries.website.exception.UserException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.theholywaffle.teamspeak3.api.exception.TS3QueryShutDownException;
import java.io.FileNotFoundException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorPageControllerAdvice {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getRequestURL() + " Not Found");
    }

    @ExceptionHandler(TooManyLoginAttempts.class)
    public ResponseEntity tooManyLoginAttemptsException(TooManyLoginAttempts ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Too many login attempts");
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity tokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ServiceProfileException.class)
    public ResponseEntity serverProfileException(ServiceProfileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity userException(UserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity fileUploadSizeException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You've reached your maximum storage capacity");
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity jwtException(JWTVerificationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TS3QueryShutDownException.class)
    public ResponseEntity ts3BotException() {
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Teamspeak services bot is down");
    }

    @ExceptionHandler(MediaException.class)
    public ResponseEntity mediaNotFound(MediaException error) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error.getMessage());
    }

    @ExceptionHandler(MediaApprovalException.class)
    public ResponseEntity mediaMissingPublicApproval(MediaApprovalException error) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleException(FileNotFoundException error) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error.getMessage());
    }

    @ExceptionHandler(NotAuthorisedException.class)
    public ResponseEntity handleException(NotAuthorisedException error) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
