package com.example.api_gateway.excep;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.api_gateway.model.dto.res.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(status.value(), "Field validation error: ");
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getCode());
        ErrorResponse error = new ErrorResponse(status.value(), ex.getMessage());
        return new ResponseEntity<>(error, status);
    }

}
