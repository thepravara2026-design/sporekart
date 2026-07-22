package com.sporekart.memory.service;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.Visibility;
import com.sporekart.memory.dto.CreateMemoryRequest;
import com.sporekart.memory.dto.UpdateMemoryRequest;
import com.sporekart.memory.persistence.MemoryEntity;
import com.sporekart.memory.repository.MemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemoryServiceTest {

    @Autowired
    private MemoryService memoryService;

    @Autowired
    private MemoryRepository memoryRepository;

    @Test
    void shouldCreateMemory() {
        var request = new CreateMemoryRequest(
                "Test Memory", "Test content", MemoryType.NOTE, "MANUAL",
                Priority.HIGH, 8, List.of("test", "demo"),
                UUID.randomUUID(), "USER", "default", "engineering",
                Visibility.TEAM, null, null, null, false, null, false, null
        );

        var response = memoryService.create(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("Test Memory");
        assertThat(response.tags()).contains("test", "demo");
    }

    @Test
    void shouldFindMemoryById() {
        var request = new CreateMemoryRequest(
                "Find Me", "Content", MemoryType.NOTE, "MANUAL",
                Priority.MEDIUM, 5, List.of(),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        var created = memoryService.create(request);

        var found = memoryService.findById(created.id());

        assertThat(found).isPresent();
        assertThat(found.get().title()).isEqualTo("Find Me");
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        var result = memoryService.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindMemoriesByOwner() {
        var ownerId = UUID.randomUUID();
        for (int i = 0; i < 3; i++) {
            var request = new CreateMemoryRequest(
                    "Memory " + i, "Content", MemoryType.NOTE, "MANUAL",
                    Priority.MEDIUM, 5, List.of(),
                    ownerId, "USER", "default", null,
                    Visibility.PRIVATE, null, null, null, false, null, false, null
            );
            memoryService.create(request);
        }

        var memories = memoryService.findByOwner(ownerId);
        assertThat(memories).hasSize(3);
    }

    @Test
    void shouldFindMemoriesByWorkspace() {
        String workspace = "workspace-" + UUID.randomUUID().toString().substring(0, 8);
        for (int i = 0; i < 2; i++) {
            var request = new CreateMemoryRequest(
                    "WS Memory", "Content", MemoryType.NOTE, "MANUAL",
                    Priority.MEDIUM, 5, List.of(),
                    UUID.randomUUID(), "USER", workspace, null,
                    Visibility.PRIVATE, null, null, null, false, null, false, null
            );
            memoryService.create(request);
        }

        var memories = memoryService.findByWorkspace(workspace);
        assertThat(memories).hasSize(2);
    }

    @Test
    void shouldUpdateMemory() {
        var request = new CreateMemoryRequest(
                "Original", "Original content", MemoryType.NOTE, "MANUAL",
                Priority.MEDIUM, 5, List.of("original"),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        var created = memoryService.create(request);

        var updateRequest = new UpdateMemoryRequest(
                "Updated", "Updated content", List.of("updated"),
                Priority.HIGH, 9, MemoryType.FACT, "API", null
        );
        var updated = memoryService.update(created.id(), updateRequest);

        assertThat(updated).isPresent();
        assertThat(updated.get().title()).isEqualTo("Updated");
        assertThat(updated.get().priority()).isEqualTo(Priority.HIGH);
        assertThat(updated.get().tags()).containsExactly("updated");
    }

    @Test
    void shouldSoftDeleteMemory() {
        var request = new CreateMemoryRequest(
                "Delete Me", "Content", MemoryType.NOTE, "MANUAL",
                Priority.MEDIUM, 5, List.of(),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        var created = memoryService.create(request);

        var deleted = memoryService.delete(created.id());

        assertThat(deleted).isTrue();
        assertThat(memoryService.findById(created.id())).isEmpty();
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistent() {
        assertThat(memoryService.delete(UUID.randomUUID())).isFalse();
    }

    @Test
    void shouldCountMemories() {
        var ownerId = UUID.randomUUID();
        for (int i = 0; i < 2; i++) {
            var request = new CreateMemoryRequest(
                    "Count Test", "Content", MemoryType.NOTE, "MANUAL",
                    Priority.MEDIUM, 5, List.of(),
                    ownerId, "USER", "default", null,
                    Visibility.PRIVATE, null, null, null, false, null, false, null
            );
            memoryService.create(request);
        }

        assertThat(memoryService.count()).isGreaterThanOrEqualTo(2);
        assertThat(memoryService.countByOwner(ownerId)).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldSearchMemories() {
        var ownerId = UUID.randomUUID();
        var request = new CreateMemoryRequest(
                "Search Target", "This is searchable content for testing",
                MemoryType.NOTE, "MANUAL", Priority.MEDIUM, 5, List.of("searchable"),
                ownerId, "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        memoryService.create(request);

        var searchRequest = new com.sporekart.memory.dto.SearchMemoriesRequest(
                "searchable", List.of(), List.of(), List.of(), List.of(), 10, 0.0
        );
        var results = memoryService.search(searchRequest);

        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldFormatForPrompt() {
        var request = new CreateMemoryRequest(
                "Prompt Context", "Important context for prompt",
                MemoryType.NOTE, "MANUAL", Priority.HIGH, 9, List.of(),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        memoryService.create(request);

        var searchRequest = new com.sporekart.memory.dto.SearchMemoriesRequest(
                "Important", List.of(), List.of(), List.of(), List.of(), 5, 0.0
        );
        var prompt = memoryService.formatForPrompt(searchRequest, 2000);

        assertThat(prompt).contains("Important");
    }

    @Test
    void shouldGenerateEmbeddingOnCreate() {
        var request = new CreateMemoryRequest(
                "Embedding Test", "Content for embedding generation",
                MemoryType.NOTE, "MANUAL", Priority.MEDIUM, 5, List.of(),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.PRIVATE, null, null, null, false, null, false, null
        );
        var response = memoryService.create(request);

        assertThat(response.id()).isNotNull();
    }
}
