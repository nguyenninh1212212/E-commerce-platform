package com.example.order_service.excep;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.order_service.model.dto.res.Message;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Message error = new Message(status.value(), ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Message> handleAccessDeniedException(AccessDeniedException ex) {
        HttpStatus status = HttpStatus.ACCEPTED;
        Message error = new Message(status.value(), ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Message> handleBaseException(BaseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getCode());
        Message error = new Message(status.value(), ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

}
