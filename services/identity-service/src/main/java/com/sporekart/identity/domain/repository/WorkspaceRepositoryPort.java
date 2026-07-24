package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.Workspace;
import java.util.List;
import java.util.Optional;

public interface WorkspaceRepositoryPort {
    Workspace save(Workspace workspace);
    Optional<Workspace> findById(String workspaceId);
    List<Workspace> findByOwnerId(String ownerId);
    boolean existsByName(String name);
}
