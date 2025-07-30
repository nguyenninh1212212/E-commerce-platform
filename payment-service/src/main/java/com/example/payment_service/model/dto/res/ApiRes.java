package com.example.payment_service.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiRes<T> {
    private int status;
    private T data;

    public static <T> ApiRes<T> of(int status, T data) {
        return ApiRes.<T>builder()
                .status(status)
                .data(data)
                .build();
    }
}
