package com.sporekart.memory.controller;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.Visibility;
import com.sporekart.memory.dto.CreateMemoryRequest;
import com.sporekart.memory.dto.UpdateMemoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MemoryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private CreateMemoryRequest sampleRequest() {
        return new CreateMemoryRequest(
                "Controller Test", "Controller content", MemoryType.NOTE, "MANUAL",
                Priority.HIGH, 8, List.of("controller", "test"),
                UUID.randomUUID(), "USER", "default", null,
                Visibility.TEAM, null, null, null, false, null, false, null
        );
    }

    @Test
    void shouldCreateMemory() {
        var response = restTemplate.postForEntity("/api/v1/memories", sampleRequest(),
                com.sporekart.memory.dto.MemoryResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    void shouldFindById() {
        var created = restTemplate.postForEntity("/api/v1/memories", sampleRequest(),
                com.sporekart.memory.dto.MemoryResponse.class).getBody();

        var response = restTemplate.getForEntity("/api/v1/memories/{id}",
                com.sporekart.memory.dto.MemoryResponse.class, created.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().title()).isEqualTo("Controller Test");
    }

    @Test
    void shouldReturn404ForUnknownId() {
        var response = restTemplate.getForEntity("/api/v1/memories/{id}",
                String.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateMemory() {
        var created = restTemplate.postForEntity("/api/v1/memories", sampleRequest(),
                com.sporekart.memory.dto.MemoryResponse.class).getBody();

        var updateRequest = new UpdateMemoryRequest(
                "Updated Title", "Updated content", List.of("updated"),
                Priority.LOW, 3, MemoryType.FACT, "API", null
        );

        restTemplate.put("/api/v1/memories/{id}", updateRequest, created.id());

        var updated = restTemplate.getForEntity("/api/v1/memories/{id}",
                com.sporekart.memory.dto.MemoryResponse.class, created.id());

        assertThat(updated.getBody().title()).isEqualTo("Updated Title");
    }

    @Test
    void shouldDeleteMemory() {
        var created = restTemplate.postForEntity("/api/v1/memories", sampleRequest(),
                com.sporekart.memory.dto.MemoryResponse.class).getBody();

        restTemplate.delete("/api/v1/memories/{id}", created.id());

        var response = restTemplate.getForEntity("/api/v1/memories/{id}",
                String.class, created.id());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldSearchMemories() {
        restTemplate.postForEntity("/api/v1/memories", sampleRequest(),
                com.sporekart.memory.dto.MemoryResponse.class);

        var searchRequest = new com.sporekart.memory.dto.SearchMemoriesRequest(
                "Controller", List.of(), List.of(), List.of(), List.of(), 10, 0.0
        );

        var response = restTemplate.postForEntity("/api/v1/memories/search", searchRequest,
                com.sporekart.memory.dto.SearchMemoriesResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldReturnStats() {
        var response = restTemplate.getForEntity("/api/v1/memories/stats",
                com.sporekart.memory.controller.MemoryController.MemoryStatsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
