package com.example.order_service.service.kafka;

import java.util.UUID;

import org.apache.kafka.common.Uuid;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order_service.model.dto.MessageSocketDTO;
import com.example.order_service.model.entity.Order;
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
    private final MongoTemplate mongoTemplate;

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
            log.error("Error kafka producer order : {}  ", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void sendStockRelease(String variantId, int quantity) {
        String key = UUID.randomUUID().toString();
        byte[] value = updateRequest(variantId, quantity).toByteArray();

        try {
            kafkaTemplate.send("orders.stock-release-updates", key, value).get();
        } catch (Exception e) {
            log.error("❌ Failed to send stock release event: variantId={}, quantity={}, error={}",
                    variantId, quantity, e.getMessage(), e);
            throw new RuntimeException("Kafka send failed", e);
        }
    }

    private MessageSocket messageSocket(String userId, MessageSocketDTO socketDTO) {
        return MessageSocket.newBuilder().setUserId(userId)
                .setMessage(socketDTO.getMessage()).setType(socketDTO.getType()).setTitle(socketDTO.getTitle()).build();
    }

    public void sendNotification(MessageSocketDTO socketDTO) {
        try {
            kafkaTemplate.send("notification.orders", Uuid.randomUuid().toString(),
                    messageSocket(AuthenticationUtil.getUserId(), socketDTO).toByteArray());
        } catch (Exception e) {
            log.error("Error kafka producer order : {} ", e.getMessage());
            throw new RuntimeException();
        }
    }

    public void sendCustomerNotification(MessageSocketDTO socketDTO, String orderId) {
        String userId = mongoTemplate.findById(orderId, Order.class).getUserId();
        try {

            kafkaTemplate.send("notification.orders", Uuid.randomUuid().toString(),
                    messageSocket(userId, socketDTO).toByteArray());
        } catch (Exception e) {
            log.error("Error kafka producer order : {} ", e.getMessage());
            throw new RuntimeException();
        }
    }
}
