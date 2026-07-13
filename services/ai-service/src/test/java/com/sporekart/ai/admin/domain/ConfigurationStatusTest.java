package com.sporekart.ai.admin.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationStatusTest {

    @ParameterizedTest
    @EnumSource(ConfigurationStatus.class)
    void testAllValuesExist(ConfigurationStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
    }

    @ParameterizedTest
    @EnumSource(ConfigurationStatus.class)
    void testValueOf(ConfigurationStatus status) {
        assertSame(status, ConfigurationStatus.valueOf(status.name()));
    }
}
