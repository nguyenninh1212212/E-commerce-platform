package com.example.vendor_service.model.dto.req;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxInfoReq {
    private String taxCode;
    private String taxAgency;
    private LocalDate registeredDate;
    private Boolean isVatPayer;
}
