package com.sporekart.identity.application.service;

import com.sporekart.identity.application.dto.LoginResponse;
import com.sporekart.identity.application.dto.RegisterRequest;
import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.common.exception.DuplicateUserException;
import com.sporekart.identity.domain.model.*;
import com.sporekart.identity.domain.model.Device.DeviceType;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.domain.repository.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private SessionService sessionService;

    @Mock
    private DeviceService deviceService;

    @Mock
    private OtpService otpService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private IdentityService identityService;

    @Captor
    private ArgumentCaptor<UserAccount> userCaptor;

    private UserAccount createTestUser() {
        return new UserAccount(
                "user-1", "user@example.com", "+1234567890",
                "encoded-pass", "John", "Doe",
                UserStatus.ACTIVE, true, false,
                Instant.now(), Instant.now(), Instant.now(),
                false, Set.of(RoleType.CUSTOMER));
    }

    private Session createTestSession() {
        return new Session(
                "session-1", "user-1", "device-1",
                "10.0.0.1", "fingerprint-1", null,
                Instant.now(), Instant.now().plusSeconds(3600));
    }

    private Device createTestDevice() {
        return new Device("device-1", "user-1", "Chrome on Windows",
                DeviceType.DESKTOP, "Windows", "Chrome", "fingerprint-1");
    }

    @Test
    void shouldRegister() {
        var request = new RegisterRequest("new@example.com", "password123", "Jane", "Doe", "email", null);
        when(userRepositoryPort.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-pass");
        when(userRepositoryPort.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var user = identityService.register(request);

        assertNotNull(user);
        assertEquals(request.email(), user.getEmail());
        assertEquals("encoded-pass", user.getPasswordHash());
        assertEquals(UserStatus.PENDING, user.getStatus());
        assertTrue(user.getRoles().contains(RoleType.CUSTOMER));
        verify(userRepositoryPort).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepositoryPort).save(any(UserAccount.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        var request = new RegisterRequest("existing@example.com", "password123", "Jane", "Doe", "email", null);
        when(userRepositoryPort.existsByEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> identityService.register(request));
        verify(userRepositoryPort).existsByEmail(request.email());
        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    void shouldLoginWithValidCredentials() {
        var user = createTestUser();
        var session = createTestSession();
        var device = createTestDevice();
        var permissions = Set.of("PRODUCT_READ", "ORDER_READ");

        when(userRepositoryPort.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPasswordHash())).thenReturn(true);
        when(deviceService.getUserDevices(user.getId())).thenReturn(List.of());
        when(deviceService.registerDevice(eq(user.getId()), anyString(), any(DeviceType.class), anyString(), anyString(), anyString()))
                .thenReturn(device);
        when(workspaceService.getUserWorkspaces(user.getId())).thenReturn(List.of());
        when(sessionService.createSession(anyString(), anyString(), anyString(), anyString(), isNull()))
                .thenReturn(session);
        when(permissionService.getPermissionsForRoles(anySet())).thenReturn(permissions);
        when(jwtService.generateAccessToken(anyString(), anyList(), anyMap())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");

        var response = identityService.loginWithSession("user@example.com", "password123", "10.0.0.1", "Mozilla/5.0");

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("user-1", response.userId());
        assertFalse(response.emailVerified());
        assertTrue(response.roles().contains("CUSTOMER"));
        verify(userRepositoryPort).findByEmail("user@example.com");
        verify(passwordEncoder).matches("password123", user.getPasswordHash());
    }

    @Test
    void shouldRejectLoginWithInvalidCredentials() {
        when(userRepositoryPort.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> identityService.loginWithSession("wrong@example.com", "wrongpass", "10.0.0.1", null));
    }

    @Test
    void shouldRefreshToken() {
        var user = createTestUser();
        var permissions = Set.of("PRODUCT_READ");
        when(jwtService.isRefreshToken("valid-refresh-token")).thenReturn(true);
        when(jwtService.extractSubject("valid-refresh-token")).thenReturn("user-1");
        when(userRepositoryPort.findById("user-1")).thenReturn(Optional.of(user));
        when(permissionService.getPermissionsForRoles(user.getRoles())).thenReturn(permissions);
        when(jwtService.generateAccessToken(anyString(), anyList(), anyMap())).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken("user-1")).thenReturn("new-refresh-token");
        when(workspaceService.getUserWorkspaces("user-1")).thenReturn(List.of());

        var response = identityService.refreshToken("valid-refresh-token", "10.0.0.1", "Mozilla/5.0");

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
        verify(jwtService).blacklist("valid-refresh-token");
    }

    @Test
    void shouldLogout() {
        doNothing().when(jwtService).blacklist(anyString());

        identityService.logout("Bearer some-test-token");

        verify(jwtService).blacklist("some-test-token");
    }

    @Test
    void shouldVerifyOtpAndLogin() {
        var user = createTestUser();
        var session = createTestSession();
        var device = createTestDevice();
        var permissions = Set.of("PRODUCT_READ");

        when(otpService.verifyOtp("user-1", "123456", OtpPurpose.LOGIN)).thenReturn(true);
        when(userRepositoryPort.findById("user-1")).thenReturn(Optional.of(user));
        when(deviceService.getUserDevices(user.getId())).thenReturn(List.of());
        when(deviceService.registerDevice(eq(user.getId()), anyString(), any(DeviceType.class), anyString(), anyString(), anyString()))
                .thenReturn(device);
        when(sessionService.createSession(anyString(), anyString(), anyString(), anyString(), isNull()))
                .thenReturn(session);
        when(permissionService.getPermissionsForRoles(anySet())).thenReturn(permissions);
        when(jwtService.generateAccessToken(anyString(), anyList(), anyMap())).thenReturn("otp-access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("otp-refresh-token");

        var response = identityService.verifyOtpAndLogin("user-1", "123456", OtpPurpose.LOGIN, "10.0.0.1", "Mozilla/5.0");

        assertNotNull(response);
        assertEquals("otp-access-token", response.accessToken());
        assertEquals("otp-refresh-token", response.refreshToken());
        verify(otpService).verifyOtp("user-1", "123456", OtpPurpose.LOGIN);
    }
}
