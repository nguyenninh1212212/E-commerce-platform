package com.example.product_service.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(403, message);
    }
}
