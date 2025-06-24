package com.example.auth_service.excep;

public class AlreadyExistException extends BaseException {
    public AlreadyExistException(String message) {
        super(message, 409);
    }
}
