package com.sporekart.ai.analytics.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceReportTest {

    @Test
    void testRecordConstruction() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var data = Map.of("key", "value");
        var summary = Map.of("count", 10);
        var report = new GovernanceReport(id, ReportType.COMPLIANCE_REPORT, "Test Report", "Desc", data, summary, now, id);

        assertEquals(id, report.id());
        assertEquals(ReportType.COMPLIANCE_REPORT, report.type());
        assertEquals("Test Report", report.title());
        assertEquals("Desc", report.description());
        assertEquals(data, report.data());
        assertEquals(summary, report.summary());
        assertEquals(now, report.generatedAt());
        assertEquals(id, report.generatedBy());
    }
}
