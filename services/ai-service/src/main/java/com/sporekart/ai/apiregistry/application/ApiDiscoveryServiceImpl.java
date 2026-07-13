package com.sporekart.ai.apiregistry.application;

import com.sporekart.ai.apiregistry.api.ApiDiscoveryService;
import com.sporekart.ai.apiregistry.domain.ApiDependency;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryEntity;
import com.sporekart.ai.apiregistry.infrastructure.persistence.ApiRegistryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiDiscoveryServiceImpl implements ApiDiscoveryService {

    private final ApiRegistryRepository repository;

    public ApiDiscoveryServiceImpl(ApiRegistryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ApiRegistryEntry> discoverByModule(String module) {
        List<ApiRegistryEntry> result = new ArrayList<>();
        for (ApiRegistryEntity entity : repository.findByModule(module)) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public List<ApiDependency> findDependencies(String apiId) {
        ApiRegistryEntity entity = repository.findByApiId(apiId)
                .orElseThrow(() -> new ApiRegistryException("API not found: " + apiId));

        Map<String, ApiRegistryEntity> byId = new HashMap<>();
        for (ApiRegistryEntity other : repository.findAll()) {
            byId.put(other.getApiId(), other);
        }

        List<ApiDependency> dependencies = new ArrayList<>();
        for (String dependencyId : entity.getDependencies()) {
            ApiRegistryEntity dep = byId.get(dependencyId);
            String name = dep != null ? dep.getApiName() : dependencyId;
            ApiDependency.DependencyType type = dep != null && dep.getModule() != null
                    && dep.getModule().equals(entity.getModule())
                    ? ApiDependency.DependencyType.INTERNAL
                    : ApiDependency.DependencyType.EXTERNAL;
            dependencies.add(new ApiDependency(dependencyId, name, type));
        }
        return dependencies;
    }

    @Override
    public Map<String, ApiRegistryEntry> getApiMap() {
        Map<String, ApiRegistryEntry> map = new HashMap<>();
        for (ApiRegistryEntity entity : repository.findAll()) {
            map.put(entity.getApiId(), toDomain(entity));
        }
        return map;
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
