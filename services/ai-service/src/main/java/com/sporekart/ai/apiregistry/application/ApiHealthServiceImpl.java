package com.sporekart.ai.apiregistry.application;

import com.sporekart.ai.apiregistry.api.ApiHealthService;
import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiHealthEntity;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiHealthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiHealthServiceImpl implements ApiHealthService {

    private final ApiHealthRepository healthRepository;

    public ApiHealthServiceImpl(ApiHealthRepository healthRepository) {
        this.healthRepository = healthRepository;
    }

    @Transactional
    @Override
    public void recordHealth(String apiId, ApiHealthStatus status) {
        if (apiId == null || apiId.isBlank()) {
            throw new ApiRegistryException("apiId is required to record health");
        }
        if (status == null) {
            status = ApiHealthStatus.UNKNOWN;
        }
        ApiHealthEntity entity = new ApiHealthEntity(null, apiId, status, Instant.now());
        healthRepository.save(entity);
    }

    @Override
    public Optional<ApiHealthStatus> getHealth(String apiId) {
        return healthRepository.findLatestByApiId(apiId).map(ApiHealthEntity::getStatus);
    }

    @Override
    public List<ApiHealthReport> getHealthReport() {
        List<ApiHealthReport> reports = new ArrayList<>();
        List<ApiHealthEntity> latest = healthRepository.findAll();
        for (ApiHealthEntity entity : latest) {
            reports.add(new ApiHealthReport(entity.getApiId(), entity.getStatus(), entity.getCheckedAt()));
        }
        return reports;
    }
}
