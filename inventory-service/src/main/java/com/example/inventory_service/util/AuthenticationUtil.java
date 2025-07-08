package com.example.inventory_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.inventory_service.excep.extd.UnauthorticationException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationUtil {
    public String getSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new UnauthorticationException("You must not allow to use this method");
        }
        return authentication.getName();
    }
}
