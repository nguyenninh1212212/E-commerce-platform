package com.example.cloud_service.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cloud_service.config.CloudConfig;
import com.example.cloud_service.model.dto.Media;
import com.example.cloud_service.model.dto.req.MediaAvatarReq;
import com.example.cloud_service.model.dto.req.MediaProductReq;
import com.example.cloud_service.model.entity.MediaAvatar;
import com.example.cloud_service.model.entity.MediaProduct;
import com.example.cloud_service.util.handleUrl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CloudServ {
    private final MongoTemplate mongoTemplate;
    private final Cloudinary cloudinary;
    private final long maxFileSize;

    public CloudServ(MongoTemplate mongoTemplate, Cloudinary cloudinary, CloudConfig cloudConfig) {
        this.mongoTemplate = mongoTemplate;
        this.cloudinary = cloudinary;
        this.maxFileSize = cloudConfig.getMaxFileSize();
    }

    public String upload(byte[] data, String resourceType, String folder) {
        validateFile(data, resourceType);
        return uploadToCloudinary(data, resourceType, folder);
    }

    public void uploadAvatar(MediaAvatarReq mediaAvatarReq) {
        Media media = mediaAvatarReq.getMedia();
        String imgName = handleUrl
                .getFileName(uploadToCloudinary(media.getFile(), media.getResourceType(), media.getAssetFolder()));
        MediaAvatar mediaAvatar = MediaAvatar.builder()
                .createdAt(Instant.now())
                .imgName(imgName)
                .isThumbnail(media.isThumbnail())
                .userId(mediaAvatarReq.getUserId())
                .build();
        mongoTemplate.save(mediaAvatar);
    }

    public List<String> uploads(MediaProductReq mediaProductReq) {
        List<MediaProduct> mediaProducts = new ArrayList<>();
        for (Media media : mediaProductReq.getMedia()) {
            String fileName = handleUrl
                    .getFileName(uploadToCloudinary(media.getFile(), media.getResourceType(), media.getAssetFolder()));
            MediaProduct mediaProduct = MediaProduct.builder()
                    .imgName(fileName)
                    .isThumbnail(media.isThumbnail())
                    .createdAt(Instant.now())
                    .productId(mediaProductReq.getProductId())
                    .build();
            mediaProducts.add(mediaProduct);
        }
        if (!mediaProducts.isEmpty()) {
            mongoTemplate.insertAll(mediaProducts);
            return mediaProducts.stream()
                    .map(MediaProduct::getImgName)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("No media files to upload");
        }
    }

    public void deleteFileByUrl(String imageUrl) {
        try {
            String publicId = getPublicId(imageUrl);
            if (publicId == null) {
                throw new IllegalArgumentException("Không trích xuất được public_id từ URL");
            }
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa tệp từ Cloudinary: " + e.getMessage(), e);
        }
    }

    public void deleteListFile(List<String> files) {
        try {
            List<String> publicIds = files.stream()
                    .map(CloudServ::getPublicId)
                    .collect(Collectors.toList());

            List<CompletableFuture<Void>> futures = publicIds.stream()
                    .map(id -> CompletableFuture.runAsync(() -> {
                        try {
                            Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
                            log.info("Deleted {}: {}", id, result);
                        } catch (Exception ex) {
                            log.error("Failed to delete file {}: {}", id, ex.getMessage());
                        }
                    }))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa tệp từ Cloudinary: " + e.getMessage(), e);
        }
    }

    private void validateFile(byte[] data, String contentType) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("File data không được để trống");
        }
        if (data.length > maxFileSize) {
            throw new RuntimeException("❌ File quá lớn: " + data.length + " bytes");
        }

    }

    private String uploadToCloudinary(byte[] data, String resourceType, String folder) {
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "overwrite", true,
                    "folder", folder);
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(data, params);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tải lên Cloudinary: " + e.getMessage(), e);
        }
    }

    private static String getPublicId(String url) {
        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2)
                return null;

            String[] pathParts = parts[1].split("/", 2);
            if (pathParts.length < 2)
                return null;

            String publicIdWithExt = pathParts[1];
            int dotIndex = publicIdWithExt.lastIndexOf(".");
            if (dotIndex != -1) {
                return publicIdWithExt.substring(0, dotIndex);
            } else {
                return publicIdWithExt;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
