package com.sporekart.memory.controller;

import com.sporekart.memory.dto.*;
import com.sporekart.memory.service.MemoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/memories")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @PostMapping
    public ResponseEntity<MemoryResponse> create(@RequestBody CreateMemoryRequest request) {
        var response = memoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponse> findById(@PathVariable UUID id) {
        return memoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MemoryResponse>> findAll(
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false) String workspace) {
        if (ownerId != null) {
            return ResponseEntity.ok(memoryService.findByOwner(ownerId));
        }
        if (workspace != null) {
            return ResponseEntity.ok(memoryService.findByWorkspace(workspace));
        }
        return ResponseEntity.ok(memoryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoryResponse> update(@PathVariable UUID id, @RequestBody UpdateMemoryRequest request) {
        return memoryService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return memoryService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable UUID id) {
        return memoryService.hardDelete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchMemoriesResponse> search(@RequestBody SearchMemoriesRequest request) {
        var results = memoryService.search(request);
        var response = new SearchMemoriesResponse(
                request.query(), results, results.size(), 0L);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search/context")
    public ResponseEntity<com.sporekart.memory.retrieval.RetrievalContext> searchWithContext(
            @RequestBody SearchMemoriesRequest request) {
        return ResponseEntity.ok(memoryService.searchWithContext(request));
    }

    @PostMapping("/search/prompt")
    public ResponseEntity<String> formatForPrompt(@RequestBody SearchMemoriesRequest request,
                                                   @RequestParam(defaultValue = "2000") int maxTokens) {
        return ResponseEntity.ok(memoryService.formatForPrompt(request, maxTokens));
    }

    @GetMapping("/stats")
    public ResponseEntity<MemoryStatsResponse> stats() {
        return ResponseEntity.ok(new MemoryStatsResponse(
                memoryService.count(),
                memoryService.countByWorkspace("default")
        ));
    }

    public record MemoryStatsResponse(long totalMemories, long defaultWorkspaceMemories) {}
}
