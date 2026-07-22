package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.prompt.dto.request.TestExecutionRequest;
import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptUsageRepository;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptPlaygroundServiceTest {

    @Mock
    private PromptVersionRepository versionRepository;

    @Mock
    private PromptUsageRepository usageRepository;

    private PromptPlaygroundService playgroundService;
    private PreviewEngine previewEngine;
    private TokenEstimator tokenEstimator;
    private UUID versionId;

    @BeforeEach
    void setUp() {
        tokenEstimator = new TokenEstimator();
        previewEngine = new PreviewEngine(versionRepository, tokenEstimator);
        playgroundService = new PromptPlaygroundService(versionRepository, previewEngine, tokenEstimator, usageRepository);
        versionId = UUID.randomUUID();
    }

    @Test
    void shouldExecuteTest() {
        var entity = new PromptVersionEntity();
        entity.setId(versionId);
        entity.setTemplateId(UUID.randomUUID());
        entity.setVersion(1);
        entity.setPromptBody("Hello {{name}}");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(UUID.randomUUID());

        when(versionRepository.findById(versionId)).thenReturn(Optional.of(entity));

        var request = new TestExecutionRequest(versionId, Map.of("name", "John"), "MOCK", "mock-model", UUID.randomUUID());
        var result = playgroundService.executeTest(request);

        assertThat(result.renderedPrompt()).isEqualTo("Hello John");
        assertThat(result.success()).isTrue();
    }

    @Test
    void shouldThrowWhenVersionNotFound() {
        when(versionRepository.findById(any())).thenReturn(Optional.empty());

        var request = new TestExecutionRequest(UUID.randomUUID(), null, "MOCK", "mock-model", null);
        assertThrows(IllegalArgumentException.class, () -> playgroundService.executeTest(request));
    }
}
