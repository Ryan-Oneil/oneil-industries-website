package biz.oneilindustries.website.exception;

public class FileExistsException extends RuntimeException {

    public FileExistsException(String error) {
        super(error);
    }
}
