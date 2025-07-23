package com.example.notification_service.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.notification_service.mapper.ToModel;
import com.example.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification.MessageSocket;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaCosumer {
    private final NotificationService notiService;

    @KafkaListener(topics = "notification.orders")
    public void CosumerNotiOrders(ConsumerRecord<String, byte[]> record) {
        try {
            MessageSocket event = MessageSocket.parseFrom(record.value());
            notiService.createNoti(ToModel.toReq(event));
        } catch (Exception e) {
            log.error("Kafkacosumer error : {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}
