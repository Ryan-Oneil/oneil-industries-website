package biz.oneilenterprise.website.controller.advice;

import biz.oneilenterprise.website.errors.ErrorDetails;
import biz.oneilenterprise.website.errors.FieldError;
import biz.oneilenterprise.website.errors.FormErrorDetails;
import biz.oneilenterprise.website.exception.LinkException;
import biz.oneilenterprise.website.exception.MediaApprovalException;
import biz.oneilenterprise.website.exception.MediaException;
import biz.oneilenterprise.website.exception.NotAuthorisedException;
import biz.oneilenterprise.website.exception.ResourceNotFoundException;
import biz.oneilenterprise.website.exception.TokenException;
import biz.oneilenterprise.website.exception.TooManyLoginAttempts;
import biz.oneilenterprise.website.exception.UserException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.theholywaffle.teamspeak3.api.exception.TS3QueryShutDownException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorPageControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        List<FieldError> errorList = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldError(error.getField(), error.getDefaultMessage(), (String) error.getRejectedValue()))
            .collect(Collectors.toList());
        FormErrorDetails errorDetails = new FormErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorList);

        return new ResponseEntity<>(errorDetails, headers, errorDetails.getStatus());
    }

    @ExceptionHandler(TooManyLoginAttempts.class)
    public ResponseEntity<ErrorDetails> tooManyLoginAttemptsException(TooManyLoginAttempts ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity tokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Teamspeak voiceservices bot is down");
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

    @ExceptionHandler(ParseException.class)
    public ResponseEntity parseException(ParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date entered, please follow yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    @ExceptionHandler(LinkException.class)
    public ResponseEntity handleLinkException(LinkException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException error) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity handleFileUploadException(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
