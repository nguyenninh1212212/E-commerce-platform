package com.example.cloud_service.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cloud_service.config.CloudConfig;
import com.example.cloud_service.controller.MediaController;
import com.example.cloud_service.model.dto.req.MediaAvatarReq;
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
    private final String RESOURCE_TYPE = "image";
    private final String ASSET_FOLDER_PRODUCT = "products";
    private final String ASSET_FOLDER_AVATAR = "avatars";

    public CloudServ(MongoTemplate mongoTemplate, Cloudinary cloudinary, CloudConfig cloudConfig) {
        this.mongoTemplate = mongoTemplate;
        this.cloudinary = cloudinary;
        this.maxFileSize = cloudConfig.getMaxFileSize();
    }

    private MediaProduct mapToMediaProduct(int index, MultipartFile multipartFile, String productId) {
        try {
            byte[] file = multipartFile.getBytes();
            validateFile(file);

            String fileName = handleUrl.getFileName(
                    uploadToCloudinary(file, RESOURCE_TYPE, ASSET_FOLDER_PRODUCT));

            return MediaProduct.builder()
                    .imgName(fileName)
                    .position(index)
                    .createdAt(Instant.now())
                    .productId(productId)
                    .build();

        } catch (IOException e) {
            log.error("🛑 Failed to process file at index {}: {}", index, e.getMessage(), e);
            return null;
        }
    }

    private void validateFile(byte[] data) {
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
                    "overwrite", false,
                    "unique_filename", true,
                    "folder", folder);
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(data, params);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tải lên Cloudinary: " + e.getMessage(), e);
        }
    }

    private void deleteByPublicId(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Deleted result: " + result);
        } catch (IOException e) {
            log.error("Failed to delete image with publicId " + publicId);
        }
    }

    public void uploadAvatar(MediaAvatarReq mediaAvatarReq, MultipartFile file) {
        byte[] media;
        try {
            media = file.getBytes();
            String imgName = handleUrl
                    .getFileName(
                            uploadToCloudinary(media, RESOURCE_TYPE,
                                    ASSET_FOLDER_AVATAR));
            MediaAvatar mediaAvatar = MediaAvatar.builder()
                    .createdAt(Instant.now())
                    .imgName(imgName)
                    .isThumbnail(mediaAvatarReq.isThumbnail())
                    .userId(mediaAvatarReq.getUserId())
                    .build();
            mongoTemplate.save(mediaAvatar);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void uploads(String productId, List<MultipartFile> files) {
        List<MediaProduct> mediaProducts = IntStream.range(0, files.size())
                .mapToObj(i -> mapToMediaProduct(i, files.get(i), productId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (mediaProducts.isEmpty()) {
            throw new IllegalArgumentException("❌ No valid media files to upload");
        }

        mongoTemplate.insertAll(mediaProducts);
    }

    public void deleteByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productId").is(productId));
        List<String> imgNameList = mongoTemplate.find(query, MediaProduct.class).stream()
                .map(MediaProduct::getImgName)
                .toList();
        for (String imgName : imgNameList) {
            deleteByPublicId(imgName);
        }
    }

    public void deleteByUserId(String UserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(UserId));
        List<String> imgNameList = mongoTemplate.find(query, MediaAvatar.class).stream()
                .map(MediaAvatar::getImgName)
                .toList();
        for (String imgName : imgNameList) {
            deleteByPublicId(imgName);
        }
    }

    public <T> List<String> getMediaUrl(String idObject, String fieldObject, Class<T> clazz, String Asset_folder,
            java.util.function.Function<T, String> imgNameExtractor) {
        Query query = new Query(Criteria.where(fieldObject).is(idObject));
        return mongoTemplate.find(query, clazz).stream()
                .map(media -> handleUrl.buildCloudinaryUrl(imgNameExtractor.apply(media), Asset_folder))
                .toList();
    }

    public List<String> getMediaProducts(String productId) {
        return getMediaUrl(productId, "productId", MediaProduct.class, ASSET_FOLDER_PRODUCT, MediaProduct::getImgName);
    }

    public List<String> getMediaAvatar(String userId) {
        return getMediaUrl(userId, "userId", MediaAvatar.class, ASSET_FOLDER_AVATAR, MediaAvatar::getImgName);

    }

}
