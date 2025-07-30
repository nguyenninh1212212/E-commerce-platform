package com.example.vendor_service.excep;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class BaseException extends RuntimeException {
    private String message;
    private int code;

    public BaseException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
