package com.sporekart.identity.interfaces.rest;

import com.sporekart.identity.application.dto.WorkspaceRequest;
import com.sporekart.identity.application.service.WorkspaceService;
import com.sporekart.identity.domain.model.Workspace;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<Workspace> createWorkspace(Authentication authentication,
                                                      @Valid @RequestBody WorkspaceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workspaceService.createWorkspace(request.name(), authentication.getName()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Workspace>> myWorkspaces(Authentication authentication) {
        return ResponseEntity.ok(workspaceService.getUserWorkspaces(authentication.getName()));
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable String workspaceId) {
        return workspaceService.getWorkspace(workspaceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
