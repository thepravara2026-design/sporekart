package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.dto.*;
import com.sporekart.identity.application.service.*;
import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.model.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IdentityService identityService;
    private final SessionService sessionService;
    private final AuditService auditService;

    public AuthController(IdentityService identityService,
                          SessionService sessionService,
                          AuditService auditService) {
        this.identityService = identityService;
        this.sessionService = sessionService;
        this.auditService = auditService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAccount> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(identityService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                                HttpServletRequest servletRequest) {
        var ip = servletRequest.getRemoteAddr();
        var userAgent = servletRequest.getHeader("User-Agent");
        var response = identityService.loginWithSession(request.username(), request.password(), ip, userAgent);
        auditService.recordEvent("LOGIN_SUCCESS", response.userId(), "Login from " + ip, ip);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader,
                                        HttpServletRequest request) {
        identityService.logout(authHeader);
        auditService.recordEvent("LOGOUT", "unknown", "User logout", request.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshRequest,
                                                  HttpServletRequest servletRequest) {
        var ip = servletRequest.getRemoteAddr();
        var userAgent = servletRequest.getHeader("User-Agent");
        var response = identityService.refreshToken(refreshRequest.refreshToken(), ip, userAgent);
        auditService.recordEvent("TOKEN_REFRESH", response.userId(), "Token refreshed", ip);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/generate")
    public ResponseEntity<Map<String, String>> generateOtp(@Valid @RequestBody OtpRequest request) {
        var purpose = OtpCode.OtpPurpose.valueOf(request.purpose().toUpperCase());
        var otp = identityService.generateOtp(request.userId(), purpose);
        return ResponseEntity.ok(Map.of(
                "message", "OTP sent",
                "expiresIn", "5 minutes"));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request,
                                                          HttpServletRequest servletRequest) {
        var purpose = OtpCode.OtpPurpose.valueOf(request.purpose().toUpperCase());
        var ip = servletRequest.getRemoteAddr();
        var userAgent = servletRequest.getHeader("User-Agent");
        var result = identityService.verifyOtpAndLogin(request.userId(), request.code(), purpose, ip, userAgent);
        if (result != null) {
            auditService.recordEvent("OTP_LOGIN_SUCCESS", result.userId(), "OTP login", ip);
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "accessToken", result.accessToken(),
                    "refreshToken", result.refreshToken(),
                    "sessionId", result.sessionId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired OTP"));
    }
}
