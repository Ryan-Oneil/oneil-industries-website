package biz.oneilindsutries.website.exception;

public class NotAuthorisedException extends RuntimeException {

    public NotAuthorisedException(String error) {
        super(error);
    }
}
