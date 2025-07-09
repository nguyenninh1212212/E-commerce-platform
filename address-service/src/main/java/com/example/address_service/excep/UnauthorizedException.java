package com.example.address_service.excep;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String e) {
        super(e, 401);
    }
}
