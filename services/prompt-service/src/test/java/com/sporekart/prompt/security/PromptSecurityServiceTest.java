package com.sporekart.prompt.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PromptSecurityServiceTest {

    private PromptSecurityService securityService;
    private UUID userId;

    @BeforeEach
    void setUp() {
        securityService = new PromptSecurityService();
        userId = UUID.randomUUID();
    }

    @Test
    void shouldIdentifyAdmin() {
        assertThat(securityService.isAdmin(userId, Set.of("AI_ADMIN"))).isTrue();
        assertThat(securityService.isAdmin(userId, Set.of("PLATFORM_ADMIN"))).isTrue();
        assertThat(securityService.isAdmin(userId, Set.of("VIEWER"))).isFalse();
    }

    @Test
    void shouldIdentifyReviewer() {
        assertThat(securityService.isReviewer(userId, Set.of("AI_REVIEWER"))).isTrue();
        assertThat(securityService.isReviewer(userId, Set.of("AI_ADMIN"))).isTrue();
        assertThat(securityService.isReviewer(userId, Set.of("DEVELOPER"))).isFalse();
    }

    @Test
    void shouldIdentifyDeveloper() {
        assertThat(securityService.isDeveloper(userId, Set.of("AI_DEVELOPER"))).isTrue();
        assertThat(securityService.isDeveloper(userId, Set.of("VIEWER"))).isFalse();
    }

    @Test
    void shouldAllowViewers() {
        assertThat(securityService.canView(userId, Set.of("VIEWER"))).isTrue();
        assertThat(securityService.canView(userId, Set.of("AI_DEVELOPER"))).isTrue();
    }

    @Test
    void shouldSanitizeInput() {
        assertThat(securityService.sanitizeInput("<script>alert('xss')</script>"))
                .doesNotContain("<script>");
        assertThat(securityService.sanitizeInput("javascript:alert(1)"))
                .doesNotContain("javascript:");
        assertThat(securityService.sanitizeInput(null)).isNull();
        assertThat(securityService.sanitizeInput("safe input")).isEqualTo("safe input");
    }
}
