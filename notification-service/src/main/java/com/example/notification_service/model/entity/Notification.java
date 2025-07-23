package com.example.notification_service.model.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.notification_service.model.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("notifications")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Notification {
    @Id
    private String id;

    private String userId;

    private String title;

    private String content;

    private NotificationType type;
    @Builder.Default
    private boolean isRead = false;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    @Indexed(expireAfterSeconds = 0)
    private Instant expiredAt = Instant.now().plus(30, ChronoUnit.DAYS);

    private Map<String, Object> metadata;
}
