package com.example.cloud_service.service.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.cloud_service.service.CloudServ;

import cloud.IdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumer {
    private final CloudServ cloudServ;

    @KafkaListener(topics = "product-delete", groupId = "cloud-service-group")
    public void onMediasProductDelete(ConsumerRecord<String, byte[]> record) {
        byte[] data = record.value();
        try {
            cloud.IdRequest productId = IdRequest.parseFrom(data);
            cloudServ.deleteByProductId(productId.getId());
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "avatar-media-delete", groupId = "cloud-service-group")
    public void onMediaAvatarDelete(ConsumerRecord<String, byte[]> record) {
        byte[] data = record.value();
        try {
            cloud.IdRequest userId = IdRequest.parseFrom(data);
            cloudServ.deleteByUserId(userId.getId());
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
        }
    }

}
