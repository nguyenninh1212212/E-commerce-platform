package com.example.order_service.excep;

import org.springframework.http.HttpStatus;

public class NotAllowed extends BaseException {
    public NotAllowed(String message) {
        super(message + "is not allowed", HttpStatus.NOT_ACCEPTABLE.value());
    }
}
