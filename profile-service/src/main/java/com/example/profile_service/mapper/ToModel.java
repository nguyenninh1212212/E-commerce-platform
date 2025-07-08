package com.example.profile_service.mapper;

import com.example.profile_service.model.dto.req.ProfileReq;
import com.example.profile_service.model.dto.res.ProfileRes;
import com.example.profile_service.model.entity.Profile;

import lombok.experimental.UtilityClass;
import profile.ProfileRequest;

@UtilityClass
public class ToModel {
    public ProfileReq ToProductReq(ProfileRequest request) {
        return ProfileReq.builder()
                .bio(request.getBio())
                .born(request.getBorn())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .genderId(request.getGenderId())
                .userId(request.getUserId())
                .build();
    }

    public ProfileRes toProfileRes(Profile profile) {
        return ProfileRes.builder()
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .bio(profile.getBio())
                .born(profile.getBorn())
                .gender(profile.getGenderId())
                .build();
    }

}
