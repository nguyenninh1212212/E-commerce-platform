package com.example.auth_service.service.kafka;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.auth_service.mapper.ToProto;
import com.example.auth_service.model.dto.req.ProfileReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import profile.ProfileRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEventProfileCreate(String userId, ProfileReq profileReq) {
        ProfileRequest event = ToProto.toProfileRequest(userId, profileReq);
        try {
            String idKafka = UUID.randomUUID().toString();
            kafkaTemplate.send("profile-create", idKafka, event.toByteArray());
            log.info("Create profile success" + idKafka);
        } catch (Exception e) {
            log.error("Error from profile create grpc ", e.getMessage());
        }
    }
}
