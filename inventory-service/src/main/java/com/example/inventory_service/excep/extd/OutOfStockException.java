package com.example.inventory_service.excep.extd;

import org.springframework.http.HttpStatus;

import com.example.inventory_service.excep.BaseException;

public class OutOfStockException extends BaseException {

    public OutOfStockException(String what) {
        super(what + "out of stock", HttpStatus.CONFLICT);
    }

}
