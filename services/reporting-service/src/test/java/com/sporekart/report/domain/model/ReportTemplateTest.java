package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ReportTemplateTest {
    @Test
    void shouldCreateTemplateWithStaticFactory() {
        ReportTemplate t = ReportTemplate.create("Executive Summary", "Template desc",
            ReportCategory.EXECUTIVE, ReportType.EXECUTIVE, "CEO",
            List.of("Overview", "Metrics"), Map.of("theme", "dark"));
        assertNotNull(t.id());
        assertEquals("Executive Summary", t.name());
        assertEquals(ReportCategory.EXECUTIVE, t.category());
        assertTrue(t.active());
        assertEquals(2, t.sections().size());
        assertFalse(t.defaultConfig().isEmpty());
    }

    @Test
    void shouldToggleActive() {
        ReportTemplate t = ReportTemplate.create("Test", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "Owner", List.of(), Map.of());
        ReportTemplate inactive = t.withActive(false);
        assertFalse(inactive.active());
        assertTrue(t.active());
    }

    @Test
    void shouldHandleNullLists() {
        ReportTemplate t = ReportTemplate.create("Test", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "Owner", null, null);
        assertTrue(t.sections().isEmpty());
        assertTrue(t.defaultConfig().isEmpty());
    }
}
