package com.example.vendor_service.model.dto.res;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxInfoRes {
    private String taxCode;
    private String taxAgency;
    private LocalDate registeredDate;
    private Boolean isVatPayer;
}
