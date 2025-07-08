package com.example.profile_service.model.dto.req;

import com.example.profile_service.validate.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProfileReq {

    @NotBlank(message = "Fullname cannot be blank", groups = { create.class })
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstname;

    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    private String lastname;

    @NotBlank(message = "GenderId cannot be blank", groups = { create.class })
    private String genderId;

    @NotBlank(message = "UserId cannot be blank", groups = { create.class })
    private String userId;

    private String born;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
