package com.example.cloud_service.cloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.cloud_service.config.CloudConfig;

@Service
public class CloudServ {

    private final Cloudinary cloudinary;
    private final long maxFileSize;

    public CloudServ(Cloudinary cloudinary, CloudConfig cloudConfig) {
        this.cloudinary = cloudinary;
        this.maxFileSize = cloudConfig.getMaxFileSize();
    }

    public String upload(byte[] data, String resourceType, String folder) {
        validateFile(data, resourceType);
        return uploadToCloudinary(data, resourceType, folder);
    }

    public List<String> uploads(List<byte[]> datas, String type, String asset_folder) {
        return datas.stream()
                .map(data -> {
                    return uploadToCloudinary(data, type, asset_folder);
                })
                .filter(Objects::nonNull)
                .toList();
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
