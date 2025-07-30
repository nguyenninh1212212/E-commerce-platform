package com.example.vendor_service.model.dto.res.vendor;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

import com.example.vendor_service.model.dto.res.PickUpAddressRes;
import com.example.vendor_service.model.dto.res.TaxInfoRes;
import com.example.vendor_service.model.dto.res.VendorShippingMethodRes;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VendorRes extends VendorBaseRes {
    private UUID id;
    private String email;
    private PickUpAddressRes address;
    private TaxInfoRes taxInfo;
    private List<VendorShippingMethodRes> shippingMethods;
}
