package com.example.auth_service.mapper;

import com.example.auth_service.model.Mail;
import com.example.auth_service.model.dto.req.ProfileReq;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ToProto {
    public profile.ProfileRequest toReq(String userId, ProfileReq profileReq) {
        return profile.ProfileRequest.newBuilder()
                .setUserId(userId)
                .setBio(profileReq.getBio())
                .setBorn(profileReq.getBorn())
                .setFirstname(profileReq.getFirstname())
                .setLastname(profileReq.getLastname())
                .setGenderId(profileReq.getGenderId())
                .build();
    }

    public mail.MailRequest toReq(Mail evMail) {
        return mail.MailRequest.newBuilder()
                .setContent(evMail.getContent())
                .setTo(evMail.getEmail())
                .setSubject(evMail.getSubject())
                .build();
    }
}
