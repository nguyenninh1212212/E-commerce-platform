package com.example.cloud_service.controller;

import com.example.cloud_service.model.dto.req.MediaAvatarReq;
import com.example.cloud_service.service.CloudServ;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final CloudServ cloudServ;

    // 👤 Upload avatar
    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestPart MediaAvatarReq req, @RequestPart MultipartFile file) {
        cloudServ.uploadAvatar(req, file);
        return ResponseEntity.ok("✅ Avatar uploaded successfully");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<String>> getMediaProducts(@PathVariable String productId) {
        return ResponseEntity.ok(cloudServ.getMediaProducts(productId));
    }

    // 📷 Upload product media (nhiều ảnh)
    @PostMapping("/products/{productId}")
    public ResponseEntity<String> uploadProductMedia(@PathVariable String productId,
            @RequestPart List<MultipartFile> files) {
        cloudServ.uploads(productId, files);
        return ResponseEntity.ok("✅ Product media uploaded successfully");
    }

    // ❌ Delete ảnh theo productId
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteByProductId(@PathVariable String productId) {
        cloudServ.deleteByProductId(productId);
        return ResponseEntity.ok("🗑 Product media deleted: " + productId);
    }

    // ❌ Delete avatar theo userId
    @DeleteMapping("/avatar/{userId}")
    public ResponseEntity<String> deleteByUserId(@PathVariable String userId) {
        cloudServ.deleteByUserId(userId);
        return ResponseEntity.ok("🗑 Avatar deleted for user: " + userId);
    }
}
