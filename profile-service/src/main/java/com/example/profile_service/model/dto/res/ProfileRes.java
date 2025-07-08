package com.example.profile_service.model.dto.res;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfileRes {
    private String firstname;
    private String lastname;
    private String born;
    private String bio;
    private String gender;
}
