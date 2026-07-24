package com.sporekart.notification.infrastructure.persistence;

import com.sporekart.notification.domain.model.NotificationMessage;
import com.sporekart.notification.domain.repository.NotificationRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JpaNotificationRepositoryAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository jpaRepository;

    public JpaNotificationRepositoryAdapter(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public NotificationMessage save(NotificationMessage message) {
        return jpaRepository.save(NotificationMessageEntity.fromDomain(message)).toDomain();
    }

    @Override
    public Optional<NotificationMessage> findById(String id) {
        return jpaRepository.findById(id).map(NotificationMessageEntity::toDomain);
    }

    @Override
    public List<NotificationMessage> findAll() {
        return jpaRepository.findAll().stream().map(NotificationMessageEntity::toDomain).toList();
    }
}
