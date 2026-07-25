package com.sporekart.report.application.sdk;

import com.sporekart.report.application.engine.TemplateEngine;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TemplateRuntimeTest {
    private TemplateRuntime runtime;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        TemplateEngine engine = new TemplateEngine(repository);
        runtime = new TemplateRuntime(engine, repository);
    }

    @Test
    void shouldGenerateAllTemplates() {
        List<ReportTemplate> templates = runtime.generateAllTemplates();
        assertFalse(templates.isEmpty());
    }

    @Test
    void shouldCreateTemplate() {
        ReportTemplate t = runtime.createTemplate("Test", "Desc", ReportCategory.REVENUE,
            ReportType.WEEKLY, "Finance", List.of("S1"), java.util.Map.of());
        assertNotNull(t.id());
    }

    @Test
    void shouldGetAllTemplates() {
        runtime.generateAllTemplates();
        assertFalse(runtime.getAllTemplates().isEmpty());
    }

    @Test
    void shouldGetActiveTemplates() {
        runtime.generateAllTemplates();
        assertFalse(runtime.getActive().isEmpty());
    }

    @Test
    void shouldDeactivateTemplate() {
        runtime.generateAllTemplates();
        ReportTemplate t = runtime.getAllTemplates().get(0);
        ReportTemplate deactivated = runtime.deactivate(t.id());
        assertFalse(deactivated.active());
    }

    @Test
    void shouldActivateTemplate() {
        runtime.generateAllTemplates();
        ReportTemplate t = runtime.getAllTemplates().get(0);
        runtime.deactivate(t.id());
        ReportTemplate activated = runtime.activate(t.id());
        assertTrue(activated.active());
    }
}
