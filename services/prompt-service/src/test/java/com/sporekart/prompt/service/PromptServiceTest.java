package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.dto.request.CreatePromptRequest;
import com.sporekart.prompt.dto.request.UpdatePromptRequest;
import com.sporekart.prompt.dto.response.PromptTemplateResponse;
import com.sporekart.prompt.entity.PromptTagEntity;
import com.sporekart.prompt.entity.PromptTemplateEntity;
import com.sporekart.prompt.events.PromptEventPublisher;
import com.sporekart.prompt.repository.PromptTagRepository;
import com.sporekart.prompt.repository.PromptTemplateRepository;
import com.sporekart.prompt.repository.PromptUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @Mock
    private PromptTemplateRepository templateRepository;

    @Mock
    private PromptTagRepository tagRepository;

    @Mock
    private PromptUsageRepository usageRepository;

    @Mock
    private PromptVersionService versionService;

    @Mock
    private PromptAuditService auditService;

    @Mock
    private PromptEventPublisher eventPublisher;

    private PromptService promptService;
    private UUID owner;
    private UUID creator;
    private PromptTemplateEntity template;

    @BeforeEach
    void setUp() {
        promptService = new PromptService(templateRepository, tagRepository, usageRepository,
                versionService, auditService, eventPublisher);
        owner = UUID.randomUUID();
        creator = UUID.randomUUID();
        template = createTemplateEntity();
    }

    @Test
    void shouldCreateTemplate() {
        when(templateRepository.existsBySlugAndIsDeletedFalse("test-slug")).thenReturn(false);
        when(templateRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<PromptTemplateEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var request = new CreatePromptRequest("Test", "test-slug", "desc",
                PromptCategory.COMMERCE, PromptScope.GLOBAL, owner, creator, null);

        var result = promptService.createTemplate(request);
        assertThat(result.name()).isEqualTo("Test");
        verify(eventPublisher).publishPromptCreated(any(), anyString(), any());
    }

    @Test
    void shouldThrowWhenSlugExists() {
        when(templateRepository.existsBySlugAndIsDeletedFalse("test-slug")).thenReturn(true);

        var request = new CreatePromptRequest("Test", "test-slug", "desc",
                PromptCategory.COMMERCE, PromptScope.GLOBAL, owner, creator, null);

        assertThrows(IllegalArgumentException.class, () -> promptService.createTemplate(request));
    }

    @Test
    void shouldGetTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(template.getId()))
                .thenReturn(Optional.of(template));

        var result = promptService.getTemplate(template.getId());
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Test Template");
    }

    @Test
    void shouldReturnEmptyForNonExistentTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());

        var result = promptService.getTemplate(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldListTemplates() {
        when(templateRepository.findByIsDeletedFalse()).thenReturn(List.of(template));

        var results = promptService.listTemplates();
        assertThat(results).hasSize(1);
    }

    @Test
    void shouldUpdateTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(template.getId()))
                .thenReturn(Optional.of(template));
        when(templateRepository.existsBySlugAndIsDeletedFalse("new-slug")).thenReturn(false);
        when(templateRepository.save(any())).thenReturn(template);

        var request = new UpdatePromptRequest("Updated", "new-slug", "new desc",
                PromptCategory.ANALYTICS, PromptScope.WORKSPACE, owner, creator, null);

        var result = promptService.updateTemplate(template.getId(), request);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowWhenUpdatingNonExistent() {
        when(templateRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());

        var request = new UpdatePromptRequest("Updated", null, null, null, null, null, creator, null);
        assertThrows(IllegalArgumentException.class,
                () -> promptService.updateTemplate(UUID.randomUUID(), request));
    }

    @Test
    void shouldArchiveTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(template.getId()))
                .thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenReturn(template);

        promptService.archiveTemplate(template.getId(), creator);
        assertThat(template.getStatus()).isEqualTo(PromptStatus.ARCHIVED);
    }

    @Test
    void shouldDeleteTemplate() {
        when(templateRepository.findByIdAndIsDeletedFalse(template.getId()))
                .thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenReturn(template);

        promptService.deleteTemplate(template.getId(), creator);
        assertThat(template.isDeleted()).isTrue();
    }

    @Test
    void shouldSearchTemplates() {
        when(templateRepository.search("test")).thenReturn(List.of(template));

        var results = promptService.search("test");
        assertThat(results).hasSize(1);
    }

    @Test
    void shouldFilterByCategory() {
        when(templateRepository.findByCategoryAndIsDeletedFalse(PromptCategory.COMMERCE))
                .thenReturn(List.of(template));

        var results = promptService.filterByCategory("COMMERCE");
        assertThat(results).hasSize(1);
    }

    @Test
    void shouldThrowForInvalidCategory() {
        assertThrows(IllegalArgumentException.class,
                () -> promptService.filterByCategory("INVALID"));
    }

    @Test
    void shouldFilterByStatus() {
        when(templateRepository.findByStatusAndIsDeletedFalse(PromptStatus.DRAFT))
                .thenReturn(List.of(template));

        var results = promptService.filterByStatus("DRAFT");
        assertThat(results).hasSize(1);
    }

    @Test
    void shouldFilterByOwner() {
        when(templateRepository.findByOwnerAndIsDeletedFalse(owner))
                .thenReturn(List.of(template));

        var results = promptService.filterByOwner(owner);
        assertThat(results).hasSize(1);
    }

    @Test
    void shouldResolveTags() {
        when(tagRepository.findByName("urgent")).thenReturn(Optional.empty());
        var tagEntity = new PromptTagEntity();
        tagEntity.setId(UUID.randomUUID());
        tagEntity.setName("urgent");
        when(tagRepository.save(any())).thenReturn(tagEntity);

        when(templateRepository.existsBySlugAndIsDeletedFalse("test-slug")).thenReturn(false);
        when(templateRepository.save(any())).thenReturn(template);

        var request = new CreatePromptRequest("Test", "test-slug", "desc",
                PromptCategory.COMMERCE, PromptScope.GLOBAL, owner, creator, List.of("urgent"));

        promptService.createTemplate(request);
        verify(tagRepository).save(any());
    }

    private PromptTemplateEntity createTemplateEntity() {
        var entity = new PromptTemplateEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Test Template");
        entity.setSlug("test-slug");
        entity.setDescription("Test description");
        entity.setCategory(PromptCategory.COMMERCE);
        entity.setScope(PromptScope.GLOBAL);
        entity.setStatus(PromptStatus.DRAFT);
        entity.setOwner(owner);
        entity.setCreatedBy(creator);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setActive(true);
        entity.setDeleted(false);
        entity.setTags(new HashSet<>());
        return entity;
    }
}
