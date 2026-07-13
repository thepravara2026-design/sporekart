package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.domain.DashboardWidget;
import com.sporekart.ai.analytics.domain.GovernanceDashboard;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceDashboardEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceDashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock private GovernanceDashboardRepository repository;
    private ObjectMapper objectMapper;
    private DashboardServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new DashboardServiceImpl(repository, objectMapper);
    }

    @Test
    void testGetDashboardByName() {
        var entity = createEntity();
        when(repository.findByName("test-dashboard")).thenReturn(Optional.of(entity));

        GovernanceDashboard dashboard = service.getDashboard("test-dashboard");

        assertNotNull(dashboard);
        assertEquals("test-dashboard", dashboard.name());
    }

    @Test
    void testGetDashboardById() {
        var entity = createEntity();
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        GovernanceDashboard dashboard = service.getDashboard(entity.getId());

        assertNotNull(dashboard);
        assertEquals(entity.getId(), dashboard.id());
    }

    @Test
    void testCreateDashboard() {
        var input = new GovernanceDashboard(null, "new-dash", "desc", List.of(), Map.of(), null, null);
        var saved = createEntity();
        when(repository.save(any(GovernanceDashboardEntity.class))).thenReturn(saved);

        GovernanceDashboard result = service.createDashboard(input);

        assertNotNull(result);
        assertNotNull(result.id());
    }

    @Test
    void testUpdateDashboard() {
        var existing = createEntity();
        var input = new GovernanceDashboard(existing.getId(), "updated", "updated desc", List.of(), Map.of("k", "v"), null, null);
        when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(repository.save(any(GovernanceDashboardEntity.class))).thenReturn(existing);

        GovernanceDashboard result = service.updateDashboard(existing.getId(), input);

        assertNotNull(result);
    }

    @Test
    void testGetAllDashboards() {
        when(repository.findAll()).thenReturn(List.of(createEntity()));

        List<GovernanceDashboard> dashboards = service.getAllDashboards();

        assertEquals(1, dashboards.size());
    }

    @Test
    void testAddWidget() {
        var entity = createEntity();
        var widget = new DashboardWidget(UUID.randomUUID(), entity.getId(), "Widget", "chart", "metric1", Map.of(), 0, 1, 1);
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        service.addWidget(entity.getId(), widget);

        verify(repository).save(entity);
    }

    @Test
    void testRemoveWidget() {
        var entity = createEntity();
        var widgetId = UUID.randomUUID();
        var widget = new DashboardWidget(widgetId, entity.getId(), "W", "chart", "m", Map.of(), 0, 1, 1);
        entity.setWidgets(objectMapper.writeValueAsString(List.of(widget)));
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        service.removeWidget(entity.getId(), widgetId);

        verify(repository).save(entity);
    }

    private GovernanceDashboardEntity createEntity() {
        var entity = new GovernanceDashboardEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test-dashboard");
        entity.setDescription("desc");
        entity.setWidgets("[]");
        entity.setConfiguration("{}");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }
}
