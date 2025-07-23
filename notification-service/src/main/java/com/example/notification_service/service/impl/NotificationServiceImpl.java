package com.example.notification_service.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.notification_service.mapper.ToModel;
import com.example.notification_service.model.dto.req.NotificationReq;
import com.example.notification_service.model.dto.res.NotificationRes;
import com.example.notification_service.model.entity.Notification;
import com.example.notification_service.model.enums.NotificationType;
import com.example.notification_service.service.NotificationService;
import com.example.notification_service.utility.AuthenticationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void createNoti(NotificationReq req) {
        Notification notification = mongoTemplate.save(ToModel.toEntity(req));
        NotificationRes notificationRes = ToModel.toRes(notification);
        messagingTemplate.convertAndSend("/topic/notification", notificationRes);
    }

    @Override
    public void deleteNoti(String notiId) {
        Notification notification = mongoTemplate.findById(notiId, Notification.class);
        if (notification.getUserId().equals(AuthenticationUtil.UserId())) {
            mongoTemplate.remove(notification);
        }
    }

    @Override
    public List<NotificationRes> getNotification() {
        List<Notification> notifications = mongoTemplate
                .find(new Query(Criteria.where("userId").is(AuthenticationUtil.UserId())), Notification.class);
        return notifications.stream().map(
                ToModel::toRes).toList();
    }

    @Override
    public List<NotificationRes> getNotificationByType(NotificationType type) {
        List<Notification> notifications = mongoTemplate
                .find(new Query(Criteria.where("userId").is(AuthenticationUtil.UserId()).and("type").is(type)),
                        Notification.class);
        return notifications.stream().map(
                ToModel::toRes).toList();
    }

}
