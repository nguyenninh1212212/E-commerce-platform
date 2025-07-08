package com.example.auth_service.model.dto.req;

import com.example.auth_service.validate.*;

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

    @NotBlank(message = "Fullname cannot be blank", groups = { RegisterValidationGroup.class })
    @Size(max = 50, message = "Firstname must not exceed 50 characters")
    private String firstname;

    @Size(max = 50, message = "Lastname must not exceed 50 characters")
    private String lastname;

    @NotBlank(message = "GenderId cannot be blank", groups = { RegisterValidationGroup.class })
    private String genderId;

    private String born;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
