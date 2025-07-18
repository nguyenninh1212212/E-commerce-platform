package com.example.order_service.model.entity;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.order_service.model.Address;
import com.example.order_service.model.ProductPurchase;
import com.example.order_service.model.enums.PaymentMethod;
import com.example.order_service.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private String id;
    private String userId;
    private ProductPurchase productPurchase;
    private PaymentMethod payment_method;
    private boolean paid;
    private Status status;
    private int quantity;
    @Builder.Default
    private Instant created_at = Instant.now();
    private Instant updated_at;
    private Instant deleted_at;
    private Instant completed_at;
    private Address address;
}
