package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.KpiStatus;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceKPIEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceKPIRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KPIServiceImpl implements KPIService {

    private final GovernanceKPIRepository repository;
    private final AnalyticsAuditService auditService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GovernanceKPI calculateKPI(String name, String module, double currentValue, double targetValue, double threshold) {
        var status = evaluateKPIStatus(currentValue, targetValue, threshold);

        var entity = new GovernanceKPIEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(name);
        entity.setDescription("KPI for " + name);
        entity.setModule(module);
        entity.setCurrentValue(currentValue);
        entity.setTargetValue(targetValue);
        entity.setThreshold(threshold);
        entity.setStatus(status.name());
        try {
            entity.setDimensions(objectMapper.writeValueAsString(Map.of(
                    "currentValue", currentValue, "targetValue", targetValue, "threshold", threshold
            )));
        } catch (Exception e) {
            entity.setDimensions("{}");
        }
        entity.setCalculatedAt(java.time.OffsetDateTime.now());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        auditService.recordAudit("KPI_CALCULATED", "GovernanceKPI", saved.getId(),
                null, Map.of("name", name, "module", module, "status", status.name()), "system");
        log.info("Calculated KPI '{}' for module '{}': status={}, current={}, target={}", name, module, status, currentValue, targetValue);
        return toDomain(saved);
    }

    @Override
    public List<GovernanceKPI> getAllKPIs() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceKPI> getKPIsByModule(String module) {
        return repository.findByModule(module).stream().map(this::toDomain).toList();
    }

    @Override
    public KpiStatus evaluateKPIStatus(double currentValue, double targetValue, double threshold) {
        if (currentValue >= targetValue) {
            return KpiStatus.ON_TRACK;
        }
        if (currentValue >= targetValue * threshold) {
            return KpiStatus.AT_RISK;
        }
        return KpiStatus.CRITICAL;
    }

    @Override
    public Map<String, Object> getKPISummary() {
        var all = repository.findAll();
        var onTrack = 0L;
        var atRisk = 0L;
        var critical = 0L;
        var byModule = new HashMap<String, Map<String, Long>>();

        for (var entity : all) {
            var moduleSummary = byModule.computeIfAbsent(entity.getModule(), k -> {
                var m = new HashMap<String, Long>();
                m.put("onTrack", 0L);
                m.put("atRisk", 0L);
                m.put("critical", 0L);
                return m;
            });
            switch (KpiStatus.valueOf(entity.getStatus())) {
                case ON_TRACK -> { moduleSummary.put("onTrack", moduleSummary.get("onTrack") + 1); onTrack++; }
                case AT_RISK -> { moduleSummary.put("atRisk", moduleSummary.get("atRisk") + 1); atRisk++; }
                case CRITICAL -> { moduleSummary.put("critical", moduleSummary.get("critical") + 1); critical++; }
            }
        }

        var summary = new HashMap<String, Object>();
        summary.put("modules", byModule);
        summary.put("totals", Map.of("onTrack", onTrack, "atRisk", atRisk, "critical", critical));
        return summary;
    }

    @SuppressWarnings("unchecked")
    private GovernanceKPI toDomain(GovernanceKPIEntity entity) {
        Map<String, Object> dimensionsMap = Map.of();
        try {
            dimensionsMap = objectMapper.readValue(entity.getDimensions(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize dimensions for KPI {}: {}", entity.getId(), e.getMessage());
        }
        return new GovernanceKPI(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getModule(),
                entity.getCurrentValue(),
                entity.getTargetValue(),
                entity.getThreshold(),
                KpiStatus.valueOf(entity.getStatus()),
                dimensionsMap,
                entity.getCalculatedAt().toInstant()
        );
    }
}
