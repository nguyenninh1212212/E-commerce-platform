package com.example.order_service.model.dto.req;

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
public class OrderReq {
    private ProductPurchase productPurchase;
    private int quantity;
    private PaymentMethod payment_method;
    private Address address;
}
