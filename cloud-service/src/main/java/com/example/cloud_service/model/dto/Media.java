package com.example.cloud_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    private byte[] file;
    private boolean isThumbnail;
    private String resourceType;
    private String assetFolder;
}