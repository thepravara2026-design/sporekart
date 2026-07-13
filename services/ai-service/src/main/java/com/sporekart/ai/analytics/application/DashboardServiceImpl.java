package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.DashboardService;
import com.sporekart.ai.analytics.domain.DashboardWidget;
import com.sporekart.ai.analytics.domain.GovernanceDashboard;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceDashboardEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceDashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final GovernanceDashboardRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public GovernanceDashboard getDashboard(String name) {
        return repository.findByName(name).map(this::toDomain).orElse(null);
    }

    @Override
    public GovernanceDashboard getDashboard(UUID id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public GovernanceDashboard createDashboard(GovernanceDashboard dashboard) {
        var entity = new GovernanceDashboardEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(dashboard.name());
        entity.setDescription(dashboard.description());
        entity.setWidgets(serializeWidgets(dashboard.widgets()));
        entity.setConfiguration(serializeMap(dashboard.configuration()));
        entity.setCreatedAt(java.time.OffsetDateTime.now());
        entity.setUpdatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        log.info("Created dashboard '{}' with id {}", saved.getName(), saved.getId());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public GovernanceDashboard updateDashboard(UUID id, GovernanceDashboard dashboard) {
        var existing = repository.findById(id).orElse(null);
        if (existing == null) {
            log.warn("Cannot update: dashboard not found {}", id);
            return null;
        }
        existing.setName(dashboard.name());
        existing.setDescription(dashboard.description());
        existing.setWidgets(serializeWidgets(dashboard.widgets()));
        existing.setConfiguration(serializeMap(dashboard.configuration()));
        existing.setUpdatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(existing);
        log.info("Updated dashboard '{}'", id);
        return toDomain(saved);
    }

    @Override
    public List<GovernanceDashboard> getAllDashboards() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void addWidget(UUID dashboardId, DashboardWidget widget) {
        var entity = repository.findById(dashboardId).orElse(null);
        if (entity == null) {
            log.warn("Cannot add widget: dashboard not found {}", dashboardId);
            return;
        }
        var widgets = deserializeWidgets(entity.getWidgets());
        var widgetWithId = new DashboardWidget(
                widget.id() != null ? widget.id() : UUID.randomUUID(),
                dashboardId,
                widget.title(),
                widget.type(),
                widget.metricName(),
                widget.configuration() != null ? widget.configuration() : Map.of(),
                widget.position(),
                widget.width(),
                widget.height()
        );
        widgets.add(widgetWithId);
        entity.setWidgets(serializeWidgets(widgets));
        entity.setUpdatedAt(java.time.OffsetDateTime.now());
        repository.save(entity);
        log.info("Added widget '{}' to dashboard {}", widget.title(), dashboardId);
    }

    @Override
    @Transactional
    public void removeWidget(UUID dashboardId, UUID widgetId) {
        var entity = repository.findById(dashboardId).orElse(null);
        if (entity == null) {
            log.warn("Cannot remove widget: dashboard not found {}", dashboardId);
            return;
        }
        var widgets = deserializeWidgets(entity.getWidgets());
        var before = widgets.size();
        widgets.removeIf(w -> w.id().equals(widgetId));
        if (widgets.size() == before) {
            log.warn("Widget {} not found in dashboard {}", widgetId, dashboardId);
            return;
        }
        entity.setWidgets(serializeWidgets(widgets));
        entity.setUpdatedAt(java.time.OffsetDateTime.now());
        repository.save(entity);
        log.info("Removed widget {} from dashboard {}", widgetId, dashboardId);
    }

    private GovernanceDashboard toDomain(GovernanceDashboardEntity entity) {
        return new GovernanceDashboard(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                deserializeWidgets(entity.getWidgets()),
                deserializeMap(entity.getConfiguration()),
                entity.getCreatedAt().toInstant(),
                entity.getUpdatedAt().toInstant()
        );
    }

    private String serializeWidgets(List<DashboardWidget> widgets) {
        try {
            return objectMapper.writeValueAsString(widgets);
        } catch (Exception e) {
            log.warn("Failed to serialize widgets: {}", e.getMessage());
            return "[]";
        }
    }

    private List<DashboardWidget> deserializeWidgets(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<DashboardWidget>>() {});
        } catch (Exception e) {
            log.warn("Failed to deserialize widgets: {}", e.getMessage());
            return List.of();
        }
    }

    private String serializeMap(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.warn("Failed to serialize map: {}", e.getMessage());
            return "{}";
        }
    }

    private Map<String, Object> deserializeMap(String json) {
        if (json == null || json.isBlank()) return Map.of();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.warn("Failed to deserialize map: {}", e.getMessage());
            return Map.of();
        }
    }
}
