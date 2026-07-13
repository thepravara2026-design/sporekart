package com.sporekart.ai.admin.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class MaintenanceStatusTest {

    @ParameterizedTest
    @EnumSource(MaintenanceStatus.class)
    void testAllValuesExist(MaintenanceStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
    }

    @ParameterizedTest
    @EnumSource(MaintenanceStatus.class)
    void testValueOf(MaintenanceStatus status) {
        assertSame(status, MaintenanceStatus.valueOf(status.name()));
    }
}
