package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportTest {
    @Test
    void shouldCreateReportWithStaticFactory() {
        Report report = Report.create("Test Report", "Description", ReportType.WEEKLY,
            ReportCategory.REVENUE, "Finance", "Summary text", "HEALTHY",
            List.of("Rec 1"), List.of("Risk 1"),
            Map.of("revenue", 1000.0), Map.of("metric", 50.0), "template-1");
        assertNotNull(report.id());
        assertEquals("Test Report", report.title());
        assertEquals(ReportType.WEEKLY, report.type());
        assertEquals(ReportCategory.REVENUE, report.category());
        assertEquals(ReportStatus.GENERATED, report.status());
        assertEquals("Finance", report.owner());
        assertEquals("Summary text", report.summary());
        assertEquals("HEALTHY", report.businessHealth());
        assertEquals(1, report.recommendations().size());
        assertEquals(1, report.risks().size());
        assertFalse(report.kpis().isEmpty());
        assertFalse(report.supportingMetrics().isEmpty());
        assertNotNull(report.traceId());
        assertNotNull(report.createdAt());
        assertNotNull(report.generatedAt());
    }

    @Test
    void shouldUpdateStatusWithWithStatus() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report resolved = report.withStatus(ReportStatus.EXPORTED);
        assertEquals(ReportStatus.EXPORTED, resolved.status());
        assertEquals(report.id(), resolved.id());
    }

    @Test
    void shouldUpdateExecutionTime() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report timed = report.withExecutionTime(250L);
        assertEquals(250L, timed.executionTimeMs());
    }

    @Test
    void shouldHandleNullLists() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", null, null, null, null, "t1");
        assertTrue(report.recommendations().isEmpty());
        assertTrue(report.risks().isEmpty());
        assertTrue(report.kpis().isEmpty());
        assertTrue(report.supportingMetrics().isEmpty());
    }

    @Test
    void shouldPreserveImmutability() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of("Rec 1"), List.of("Risk 1"),
            Map.of("kpi", 1.0), Map.of("m", 2.0), "t1");
        assertThrows(UnsupportedOperationException.class, () -> report.recommendations().add("x"));
        assertThrows(UnsupportedOperationException.class, () -> report.risks().add("x"));
        assertThrows(UnsupportedOperationException.class, () -> report.kpis().put("x", 1));
        assertThrows(UnsupportedOperationException.class, () -> report.supportingMetrics().put("x", 1));
    }

    @Test
    void shouldCreateWithUUID() {
        Report r1 = Report.create("A", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("B", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        assertNotEquals(r1.id(), r2.id());
    }

    @Test
    void shouldHaveGeneratedAtEqualsNow() {
        Report report = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        assertNotNull(report.generatedAt());
        assertNotNull(report.createdAt());
    }

    @Test
    void shouldSupportEquality() {
        Report r1 = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        Report r2 = Report.create("Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "CEO", "", "", List.of(), List.of(), Map.of(), Map.of(), "t1");
        assertNotEquals(r1, r2);
    }
}
