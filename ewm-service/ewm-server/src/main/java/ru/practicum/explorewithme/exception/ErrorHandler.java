package ru.practicum.explorewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorMessage.setReason(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        StringBuilder message = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String fieldErrorMessage = error.getDefaultMessage();
            message.append("Field: ").append(fieldName).append(". Error: ").append(fieldErrorMessage);
        });
        errorMessage.setMessage(message.toString());
        log.info("VALIDATION ERRORS - {}!", message);
        return errorMessage;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorMessage handleNoSuchElementException(NoSuchElementException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.NOT_FOUND.toString());
        errorMessage.setReason(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorMessage.setMessage(ex.getMessage());
        log.info("404 {}", ex.getMessage());
        return errorMessage;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorMessage.setReason(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        StringBuilder message = new StringBuilder();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String fieldErrorMessage = violation.getMessage();
            message.append("Field: ").append(fieldName).append(". Error: ").append(fieldErrorMessage).append("; ");
        });
        errorMessage.setMessage(message.toString());
        log.info("CONSTRAINT VIOLATION ERRORS - {}!", message);
        return errorMessage;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorMessage handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(HttpStatus.CONFLICT.toString());
        errorMessage.setReason(HttpStatus.CONFLICT.getReasonPhrase());
        errorMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorMessage.setMessage(ex.getMessage());
        log.info("DATA INTEGRITY VIOLATION ERRORS - {}!", ex.getMessage());
        return errorMessage;
    }

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
