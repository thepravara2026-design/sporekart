package com.sporekart.report.application.engine;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TemplateEngineTest {
    private TemplateEngine engine;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        engine = new TemplateEngine(repository);
    }

    @Test
    void shouldGenerateAllTemplates() {
        List<ReportTemplate> templates = engine.generateAllTemplates();
        assertFalse(templates.isEmpty());
        assertTrue(templates.size() >= 14);
    }

    @Test
    void shouldCreateTemplate() {
        ReportTemplate t = engine.createTemplate("Test Template", "Desc", ReportCategory.REVENUE,
            ReportType.WEEKLY, "Finance", List.of("Section 1"), java.util.Map.of("key", "value"));
        assertNotNull(t.id());
        assertEquals("Test Template", t.name());
    }

    @Test
    void shouldGetTemplateById() {
        ReportTemplate created = engine.createTemplate("Get Test", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), java.util.Map.of());
        ReportTemplate found = engine.getTemplate(created.id());
        assertEquals(created.id(), found.id());
    }

    @Test
    void shouldGetAllTemplates() {
        engine.generateAllTemplates();
        assertFalse(engine.getAllTemplates().isEmpty());
    }

    @Test
    void shouldGetActiveTemplates() {
        engine.generateAllTemplates();
        List<ReportTemplate> active = engine.getActiveTemplates();
        assertFalse(active.isEmpty());
        assertTrue(active.stream().allMatch(ReportTemplate::active));
    }

    @Test
    void shouldDeactivateTemplate() {
        ReportTemplate t = engine.createTemplate("Deact", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), java.util.Map.of());
        ReportTemplate deactivated = engine.deactivateTemplate(t.id());
        assertFalse(deactivated.active());
    }

    @Test
    void shouldActivateTemplate() {
        ReportTemplate t = engine.createTemplate("Act", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), java.util.Map.of());
        engine.deactivateTemplate(t.id());
        ReportTemplate activated = engine.activateTemplate(t.id());
        assertTrue(activated.active());
    }

    @Test
    void shouldUpdateTemplate() {
        ReportTemplate t = engine.createTemplate("Old", "", ReportCategory.EXECUTIVE,
            ReportType.EXECUTIVE, "CEO", List.of(), java.util.Map.of());
        ReportTemplate updated = engine.updateTemplate(t.id(), "New", "New desc", List.of("S1"), null);
        assertEquals("New", updated.name());
        assertEquals("New desc", updated.description());
    }
}
