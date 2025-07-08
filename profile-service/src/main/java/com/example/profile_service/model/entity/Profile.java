package com.example.profile_service.model.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Profile {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String userId;
    private String born;
    private String bio;
    private Instant createdAt;
    private Instant updateAt;
    private String genderId;
}
