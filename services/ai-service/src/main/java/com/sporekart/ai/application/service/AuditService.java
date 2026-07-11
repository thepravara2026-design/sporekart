package com.sporekart.ai.application.service;

import com.sporekart.ai.infrastructure.persistence.entity.AuditLogEntity;
import com.sporekart.ai.infrastructure.persistence.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String entityType, String entityId, String action, String details) {
        UUID performedBy = getCurrentUserId();
        AuditLogEntity entity = new AuditLogEntity(
                UUID.randomUUID(),
                entityType,
                entityId,
                action,
                details,
                performedBy,
                OffsetDateTime.now());
        auditLogRepository.save(entity);
    }

    private UUID getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    try {
                        if (auth.getPrincipal() instanceof String name) {
                            return UUID.nameUUIDFromBytes(name.getBytes());
                        }
                        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails user) {
                            return UUID.nameUUIDFromBytes(user.getUsername().getBytes());
                        }
                    } catch (Exception e) {
                    }
                    return null;
                })
                .orElse(null);
    }
}
