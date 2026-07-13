package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.infrastructure.persistence.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceHealthServiceImplTest {

    @Mock private GovernanceRepository governanceRepository;
    @Mock private GovernanceConfigurationRepository configRepository;
    @Mock private GovernanceAuditRepository auditRepository;

    @Test
    void testCheckHealth() {
        when(governanceRepository.count()).thenReturn(5L);

        GovernanceHealthServiceImpl healthService = new GovernanceHealthServiceImpl(
            governanceRepository, configRepository, auditRepository);

        Map<String, Object> health = healthService.checkHealth();
        assertEquals("UP", health.get("status"));
        assertEquals("governance", health.get("service"));
    }

    @Test
    void testIsOperational_WhenDbWorks() {
        when(governanceRepository.count()).thenReturn(1L);
        GovernanceHealthServiceImpl healthService = new GovernanceHealthServiceImpl(
            governanceRepository, configRepository, auditRepository);
        assertTrue(healthService.isOperational());
    }

    @Test
    void testIsOperational_WhenDbFails() {
        when(governanceRepository.count()).thenThrow(new RuntimeException("DB down"));
        GovernanceHealthServiceImpl healthService = new GovernanceHealthServiceImpl(
            governanceRepository, configRepository, auditRepository);
        assertFalse(healthService.isOperational());
    }

    @Test
    void testGetMetrics() {
        when(governanceRepository.count()).thenReturn(3L);
        GovernanceHealthServiceImpl healthService = new GovernanceHealthServiceImpl(
            governanceRepository, configRepository, auditRepository);
        Map<String, Object> metrics = healthService.getMetrics();
        assertEquals(3L, metrics.get("totalPolicies"));
    }
}
