package com.example.vendor_service.model.dto.req;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickUpAddressReq {
    private String phoneNumber;
    private String name;
    private String address;
    private String addressDetail;
}
