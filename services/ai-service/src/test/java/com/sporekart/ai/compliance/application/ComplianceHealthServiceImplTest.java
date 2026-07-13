package com.sporekart.ai.compliance.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceHealthServiceImplTest {

    private ComplianceHealthServiceImpl healthService;

    @BeforeEach
    void setUp() {
        healthService = new ComplianceHealthServiceImpl();
    }

    @Test
    void testIsHealthy() {
        assertTrue(healthService.isHealthy());
    }

    @Test
    void testGetHealthDetails() {
        Map<String, Object> details = healthService.getHealthDetails();

        assertNotNull(details);
        assertEquals("UP", details.get("status"));
        assertEquals("compliance", details.get("service"));
        assertNotNull(details.get("timestamp"));
    }

    @Test
    void testGetReadiness() {
        Map<String, Object> readiness = healthService.getReadiness();

        assertNotNull(readiness);
        assertTrue((Boolean) readiness.get("ready"));
        assertEquals("checked", readiness.get("dependencies"));
    }
}
