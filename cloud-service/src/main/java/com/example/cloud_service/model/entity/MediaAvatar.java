package com.example.cloud_service.model.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "mediaAvatar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaAvatar {
    @Id
    private String id;
    private String userId;
    private String imgName;
    private boolean isThumbnail;
    private Instant createdAt;
}
