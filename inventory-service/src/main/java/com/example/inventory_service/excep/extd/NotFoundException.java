package com.example.inventory_service.excep.extd;

import org.springframework.http.HttpStatus;

import com.example.inventory_service.excep.BaseException;

public class NotFoundException extends BaseException {

    public NotFoundException(String what) {
        super(what + " not found.", HttpStatus.NOT_FOUND);
    }

}
