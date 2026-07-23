package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.AuditEvent;
import java.time.Instant;
import java.util.List;

public interface AuditEventRepositoryPort {
    AuditEvent save(AuditEvent event);
    List<AuditEvent> findByUserId(String userId);
    List<AuditEvent> findByEventType(String eventType);
    List<AuditEvent> findByDateRange(Instant from, Instant to);
    long countByEventType(String eventType);
}
