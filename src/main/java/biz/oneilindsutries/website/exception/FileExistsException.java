package biz.oneilindsutries.website.exception;

public class FileExistsException extends RuntimeException {

    public FileExistsException(String error) {
        super(error);
    }
}
