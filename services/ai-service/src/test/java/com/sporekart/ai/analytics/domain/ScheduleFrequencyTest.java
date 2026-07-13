package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleFrequencyTest {

    @ParameterizedTest
    @EnumSource(ScheduleFrequency.class)
    void testAllValuesExist(ScheduleFrequency value) {
        assertNotNull(value);
        assertNotNull(value.name());
    }
}
