package com.sporekart.ai.promptregistry.application;

import com.sporekart.ai.promptregistry.api.PromptRegistryService;
import com.sporekart.ai.promptregistry.domain.PromptComparisonResult;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;
import com.sporekart.ai.promptregistry.domain.PromptVersionInfo;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryEntity;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryRepository;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptVersionHistoryEntity;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptVersionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromptRegistryServiceImpl implements PromptRegistryService {

    private final PromptRegistryRepository registryRepository;
    private final PromptVersionHistoryRepository historyRepository;

    public PromptRegistryServiceImpl(PromptRegistryRepository registryRepository,
                                     PromptVersionHistoryRepository historyRepository) {
        this.registryRepository = registryRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    @Override
    public PromptRegistryEntry registerPrompt(PromptRegistryEntry entry) {
        String promptId = entry.getPromptId() != null && !entry.getPromptId().isBlank()
                ? entry.getPromptId()
                : UUID.randomUUID().toString();
        int version = entry.getVersion() > 0 ? entry.getVersion() : 1;
        PromptStatus status = entry.getStatus() != null ? entry.getStatus() : PromptStatus.DRAFT;
        Instant now = Instant.now();

        PromptRegistryEntity entity = new PromptRegistryEntity(
                null, promptId, entry.getPromptName(), entry.getDescription(),
                entry.getPromptText(), version, entry.getOwner(), entry.getTags(),
                status, entry.getPreviousVersionId(), entry.getMetadata(), now, now);
        PromptRegistryEntity saved = registryRepository.save(entity);

        historyRepository.save(new PromptVersionHistoryEntity(
                null, promptId, version, status, "Initial registration", entry.getPromptText(), now));

        return toDomain(saved);
    }

    @Transactional
    @Override
    public PromptRegistryEntry updatePrompt(PromptRegistryEntry entry) {
        if (entry.getPromptId() == null || entry.getPromptId().isBlank()) {
            throw new PromptRegistryException("promptId is required for update");
        }
        PromptRegistryEntity existing = registryRepository.findByPromptId(entry.getPromptId())
                .orElseThrow(() -> new PromptRegistryException("Prompt not found: " + entry.getPromptId()));

        int nextVersion = existing.getVersion() + 1;
        Instant now = Instant.now();
        String previousVersionId = existing.getPromptId() + ":" + existing.getVersion();

        existing.setPromptName(entry.getPromptName() != null ? entry.getPromptName() : existing.getPromptName());
        existing.setDescription(entry.getDescription() != null ? entry.getDescription() : existing.getDescription());
        existing.setPromptText(entry.getPromptText() != null ? entry.getPromptText() : existing.getPromptText());
        existing.setOwner(entry.getOwner() != null ? entry.getOwner() : existing.getOwner());
        existing.setTags(entry.getTags() != null ? entry.getTags() : existing.getTags());
        existing.setStatus(entry.getStatus() != null ? entry.getStatus() : existing.getStatus());
        existing.setMetadata(entry.getMetadata() != null ? entry.getMetadata() : existing.getMetadata());
        existing.setVersion(nextVersion);
        existing.setPreviousVersionId(previousVersionId);
        existing.setUpdatedAt(now);

        PromptRegistryEntity saved = registryRepository.save(existing);
        historyRepository.save(new PromptVersionHistoryEntity(
                null, saved.getPromptId(), nextVersion, saved.getStatus(),
                "Updated prompt", saved.getPromptText(), now));

        return toDomain(saved);
    }

    @Override
    public Optional<PromptRegistryEntry> getPrompt(String promptId) {
        return registryRepository.findByPromptId(promptId).map(this::toDomain);
    }

    @Override
    public List<PromptRegistryEntry> listPrompts() {
        List<PromptRegistryEntry> result = new ArrayList<>();
        for (PromptRegistryEntity entity : registryRepository.findAll()) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public List<PromptVersionInfo> getVersions(String promptId) {
        List<PromptVersionInfo> result = new ArrayList<>();
        for (PromptVersionHistoryEntity history : historyRepository.findByPromptIdOrderByVersionDesc(promptId)) {
            result.add(new PromptVersionInfo(
                    history.getVersion(), history.getStatus(), history.getCreatedAt(), history.getChangeSummary()));
        }
        return result;
    }

    @Transactional
    @Override
    public PromptRegistryEntry rollbackToVersion(String promptId, int version) {
        PromptRegistryEntity existing = registryRepository.findByPromptId(promptId)
                .orElseThrow(() -> new PromptRegistryException("Prompt not found: " + promptId));

        PromptVersionHistoryEntity target = historyRepository.findByPromptIdAndVersion(promptId, version)
                .orElseThrow(() -> new PromptRegistryException(
                        "Version not found: " + version + " for prompt " + promptId));

        int nextVersion = existing.getVersion() + 1;
        Instant now = Instant.now();
        String previousVersionId = existing.getPromptId() + ":" + existing.getVersion();

        existing.setPromptText(target.getPromptText());
        existing.setStatus(target.getStatus());
        existing.setVersion(nextVersion);
        existing.setPreviousVersionId(previousVersionId);
        existing.setUpdatedAt(now);

        PromptRegistryEntity saved = registryRepository.save(existing);
        historyRepository.save(new PromptVersionHistoryEntity(
                null, promptId, nextVersion, target.getStatus(),
                "Rollback to version " + version, target.getPromptText(), now));

        return toDomain(saved);
    }

    @Override
    public PromptComparisonResult compareVersions(String promptId, int versionA, int versionB) {
        PromptVersionHistoryEntity a = historyRepository.findByPromptIdAndVersion(promptId, versionA)
                .orElseThrow(() -> new PromptRegistryException(
                        "Version not found: " + versionA + " for prompt " + promptId));
        PromptVersionHistoryEntity b = historyRepository.findByPromptIdAndVersion(promptId, versionB)
                .orElseThrow(() -> new PromptRegistryException(
                        "Version not found: " + versionB + " for prompt " + promptId));

        List<String> differences = new ArrayList<>();
        if (!safeEquals(a.getPromptText(), b.getPromptText())) {
            differences.add("promptText differs");
        }
        if (a.getStatus() != b.getStatus()) {
            differences.add("status differs: " + a.getStatus() + " -> " + b.getStatus());
        }
        if (!safeEquals(a.getChangeSummary(), b.getChangeSummary())) {
            differences.add("changeSummary differs");
        }

        return new PromptComparisonResult(promptId, versionA, versionB, differences);
    }

    private boolean safeEquals(String x, String y) {
        return x == null ? y == null : x.equals(y);
    }

    private PromptRegistryEntry toDomain(PromptRegistryEntity entity) {
        return new PromptRegistryEntry(
                entity.getPromptId(), entity.getPromptName(), entity.getDescription(),
                entity.getPromptText(), entity.getVersion(), entity.getOwner(),
                entity.getTags(), entity.getStatus(), entity.getPreviousVersionId(),
                entity.getMetadata(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
