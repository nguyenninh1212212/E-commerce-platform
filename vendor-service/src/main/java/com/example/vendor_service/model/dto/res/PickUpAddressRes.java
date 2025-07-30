package com.example.vendor_service.model.dto.res;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickUpAddressRes {
    private String phoneNumber;
    private String name;
    private String address;
    private String addressDetail;
}
