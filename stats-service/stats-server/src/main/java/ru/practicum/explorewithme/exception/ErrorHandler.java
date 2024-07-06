package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorMessage handleValidationException(ValidationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorMessage.setReason(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorMessage.setMessage(ex.getMessage());
        return errorMessage;
    }
}
