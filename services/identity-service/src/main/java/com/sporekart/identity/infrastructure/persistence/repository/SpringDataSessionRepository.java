package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.infrastructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface SpringDataSessionRepository extends JpaRepository<SessionEntity, String> {
    List<SessionEntity> findByUserIdAndRevokedFalseAndExpiresAtAfter(String userId, Instant now);
    List<SessionEntity> findByUserId(String userId);
    long countByUserIdAndRevokedFalseAndExpiresAtAfter(String userId, Instant now);
    void deleteByExpiresAtBefore(Instant now);
}
