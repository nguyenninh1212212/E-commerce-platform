package com.example.email_service.excep;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.email_service.model.dto.res.Message;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleGenericException(Exception e) {
        Message message = new Message(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Có thể thêm nhiều @ExceptionHandler khác nếu muốn
}
