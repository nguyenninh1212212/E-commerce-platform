package com.example.variant_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationUtil {
    public String getSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("You must not use this method");
        }
        return authentication.getName();
    }
}
