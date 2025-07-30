package com.example.payment_service.model.dto.req;

import lombok.Data;

@Data
public class VNPayRequest {
    private int amount;
    private String orderInfo;
    private String orderType;
    private String bankCode;
    private String locale;
}
