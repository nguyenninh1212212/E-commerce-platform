package com.example.cloud_service.model.dto.req;

import java.util.List;

import com.example.cloud_service.model.dto.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaProductReq {
    private String productId;
    private List<Media> media;
}
