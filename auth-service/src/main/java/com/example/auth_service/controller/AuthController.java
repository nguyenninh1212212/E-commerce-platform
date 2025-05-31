package com.example.auth_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.model.dto.req.AuthReq;
import com.example.auth_service.model.dto.req.TokenReq;
import com.example.auth_service.model.dto.res.AuthRes;
import com.example.auth_service.service.AuthServ;
import com.example.auth_service.validate.LoginValidationGroup;
import com.example.auth_service.validate.RegisterValidationGroup;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Hallo")

public class AuthController {
    private final AuthServ authServ;

    @Operation(summary = "Register")
    @PostMapping("/register")
    ResponseEntity<AuthRes> register(@RequestBody @Validated(RegisterValidationGroup.class) AuthReq req) {
        return ResponseEntity.ok(authServ.register(req));
    }

    @Operation()
    @PostMapping("/login")
    ResponseEntity<AuthRes> login(@RequestBody @Validated(LoginValidationGroup.class) AuthReq req) {
        return ResponseEntity.ok(authServ.login(req));
    }

    @Operation()
    @PostMapping("/refresh")
    ResponseEntity<String> TherefreshToken(@RequestBody TokenReq request) {
        return ResponseEntity.ok("accessToken : " + authServ.refreshToken(request.getToken()));
    }

}
