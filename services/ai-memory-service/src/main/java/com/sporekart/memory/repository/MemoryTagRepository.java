package com.sporekart.memory.repository;

import com.sporekart.memory.persistence.MemoryTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoryTagRepository extends JpaRepository<MemoryTagEntity, UUID> {
    Optional<MemoryTagEntity> findByName(String name);
    boolean existsByName(String name);
}
