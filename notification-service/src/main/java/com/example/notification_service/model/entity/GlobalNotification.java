package com.example.notification_service.model.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("global_notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalNotification {
    @Id
    private String id;
    private String title;
    private String content;
    @Builder.Default
    private boolean isRead = false;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    @Indexed(expireAfterSeconds = 0)
    private Instant expiredAt = Instant.now().plus(30, ChronoUnit.DAYS);
}
