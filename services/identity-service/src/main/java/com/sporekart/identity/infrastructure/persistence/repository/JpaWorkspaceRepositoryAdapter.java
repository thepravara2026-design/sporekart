package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.Workspace;
import com.sporekart.identity.domain.repository.WorkspaceRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.WorkspaceEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaWorkspaceRepositoryAdapter implements WorkspaceRepositoryPort {

    private final SpringDataWorkspaceRepository repository;

    public JpaWorkspaceRepositoryAdapter(SpringDataWorkspaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Workspace save(Workspace workspace) {
        return toDomain(repository.save(toEntity(workspace)));
    }

    @Override
    public Optional<Workspace> findById(String workspaceId) {
        return repository.findById(workspaceId).map(this::toDomain);
    }

    @Override
    public List<Workspace> findByOwnerId(String ownerId) {
        return repository.findByOwnerId(ownerId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    private Workspace toDomain(WorkspaceEntity entity) {
        return new Workspace(
                entity.getWorkspaceId(), entity.getName(),
                entity.getOwnerId(), entity.getCreatedAt());
    }

    private WorkspaceEntity toEntity(Workspace workspace) {
        var entity = new WorkspaceEntity();
        entity.setWorkspaceId(workspace.getWorkspaceId());
        entity.setName(workspace.getName());
        entity.setOwnerId(workspace.getOwnerId());
        entity.setCreatedAt(workspace.getCreatedAt());
        return entity;
    }
}
