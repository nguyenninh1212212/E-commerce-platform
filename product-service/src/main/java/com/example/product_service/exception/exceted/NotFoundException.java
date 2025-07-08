package com.example.product_service.exception.exceted;

import com.example.product_service.exception.BaseException;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(404, message + "not found");
    }
}
