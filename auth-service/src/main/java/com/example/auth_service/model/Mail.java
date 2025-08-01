package com.example.auth_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {
    private String email;
    private String content;
    private String Subject;
}