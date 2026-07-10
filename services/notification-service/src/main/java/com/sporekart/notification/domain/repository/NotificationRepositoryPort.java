package com.sporekart.notification.domain.repository;

import com.sporekart.notification.domain.model.NotificationMessage;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {
    NotificationMessage save(NotificationMessage message);

    Optional<NotificationMessage> findById(String id);

    List<NotificationMessage> findAll();
}
