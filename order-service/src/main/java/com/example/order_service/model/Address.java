package com.example.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
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
    private String type;
}
