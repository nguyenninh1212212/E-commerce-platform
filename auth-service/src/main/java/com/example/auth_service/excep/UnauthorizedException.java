package com.example.auth_service.excep;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String e) {
        super(e, 401);
    }
}
