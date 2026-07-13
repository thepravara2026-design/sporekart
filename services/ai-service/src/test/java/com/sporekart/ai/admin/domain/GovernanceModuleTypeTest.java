package com.sporekart.ai.admin.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceModuleTypeTest {

    @ParameterizedTest
    @EnumSource(GovernanceModuleType.class)
    void testAllValuesExist(GovernanceModuleType type) {
        assertNotNull(type);
        assertNotNull(type.name());
    }

    @ParameterizedTest
    @EnumSource(GovernanceModuleType.class)
    void testValueOf(GovernanceModuleType type) {
        assertSame(type, GovernanceModuleType.valueOf(type.name()));
    }
}
