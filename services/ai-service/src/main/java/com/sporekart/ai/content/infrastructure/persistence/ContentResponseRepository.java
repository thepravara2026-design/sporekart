package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentResponseRepository extends JpaRepository<ContentResponseEntity, UUID> {
    Optional<ContentResponseEntity> findByRequestId(UUID requestId);
}
