package com.example.auth_service.excep;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String e) {
        super(e);
    }
}
