package com.sporekart.identity.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.identity.application.dto.*;
import com.sporekart.identity.application.service.AuditService;
import com.sporekart.identity.application.service.IdentityService;
import com.sporekart.identity.application.service.SessionService;
import com.sporekart.identity.domain.model.OtpCode;
import com.sporekart.identity.domain.model.OtpCode.OtpPurpose;
import com.sporekart.identity.domain.model.UserAccount;
import com.sporekart.identity.domain.model.UserStatus;
import com.sporekart.identity.domain.model.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IdentityService identityService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private AuditService auditService;

    @Test
    void shouldRegisterUser() throws Exception {
        var request = new RegisterRequest("user@example.com", "secret123", "Ada", "Lovelace", "email", null);
        var user = new UserAccount(
                "user-1", "user@example.com", null, "encoded", "Ada", "Lovelace",
                UserStatus.PENDING, false, false, Instant.now(), Instant.now(), null, false, Set.of(RoleType.CUSTOMER));
        when(identityService.register(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void shouldLogin() throws Exception {
        var request = new LoginRequest("user@example.com", "secret123");
        var response = new LoginResponse(
                "access-token", "refresh-token", "Bearer", 900L, "session-1",
                "user-1", List.of("CUSTOMER"), List.of("PRODUCT_READ"),
                true, false, Map.of("workspaceId", "ws-1", "name", "MyWorkspace"));
        when(identityService.loginWithSession(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("User-Agent", "Mozilla/5.0")
                        .with(request1 -> {
                            request1.setRemoteAddr("10.0.0.1");
                            return request1;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.userId").value("user-1"));
    }

    @Test
    void shouldLogout() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer test-token")
                        .with(request1 -> {
                            request1.setRemoteAddr("10.0.0.1");
                            return request1;
                        }))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRefreshToken() throws Exception {
        var request = new RefreshTokenRequest("old-refresh-token");
        var response = new LoginResponse(
                "new-access-token", "new-refresh-token", "Bearer", 900L, "session-2",
                "user-1", List.of("CUSTOMER"), List.of("PRODUCT_READ"),
                true, false, null);
        when(identityService.refreshToken(anyString(), anyString(), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("User-Agent", "Mozilla/5.0")
                        .with(request1 -> {
                            request1.setRemoteAddr("10.0.0.1");
                            return request1;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"));
    }

    @Test
    void shouldGenerateOtp() throws Exception {
        var request = new OtpRequest("user-1", "LOGIN");
        var otp = new OtpCode(1L, "user-1", "123456", OtpPurpose.LOGIN,
                Instant.now().plusSeconds(300), 5, 0, false, Instant.now());
        when(identityService.generateOtp("user-1", OtpPurpose.LOGIN)).thenReturn(otp);

        mockMvc.perform(post("/auth/otp/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent"))
                .andExpect(jsonPath("$.expiresIn").value("5 minutes"));
    }

    @Test
    void shouldVerifyOtp() throws Exception {
        var request = new OtpVerifyRequest("user-1", "123456", "LOGIN");
        var response = new LoginResponse(
                "otp-access-token", "otp-refresh-token", "Bearer", 900L, "session-3",
                "user-1", List.of("CUSTOMER"), List.of("PRODUCT_READ"),
                true, false, null);
        when(identityService.verifyOtpAndLogin(anyString(), anyString(), any(OtpPurpose.class), anyString(), anyString()))
                .thenReturn(response);

        mockMvc.perform(post("/auth/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("User-Agent", "Mozilla/5.0")
                        .with(request1 -> {
                            request1.setRemoteAddr("10.0.0.1");
                            return request1;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.accessToken").value("otp-access-token"));
    }
}
