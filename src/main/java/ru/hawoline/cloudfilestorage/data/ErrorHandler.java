package ru.hawoline.cloudfilestorage.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hawoline.cloudfilestorage.data.entity.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMatchException() {
        ErrorResponse errorResponse = new ErrorResponse("User exists");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
