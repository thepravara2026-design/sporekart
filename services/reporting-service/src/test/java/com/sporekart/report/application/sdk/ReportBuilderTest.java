package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ReportBuilderTest {
    @Test
    void shouldBuildReportWithAllFields() {
        Report report = ReportBuilder.builder()
            .withTitle("Revenue Summary")
            .withDescription("Weekly revenue breakdown")
            .withType(ReportType.WEEKLY)
            .withCategory(ReportCategory.REVENUE)
            .withOwner("Finance")
            .withSummary("Revenue up 12%")
            .withBusinessHealth("HEALTHY")
            .addRecommendation("Expand wholesale")
            .addRisk("Concentration risk")
            .addKpi("revenue", 712000.0)
            .addMetric("transactions", 8450)
            .withTemplateId("revenue-template")
            .build();
        assertNotNull(report.id());
        assertEquals("Revenue Summary", report.title());
        assertEquals(ReportType.WEEKLY, report.type());
        assertEquals(ReportCategory.REVENUE, report.category());
        assertEquals("Finance", report.owner());
        assertEquals(1, report.recommendations().size());
        assertEquals(1, report.risks().size());
        assertEquals(1, report.kpis().size());
        assertEquals(1, report.supportingMetrics().size());
        assertEquals(ReportStatus.GENERATED, report.status());
    }

    @Test
    void shouldFailWithoutTitle() {
        assertThrows(IllegalStateException.class, () ->
            ReportBuilder.builder().withOwner("CEO").build());
    }

    @Test
    void shouldFailWithoutOwner() {
        assertThrows(IllegalStateException.class, () ->
            ReportBuilder.builder().withTitle("Test").build());
    }

    @Test
    void shouldUseDefaults() {
        Report report = ReportBuilder.builder()
            .withTitle("Test")
            .withOwner("CEO")
            .build();
        assertEquals(ReportType.EXECUTIVE, report.type());
        assertEquals(ReportCategory.EXECUTIVE, report.category());
    }

    @Test
    void shouldSupportMultipleRecommendations() {
        Report report = ReportBuilder.builder()
            .withTitle("Test").withOwner("CEO")
            .withRecommendations(List.of("Rec 1", "Rec 2", "Rec 3"))
            .build();
        assertEquals(3, report.recommendations().size());
    }

    @Test
    void shouldSupportMultipleKpis() {
        Report report = ReportBuilder.builder()
            .withTitle("Test").withOwner("CEO")
            .withKpis(Map.of("k1", 1, "k2", 2, "k3", 3))
            .build();
        assertEquals(3, report.kpis().size());
    }

    @Test
    void shouldSupportChainedAddKpi() {
        Report report = ReportBuilder.builder()
            .withTitle("Test").withOwner("CEO")
            .addKpi("a", 1).addKpi("b", 2)
            .build();
        assertEquals(2, report.kpis().size());
    }

    @Test
    void shouldSupportChainedAddRecommendation() {
        Report report = ReportBuilder.builder()
            .withTitle("Test").withOwner("CEO")
            .addRecommendation("R1").addRecommendation("R2")
            .build();
        assertEquals(2, report.recommendations().size());
    }
}
