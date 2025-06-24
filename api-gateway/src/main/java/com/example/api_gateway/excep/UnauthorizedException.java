package com.example.api_gateway.excep;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String e) {
        super(e, 401);
    }
}
