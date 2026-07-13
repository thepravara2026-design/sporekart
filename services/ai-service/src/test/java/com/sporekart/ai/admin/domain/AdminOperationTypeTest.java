package com.sporekart.ai.admin.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class AdminOperationTypeTest {

    @ParameterizedTest
    @EnumSource(AdminOperationType.class)
    void testAllValuesExist(AdminOperationType type) {
        assertNotNull(type);
        assertNotNull(type.name());
    }

    @ParameterizedTest
    @EnumSource(AdminOperationType.class)
    void testValueOf(AdminOperationType type) {
        assertSame(type, AdminOperationType.valueOf(type.name()));
    }
}
