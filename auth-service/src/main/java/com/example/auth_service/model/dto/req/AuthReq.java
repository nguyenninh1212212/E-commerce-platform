package com.example.auth_service.model.dto.req;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import com.example.auth_service.validate.ForgorPasswordValidate;
import com.example.auth_service.validate.LoginValidationGroup;
import com.example.auth_service.validate.RegisterValidationGroup;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthReq {
    @NotBlank(message = "Username cannot blank", groups = { LoginValidationGroup.class, RegisterValidationGroup.class })
    private String username;
    @NotBlank(message = "Password cannot blank", groups = { LoginValidationGroup.class, RegisterValidationGroup.class,
            ForgorPasswordValidate.class })
    private String password;
    @NotNull(message = "Profile cannot null", groups = { RegisterValidationGroup.class })
    private ProfileReq profile;
    private String email;

}
