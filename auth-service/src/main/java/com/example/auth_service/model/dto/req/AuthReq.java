package com.example.auth_service.model.dto.req;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import com.example.auth_service.validate.ForgorPasswordValidate;
import com.example.auth_service.validate.LoginValidationGroup;
import com.example.auth_service.validate.RegisterValidationGroup;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Fullname cannot blank", groups = { RegisterValidationGroup.class
    })
    private String fullname;
    @NotBlank(message = "Date cannot blank", groups = { RegisterValidationGroup.class
    })
    private Date birth;
    @NotBlank(message = "Email cannot blank", groups = { RegisterValidationGroup.class
    })
    private String email;

}
