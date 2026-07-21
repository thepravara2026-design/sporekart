package com.sporekart.ai.memory.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sporekart.ai.memory.interfaces.rest.dto.MemoryEntryRequest;
import com.sporekart.ai.memory.interfaces.rest.dto.MemoryEntryResponse;
import com.sporekart.ai.memory.interfaces.rest.dto.MemoryQueryRequest;
import com.sporekart.ai.memory.interfaces.rest.dto.MemoryStatsResponse;
import com.sporekart.ai.memory.interfaces.rest.dto.MemorySummaryResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai/memory")
public class MemoryController {

    @PostMapping
    public ResponseEntity<MemoryEntryResponse> storeMemory(@RequestBody MemoryEntryRequest request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryEntryResponse> getMemory(@PathVariable UUID id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<MemoryEntryResponse>> searchMemories(@RequestBody MemoryQueryRequest request) {
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoryEntryResponse> updateMemory(@PathVariable UUID id, @RequestBody MemoryEntryRequest request) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> deleteBySession(@PathVariable String sessionId) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/agent/{agentId}")
    public ResponseEntity<Void> deleteByAgent(@PathVariable String agentId) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<MemoryStatsResponse> getStats() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<MemorySummaryResponse>> getSummaries() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/consolidate")
    public ResponseEntity<Void> consolidate() {
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/prune")
    public ResponseEntity<Void> prune() {
        return ResponseEntity.accepted().build();
    }
}
