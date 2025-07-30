package com.example.vendor_service.excep;

public class NotFound extends BaseException {
    public NotFound(String message) {
        super(message + "is not exist", 404);
    }
}
