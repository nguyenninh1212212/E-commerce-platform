package com.example.inventory_service.excep.extd;

import org.springframework.http.HttpStatus;

import com.example.inventory_service.excep.BaseException;

public class AlreadyExistsException extends BaseException {

    public AlreadyExistsException(String what) {
        super(what + " already exists.", HttpStatus.CONFLICT);
    }

}
