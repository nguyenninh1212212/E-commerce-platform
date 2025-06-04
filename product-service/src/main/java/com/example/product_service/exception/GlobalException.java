package com.example.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.product_service.model.dto.res.ApiRes;

@ControllerAdvice
public class GlobalException {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiRes<String>> handleException(Exception e) {
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }

        @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
        public ResponseEntity<ApiRes<String>> handleBadRequest(IllegalArgumentException e) {
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}
