package com.example.product_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.product_service.exception.UnauthorizedException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationUtil {
    public String getSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new UnauthorizedException("You must not use this method");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getSubject();
    }
}
