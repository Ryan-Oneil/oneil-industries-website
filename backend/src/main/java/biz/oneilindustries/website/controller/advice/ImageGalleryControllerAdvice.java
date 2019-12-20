package biz.oneilindustries.website.controller.advice;

import biz.oneilindustries.website.exception.MediaException;
import biz.oneilindustries.website.exception.NotAuthorisedException;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ImageGalleryControllerAdvice {

    @ExceptionHandler(MediaException.class)
    public ResponseEntity handleException(MediaException error) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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

    //IOException
    @ExceptionHandler(IOException.class)
    public ResponseEntity handleException(IOException error) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error.getMessage());
    }
}
