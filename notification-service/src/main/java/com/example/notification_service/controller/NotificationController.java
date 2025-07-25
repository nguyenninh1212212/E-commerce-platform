package com.example.notification_service.controller;

import com.example.notification_service.model.ApiRes;
import com.example.notification_service.model.dto.req.NotificationReq;
import com.example.notification_service.model.dto.res.NotificationRes;
import com.example.notification_service.model.enums.NotificationType;
import com.example.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationReq req) {
        notificationService.createNoti(req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("id") String notiId) {
        notificationService.deleteNoti(notiId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NotificationRes>> getNotifications() {
        List<NotificationRes> notifications = notificationService.getNotification();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type")
    public ResponseEntity<ApiRes<List<NotificationRes>>> getNotificationsByType(
            @RequestParam NotificationType type) {
        List<NotificationRes> notifications = notificationService.getNotificationByType(type);
        return ResponseEntity.ok(ApiRes.<List<NotificationRes>>builder().status(200).data(notifications).build());
    }
}
