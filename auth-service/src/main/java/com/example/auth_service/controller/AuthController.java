package com.example.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.model.dto.req.AuthReq;
import com.example.auth_service.model.dto.req.OtpReq;
import com.example.auth_service.model.dto.req.TokenReq;
import com.example.auth_service.model.dto.res.AuthRes;
import com.example.auth_service.service.AuthServ;
import com.example.auth_service.validate.RegisterValidationGroup;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServ authServ;

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody @Validated(RegisterValidationGroup.class) AuthReq req) {
        return ResponseEntity.ok(authServ.register(req));
    }

    @PostMapping("/login/send-otp")
    ResponseEntity<String> login(@RequestBody AuthReq req) {
        authServ.login(req);
        return ResponseEntity.ok("Otp was sent");
    }

    @PostMapping("/login/confirm-otp")
    ResponseEntity<String> confirmOtp(@RequestBody OtpReq req) {
        authServ.verifyOtpAndLogin(req);
        return ResponseEntity.ok("Otp confirm");
    }

    @PostMapping("/refresh")
    ResponseEntity<String> TherefreshToken(@RequestBody TokenReq request) {
        return ResponseEntity.ok("accessToken : " + authServ.refreshToken(request.getToken()));
    }

}
