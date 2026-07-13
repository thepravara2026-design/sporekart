package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class ReportFormatTest {

    @ParameterizedTest
    @EnumSource(ReportFormat.class)
    void testAllValuesExist(ReportFormat value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
