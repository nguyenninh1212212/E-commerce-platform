package com.example.inventory_service.excep;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    // Getter
}