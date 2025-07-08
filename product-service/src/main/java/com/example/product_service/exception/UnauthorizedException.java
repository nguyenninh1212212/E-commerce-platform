package com.example.product_service.exception;

import org.springframework.util.StringUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(401, StringUtils.hasText(message) ? message : "Token invalid");
    }
}
