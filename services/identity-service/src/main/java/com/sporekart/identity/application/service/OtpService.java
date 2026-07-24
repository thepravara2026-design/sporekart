package com.sporekart.identity.application.service;

import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.domain.repository.OtpRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Service
public class OtpService {

    private final OtpRepositoryPort otpRepository;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;
    private static final long RATE_LIMIT_WINDOW_SECONDS = 60;
    private static final int MAX_REQUESTS_PER_WINDOW = 3;

    public OtpService(OtpRepositoryPort otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Transactional
    public OtpCode generateOtp(String userId, OtpPurpose purpose) {
        var since = Instant.now().minusSeconds(RATE_LIMIT_WINDOW_SECONDS);
        var recentCount = otpRepository.countRecentByUserIdAndPurpose(userId, purpose, since);
        if (recentCount >= MAX_REQUESTS_PER_WINDOW) {
            throw new BusinessException("Too many OTP requests. Please try again later.");
        }
        var code = String.format("%06d", RANDOM.nextInt(1000000));
        var expiresAt = Instant.now().plusSeconds(OTP_EXPIRATION_MINUTES * 60);
        var otpCode = new OtpCode(null, userId, code, purpose, expiresAt, MAX_ATTEMPTS, 0, false, Instant.now());
        return otpRepository.save(otpCode);
    }

    @Transactional
    public boolean verifyOtp(String userId, String code, OtpPurpose purpose) {
        var otpOpt = otpRepository.findLatestByUserIdAndPurpose(userId, purpose);
        if (otpOpt.isEmpty()) {
            return false;
        }
        var otp = otpOpt.get();
        if (otp.isVerified()) {
            return false;
        }
        if (otp.isExpired()) {
            return false;
        }
        if (otp.isMaxAttemptsReached()) {
            return false;
        }
        otp.incrementAttempts();
        if (otp.getCode().equals(code)) {
            otp.markVerified();
            otpRepository.save(otp);
            return true;
        }
        otpRepository.save(otp);
        return false;
    }

    public Optional<OtpCode> getLatestOtp(String userId, OtpPurpose purpose) {
        return otpRepository.findLatestByUserIdAndPurpose(userId, purpose);
    }

    @Transactional
    public void cleanupExpired() {
        otpRepository.deleteExpired();
    }
}
