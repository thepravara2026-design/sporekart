package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.api.GovernanceReportingService;
import com.sporekart.ai.analytics.domain.GovernanceReport;
import com.sporekart.ai.analytics.domain.ReportType;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GovernanceReportingServiceImpl implements GovernanceReportingService {

    private final GovernanceReportRepository repository;
    private final AnalyticsAuditService auditService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GovernanceReport generateReport(ReportType type, Map<String, Object> params) {
        var entity = new GovernanceReportEntity();
        entity.setId(UUID.randomUUID());
        entity.setType(type.name());
        entity.setTitle("Report - " + type.name());
        entity.setDescription("Auto-generated " + type.name().toLowerCase().replace("_", " "));
        try {
            entity.setData(objectMapper.writeValueAsString(Map.of("parameters", params, "generatedAt", Instant.now().toString())));
            entity.setSummary(objectMapper.writeValueAsString(Map.of("type", type.name(), "totalDataPoints", 0, "generatedBy", "system")));
        } catch (Exception e) {
            log.warn("Failed to serialize report data: {}", e.getMessage());
            entity.setData("{}");
            entity.setSummary("{}");
        }
        entity.setGeneratedAt(java.time.OffsetDateTime.now());
        entity.setGeneratedBy(UUID.randomUUID());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        auditService.recordAudit("REPORT_GENERATED", "GovernanceReport", saved.getId(), null,
                Map.of("type", type.name()), "system");
        log.info("Generated {} report: {}", type, saved.getId());
        return toDomain(saved);
    }

    @Override
    public GovernanceReport getReport(UUID id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<GovernanceReport> getReportsByType(ReportType type) {
        return repository.findByType(type.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceReport> getReportsByDateRange(Instant from, Instant to) {
        var fromLdt = LocalDateTime.ofInstant(from, ZoneId.systemDefault());
        var toLdt = LocalDateTime.ofInstant(to, ZoneId.systemDefault());
        return repository.findByGeneratedAtBetween(fromLdt, toLdt).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public GovernanceReport regenerateReport(UUID id) {
        var existing = repository.findById(id).orElse(null);
        if (existing == null) {
            log.warn("Cannot regenerate: report not found {}", id);
            return null;
        }

        var entity = new GovernanceReportEntity();
        entity.setId(UUID.randomUUID());
        entity.setType(existing.getType());
        entity.setTitle(existing.getTitle() + " (Regenerated)");
        entity.setDescription(existing.getDescription());
        try {
            entity.setData(objectMapper.writeValueAsString(Map.of(
                    "regeneratedAt", Instant.now().toString(),
                    "originalReportId", id.toString(),
                    "parameters", existing.getData()
            )));
            entity.setSummary(objectMapper.writeValueAsString(Map.of(
                    "type", existing.getType(),
                    "regenerated", true,
                    "previousGeneratedAt", existing.getGeneratedAt().toString()
            )));
        } catch (Exception e) {
            log.warn("Failed to serialize regenerated report data: {}", e.getMessage());
            entity.setData("{}");
            entity.setSummary("{}");
        }
        entity.setGeneratedAt(java.time.OffsetDateTime.now());
        entity.setGeneratedBy(existing.getGeneratedBy());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        auditService.recordAudit("REPORT_REGENERATED", "GovernanceReport", saved.getId(),
                null, Map.of("originalId", id.toString()), "system");
        log.info("Regenerated report {} -> {}", id, saved.getId());
        return toDomain(saved);
    }

    @SuppressWarnings("unchecked")
    private GovernanceReport toDomain(GovernanceReportEntity entity) {
        Map<String, Object> dataMap = Map.of();
        Map<String, Object> summaryMap = Map.of();
        try {
            dataMap = objectMapper.readValue(entity.getData(), HashMap.class);
            summaryMap = objectMapper.readValue(entity.getSummary(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize report data for {}: {}", entity.getId(), e.getMessage());
        }
        return new GovernanceReport(
                entity.getId(),
                ReportType.valueOf(entity.getType()),
                entity.getTitle(),
                entity.getDescription(),
                dataMap,
                summaryMap,
                entity.getGeneratedAt().toInstant(),
                entity.getGeneratedBy()
        );
    }
}
