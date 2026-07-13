package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class TrendDirectionTest {

    @ParameterizedTest
    @EnumSource(TrendDirection.class)
    void testAllValuesExist(TrendDirection value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
