package com.example.cloud_service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class handleUrl {
    private final String cloudName = "your_cloud_name"; // Replace with your actual Cloudinary cloud name
    private final String imgService = "/image/upload/";

    public String buildCloudinaryUrl(String folder, String imgName) {
        return "https://res.cloudinary.com/" + cloudName + imgService + folder + imgName;
    }

    public String getFileName(String path) {
        int lastSlash = path.lastIndexOf("/");
        return (lastSlash >= 0) ? path.substring(lastSlash) : path;
    }

}
