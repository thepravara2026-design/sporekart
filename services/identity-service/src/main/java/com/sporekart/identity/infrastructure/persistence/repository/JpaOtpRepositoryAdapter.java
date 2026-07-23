package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.domain.repository.OtpRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.OtpCodeEntity;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public class JpaOtpRepositoryAdapter implements OtpRepositoryPort {

    private final SpringDataOtpRepository repository;

    public JpaOtpRepositoryAdapter(SpringDataOtpRepository repository) {
        this.repository = repository;
    }

    @Override
    public OtpCode save(OtpCode otpCode) {
        return toDomain(repository.save(toEntity(otpCode)));
    }

    @Override
    public Optional<OtpCode> findLatestByUserIdAndPurpose(String userId, OtpPurpose purpose) {
        return repository.findFirstByUserIdAndPurposeOrderByCreatedAtDesc(userId, purpose)
                .map(this::toDomain);
    }

    @Override
    public void markVerified(Long id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setVerified(true);
            repository.save(entity);
        });
    }

    @Override
    public void deleteExpired() {
        repository.deleteByExpiresAtBefore(Instant.now());
    }

    @Override
    public long countRecentByUserIdAndPurpose(String userId, OtpPurpose purpose, Instant since) {
        return repository.countByUserIdAndPurposeAndCreatedAtAfter(userId, purpose, since);
    }

    private OtpCode toDomain(OtpCodeEntity entity) {
        return new OtpCode(
                entity.getId(), entity.getUserId(), entity.getCode(),
                entity.getPurpose(), entity.getExpiresAt(), 5,
                entity.getAttempts(), entity.isVerified(), entity.getCreatedAt());
    }

    private OtpCodeEntity toEntity(OtpCode otpCode) {
        var entity = new OtpCodeEntity();
        entity.setId(otpCode.getId());
        entity.setUserId(otpCode.getUserId());
        entity.setCode(otpCode.getCode());
        entity.setPurpose(otpCode.getPurpose());
        entity.setExpiresAt(otpCode.getExpiresAt());
        entity.setAttempts(otpCode.getAttempts());
        entity.setVerified(otpCode.isVerified());
        entity.setCreatedAt(otpCode.getCreatedAt());
        return entity;
    }
}
