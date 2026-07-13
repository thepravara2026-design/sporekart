package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.api.DecisionMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class DecisionHealthServiceImplTest {

    @Mock private DecisionMetricsService metricsService;

    private DecisionHealthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionHealthServiceImpl(metricsService);
    }

    @Test
    void checkHealthReturnsUpStatus() {
        when(metricsService.getDetailedMetrics()).thenReturn(Map.of("total", 0L));
        when(metricsService.getTotalDecisions()).thenReturn(0L);

        Map<String, Object> health = service.checkHealth();

        assertEquals("UP", health.get("status"));
        assertEquals("decision-engine", health.get("service"));
        assertTrue(health.containsKey("timestamp"));
        assertTrue(health.containsKey("details"));
    }

    @Test
    void isOperationalReturnsTrue() {
        assertTrue(service.isOperational());
    }
}
