package com.example.auth_service.repo;

import org.springframework.data.jpa.domain.Specification;

import com.example.auth_service.model.entity.Auth;

public class AuthSpeci {
    public static Specification<Auth> hasUsername(String username) {
        return (arg0, arg1, arg2) -> {
            if (username == null) {
                throw new IllegalArgumentException("Username cannot be null");
            }
            return arg2.equal(arg0.get("username"), username);

        };
    }

    public static Specification<Auth> hasEmail(String email) {
        return (arg0, arg1, arg2) -> {
            if (email == null) {
                return null;
            }
            return arg2.equal(arg0.get("email"), email);

        };
    }

    public static Specification<Auth> hasGoogleId(String google) {
        return (arg0, arg1, arg2) -> {
            if (google != null) {
                return arg2.equal(arg0.get("googleId"), google);
            }
            return null;
        };
    }

    public static Specification<Auth> hasRefreshToken(String token) {
        return (arg0, arg1, arg2) -> {
            if (token != null) {
                return arg2.equal(arg0.get("refreshToken"), token);
            }
            return null;
        };
    }
}
