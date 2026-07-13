package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdminAuditRepository extends JpaRepository<AdminAuditEntity, UUID> {
    List<AdminAuditEntity> findByEntityId(UUID entityId);
    List<AdminAuditEntity> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
