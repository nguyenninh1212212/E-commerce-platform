package com.example.auth_service.model.dto.req;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAuthReq {
    private String fullname;
    private Date birth;
    private String email;
    private MultipartFile avatar;
}
