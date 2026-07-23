package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.AuditEvent;
import com.sporekart.identity.domain.repository.AuditEventRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditEventRepositoryPort auditEventRepository;

    public AuditService(AuditEventRepositoryPort auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Transactional
    public void recordEvent(String eventType, String actorId, String details, String ipAddress) {
        var event = new AuditEvent(null, eventType, actorId, details, ipAddress, Instant.now());
        auditEventRepository.save(event);
        log.info("Audit event: type={}, actor={}, details={}", eventType, actorId, details);
    }

    public List<AuditEvent> getUserEvents(String userId) {
        return auditEventRepository.findByUserId(userId);
    }

    public List<AuditEvent> getEventsByType(String eventType) {
        return auditEventRepository.findByEventType(eventType);
    }

    public List<AuditEvent> getEventsByDateRange(Instant from, Instant to) {
        return auditEventRepository.findByDateRange(from, to);
    }

    public long countByEventType(String eventType) {
        return auditEventRepository.countByEventType(eventType);
    }
}
