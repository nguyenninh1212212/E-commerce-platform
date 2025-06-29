package com.example.cloud_service.model.dto.req;

import com.example.cloud_service.model.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaAvatarReq {
    private String userId;
    private Media media;
}
