package com.example.profile_service.model.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Gender")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Gender {
    @Id
    private String id;
    private String name;
    private Instant updateAt;
}
