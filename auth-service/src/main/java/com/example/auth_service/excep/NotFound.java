package com.example.auth_service.excep;

public class NotFound extends BaseException {
    public NotFound(String message) {
        super(message, 404);
    }
}
