package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.domain.ReportSchedule;
import com.sporekart.ai.analytics.domain.ReportType;
import com.sporekart.ai.analytics.domain.ScheduleFrequency;
import com.sporekart.ai.analytics.infrastructure.persistence.ReportScheduleEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.ReportScheduleRepository;
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
class ScheduledReportServiceImplTest {

    @Mock private ReportScheduleRepository repository;
    private ObjectMapper objectMapper;
    private ScheduledReportServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new ScheduledReportServiceImpl(repository, objectMapper);
    }

    @Test
    void testCreateSchedule() {
        var entity = createEntity();
        when(repository.save(any(ReportScheduleEntity.class))).thenReturn(entity);

        var input = new ReportSchedule(null, ReportType.WEEKLY_REPORT, ScheduleFrequency.WEEKLY, Map.of(), true, UUID.randomUUID(), null, null);
        ReportSchedule result = service.createSchedule(input);

        assertNotNull(result);
        assertNotNull(result.id());
    }

    @Test
    void testUpdateSchedule() {
        var existing = createEntity();
        when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(repository.save(any(ReportScheduleEntity.class))).thenReturn(existing);

        var input = new ReportSchedule(existing.getId(), ReportType.DAILY_REPORT, ScheduleFrequency.DAILY, Map.of(), true, existing.getCreatedBy(), null, null);
        ReportSchedule result = service.updateSchedule(existing.getId(), input);

        assertNotNull(result);
    }

    @Test
    void testDeleteSchedule() {
        when(repository.existsById(any(UUID.class))).thenReturn(true);

        service.deleteSchedule(UUID.randomUUID());

        verify(repository).deleteById(any(UUID.class));
    }

    @Test
    void testGetSchedulesByFrequency() {
        when(repository.findByFrequency("WEEKLY")).thenReturn(List.of(createEntity()));

        List<ReportSchedule> schedules = service.getSchedulesByFrequency(ScheduleFrequency.WEEKLY);

        assertEquals(1, schedules.size());
    }

    @Test
    void testGetAllActiveSchedules() {
        when(repository.findByActiveTrue()).thenReturn(List.of(createEntity()));

        List<ReportSchedule> schedules = service.getAllActiveSchedules();

        assertEquals(1, schedules.size());
    }

    private ReportScheduleEntity createEntity() {
        var entity = new ReportScheduleEntity();
        entity.setId(UUID.randomUUID());
        entity.setReportType("WEEKLY_REPORT");
        entity.setFrequency("WEEKLY");
        entity.setConfiguration("{}");
        entity.setActive(true);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }
}
