package com.example.address_service.model.entity;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    private String id;
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String country;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String addressLine2;
    private String postalCode;
    private boolean isDefault;
    private String type;
    @CreatedDate
    private Long createdAt;
    @LastModifiedDate
    private Long updatedAt;
}
