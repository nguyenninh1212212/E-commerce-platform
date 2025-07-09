package com.example.address_service.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRes {
    private String id;
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
}
