package biz.oneilenterprise.website.errors;

import java.util.List;
import org.springframework.http.HttpStatus;

public class FormErrorDetails {

    private final HttpStatus status;
    private final String message;
    private final List<FieldError> errors;

    public FormErrorDetails(HttpStatus status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
