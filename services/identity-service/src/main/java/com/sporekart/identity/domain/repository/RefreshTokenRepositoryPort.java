package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findValidByUserId(String userId);
    void revokeAllByUserId(String userId);
    void deleteExpired();
}
