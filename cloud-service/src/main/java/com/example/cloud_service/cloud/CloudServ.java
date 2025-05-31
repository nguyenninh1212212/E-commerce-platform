package com.example.cloud_service.cloud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cloud_service.config.CloudConfig;

@Service
public class CloudServ {

    private final Cloudinary cloudinary;
    private final long maxFileSize;
    private final List<String> allowedMimeTypes;

    public CloudServ(Cloudinary cloudinary, CloudConfig cloudConfig) {
        this.cloudinary = cloudinary;
        this.maxFileSize = cloudConfig.getMaxFileSize();
        this.allowedMimeTypes = cloudConfig.getAllowedMimeTypes();
    }

    public Optional<String> getUrl(MultipartFile file, String folder) {
        return Optional.ofNullable(file)
                .filter(validSize())
                .filter(validMime())
                .map(f -> uploadToCloudinary(f, folder));
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

    private Predicate<MultipartFile> validSize() {
        return file -> {
            boolean ok = file.getSize() <= maxFileSize;
            if (!ok)
                throw new RuntimeException("❌ File quá lớn: " + file.getSize() + " bytes");
            return ok;
        };
    }

    private Predicate<MultipartFile> validMime() {
        return file -> {

            boolean ok = allowedMimeTypes.contains(file.getContentType());
            if (!ok)
                throw new RuntimeException("❌ MIME type không hợp lệ: ");
            return ok;
        };
    }

    @SuppressWarnings("unchecked")
    private String uploadToCloudinary(MultipartFile file, String folder) {
        try {
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", folder, "folder", folder));
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
