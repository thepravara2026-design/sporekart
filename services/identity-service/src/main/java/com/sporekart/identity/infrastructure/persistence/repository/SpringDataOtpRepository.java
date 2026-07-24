package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.infrastructure.persistence.entity.OtpCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface SpringDataOtpRepository extends JpaRepository<OtpCodeEntity, Long> {
    Optional<OtpCodeEntity> findFirstByUserIdAndPurposeOrderByCreatedAtDesc(String userId, OtpPurpose purpose);
    long countByUserIdAndPurposeAndCreatedAtAfter(String userId, OtpPurpose purpose, Instant since);
    void deleteByExpiresAtBefore(Instant now);
}
