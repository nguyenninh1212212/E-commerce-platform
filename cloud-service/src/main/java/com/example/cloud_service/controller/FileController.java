package com.example.cloud_service.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cloud_service.cloud.CloudServ;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final CloudServ cloudServ;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestPart MultipartFile file,
            @RequestPart String folder) {
        String url = cloudServ.getUrl(file, folder).get();
        return ResponseEntity.ok(url);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<String> delete(@RequestPart String imageUrl) {
        cloudServ.deleteFileByUrl(imageUrl);
        return ResponseEntity.ok("✅ Xóa tệp thành công");
    }

}
