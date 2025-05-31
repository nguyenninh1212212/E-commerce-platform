package com.example.auth_service.excep;

public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}
