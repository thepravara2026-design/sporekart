package com.sporekart.memory.repository;

import com.sporekart.memory.persistence.MemorySummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemorySummaryRepository extends JpaRepository<MemorySummaryEntity, UUID> {
    List<MemorySummaryEntity> findByMemoryIdOrderByCreatedAtDesc(UUID memoryId);
    Optional<MemorySummaryEntity> findTopByMemoryIdOrderByVersionDesc(UUID memoryId);
}
