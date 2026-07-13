package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class MetricTypeTest {

    @ParameterizedTest
    @EnumSource(MetricType.class)
    void testAllValuesExist(MetricType value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
