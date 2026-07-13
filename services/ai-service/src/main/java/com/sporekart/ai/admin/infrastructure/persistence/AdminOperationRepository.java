package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdminOperationRepository extends JpaRepository<AdminOperationEntity, UUID> {
    List<AdminOperationEntity> findByType(String type);
    List<AdminOperationEntity> findByPerformedAtBetween(LocalDateTime from, LocalDateTime to);
}
