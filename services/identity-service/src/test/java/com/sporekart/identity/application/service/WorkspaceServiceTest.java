package com.sporekart.identity.application.service;

import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.domain.model.Workspace;
import com.sporekart.identity.domain.repository.WorkspaceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepositoryPort workspaceRepository;

    @InjectMocks
    private WorkspaceService workspaceService;

    @Test
    void shouldCreateWorkspace() {
        var name = "MyWorkspace";
        var ownerId = "user-1";
        when(workspaceRepository.existsByName(name)).thenReturn(false);
        when(workspaceRepository.save(any(Workspace.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var workspace = workspaceService.createWorkspace(name, ownerId);

        assertNotNull(workspace);
        assertEquals(name, workspace.getName());
        assertEquals(ownerId, workspace.getOwnerId());
        assertNotNull(workspace.getWorkspaceId());
        verify(workspaceRepository).existsByName(name);
        verify(workspaceRepository).save(any(Workspace.class));
    }

    @Test
    void shouldRejectDuplicateName() {
        var name = "MyWorkspace";
        var ownerId = "user-1";
        when(workspaceRepository.existsByName(name)).thenReturn(true);

        assertThrows(BusinessException.class, () -> workspaceService.createWorkspace(name, ownerId));
        verify(workspaceRepository).existsByName(name);
    }

    @Test
    void shouldGetWorkspace() {
        var workspaceId = "ws-1";
        var workspace = new Workspace(workspaceId, "MyWorkspace", "user-1", Instant.now());
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));

        var result = workspaceService.getWorkspace(workspaceId);

        assertTrue(result.isPresent());
        assertEquals(workspaceId, result.get().getWorkspaceId());
        verify(workspaceRepository).findById(workspaceId);
    }

    @Test
    void shouldGetUserWorkspaces() {
        var ownerId = "user-1";
        var workspaces = List.of(
                new Workspace("ws-1", "Workspace A", ownerId, Instant.now()),
                new Workspace("ws-2", "Workspace B", ownerId, Instant.now()));
        when(workspaceRepository.findByOwnerId(ownerId)).thenReturn(workspaces);

        var result = workspaceService.getUserWorkspaces(ownerId);

        assertEquals(2, result.size());
        verify(workspaceRepository).findByOwnerId(ownerId);
    }
}
