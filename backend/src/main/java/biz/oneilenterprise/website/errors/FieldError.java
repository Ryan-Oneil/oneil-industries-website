package biz.oneilenterprise.website.errors;

public class FieldError {

    private final String property;
    private final String message;
    private final String rejectedValue;

    public FieldError(String property, String message, String rejectedValue) {
        this.property = property;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getProperty() {
        return property;
    }

    public String getMessage() {
        return message;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
