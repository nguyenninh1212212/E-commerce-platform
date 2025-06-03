package com.example.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.product_service.model.dto.res.ApiRes;
import com.google.api.Http;

@ControllerAdvice
public class GlobalException {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiRes> handleException(Exception e) {
                ApiRes response = ApiRes.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }

        @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
        public ResponseEntity<ApiRes> handleBadRequest(IllegalArgumentException e) {
                ApiRes response = ApiRes.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}
