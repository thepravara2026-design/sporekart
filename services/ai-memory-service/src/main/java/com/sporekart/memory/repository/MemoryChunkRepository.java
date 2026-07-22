package com.sporekart.memory.repository;

import com.sporekart.memory.persistence.MemoryChunkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryChunkRepository extends JpaRepository<MemoryChunkEntity, UUID> {
    List<MemoryChunkEntity> findByMemoryIdOrderByChunkIndexAsc(UUID memoryId);
    void deleteByMemoryId(UUID memoryId);
}
