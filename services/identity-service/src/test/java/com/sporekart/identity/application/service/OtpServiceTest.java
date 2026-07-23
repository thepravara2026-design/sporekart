package com.sporekart.identity.application.service;

import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.repository.OtpRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    private OtpRepositoryPort otpRepository;
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        otpRepository = mock(OtpRepositoryPort.class);
        otpService = new OtpService(otpRepository);
    }

    @Test
    void shouldGenerateOtp() {
        when(otpRepository.countRecentByUserIdAndPurpose(anyString(), any(), any())).thenReturn(0L);
        when(otpRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var otp = otpService.generateOtp("user-1", OtpCode.OtpPurpose.LOGIN);

        assertNotNull(otp);
        assertEquals("user-1", otp.getUserId());
        assertEquals(OtpCode.OtpPurpose.LOGIN, otp.getPurpose());
        assertNotNull(otp.getCode());
        assertEquals(6, otp.getCode().length());
        assertFalse(otp.isVerified());
        verify(otpRepository).save(any(OtpCode.class));
    }

    @Test
    void shouldVerifyValidOtp() {
        var otp = new OtpCode(1L, "user-1", "123456", OtpCode.OtpPurpose.LOGIN,
                Instant.now().plusSeconds(300), 5, 0, false, Instant.now());
        when(otpRepository.findLatestByUserIdAndPurpose("user-1", OtpCode.OtpPurpose.LOGIN))
                .thenReturn(Optional.of(otp));
        when(otpRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = otpService.verifyOtp("user-1", "123456", OtpCode.OtpPurpose.LOGIN);

        assertTrue(result);
        assertTrue(otp.isVerified());
        verify(otpRepository).save(otp);
    }

    @Test
    void shouldRejectExpiredOtp() {
        var otp = new OtpCode(1L, "user-1", "123456", OtpCode.OtpPurpose.LOGIN,
                Instant.now().minusSeconds(10), 5, 0, false, Instant.now().minusSeconds(600));
        when(otpRepository.findLatestByUserIdAndPurpose("user-1", OtpCode.OtpPurpose.LOGIN))
                .thenReturn(Optional.of(otp));

        var result = otpService.verifyOtp("user-1", "123456", OtpCode.OtpPurpose.LOGIN);

        assertFalse(result);
        verify(otpRepository, never()).save(any());
    }

    @Test
    void shouldRejectMaxAttemptsExceeded() {
        var otp = new OtpCode(1L, "user-1", "123456", OtpCode.OtpPurpose.LOGIN,
                Instant.now().plusSeconds(300), 5, 5, false, Instant.now());
        when(otpRepository.findLatestByUserIdAndPurpose("user-1", OtpCode.OtpPurpose.LOGIN))
                .thenReturn(Optional.of(otp));

        var result = otpService.verifyOtp("user-1", "123456", OtpCode.OtpPurpose.LOGIN);

        assertFalse(result);
        verify(otpRepository, never()).save(any());
    }

    @Test
    void shouldRejectAlreadyVerifiedOtp() {
        var otp = new OtpCode(1L, "user-1", "123456", OtpCode.OtpPurpose.LOGIN,
                Instant.now().plusSeconds(300), 5, 0, true, Instant.now());
        when(otpRepository.findLatestByUserIdAndPurpose("user-1", OtpCode.OtpPurpose.LOGIN))
                .thenReturn(Optional.of(otp));

        var result = otpService.verifyOtp("user-1", "123456", OtpCode.OtpPurpose.LOGIN);

        assertFalse(result);
        verify(otpRepository, never()).save(any());
    }

    @Test
    void shouldEnforceRateLimit() {
        when(otpRepository.countRecentByUserIdAndPurpose(anyString(), any(), any())).thenReturn(3L);

        assertThrows(BusinessException.class,
                () -> otpService.generateOtp("user-1", OtpCode.OtpPurpose.LOGIN));
        verify(otpRepository, never()).save(any());
    }

    @Test
    void shouldRejectWrongCode() {
        var otp = new OtpCode(1L, "user-1", "123456", OtpCode.OtpPurpose.LOGIN,
                Instant.now().plusSeconds(300), 5, 0, false, Instant.now());
        when(otpRepository.findLatestByUserIdAndPurpose("user-1", OtpCode.OtpPurpose.LOGIN))
                .thenReturn(Optional.of(otp));
        when(otpRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = otpService.verifyOtp("user-1", "wrong-code", OtpCode.OtpPurpose.LOGIN);

        assertFalse(result);
        assertEquals(1, otp.getAttempts());
        assertFalse(otp.isVerified());
        verify(otpRepository).save(otp);
    }
}
