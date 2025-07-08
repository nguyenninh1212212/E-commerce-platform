package com.example.profile_service.excep;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.profile_service.model.dto.res.Message;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> GlobalHandleException(Exception ex) {
        Message message = Message.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(message);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Message> HandleBaseException(BaseException ex) {
        Message message = Message.builder()
                .message(ex.getMessage())
                .status(ex.getStatus())
                .build();
        return ResponseEntity.status(ex.getStatus()).body(message);
    }

}
