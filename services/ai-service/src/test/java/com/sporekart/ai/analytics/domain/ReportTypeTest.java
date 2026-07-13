package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class ReportTypeTest {

    @ParameterizedTest
    @EnumSource(ReportType.class)
    void testAllValuesExist(ReportType value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
