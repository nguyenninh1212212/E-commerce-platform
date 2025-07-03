package com.example.cloud_service.util;

import org.springframework.beans.factory.annotation.Value;

import lombok.experimental.UtilityClass;

@UtilityClass
public class handleUrl {
    private String cloudName = "dxpyuj1mm"; // Replace with your actual Cloudinary cloud name
    private final String imgService = "/image/upload/";

    public String buildCloudinaryUrl(String imgName, String folder) {
        return "https://res.cloudinary.com/" + cloudName + imgService + folder + "/" + imgName;
    }

    public String getFileName(String path) {
        int lastSlash = path.lastIndexOf("/");
        return (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;
    }

    public String getPublicId(String imgName) {
        String[] img = imgName.split(".");
        return img[0];
    }

}
