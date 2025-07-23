package com.example.notification_service.model.dto.req;

import java.util.Map;

import com.example.notification_service.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotificationReq {
    private String userId;

    private String title;

    private String content;

    private NotificationType type;

    private Map<String, Object> metadata;
}
