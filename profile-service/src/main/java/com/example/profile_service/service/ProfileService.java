package com.example.profile_service.service;

import com.example.profile_service.model.dto.req.ProfileReq;
import com.example.profile_service.model.dto.res.ProfileRes;

public interface ProfileService {
    void createProfile(ProfileReq req);

    void updateProfile(ProfileReq req);

    ProfileRes getProfile();

}