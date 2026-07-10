package com.sporekart.notification.application.service;

import com.sporekart.notification.domain.model.NotificationChannel;
import com.sporekart.notification.domain.model.NotificationMessage;
import com.sporekart.notification.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepositoryPort repositoryPort;

    public NotificationService(NotificationRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public NotificationMessage send(String recipient, String subject, String body, NotificationChannel channel) {
        NotificationMessage message = NotificationMessage.create(recipient, subject, body, channel);
        return repositoryPort.save(message);
    }

    public Optional<NotificationMessage> getById(String id) {
        return repositoryPort.findById(id);
    }

    public List<NotificationMessage> listAll() {
        return repositoryPort.findAll();
    }
}
