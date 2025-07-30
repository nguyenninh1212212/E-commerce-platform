package com.example.vendor_service.model.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PickUpAddress {
    @Id
    @UuidGenerator
    private UUID id;
    private String phoneNumber;
    private String name;
    private String address;
    private String addressDetail;
}
