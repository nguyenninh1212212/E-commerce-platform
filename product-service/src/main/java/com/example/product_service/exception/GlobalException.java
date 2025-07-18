package com.example.product_service.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
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

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiRes<String>> handleAccessDenied(AccessDeniedException e) {
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(response);
        }

        @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
        public ResponseEntity<ApiRes<String>> handleBadRequest(RuntimeException e) {
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .data(e.getMessage())
                                .build();
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiRes<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
                String errorMessage = ex.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.joining("; "));
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .data(errorMessage)
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler({ org.springframework.security.authentication.BadCredentialsException.class,
                        org.springframework.security.core.AuthenticationException.class })
        public ResponseEntity<ApiRes<String>> handleUnauthorizedException(Exception ex) {
                ApiRes<String> response = ApiRes.<String>builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .data(ex.getMessage()) // hoặc ex.getMessage()
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

}
