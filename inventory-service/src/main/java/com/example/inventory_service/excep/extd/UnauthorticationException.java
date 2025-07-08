package com.example.inventory_service.excep.extd;

import org.springframework.http.HttpStatus;

import com.example.inventory_service.excep.BaseException;

public class UnauthorticationException extends BaseException {
    public UnauthorticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
