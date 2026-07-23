package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.infrastructure.persistence.entity.AuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface SpringDataAuditEventRepository extends JpaRepository<AuditEventEntity, Long> {
    List<AuditEventEntity> findByActorId(String actorId);
    List<AuditEventEntity> findByEventType(String eventType);
    List<AuditEventEntity> findByCreatedAtBetween(Instant from, Instant to);
    long countByEventType(String eventType);
}
