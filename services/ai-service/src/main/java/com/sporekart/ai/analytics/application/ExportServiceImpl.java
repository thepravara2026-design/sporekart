package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.ExportService;
import com.sporekart.ai.analytics.domain.GovernanceExport;
import com.sporekart.ai.analytics.domain.ReportFormat;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceExportEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceExportRepository;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceReportRepository;
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
public class ExportServiceImpl implements ExportService {

    private final GovernanceExportRepository exportRepository;
    private final GovernanceReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GovernanceExport exportReport(UUID reportId, ReportFormat format) {
        var report = reportRepository.findById(reportId).orElse(null);
        if (report == null) {
            log.warn("Cannot export: report not found {}", reportId);
            return null;
        }

        var entity = new GovernanceExportEntity();
        entity.setId(UUID.randomUUID());
        entity.setReportId(reportId);
        entity.setFormat(format.name());
        entity.setFileName(report.getTitle().replaceAll("\\s+", "_") + "." + format.name().toLowerCase());
        entity.setFileSize(0L);
        try {
            entity.setMetadata(objectMapper.writeValueAsString(Map.of(
                    "reportTitle", report.getTitle(),
                    "reportType", report.getType(),
                    "format", format.name()
            )));
        } catch (Exception e) {
            entity.setMetadata("{}");
        }
        entity.setExportedAt(java.time.OffsetDateTime.now());
        entity.setExportedBy(UUID.randomUUID());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = exportRepository.save(entity);
        log.info("Exported report {} as {} format -> export {}", reportId, format, saved.getId());
        return toDomain(saved);
    }

    @Override
    public GovernanceExport getExport(UUID id) {
        return exportRepository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<GovernanceExport> getExportsByReport(UUID reportId) {
        return exportRepository.findByReportId(reportId).stream().map(this::toDomain).toList();
    }

    @Override
    public byte[] getExportData(UUID id) {
        var export = exportRepository.findById(id).orElse(null);
        if (export == null) {
            log.warn("Export not found: {}", id);
            return new byte[0];
        }
        log.debug("Returning stub export data for export {}", id);
        return new byte[0];
    }

    @SuppressWarnings("unchecked")
    private GovernanceExport toDomain(GovernanceExportEntity entity) {
        Map<String, Object> metadataMap = Map.of();
        try {
            metadataMap = objectMapper.readValue(entity.getMetadata(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize metadata for export {}: {}", entity.getId(), e.getMessage());
        }
        return new GovernanceExport(
                entity.getId(),
                entity.getReportId(),
                ReportFormat.valueOf(entity.getFormat()),
                entity.getFileName(),
                entity.getFileSize(),
                metadataMap,
                entity.getExportedAt().toInstant(),
                entity.getExportedBy()
        );
    }
}
