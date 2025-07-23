package com.example.notification_service.mapper;

import com.example.notification_service.model.dto.req.NotificationReq;
import com.example.notification_service.model.dto.res.NotificationRes;
import com.example.notification_service.model.entity.Notification;
import com.example.notification_service.model.enums.NotificationType;

import lombok.experimental.UtilityClass;
import notification.MessageSocket;

@UtilityClass
public class ToModel {
    public Notification toEntity(NotificationReq req) {
        return Notification.builder()
                .content(req.getContent())
                .title(req.getTitle())
                .type(req.getType())
                .userId(req.getUserId())
                .metadata(null)
                .build();
    }

    public NotificationReq toReq(MessageSocket req) {
        return NotificationReq.builder()
                .content(req.getMessage())
                .title(req.getTitle())
                .type(NotificationType.valueOf(req.getType().toUpperCase()))
                .userId(req.getUserId())
                .build();
    }

    public NotificationRes toRes(Notification notification) {
        return NotificationRes.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .expiredAt(notification.getExpiredAt())
                .metadata(notification.getMetadata())
                .build();
    }
}
