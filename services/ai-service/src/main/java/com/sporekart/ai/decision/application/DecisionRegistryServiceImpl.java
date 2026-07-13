package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionRegistryService;
import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionRegistryEntity;
import com.sporekart.ai.decision.infrastructure.persistence.DecisionRegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecisionRegistryServiceImpl implements DecisionRegistryService {
    private final DecisionRegistryRepository repository;

    @Override
    public DecisionRegistry register(DecisionRegistry registry) {
        DecisionRegistryEntity e = new DecisionRegistryEntity();
        e.setId(registry.id()); e.setName(registry.name()); e.setModule(registry.module());
        e.setEndpoint(registry.endpoint()); e.setIsActive(registry.isActive());
        e.setIsRegistered(true); e.setConfig(registry.config() != null ? registry.config().toString() : "{}");
        e.setRegisteredAt(OffsetDateTime.now()); e.setUpdatedAt(OffsetDateTime.now());
        repository.save(e);
        return registry;
    }

    @Override public void unregister(UUID id) {
        repository.findById(id).ifPresent(e -> { e.setIsDeleted(true); repository.save(e); });
    }
    @Override public Optional<DecisionRegistry> findById(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(e -> new DecisionRegistry(
            e.getId(), e.getName(), e.getModule(), e.getEndpoint(),
            e.getIsActive(), e.getIsRegistered(), new HashMap<>(), e.getRegisteredAt(), e.getUpdatedAt()));
    }
    @Override public List<DecisionRegistry> findByModule(String module) {
        return repository.findByModuleAndIsDeletedFalse(module).stream()
            .map(e -> new DecisionRegistry(e.getId(), e.getName(), e.getModule(), e.getEndpoint(),
                e.getIsActive(), e.getIsRegistered(), new HashMap<>(), e.getRegisteredAt(), e.getUpdatedAt())).toList();
    }
    @Override public List<DecisionRegistry> findAll() {
        return repository.findByIsDeletedFalse().stream()
            .map(e -> new DecisionRegistry(e.getId(), e.getName(), e.getModule(), e.getEndpoint(),
                e.getIsActive(), e.getIsRegistered(), new HashMap<>(), e.getRegisteredAt(), e.getUpdatedAt())).toList();
    }
    @Override public boolean isRegistered(String module, String endpoint) { return false; }
}
