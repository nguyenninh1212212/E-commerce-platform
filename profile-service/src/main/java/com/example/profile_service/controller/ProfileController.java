package com.example.profile_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.profile_service.model.dto.req.ProfileReq;
import com.example.profile_service.model.dto.res.ApiRes;
import com.example.profile_service.model.dto.res.Message;
import com.example.profile_service.model.dto.res.ProfileRes;
import com.example.profile_service.service.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiRes<ProfileRes>> getProfile() {
        ProfileRes profile = profileService.getProfile();
        ApiRes<ProfileRes> response = ApiRes.<ProfileRes>builder()
                .status(HttpStatus.OK.value())
                .data(profile)
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Message> updateProfile(@RequestBody ProfileReq req) {
        profileService.updateProfile(req);
        Message message = Message.builder()
                .message("Update successfully!!")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(message);
    }
}
