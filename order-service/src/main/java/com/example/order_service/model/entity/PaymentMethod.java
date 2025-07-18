package com.example.order_service.model.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.order_service.model.enums.PAYMENTMETHOD;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("payment_method")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    @Id
    private String id;
    private PAYMENTMETHOD type;
    private boolean useable;
    private String description;
    @Builder.Default
    private Instant createdAt = Instant.now();
    private Instant updatedAt;
}
