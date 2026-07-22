package com.sporekart.memory.repository;

import com.sporekart.memory.persistence.MemoryAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryAuditRepository extends JpaRepository<MemoryAuditEntity, UUID> {
    List<MemoryAuditEntity> findByMemoryIdOrderByCreatedAtDesc(UUID memoryId);
}
