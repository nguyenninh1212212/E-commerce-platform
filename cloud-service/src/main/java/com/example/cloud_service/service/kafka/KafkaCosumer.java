package com.example.cloud_service.service.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.cloud_service.mapper.ToModel;
import com.example.cloud_service.model.dto.req.MediaAvatarReq;
import com.example.cloud_service.model.dto.req.MediaProductReq;
import com.example.cloud_service.service.CloudServ;

import cloud.CloudUrl;
import cloud.CloudAvatarRequest;
import cloud.CloudRequests;
import cloud.CloudUrls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumer {
    private final CloudServ cloudServ;

    @KafkaListener(topics = "product-media-create", groupId = "cloud-service-group")
    public void onMediaProductCreate(ConsumerRecord<String, byte[]> record) {
        byte[] value = record.value();
        try {
            CloudRequests cloudRequests = CloudRequests.parseFrom(value);
            MediaProductReq mediaProductReq = MediaProductReq.builder()
                    .media(cloudRequests.getMediaList().stream().map(ToModel::toMedia).toList())
                    .productId(cloudRequests.getProductId())
                    .build();
            cloudServ.uploads(mediaProductReq);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error upload file: {}", e.getMessage(), e);

        }
    }

    @KafkaListener(topics = "product-media-delete", groupId = "cloud-service-group")
    public void onMediasProductDelete(ConsumerRecord<String, byte[]> record) {
        byte[] data = record.value();
        try {
            CloudUrls cloudUrls = CloudUrls.parseFrom(data);
            List<String> imageUrls = cloudUrls.getUrlList();
            cloudServ.deleteListFile(imageUrls);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "avatar-media-create", groupId = "cloud-service-group")
    public void onMediaAvatarCreate(ConsumerRecord<String, byte[]> record) {
        byte[] value = record.value();
        try {
            CloudAvatarRequest cloudRequests = CloudAvatarRequest.parseFrom(value);
            MediaAvatarReq mediaAvatarReq = MediaAvatarReq.builder()
                    .media(ToModel.toMedia(cloudRequests.getMedia()))
                    .userId(cloudRequests.getUserId())
                    .build();
            cloudServ.uploadAvatar(mediaAvatarReq);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error upload file: {}", e.getMessage(), e);

        }
    }

    @KafkaListener(topics = "avatar-media-delete", groupId = "cloud-service-group")
    public void onMediaAvatarDelete(ConsumerRecord<String, byte[]> record) {
        byte[] data = record.value();
        try {
            CloudUrl cloudUrl = CloudUrl.parseFrom(data);
            String imageUrls = cloudUrl.getUrl();
            cloudServ.deleteFileByUrl(imageUrls);
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
        }
    }

}
