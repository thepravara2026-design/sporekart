package com.sporekart.report.application.sdk;

import com.sporekart.report.application.engine.TemplateEngine;
import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TemplateRuntime {
    private final TemplateEngine templateEngine;
    private final ReportRepositoryPort repository;

    public TemplateRuntime(TemplateEngine templateEngine, ReportRepositoryPort repository) {
        this.templateEngine = templateEngine;
        this.repository = repository;
    }

    public List<ReportTemplate> generateAllTemplates() {
        List<ReportTemplate> templates = templateEngine.generateAllTemplates();
        templates.forEach(repository::saveTemplate);
        return templates;
    }

    public ReportTemplate createTemplate(String name, String description, ReportCategory category,
                                          ReportType type, String owner, List<String> sections,
                                          Map<String, Object> defaultConfig) {
        return templateEngine.createTemplate(name, description, category, type, owner, sections, defaultConfig);
    }

    public List<ReportTemplate> getAllTemplates() { return repository.findAllTemplates(); }
    public Optional<ReportTemplate> findById(String id) { return repository.findTemplateById(id); }
    public List<ReportTemplate> getActive() { return repository.findActiveTemplates(); }
    public ReportTemplate updateTemplate(String id, String name, String description,
                                          List<String> sections, Map<String, Object> defaultConfig) {
        return templateEngine.updateTemplate(id, name, description, sections, defaultConfig);
    }
    public ReportTemplate deactivate(String id) { return templateEngine.deactivateTemplate(id); }
    public ReportTemplate activate(String id) { return templateEngine.activateTemplate(id); }
}
