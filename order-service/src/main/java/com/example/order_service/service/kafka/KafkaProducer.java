package com.example.order_service.service.kafka;

import org.apache.kafka.common.Uuid;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order_service.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification.MessageSocket;
import order.OrderInventoryUpdateRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    private OrderInventoryUpdateRequest updateRequest(String variantId, int quantity) {
        return OrderInventoryUpdateRequest.newBuilder()
                .setQuantity(quantity)
                .setVariantId(variantId)
                .build();
    }

    public void sendStockReverse(String variantId, int quantity) {
        try {
            kafkaTemplate.send("orders.stock-reverse-updates", Uuid.randomUuid().toString(),
                    updateRequest(variantId, quantity).toByteArray());
        } catch (Exception e) {
            log.error("Error kafka producer order : ", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void sendStockRelease(String variantId, int quantity) {

        try {
            kafkaTemplate.send("orders.stock-release-updates", Uuid.randomUuid().toString(),
                    updateRequest(variantId, quantity).toByteArray());
        } catch (Exception e) {
            log.error("Error kafka producer order : ", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void sendNotification(String message) {

        try {
            MessageSocket messageSocket = MessageSocket.newBuilder().setUserId(AuthenticationUtil.getUserId())
                    .setMessage(message).build();
            kafkaTemplate.send("notification.orders", Uuid.randomUuid().toString(),
                    messageSocket.toByteArray());
        } catch (Exception e) {
            log.error("Error kafka producer order : ", e.getMessage());
            throw new RuntimeException();
        }
    }
}
