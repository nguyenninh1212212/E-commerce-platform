package com.example.order_service.excep;

public class NotFound extends BaseException {
    public NotFound(String message) {
        super(message, 404);
    }
}
