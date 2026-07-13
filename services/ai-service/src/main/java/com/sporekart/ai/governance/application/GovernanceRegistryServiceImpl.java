package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceRegistryService;
import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceRegistryEntity;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernanceRegistryServiceImpl implements GovernanceRegistryService {

    private final GovernanceRegistryRepository repository;

    @Override
    public GovernanceRegistry register(GovernanceRegistry registry) {
        GovernanceRegistryEntity entity = new GovernanceRegistryEntity();
        entity.setId(registry.id());
        entity.setName(registry.name());
        entity.setModule(registry.module());
        entity.setEndpoint(registry.endpoint());
        entity.setScope(registry.scope().name());
        entity.setMode(registry.mode().name());
        entity.setConfig(mapToString(registry.config()));
        entity.setIsRegistered(true);
        entity.setRegisteredAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);
        log.info("Registered governance entry: {} in module {}", registry.name(), registry.module());
        return registry;
    }

    @Override
    public void unregister(UUID id) {
        repository.findById(id).ifPresent(entity -> {
            entity.setIsDeleted(true);
            repository.save(entity);
        });
    }

    @Override
    public Optional<GovernanceRegistry> findById(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<GovernanceRegistry> findByModule(String module) {
        return repository.findByModuleAndIsDeletedFalse(module).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceRegistry> findAll() {
        return repository.findByIsDeletedFalse().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean isRegistered(String module, String endpoint) {
        return repository.findByModuleAndEndpointAndIsDeletedFalse(module, endpoint).isPresent();
    }

    private GovernanceRegistry toDomain(GovernanceRegistryEntity e) {
        return new GovernanceRegistry(
            e.getId(), e.getName(), e.getModule(), e.getEndpoint(),
            GovernanceScope.valueOf(e.getScope()), GovernanceMode.valueOf(e.getMode()),
            stringToMap(e.getConfig()), e.getIsRegistered(),
            e.getRegisteredAt(), e.getUpdatedAt()
        );
    }

    private String mapToString(Map<String, Object> map) {
        if (map == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":\"").append(v).append("\","));
        if (sb.length() > 1) sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> stringToMap(String str) {
        if (str == null || str.isBlank()) return new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String content = str.replace("{", "").replace("}", "");
        if (content.isBlank()) return map;
        for (String pair : content.split(",")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                map.put(kv[0].replace("\"", "").trim(), kv[1].replace("\"", "").trim());
            }
        }
        return map;
    }
}
