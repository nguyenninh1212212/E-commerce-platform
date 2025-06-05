package com.example.product_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudClientService {
    String uploadToCloud(MultipartFile file, String folder);

    String deleteFromCloud(String url);
}
