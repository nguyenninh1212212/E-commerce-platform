package com.example.vendor_service.model.dto.res;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethodRes {
    private Long id;
    private String methodCode;
    private String methodName;
}
