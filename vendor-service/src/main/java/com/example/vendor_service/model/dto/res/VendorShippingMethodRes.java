package com.example.vendor_service.model.dto.res;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorShippingMethodRes {
    private Long id;
    private String methodCode;
    private String methodName;
    private Boolean enabled;
    private BigDecimal feeOverride;
}
