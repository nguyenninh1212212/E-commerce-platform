package com.example.vendor_service.model.dto.req;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorReq {
    private String nameStore;
    private String email;
    private PickUpAddressReq address;
    private TaxInfoReq taxInfo;
}
