package com.example.email_service.model.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaiLReq {
    private String content;
    private String Subject;
}