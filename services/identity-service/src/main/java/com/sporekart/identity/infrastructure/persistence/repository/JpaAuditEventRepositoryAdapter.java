package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.AuditEvent;
import com.sporekart.identity.domain.repository.AuditEventRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.AuditEventEntity;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public class JpaAuditEventRepositoryAdapter implements AuditEventRepositoryPort {

    private final SpringDataAuditEventRepository repository;

    public JpaAuditEventRepositoryAdapter(SpringDataAuditEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditEvent save(AuditEvent event) {
        return toDomain(repository.save(toEntity(event)));
    }

    @Override
    public List<AuditEvent> findByUserId(String userId) {
        return repository.findByActorId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<AuditEvent> findByEventType(String eventType) {
        return repository.findByEventType(eventType).stream().map(this::toDomain).toList();
    }

    @Override
    public List<AuditEvent> findByDateRange(Instant from, Instant to) {
        return repository.findByCreatedAtBetween(from, to).stream().map(this::toDomain).toList();
    }

    @Override
    public long countByEventType(String eventType) {
        return repository.countByEventType(eventType);
    }

    private AuditEvent toDomain(AuditEventEntity entity) {
        return new AuditEvent(
                entity.getId(), entity.getEventType(), entity.getActorId(),
                entity.getDetails(), entity.getIpAddress(), entity.getCreatedAt());
    }

    private AuditEventEntity toEntity(AuditEvent event) {
        var entity = new AuditEventEntity();
        entity.setId(event.getId());
        entity.setEventType(event.getEventType());
        entity.setActorId(event.getActorId());
        entity.setDetails(event.getDetails());
        entity.setIpAddress(event.getIpAddress());
        entity.setCreatedAt(event.getCreatedAt());
        return entity;
    }
}
