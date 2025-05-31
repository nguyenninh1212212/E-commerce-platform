package com.example.cloud_service.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
@Data
public class CloudConfig {

    private String apiSecret;
    private String apiKey;
    private String cloudName;
    private long maxFileSize;
    private List<String> allowedMimeTypes;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                Map.of("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret));
    }
}
