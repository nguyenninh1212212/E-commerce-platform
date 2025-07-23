package com.example.notification_service.service;

import java.util.List;

import com.example.notification_service.model.dto.req.NotificationReq;
import com.example.notification_service.model.dto.res.NotificationRes;
import com.example.notification_service.model.enums.NotificationType;

public interface NotificationService {
    void createNoti(NotificationReq req);

    void deleteNoti(String notiId);

    List<NotificationRes> getNotification();

    List<NotificationRes> getNotificationByType(NotificationType type);

}
