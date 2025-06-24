package com.example.api_gateway.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}
