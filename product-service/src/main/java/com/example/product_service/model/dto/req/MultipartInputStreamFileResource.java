package com.example.product_service.model.dto.req;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    public MultipartInputStreamFileResource(MultipartFile multipartFile) throws IOException {
        super(multipartFile.getInputStream());
        this.filename = multipartFile.getOriginalFilename();
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1; // Fixes known bug in Spring when sending unknown-length files
    }
}
