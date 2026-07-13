package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

class DecisionConfigurationServiceImplTest {

    private DecisionConfigurationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionConfigurationServiceImpl();
    }

    @Test
    void setAndGetRoundTrip() {
        service.setConfig("test.key", "test-value", "test description");
        Optional<String> result = service.getConfig("test.key");
        assertTrue(result.isPresent());
        assertEquals("test-value", result.get());
    }

    @Test
    void getConfigReturnsEmptyForUnknownKey() {
        Optional<String> result = service.getConfig("unknown.key");
        assertTrue(result.isEmpty());
    }
}
