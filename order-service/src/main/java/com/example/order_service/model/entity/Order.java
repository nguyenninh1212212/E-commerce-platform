package com.example.order_service.model.entity;

import java.time.Instant;
import java.util.List;

import org.checkerframework.common.value.qual.EnumVal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.order_service.model.Attributes;
import com.example.order_service.model.Customer;
import com.example.order_service.model.Product;
import com.example.order_service.model.enums.PaymentMethod;
import com.example.order_service.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;
    private Customer customer;
    private Product product;
    private List<Attributes> attributes;
    private int total_quantity;
    private double total_price;
    private PaymentMethod payment_method;
    private Status status;
    private Instant created_at;
    private Instant updated_at;
    private Instant deleted_at;
    private Instant completed_at;
    // Getters and Setters
}
