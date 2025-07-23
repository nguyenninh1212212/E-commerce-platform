package com.example.notification_service.model.dto.res;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;

import com.example.notification_service.model.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotificationRes {
    @Id
    private String id;
    private String title;
    private String content;
    private NotificationType type;
    private boolean isRead;
    private Instant createdAt;
    private Instant expiredAt;
    private Map<String, Object> metadata;
}
