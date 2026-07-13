package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.KpiStatus;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceKPIEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceKPIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KPIServiceImplTest {

    @Mock private GovernanceKPIRepository repository;
    @Mock private AnalyticsAuditService auditService;
    private ObjectMapper objectMapper;
    private KPIServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new KPIServiceImpl(repository, auditService, objectMapper);
    }

    @Test
    void testCalculateKPI_OnTrack() {
        var entity = createEntity(KpiStatus.ON_TRACK);
        when(repository.save(any(GovernanceKPIEntity.class))).thenReturn(entity);

        GovernanceKPI kpi = service.calculateKPI("test-kpi", "module1", 95.0, 90.0, 0.5);

        assertNotNull(kpi);
        assertEquals("test-kpi", kpi.name());
        assertEquals(KpiStatus.ON_TRACK, kpi.status());
    }

    @Test
    void testEvaluateKPIStatus_OnTrack() {
        assertEquals(KpiStatus.ON_TRACK, service.evaluateKPIStatus(100.0, 90.0, 0.5));
        assertEquals(KpiStatus.ON_TRACK, service.evaluateKPIStatus(90.0, 90.0, 0.5));
    }

    @Test
    void testEvaluateKPIStatus_AtRisk() {
        assertEquals(KpiStatus.AT_RISK, service.evaluateKPIStatus(50.0, 90.0, 0.5));
        assertEquals(KpiStatus.AT_RISK, service.evaluateKPIStatus(45.0, 90.0, 0.5));
    }

    @Test
    void testEvaluateKPIStatus_Critical() {
        assertEquals(KpiStatus.CRITICAL, service.evaluateKPIStatus(30.0, 90.0, 0.5));
        assertEquals(KpiStatus.CRITICAL, service.evaluateKPIStatus(44.0, 90.0, 0.5));
    }

    @Test
    void testGetAllKPIs() {
        when(repository.findAll()).thenReturn(List.of(createEntity(KpiStatus.ON_TRACK)));

        List<GovernanceKPI> kpis = service.getAllKPIs();

        assertEquals(1, kpis.size());
    }

    @Test
    void testGetKPIsByModule() {
        when(repository.findByModule("module1")).thenReturn(List.of(createEntity(KpiStatus.AT_RISK)));

        List<GovernanceKPI> kpis = service.getKPIsByModule("module1");

        assertEquals(1, kpis.size());
    }

    @Test
    void testGetKPISummary() {
        var e1 = createEntity(KpiStatus.ON_TRACK);
        e1.setModule("mod1");
        var e2 = createEntity(KpiStatus.AT_RISK);
        e2.setModule("mod1");
        var e3 = createEntity(KpiStatus.CRITICAL);
        e3.setModule("mod1");
        when(repository.findAll()).thenReturn(List.of(e1, e2, e3));

        Map<String, Object> summary = service.getKPISummary();

        assertNotNull(summary);
        assertTrue(summary.containsKey("totals"));
        assertTrue(summary.containsKey("modules"));
    }

    private GovernanceKPIEntity createEntity(KpiStatus status) {
        var entity = new GovernanceKPIEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test-kpi");
        entity.setDescription("KPI for test-kpi");
        entity.setModule("module1");
        entity.setCurrentValue(95.0);
        entity.setTargetValue(90.0);
        entity.setThreshold(0.5);
        entity.setStatus(status.name());
        entity.setDimensions("{}");
        entity.setCalculatedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }
}
