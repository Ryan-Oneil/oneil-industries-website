package biz.oneilindustries.website.exception;

public class TooManyLoginAttempts extends RuntimeException {

    public TooManyLoginAttempts(String message) {
        super(message);
    }
}
