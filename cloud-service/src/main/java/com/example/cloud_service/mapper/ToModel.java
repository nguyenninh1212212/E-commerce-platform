package com.example.cloud_service.mapper;

import com.example.cloud_service.model.dto.Media;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ToModel {
    public Media toMedia(cloud.Media media) {
        return Media.builder()
                .assetFolder(media.getAssetFolder())
                .resourceType(media.getResourceType())
                .isThumbnail(media.getIsThumbnail())
                .file(media.getMedia().toByteArray())
                .build();
    }
}
