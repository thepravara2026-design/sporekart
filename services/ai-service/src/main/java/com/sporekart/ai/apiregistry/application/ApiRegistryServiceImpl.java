package com.sporekart.ai.apiregistry.application;

import com.sporekart.ai.apiregistry.api.ApiRegistryService;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryEntity;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiRegistryServiceImpl implements ApiRegistryService {

    private final ApiRegistryRepository repository;

    public ApiRegistryServiceImpl(ApiRegistryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ApiRegistryEntry registerApi(ApiRegistryEntry entry) {
        String apiId = entry.getApiId() != null && !entry.getApiId().isBlank()
                ? entry.getApiId()
                : UUID.randomUUID().toString();
        Instant now = Instant.now();

        ApiRegistryEntity entity = new ApiRegistryEntity(
                null, apiId, entry.getApiName(), entry.getApiPath(), entry.getHttpMethod(),
                entry.getModule(), entry.getOwner(), entry.getDescription(), entry.getVersion(),
                entry.isDeprecated(), entry.getDeprecationNotice(), entry.isAuthRequired(),
                entry.getRolesAllowed(), entry.getConsumers(), entry.getDependencies(),
                entry.getOpenApiSpec(), entry.getHealthStatus(), now, now);
        ApiRegistryEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Transactional
    @Override
    public ApiRegistryEntry updateApi(ApiRegistryEntry entry) {
        if (entry.getApiId() == null || entry.getApiId().isBlank()) {
            throw new ApiRegistryException("apiId is required for update");
        }
        ApiRegistryEntity existing = repository.findByApiId(entry.getApiId())
                .orElseThrow(() -> new ApiRegistryException("API not found: " + entry.getApiId()));
        Instant now = Instant.now();

        existing.setApiName(entry.getApiName() != null ? entry.getApiName() : existing.getApiName());
        existing.setApiPath(entry.getApiPath() != null ? entry.getApiPath() : existing.getApiPath());
        existing.setHttpMethod(entry.getHttpMethod() != null ? entry.getHttpMethod() : existing.getHttpMethod());
        existing.setModule(entry.getModule() != null ? entry.getModule() : existing.getModule());
        existing.setOwner(entry.getOwner() != null ? entry.getOwner() : existing.getOwner());
        existing.setDescription(entry.getDescription() != null ? entry.getDescription() : existing.getDescription());
        existing.setVersion(entry.getVersion() != null ? entry.getVersion() : existing.getVersion());
        existing.setDeprecated(entry.isDeprecated());
        existing.setDeprecationNotice(entry.getDeprecationNotice() != null
                ? entry.getDeprecationNotice() : existing.getDeprecationNotice());
        existing.setAuthRequired(entry.isAuthRequired());
        existing.setRolesAllowed(entry.getRolesAllowed() != null ? entry.getRolesAllowed() : existing.getRolesAllowed());
        existing.setConsumers(entry.getConsumers() != null ? entry.getConsumers() : existing.getConsumers());
        existing.setDependencies(entry.getDependencies() != null ? entry.getDependencies() : existing.getDependencies());
        existing.setOpenApiSpec(entry.getOpenApiSpec() != null ? entry.getOpenApiSpec() : existing.getOpenApiSpec());
        existing.setHealthStatus(entry.getHealthStatus() != null ? entry.getHealthStatus() : existing.getHealthStatus());
        existing.setUpdatedAt(now);

        ApiRegistryEntity saved = repository.save(existing);
        return toDomain(saved);
    }

    @Override
    public Optional<ApiRegistryEntry> getApi(String apiId) {
        return repository.findByApiId(apiId).map(this::toDomain);
    }

    @Override
    public List<ApiRegistryEntry> listApis() {
        List<ApiRegistryEntry> result = new ArrayList<>();
        for (ApiRegistryEntity entity : repository.findAll()) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public List<ApiRegistryEntry> searchByModule(String module) {
        List<ApiRegistryEntry> result = new ArrayList<>();
        for (ApiRegistryEntity entity : repository.findByModule(module)) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public List<ApiRegistryEntry> searchByPath(String path) {
        List<ApiRegistryEntry> result = new ArrayList<>();
        for (ApiRegistryEntity entity : repository.findByApiPathContaining(path)) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public List<ApiRegistryEntry> getByOwner(String owner) {
        List<ApiRegistryEntry> result = new ArrayList<>();
        for (ApiRegistryEntity entity : repository.findByOwner(owner)) {
            result.add(toDomain(entity));
        }
        return result;
    }

    private ApiRegistryEntry toDomain(ApiRegistryEntity entity) {
        return new ApiRegistryEntry(
                entity.getApiId(), entity.getApiName(), entity.getApiPath(), entity.getHttpMethod(),
                entity.getModule(), entity.getOwner(), entity.getDescription(), entity.getVersion(),
                entity.isDeprecated(), entity.getDeprecationNotice(), entity.isAuthRequired(),
                entity.getRolesAllowed(), entity.getConsumers(), entity.getDependencies(),
                entity.getOpenApiSpec(), entity.getHealthStatus(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
