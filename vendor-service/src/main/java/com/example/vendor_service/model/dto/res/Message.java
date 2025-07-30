package com.example.vendor_service.model.dto.res;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private int status;
    private String message;

    public static Message of(int status, String message) {
        return Message.builder()
                .status(status)
                .message(message)
                .build();
    }
}
