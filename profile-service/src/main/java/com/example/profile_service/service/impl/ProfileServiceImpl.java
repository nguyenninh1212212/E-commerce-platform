package com.example.profile_service.service.impl;

import java.time.Instant;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.profile_service.excep.Exceptend.AlreadyExist;
import com.example.profile_service.excep.Exceptend.NotFound;
import com.example.profile_service.excep.Exceptend.Unauthorize;
import com.example.profile_service.mapper.ToModel;
import com.example.profile_service.model.dto.req.ProfileReq;
import com.example.profile_service.model.dto.res.ProfileRes;
import com.example.profile_service.model.entity.Profile;
import com.example.profile_service.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final MongoTemplate mongoTemplate;

    private static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof Jwt)) {
            throw new Unauthorize();
        }
        return authentication.getName();
    }

    @Override
    public void createProfile(ProfileReq req) {
        Profile profile = mongoTemplate.findOne(new Query(Criteria.where("userId").is(req.getUserId())), Profile.class);
        if (profile != null) {
            throw new AlreadyExist("Profile");
        }
        profile = Profile.builder()
                .bio(req.getBio())
                .born(req.getBorn())
                .createdAt(Instant.now())
                .genderId(req.getGenderId())
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .userId(req.getUserId())
                .build();
        mongoTemplate.save(profile);
    }

    @Override
    public void updateProfile(ProfileReq req) {

        Profile profile = mongoTemplate.findOne(new Query(Criteria.where("userId").is(req.getUserId())), Profile.class);
        if (profile == null) {
            throw new NotFound("Profile");
        }

        Update update = new Update()
                .set("bio", req.getBio())
                .set("born", req.getBorn())
                .set("genderId", req.getGenderId())
                .set("updateAt", Instant.now())
                .set("firstname", req.getFirstname())
                .set("lastname", req.getLastname());
        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(getUserId())), update, Profile.class);
    }

    @Override
    public ProfileRes getProfile() {
        Profile profile = mongoTemplate.findOne(new Query(Criteria.where("userId").is(getUserId())), Profile.class);
        if (profile == null) {
            throw new NotFound("Profile");
        }
        ProfileRes profileRes = ToModel.toProfileRes(profile);
        return profileRes;

    }

}
