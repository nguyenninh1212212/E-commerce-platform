package com.example.profile_service.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.profile_service.mapper.ToModel;
import com.example.profile_service.model.dto.req.ProfileReq;
import com.example.profile_service.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import profile.ProfileRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ProfileService profileService;
    private static final String TOPIC_CREATE = "profile-create";
    private static final String TOPIC_UPDATE = "profile-update";

    @KafkaListener(topics = TOPIC_CREATE)
    public void cosumerCreateProfile(ConsumerRecord<String, byte[]> event) throws Exception {
        ProfileRequest request = ProfileRequest.parseFrom(event.value());
        ProfileReq req = ToModel.ToProductReq(request);
        profileService.createProfile(req);
        log.info("Cosumer listen create profile request from " + TOPIC_CREATE);
    }

    @KafkaListener(topics = TOPIC_UPDATE)
    public void cosumerUpdateProfile(ConsumerRecord<String, byte[]> event) {

    }
}
