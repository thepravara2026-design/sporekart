package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.RefreshToken;
import com.sporekart.identity.domain.repository.RefreshTokenRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.RefreshTokenEntity;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final SpringDataRefreshTokenRepository repository;

    public JpaRefreshTokenRepositoryAdapter(SpringDataRefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return toDomain(repository.save(toEntity(refreshToken)));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(this::toDomain);
    }

    @Override
    public Optional<RefreshToken> findValidByUserId(String userId) {
        return repository.findFirstByUserIdAndRevokedFalseAndExpiresAtAfter(userId, Instant.now())
                .map(this::toDomain);
    }

    @Override
    public void revokeAllByUserId(String userId) {
        var tokens = repository.findFirstByUserIdAndRevokedFalseAndExpiresAtAfter(userId, Instant.now());
        tokens.ifPresent(t -> {
            t.setRevoked(true);
            t.setRevokedAt(Instant.now());
            repository.save(t);
        });
    }

    @Override
    public void deleteExpired() {
        repository.deleteByExpiresAtBefore(Instant.now());
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(), entity.getToken(), entity.getUserId(),
                entity.isRevoked(), entity.getRevokedAt(), entity.getReplacedBy(),
                entity.getExpiresAt(), entity.getCreatedAt());
    }

    private RefreshTokenEntity toEntity(RefreshToken token) {
        var entity = new RefreshTokenEntity();
        entity.setId(token.getId());
        entity.setToken(token.getToken());
        entity.setUserId(token.getUserId());
        entity.setRevoked(token.isRevoked());
        entity.setRevokedAt(token.getRevokedAt());
        entity.setReplacedBy(token.getReplacedBy());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setCreatedAt(token.getCreatedAt());
        return entity;
    }
}
