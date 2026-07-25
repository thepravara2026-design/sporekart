package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BusinessIntelligenceReportTest {
    @Test
    void shouldCreateBiReportWithStaticFactory() {
        BusinessIntelligenceReport r = BusinessIntelligenceReport.create(
            "Executive BI", "Aggregated intelligence", ReportType.EXECUTIVE,
            ReportCategory.EXECUTIVE, "Executive summary here", "HEALTHY",
            List.of("Rec 1"), List.of("Risk 1"),
            Map.of("revenue", 1000.0), Map.of("metric", 50.0),
            List.of(Map.of("source", "Analytics")));
        assertNotNull(r.id());
        assertEquals("Executive BI", r.title());
        assertEquals(ReportCategory.EXECUTIVE, r.category());
        assertEquals(1, r.recommendations().size());
        assertEquals(1, r.dataSources().size());
        assertNotNull(r.generatedAt());
    }

    @Test
    void shouldHandleNullLists() {
        BusinessIntelligenceReport r = BusinessIntelligenceReport.create(
            "Test", "", ReportType.EXECUTIVE, ReportCategory.EXECUTIVE,
            "", "", null, null, null, null, null);
        assertTrue(r.recommendations().isEmpty());
        assertTrue(r.riskFlags().isEmpty());
        assertTrue(r.aggregatedKpis().isEmpty());
        assertTrue(r.supportingMetrics().isEmpty());
        assertTrue(r.dataSources().isEmpty());
    }
}
