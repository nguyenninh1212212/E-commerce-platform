package com.example.order_service.model.dto.res;

import java.time.Instant;

import com.example.order_service.model.Address;
import com.example.order_service.model.ProductPurchase;
import com.example.order_service.model.enums.PaymentMethod;
import com.example.order_service.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRes {
    private String id;
    private ProductPurchase productPurchase;
    private int total_quantity;
    private double total_price;
    private PaymentMethod payment_method;
    private boolean paid;
    private Status status;
    private Instant completed_at;
    private Address address;
}
