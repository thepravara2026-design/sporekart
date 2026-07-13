package com.sporekart.ai.knowledgeregistry.interfaces.rest;

import com.sporekart.ai.knowledgeregistry.api.KnowledgeHealthService;
import com.sporekart.ai.knowledgeregistry.api.KnowledgeRegistryService;
import com.sporekart.ai.knowledgeregistry.api.KnowledgeSyncService;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceHealthEntity;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSourceHealthDto;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSourceRequestDto;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSourceResponseDto;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSyncRequestDto;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSyncResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/knowledge-registry")
public class KnowledgeRegistryController {

    private final KnowledgeRegistryService registryService;
    private final KnowledgeSyncService syncService;
    private final KnowledgeHealthService healthService;

    public KnowledgeRegistryController(KnowledgeRegistryService registryService,
                                       KnowledgeSyncService syncService,
                                       KnowledgeHealthService healthService) {
        this.registryService = registryService;
        this.syncService = syncService;
        this.healthService = healthService;
    }

    @PostMapping("/register")
    public ResponseEntity<KnowledgeSourceResponseDto> register(
            @Valid @RequestBody KnowledgeSourceRequestDto request) {
        KnowledgeSourceEntry created = registryService.registerSource(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(KnowledgeSourceResponseDto.from(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KnowledgeSourceResponseDto> update(
            @PathVariable("id") String id,
            @Valid @RequestBody KnowledgeSourceRequestDto request) {
        KnowledgeSourceEntry updated = registryService.updateSource(id, toDomain(request));
        return ResponseEntity.ok(KnowledgeSourceResponseDto.from(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeSourceResponseDto> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(KnowledgeSourceResponseDto.from(registryService.getSource(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<List<KnowledgeSourceResponseDto>> list() {
        return ResponseEntity.ok(registryService.listSources().stream()
                .map(KnowledgeSourceResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<KnowledgeSourceResponseDto>> search(@RequestParam("name") String name) {
        return ResponseEntity.ok(registryService.searchSources(name).stream()
                .map(KnowledgeSourceResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<KnowledgeSourceResponseDto>> getByType(
            @PathVariable("type") KnowledgeSourceType type) {
        return ResponseEntity.ok(registryService.getByType(type).stream()
                .map(KnowledgeSourceResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<KnowledgeSourceResponseDto>> getByOwner(
            @PathVariable("owner") String owner) {
        return ResponseEntity.ok(registryService.getByOwner(owner).stream()
                .map(KnowledgeSourceResponseDto::from).collect(Collectors.toList()));
    }

    @GetMapping("/sync-status/{status}")
    public ResponseEntity<List<KnowledgeSourceResponseDto>> getBySyncStatus(
            @PathVariable("status") SyncStatus status) {
        return ResponseEntity.ok(registryService.getBySyncStatus(status).stream()
                .map(KnowledgeSourceResponseDto::from).collect(Collectors.toList()));
    }

    @PostMapping("/{id}/sync")
    public ResponseEntity<KnowledgeSyncResponseDto> triggerSync(
            @PathVariable("id") String id,
            @RequestBody(required = false) KnowledgeSyncRequestDto request) {
        KnowledgeSourceEntry entry = syncService.triggerSync(id);
        return ResponseEntity.accepted().body(KnowledgeSyncResponseDto.from(entry));
    }

    @GetMapping("/{id}/sync/status")
    public ResponseEntity<SyncStatus> getSyncStatus(@PathVariable("id") String id) {
        return ResponseEntity.ok(syncService.getSyncStatus(id));
    }

    @PostMapping("/{id}/health")
    public ResponseEntity<KnowledgeSourceHealthDto> recordHealth(
            @PathVariable("id") String id,
            @Valid @RequestBody KnowledgeSourceHealthDto request) {
        KnowledgeSourceHealthEntity recorded = healthService.recordHealth(id, request.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(KnowledgeSourceHealthDto.from(recorded));
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<List<KnowledgeSourceHealthDto>> getHealth(@PathVariable("id") String id) {
        List<KnowledgeSourceHealthDto> history = healthService.getHealthHistory(id).stream()
                .map(KnowledgeSourceHealthDto::from).collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    private KnowledgeSourceEntry toDomain(KnowledgeSourceRequestDto request) {
        KnowledgeSourceEntry entry = new KnowledgeSourceEntry();
        entry.setSourceId(request.getSourceId());
        entry.setSourceName(request.getSourceName());
        entry.setSourceType(request.getSourceType());
        entry.setDescription(request.getDescription());
        entry.setOwner(request.getOwner());
        entry.setMetadata(request.getMetadata() == null ? new HashMap<>() : new HashMap<>(request.getMetadata()));
        entry.setRefreshPolicy(request.getRefreshPolicy());
        entry.setRefreshCron(request.getRefreshCron());
        return entry;
    }
}
