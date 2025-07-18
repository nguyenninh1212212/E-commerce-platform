package com.example.order_service.excep;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super("You do not have permission to perform this method.", 401);
    }
}
