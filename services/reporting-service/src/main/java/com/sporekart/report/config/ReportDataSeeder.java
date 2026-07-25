package com.sporekart.report.config;

import com.sporekart.report.application.engine.BusinessIntelligenceRuntime;
import com.sporekart.report.application.engine.ReportEngine;
import com.sporekart.report.application.engine.TemplateEngine;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ReportDataSeeder implements CommandLineRunner {
    private final ReportEngine reportEngine;
    private final TemplateEngine templateEngine;
    private final BusinessIntelligenceRuntime biRuntime;
    private final ReportRepositoryPort repository;
    private final ReportConfig config;

    public ReportDataSeeder(ReportEngine reportEngine, TemplateEngine templateEngine,
                            BusinessIntelligenceRuntime biRuntime, ReportRepositoryPort repository,
                            ReportConfig config) {
        this.reportEngine = reportEngine;
        this.templateEngine = templateEngine;
        this.biRuntime = biRuntime;
        this.repository = repository;
        this.config = config;
    }

    @Override
    public void run(String... args) {
        if (repository.findAllReports().isEmpty()) {
            seedReports();
        }
        if (repository.findAllTemplates().isEmpty()) {
            seedTemplates();
        }
        if (repository.findAllBiReports().isEmpty()) {
            seedBiReports();
        }
    }

    private void seedReports() {
        reportEngine.generateAllReports();
    }

    private void seedTemplates() {
        templateEngine.generateAllTemplates();
    }

    private void seedBiReports() {
        biRuntime.generateBiReports();
    }
}
