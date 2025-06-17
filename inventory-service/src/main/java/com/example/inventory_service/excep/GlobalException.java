package com.example.inventory_service.excep;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.inventory_service.model.dto.res.ErrorRes;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleException(Exception ex) {
        ErrorRes errorRes = ErrorRes.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorRes> baseException(BaseException ex) {
        ErrorRes errorRes = ErrorRes.builder()
                .message(ex.getMessage())
                .status(ex.getStatus())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }
}
