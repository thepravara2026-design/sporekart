package com.sporekart.notification.infrastructure.persistence;

import com.sporekart.notification.domain.model.NotificationMessage;
import com.sporekart.notification.domain.repository.NotificationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryNotificationRepository implements NotificationRepositoryPort {
    private final Map<String, NotificationMessage> messagesById = new ConcurrentHashMap<>();

    @Override
    public NotificationMessage save(NotificationMessage message) {
        messagesById.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<NotificationMessage> findById(String id) {
        return Optional.ofNullable(messagesById.get(id));
    }

    @Override
    public List<NotificationMessage> findAll() {
        return new ArrayList<>(messagesById.values());
    }
}
