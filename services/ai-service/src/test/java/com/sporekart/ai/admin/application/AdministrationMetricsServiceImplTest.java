package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.domain.AdminOperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministrationMetricsServiceImplTest {

    private AdministrationMetricsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AdministrationMetricsServiceImpl();
    }

    @Test
    void testInitialCountsAreZero() {
        assertEquals(0, service.getTotalOperations());
        assertEquals(0, service.getConfigurationChanges());
        assertEquals(0, service.getRollbackCount());
        assertEquals(0, service.getFeatureFlagChanges());
    }

    @Test
    void testRecordOperation() {
        service.recordOperation(AdminOperationType.CONFIG_UPDATE);
        assertEquals(1, service.getTotalOperations());
    }

    @Test
    void testRecordConfigurationChange() {
        service.recordConfigurationChange();
        assertEquals(1, service.getConfigurationChanges());
    }

    @Test
    void testRecordRollback() {
        service.recordRollback();
        assertEquals(1, service.getRollbackCount());
    }

    @Test
    void testRecordFeatureFlagChange() {
        service.recordFeatureFlagChange();
        assertEquals(1, service.getFeatureFlagChanges());
    }

    @Test
    void testGetOperationDistribution() {
        service.recordOperation(AdminOperationType.CONFIG_UPDATE);
        service.recordOperation(AdminOperationType.CONFIG_UPDATE);
        service.recordOperation(AdminOperationType.FEATURE_FLAG_CHANGE);

        var dist = service.getOperationDistribution();
        assertEquals(2, dist.get(AdminOperationType.CONFIG_UPDATE.name()).longValue());
        assertEquals(1, dist.get(AdminOperationType.FEATURE_FLAG_CHANGE.name()).longValue());
    }

    @Test
    void testGetStatistics() {
        service.recordOperation(AdminOperationType.MODULE_ENABLE);
        service.recordConfigurationChange();
        service.recordRollback();
        service.recordFeatureFlagChange();

        var stats = service.getStatistics();
        assertEquals(1L, stats.get("totalOperations"));
        assertEquals(1L, stats.get("configurationChanges"));
        assertEquals(1L, stats.get("rollbacks"));
        assertEquals(1L, stats.get("featureFlagChanges"));
        assertEquals("operational", stats.get("status"));
        assertNotNull(stats.get("operationsByType"));
    }

    @Test
    void testMultipleRecords() {
        for (int i = 0; i < 5; i++) {
            service.recordOperation(AdminOperationType.CONFIG_UPDATE);
        }
        assertEquals(5, service.getTotalOperations());
    }
}
