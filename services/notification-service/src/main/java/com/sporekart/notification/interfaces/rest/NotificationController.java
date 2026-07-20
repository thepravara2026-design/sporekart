package com.sporekart.notification.interfaces.rest;

import com.sporekart.notification.application.service.NotificationService;
import com.sporekart.notification.domain.model.NotificationChannel;
import com.sporekart.notification.domain.model.NotificationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationMessage>> list() {
        return ResponseEntity.ok(notificationService.listAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationMessage> send(@RequestBody SendNotificationRequest request) {
        NotificationMessage message = notificationService.send(request.recipient(), request.subject(), request.body(),
                request.channel());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    public record SendNotificationRequest(String recipient, String subject, String body, NotificationChannel channel) {
    }
}
