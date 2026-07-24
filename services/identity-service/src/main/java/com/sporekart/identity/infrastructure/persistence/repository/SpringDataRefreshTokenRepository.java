package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.infrastructure.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findFirstByUserIdAndRevokedFalseAndExpiresAtAfter(String userId, Instant now);
    void deleteByExpiresAtBefore(Instant now);
}
