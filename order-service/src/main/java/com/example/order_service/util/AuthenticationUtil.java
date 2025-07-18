package com.example.order_service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.order_service.excep.UnauthorizedException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class AuthenticationUtil {
    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentiaction : ", authentication);
        if (authentication == null) {
            throw new UnauthorizedException();
        }
        return authentication.getName();
    }
}
