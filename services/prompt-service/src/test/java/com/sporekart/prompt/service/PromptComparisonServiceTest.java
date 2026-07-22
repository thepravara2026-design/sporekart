package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptComparisonServiceTest {

    @Mock
    private PromptVersionRepository versionRepository;

    private PromptComparisonService comparisonService;
    private TokenEstimator tokenEstimator;
    private UUID templateId;

    @BeforeEach
    void setUp() {
        tokenEstimator = new TokenEstimator();
        comparisonService = new PromptComparisonService(versionRepository, tokenEstimator);
        templateId = UUID.randomUUID();
    }

    @Test
    void shouldCompareVersions() {
        var v1 = createVersion(1, "Hello {{name}}");
        var v2 = createVersion(2, "Hello {{name}}, welcome to {{place}}");

        when(versionRepository.findByTemplateIdAndVersion(templateId, 1)).thenReturn(Optional.of(v1));
        when(versionRepository.findByTemplateIdAndVersion(templateId, 2)).thenReturn(Optional.of(v2));

        var result = comparisonService.compare(templateId, 1, 2);
        assertThat(result.addedLines()).isNotEmpty();
    }

    @Test
    void shouldThrowWhenVersionNotFound() {
        when(versionRepository.findByTemplateIdAndVersion(templateId, 99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> comparisonService.compare(templateId, 99, 1));
    }

    private PromptVersionEntity createVersion(int ver, String body) {
        var entity = new PromptVersionEntity();
        entity.setId(UUID.randomUUID());
        entity.setTemplateId(templateId);
        entity.setVersion(ver);
        entity.setPromptBody(body);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(UUID.randomUUID());
        return entity;
    }
}
