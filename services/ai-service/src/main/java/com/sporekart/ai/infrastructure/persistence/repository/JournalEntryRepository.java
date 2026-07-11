package com.sporekart.ai.infrastructure.persistence.repository;

import com.sporekart.ai.infrastructure.persistence.entity.JournalEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntryEntity, UUID> {
}
