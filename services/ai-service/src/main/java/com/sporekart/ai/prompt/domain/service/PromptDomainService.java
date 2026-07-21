package com.sporekart.ai.prompt.domain.service;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.exception.InvalidStatusTransitionException;
import com.sporekart.ai.prompt.domain.exception.PromptNotFoundException;
import com.sporekart.ai.prompt.domain.repository.PromptRepository;
import com.sporekart.ai.prompt.domain.valueobject.PromptId;
import com.sporekart.ai.prompt.domain.valueobject.PromptStatus;
import java.util.List;
import java.util.Optional;

public class PromptDomainService {
    private final PromptRepository repository;

    public PromptDomainService(PromptRepository repository) {
        this.repository = repository;
    }

    public Prompt findById(PromptId id) {
        return repository.findById(id)
            .orElseThrow(() -> new PromptNotFoundException(id.value()));
    }

    public List<Prompt> findByOwner(String owner) {
        return repository.findByOwner(owner);
    }

    public List<Prompt> findByStatus(PromptStatus status) {
        return repository.findByStatus(status.name());
    }

    public void publishPrompt(PromptId id, String performedBy) {
        var prompt = findById(id);
        prompt.publish(performedBy);
        repository.save(prompt);
    }

    public void deprecatePrompt(PromptId id, String performedBy, String reason) {
        var prompt = findById(id);
        prompt.deprecate(performedBy, reason);
        repository.save(prompt);
    }

    public void archivePrompt(PromptId id, String performedBy, String reason) {
        var prompt = findById(id);
        prompt.archive(performedBy, reason);
        repository.save(prompt);
    }

    public boolean canTransition(PromptId id, PromptStatus target) {
        try {
            var prompt = findById(id);
            return prompt.status().canTransitionTo(target);
        } catch (PromptNotFoundException e) {
            return false;
        }
    }

    public Optional<Prompt> findActiveByName(String name) {
        return repository.findByName(name)
            .filter(p -> p.status() == PromptStatus.PUBLISHED);
    }
}
