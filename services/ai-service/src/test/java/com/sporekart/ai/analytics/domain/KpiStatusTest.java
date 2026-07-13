package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class KpiStatusTest {

    @ParameterizedTest
    @EnumSource(KpiStatus.class)
    void testAllValuesExist(KpiStatus value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
