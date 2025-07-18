package com.example.order_service.excep;

public class AlreadyExist extends BaseException {
    public AlreadyExist(String message) {
        super(message, 409);
    }
}
