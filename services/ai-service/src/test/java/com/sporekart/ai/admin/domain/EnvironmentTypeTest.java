package com.sporekart.ai.admin.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTypeTest {

    @ParameterizedTest
    @EnumSource(EnvironmentType.class)
    void testAllValuesExist(EnvironmentType type) {
        assertNotNull(type);
        assertNotNull(type.name());
    }

    @ParameterizedTest
    @EnumSource(EnvironmentType.class)
    void testValueOf(EnvironmentType type) {
        assertSame(type, EnvironmentType.valueOf(type.name()));
    }
}
