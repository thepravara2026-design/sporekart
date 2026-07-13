package com.sporekart.ai.promptregistry.application;

import com.sporekart.ai.promptregistry.api.PromptSearchService;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryEntity;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromptSearchServiceImpl implements PromptSearchService {

    private final PromptRegistryRepository registryRepository;

    public PromptSearchServiceImpl(PromptRegistryRepository registryRepository) {
        this.registryRepository = registryRepository;
    }

    @Override
    public List<PromptRegistryEntry> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return toDomainList(registryRepository.findAll());
        }
        return toDomainList(registryRepository.findByPromptNameContaining(name));
    }

    @Override
    public List<PromptRegistryEntry> searchByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return toDomainList(registryRepository.findAll());
        }
        List<PromptRegistryEntity> matched = new ArrayList<>();
        for (String tag : tags) {
            matched.addAll(registryRepository.findByTagsContaining(tag));
        }
        return toDomainList(deduplicate(matched));
    }

    @Override
    public List<PromptRegistryEntry> searchByStatus(PromptStatus status) {
        if (status == null) {
            return toDomainList(registryRepository.findAll());
        }
        return toDomainList(registryRepository.findByStatus(status));
    }

    @Override
    public List<PromptRegistryEntry> searchByText(String text) {
        if (text == null || text.isBlank()) {
            return toDomainList(registryRepository.findAll());
        }
        List<PromptRegistryEntity> matched = new ArrayList<>();
        String lower = text.toLowerCase();
        for (PromptRegistryEntity entity : registryRepository.findAll()) {
            if ((entity.getPromptName() != null && entity.getPromptName().toLowerCase().contains(lower))
                    || (entity.getPromptText() != null && entity.getPromptText().toLowerCase().contains(lower))
                    || (entity.getDescription() != null && entity.getDescription().toLowerCase().contains(lower))) {
                matched.add(entity);
            }
        }
        return toDomainList(matched);
    }

    private List<PromptRegistryEntity> deduplicate(List<PromptRegistryEntity> entities) {
        List<PromptRegistryEntity> result = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        for (PromptRegistryEntity entity : entities) {
            if (!seen.contains(entity.getPromptId())) {
                seen.add(entity.getPromptId());
                result.add(entity);
            }
        }
        return result;
    }

    private List<PromptRegistryEntry> toDomainList(List<PromptRegistryEntity> entities) {
        List<PromptRegistryEntry> result = new ArrayList<>();
        for (PromptRegistryEntity entity : entities) {
            result.add(new PromptRegistryEntry(
                    entity.getPromptId(), entity.getPromptName(), entity.getDescription(),
                    entity.getPromptText(), entity.getVersion(), entity.getOwner(),
                    entity.getTags(), entity.getStatus(), entity.getPreviousVersionId(),
                    entity.getMetadata(), entity.getCreatedAt(), entity.getUpdatedAt()));
        }
        return result;
    }
}
