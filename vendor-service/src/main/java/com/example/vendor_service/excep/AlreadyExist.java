package com.example.vendor_service.excep;

import org.springframework.http.HttpStatus;

public class AlreadyExist extends BaseException {
    public AlreadyExist(String message) {
        super(message + " already exist", HttpStatus.CONFLICT.value());
    }
}
