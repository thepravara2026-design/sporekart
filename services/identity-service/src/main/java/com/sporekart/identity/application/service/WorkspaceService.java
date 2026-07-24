package com.sporekart.identity.application.service;

import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.domain.model.Workspace;
import com.sporekart.identity.domain.repository.WorkspaceRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepositoryPort workspaceRepository;

    public WorkspaceService(WorkspaceRepositoryPort workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public Workspace createWorkspace(String name, String ownerId) {
        if (workspaceRepository.existsByName(name)) {
            throw new BusinessException("Workspace name already exists");
        }
        var workspaceId = UUID.randomUUID().toString();
        var workspace = new Workspace(workspaceId, name, ownerId, Instant.now());
        return workspaceRepository.save(workspace);
    }

    public Optional<Workspace> getWorkspace(String workspaceId) {
        return workspaceRepository.findById(workspaceId);
    }

    public List<Workspace> getUserWorkspaces(String ownerId) {
        return workspaceRepository.findByOwnerId(ownerId);
    }
}
