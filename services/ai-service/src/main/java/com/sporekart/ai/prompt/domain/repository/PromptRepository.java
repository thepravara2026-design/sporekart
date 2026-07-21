package com.sporekart.ai.prompt.domain.repository;

import com.sporekart.ai.prompt.domain.aggregate.Prompt;
import com.sporekart.ai.prompt.domain.valueobject.PromptId;
import java.util.List;
import java.util.Optional;

public interface PromptRepository {
    void save(Prompt prompt);
    Optional<Prompt> findById(PromptId id);
    Optional<Prompt> findByName(String name);
    List<Prompt> findAll();
    List<Prompt> findByOwner(String owner);
    List<Prompt> findByStatus(String status);
    void delete(PromptId id);
    boolean exists(PromptId id);
    long count();
}
