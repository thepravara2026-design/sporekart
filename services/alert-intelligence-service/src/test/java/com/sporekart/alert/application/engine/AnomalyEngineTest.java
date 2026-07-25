package com.sporekart.alert.application.engine;

import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnomalyEngineTest {

    @Mock private AlertRepositoryPort repository;
    private AnomalyEngine engine;

    @BeforeEach
    void setUp() {
        when(repository.saveAnomaly(any())).thenAnswer(i -> i.getArgument(0));
        engine = new AnomalyEngine(repository);
    }

    @Test
    void detectAllAnomaliesShouldReturnAnomalies() {
        var anomalies = engine.detectAllAnomalies();
        assertNotNull(anomalies);
        assertFalse(anomalies.isEmpty());
        assertTrue(anomalies.size() >= 8);
    }

    @Test
    void detectAllAnomaliesShouldCoverMultipleTypes() {
        var anomalies = engine.detectAllAnomalies();
        var types = anomalies.stream().map(a -> a.type()).distinct().toList();
        assertTrue(types.size() >= 6);
    }
}
