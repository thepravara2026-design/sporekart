package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.ScheduledReportService;
import com.sporekart.ai.analytics.domain.ReportSchedule;
import com.sporekart.ai.analytics.domain.ReportType;
import com.sporekart.ai.analytics.domain.ScheduleFrequency;
import com.sporekart.ai.analytics.infrastructure.persistence.ReportScheduleEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.ReportScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledReportServiceImpl implements ScheduledReportService {

    private final ReportScheduleRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ReportSchedule createSchedule(ReportSchedule schedule) {
        var entity = new ReportScheduleEntity();
        entity.setId(UUID.randomUUID());
        entity.setReportType(schedule.reportType().name());
        entity.setFrequency(schedule.frequency().name());
        try {
            entity.setConfiguration(objectMapper.writeValueAsString(schedule.configuration() != null ? schedule.configuration() : Map.of()));
        } catch (Exception e) {
            entity.setConfiguration("{}");
        }
        entity.setActive(true);
        entity.setCreatedBy(schedule.createdBy());
        entity.setCreatedAt(java.time.OffsetDateTime.now());
        entity.setUpdatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        log.info("Created schedule {} for report type {} with frequency {}",
                saved.getId(), saved.getReportType(), saved.getFrequency());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ReportSchedule updateSchedule(UUID id, ReportSchedule schedule) {
        var entity = repository.findById(id).orElse(null);
        if (entity == null) {
            log.warn("Cannot update: schedule not found {}", id);
            return null;
        }
        entity.setReportType(schedule.reportType().name());
        entity.setFrequency(schedule.frequency().name());
        try {
            entity.setConfiguration(objectMapper.writeValueAsString(schedule.configuration() != null ? schedule.configuration() : Map.of()));
        } catch (Exception e) {
            entity.setConfiguration("{}");
        }
        entity.setActive(schedule.active());
        entity.setUpdatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        log.info("Updated schedule {}", id);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteSchedule(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Deleted schedule {}", id);
        } else {
            log.warn("Cannot delete: schedule not found {}", id);
        }
    }

    @Override
    public List<ReportSchedule> getSchedulesByFrequency(ScheduleFrequency frequency) {
        return repository.findByFrequency(frequency.name()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ReportSchedule> getAllActiveSchedules() {
        return repository.findByActiveTrue().stream().map(this::toDomain).toList();
    }

    @SuppressWarnings("unchecked")
    private ReportSchedule toDomain(ReportScheduleEntity entity) {
        Map<String, Object> configMap = Map.of();
        try {
            configMap = objectMapper.readValue(entity.getConfiguration(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize configuration for schedule {}: {}", entity.getId(), e.getMessage());
        }
        return new ReportSchedule(
                entity.getId(),
                ReportType.valueOf(entity.getReportType()),
                ScheduleFrequency.valueOf(entity.getFrequency()),
                configMap,
                entity.isActive(),
                entity.getCreatedBy(),
                entity.getCreatedAt().toInstant(),
                entity.getUpdatedAt().toInstant()
        );
    }
}
