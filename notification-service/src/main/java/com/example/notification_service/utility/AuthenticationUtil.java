package com.example.notification_service.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import lombok.experimental.UtilityClass;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@UtilityClass
public class AuthenticationUtil {
    public String UserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Unauthorized");
        }
        return authentication.getName();
    }
}
