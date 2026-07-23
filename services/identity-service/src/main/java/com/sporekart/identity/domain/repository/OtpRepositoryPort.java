package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import java.util.Optional;

public interface OtpRepositoryPort {
    OtpCode save(OtpCode otpCode);
    Optional<OtpCode> findLatestByUserIdAndPurpose(String userId, OtpPurpose purpose);
    void markVerified(Long id);
    void deleteExpired();
    long countRecentByUserIdAndPurpose(String userId, OtpPurpose purpose, java.time.Instant since);
}
