package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.automation.domain.ExpirationPolicy;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpirationManagerImplTest {

    private ExpirationManagerImpl expirationManager;

    @BeforeEach
    void setUp() {
        expirationManager = new ExpirationManagerImpl();
    }

    @Test
    void findExpiredEntitiesShouldReturnExpiredPolicies() {
        var id = UUID.randomUUID();
        var policy = new ExpirationPolicy(
            id, "test-policy", "policy", 100, "ARCHIVE", true,
            Instant.now().minus(Duration.ofDays(1)), Instant.now()
        );
        expirationManager.getExpirationPolicies().add(policy);

        var expired = expirationManager.findExpiredEntities();
        assertFalse(expired.isEmpty());
        assertEquals(id, expired.getFirst().id());
    }

    @Test
    void findExpiredEntitiesShouldSkipNonExpired() {
        var policy = new ExpirationPolicy(
            UUID.randomUUID(), "test", "policy", 86400000, "ARCHIVE", true,
            Instant.now(), Instant.now()
        );
        expirationManager.getExpirationPolicies().add(policy);

        var expired = expirationManager.findExpiredEntities();
        assertTrue(expired.isEmpty());
    }

    @Test
    void findExpiredEntitiesShouldSkipDisabled() {
        var policy = new ExpirationPolicy(
            UUID.randomUUID(), "test", "policy", 0, "ARCHIVE", false,
            Instant.now().minus(Duration.ofDays(1)), Instant.now()
        );
        expirationManager.getExpirationPolicies().add(policy);

        var expired = expirationManager.findExpiredEntities();
        assertTrue(expired.isEmpty());
    }

    @Test
    void applyExpirationShouldNotThrow() {
        assertDoesNotThrow(() -> expirationManager.applyExpiration(UUID.randomUUID(), "policy"));
    }

    @Test
    void getExpirationPoliciesShouldReturnAll() {
        var policy = new ExpirationPolicy(
            UUID.randomUUID(), "test", "policy", 100, "ARCHIVE", true,
            Instant.now(), Instant.now()
        );
        expirationManager.getExpirationPolicies().add(policy);

        var policies = expirationManager.getExpirationPolicies();
        assertEquals(1, policies.size());
    }
}
