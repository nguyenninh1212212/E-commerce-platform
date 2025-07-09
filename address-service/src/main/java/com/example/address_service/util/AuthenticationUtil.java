package com.example.address_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.address_service.excep.UnauthorizedException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationUtil {
    public String getSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof Jwt)) {
            throw new UnauthorizedException();
        }
        return authentication.getName();
    }
}
