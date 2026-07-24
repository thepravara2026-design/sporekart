package com.sporekart.identity.application.service;

import com.sporekart.identity.application.dto.*;
import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.common.exception.DuplicateUserException;
import com.sporekart.identity.domain.model.*;
import com.sporekart.identity.domain.model.Device.DeviceType;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.domain.repository.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final DeviceService deviceService;
    private final OtpService otpService;
    private final PermissionService permissionService;
    private final WorkspaceService workspaceService;
    private final AuditService auditService;

    public IdentityService(UserRepositoryPort userRepositoryPort,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           SessionService sessionService,
                           DeviceService deviceService,
                           OtpService otpService,
                           PermissionService permissionService,
                           WorkspaceService workspaceService,
                           AuditService auditService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.deviceService = deviceService;
        this.otpService = otpService;
        this.permissionService = permissionService;
        this.workspaceService = workspaceService;
        this.auditService = auditService;
    }

    public Optional<UserAccount> getById(String id) {
        return userRepositoryPort.findById(id);
    }

    @Transactional
    public UserAccount register(RegisterRequest request) {
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new DuplicateUserException("Email already registered");
        }
        var user = new UserAccount(
                UUID.randomUUID().toString(),
                request.email(),
                request.phone(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserStatus.PENDING,
                false,
                false,
                Instant.now(),
                Instant.now(),
                null,
                false,
                Set.of(RoleType.CUSTOMER));
        return userRepositoryPort.save(user);
    }

    @Transactional
    public LoginResponse loginWithSession(String username, String password,
                                           String ipAddress, String userAgent) {
        var user = userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            auditService.recordEvent("LOGIN_FAILED", user.getId(), "Invalid password from " + ipAddress, ipAddress);
            throw new BusinessException("Invalid credentials");
        }
        validateAccountStatus(user);

        var deviceFingerprint = userAgent != null ? userAgent.hashCode() + "-" + ipAddress : ipAddress;
        var deviceId = registerDeviceIfNeeded(user.getId(), userAgent, deviceFingerprint);
        var workspace = workspaceService.getUserWorkspaces(user.getId()).stream().findFirst();
        var session = sessionService.createSession(
                user.getId(), deviceId, ipAddress, deviceFingerprint,
                workspace.map(Workspace::getWorkspaceId).orElse(null));

        var roles = user.getRoles().stream().map(RoleType::name).toList();
        var permissions = user.getRoles().stream()
                .flatMap(r -> permissionService.getPermissionsForRoles(Set.of(r)).stream())
                .collect(Collectors.toSet());

        var extraClaims = new HashMap<String, Object>();
        extraClaims.put("sessionId", session.getSessionId());
        extraClaims.put("deviceId", deviceId);
        extraClaims.put("permissions", List.copyOf(permissions));

        var accessToken = jwtService.generateAccessToken(user.getId(), roles, extraClaims);
        var refreshToken = jwtService.generateRefreshToken(user.getId());

        return buildLoginResponse(accessToken, refreshToken, session.getSessionId(),
                user, roles, permissions, workspace.orElse(null));
    }

    @Transactional
    public LoginResponse refreshToken(String refreshTokenValue, String ipAddress, String userAgent) {
        if (!jwtService.isRefreshToken(refreshTokenValue)) {
            throw new BusinessException("Invalid refresh token");
        }
        var subject = jwtService.extractSubject(refreshTokenValue);
        if (subject == null) {
            throw new BusinessException("Invalid or expired refresh token");
        }
        jwtService.blacklist(refreshTokenValue);

        var user = userRepositoryPort.findById(subject)
                .orElseThrow(() -> new BusinessException("User not found"));
        validateAccountStatus(user);

        var newAccessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getRoles().stream().map(RoleType::name).toList(),
                Map.of("permissions", List.copyOf(
                        permissionService.getPermissionsForRoles(user.getRoles()))));
        var newRefreshToken = jwtService.generateRefreshToken(user.getId());
        var workspace = workspaceService.getUserWorkspaces(user.getId()).stream().findFirst();

        return buildLoginResponse(newAccessToken, newRefreshToken, null,
                user,
                user.getRoles().stream().map(RoleType::name).toList(),
                permissionService.getPermissionsForRoles(user.getRoles()),
                workspace.orElse(null));
    }

    @Transactional
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.substring(7);
            var sessionId = extractClaim(token, "sessionId");
            if (sessionId != null) {
                sessionService.revokeSession(sessionId);
            }
            jwtService.blacklist(token);
        }
    }

    public LoginResponse verifyOtpAndLogin(String userId, String code,
                                            OtpPurpose purpose, String ipAddress, String userAgent) {
        var verified = otpService.verifyOtp(userId, code, purpose);
        if (!verified) return null;
        var user = userRepositoryPort.findById(userId).orElse(null);
        if (user == null) return null;
        var roles = user.getRoles().stream().map(RoleType::name).toList();
        var permissions = permissionService.getPermissionsForRoles(user.getRoles());
        var deviceFingerprint = userAgent != null ? userAgent.hashCode() + "-" + ipAddress : ipAddress;
        var deviceId = registerDeviceIfNeeded(user.getId(), userAgent, deviceFingerprint);
        var session = sessionService.createSession(user.getId(), deviceId, ipAddress, deviceFingerprint, null);

        var accessToken = jwtService.generateAccessToken(user.getId(), roles, Map.of(
                "sessionId", session.getSessionId(),
                "authMethod", "otp"));
        var refreshToken = jwtService.generateRefreshToken(user.getId());

        return buildLoginResponse(accessToken, refreshToken, session.getSessionId(),
                user, roles, permissions, null);
    }

    public OtpCode generateOtp(String userId, OtpPurpose purpose) {
        return otpService.generateOtp(userId, purpose);
    }

    private void validateAccountStatus(UserAccount user) {
        if (user.isDeleted() || user.getStatus() == UserStatus.LOCKED) {
            throw new BusinessException("Account is locked or deleted");
        }
    }

    private String registerDeviceIfNeeded(String userId, String userAgent, String fingerprint) {
        var existingDevice = deviceService.getUserDevices(userId).stream()
                .filter(d -> d.getDeviceIdentifier().equals(fingerprint))
                .findFirst();
        if (existingDevice.isPresent()) {
            var device = existingDevice.get();
            deviceService.registerDevice(userId, device.getName(), device.getType(),
                    device.getOs(), extractBrowser(userAgent), fingerprint);
            return device.getDeviceId();
        }
        var device = deviceService.registerDevice(userId, "Unknown Device", DeviceType.UNKNOWN,
                extractOs(userAgent), extractBrowser(userAgent), fingerprint);
        return device.getDeviceId();
    }

    private LoginResponse buildLoginResponse(String accessToken, String refreshToken,
                                              String sessionId, UserAccount user,
                                              List<String> roles, Set<String> permissions,
                                              Workspace workspace) {
        return new LoginResponse(
                accessToken, refreshToken, "Bearer", 900L, sessionId,
                user.getId(), roles, List.copyOf(permissions),
                user.isEmailVerified(), user.isPhoneVerified(),
                workspace != null ? Map.of(
                        "workspaceId", workspace.getWorkspaceId(),
                        "name", workspace.getName()) : null);
    }

    private String extractClaim(String token, String claim) {
        try {
            var parsed = com.nimbusds.jwt.SignedJWT.parse(token);
            var value = parsed.getJWTClaimsSet().getStringClaim(claim);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractOs(String userAgent) {
        if (userAgent == null) return "unknown";
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return "unknown";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent == null) return "unknown";
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) return "Safari";
        if (userAgent.contains("Edg")) return "Edge";
        return "unknown";
    }
}
