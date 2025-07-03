package com.example.cloud_service.model.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "mediaProduct")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaProduct {
    @Id
    private String id;
    private String productId;
    private String imgName;
    private int position;
    private Instant createdAt;
}
