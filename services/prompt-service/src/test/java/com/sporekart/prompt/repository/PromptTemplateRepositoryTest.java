package com.sporekart.prompt.repository;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.entity.PromptTagEntity;
import com.sporekart.prompt.entity.PromptTemplateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PromptTemplateRepositoryTest {

    @Autowired
    private PromptTemplateRepository templateRepository;

    @Autowired
    private PromptTagRepository tagRepository;

    private UUID owner;
    private UUID creator;
    private PromptTemplateEntity template;

    @BeforeEach
    void setUp() {
        owner = UUID.randomUUID();
        creator = UUID.randomUUID();

        template = new PromptTemplateEntity();
        template.setName("Test Template");
        template.setSlug("test-template");
        template.setDescription("Test description");
        template.setCategory(PromptCategory.COMMERCE);
        template.setScope(PromptScope.GLOBAL);
        template.setStatus(PromptStatus.DRAFT);
        template.setOwner(owner);
        template.setCreatedBy(creator);
        template.setCreatedAt(OffsetDateTime.now());
        template.setUpdatedAt(OffsetDateTime.now());
        template.setActive(true);
        template.setDeleted(false);
        template = templateRepository.save(template);
    }

    @Test
    void shouldSaveAndFindTemplate() {
        var found = templateRepository.findByIdAndIsDeletedFalse(template.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Template");
    }

    @Test
    void shouldFindBySlug() {
        var found = templateRepository.findBySlugAndIsDeletedFalse("test-template");
        assertThat(found).isPresent();
    }

    @Test
    void shouldFindByCategory() {
        var results = templateRepository.findByCategoryAndIsDeletedFalse(PromptCategory.COMMERCE);
        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldFindByStatus() {
        var results = templateRepository.findByStatusAndIsDeletedFalse(PromptStatus.DRAFT);
        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldFindByOwner() {
        var results = templateRepository.findByOwnerAndIsDeletedFalse(owner);
        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldFindByCreatedBy() {
        var results = templateRepository.findByCreatedByAndIsDeletedFalse(creator);
        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldCheckSlugExists() {
        assertThat(templateRepository.existsBySlugAndIsDeletedFalse("test-template")).isTrue();
        assertThat(templateRepository.existsBySlugAndIsDeletedFalse("nonexistent")).isFalse();
    }

    @Test
    void shouldExcludeDeletedTemplates() {
        template.setDeleted(true);
        templateRepository.save(template);

        var found = templateRepository.findByIdAndIsDeletedFalse(template.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldSearchTemplates() {
        var results = templateRepository.search("Test");
        assertThat(results).isNotEmpty();
    }

    @Test
    void shouldFindByTags() {
        var tag = new PromptTagEntity();
        tag.setName("urgent");
        tag = tagRepository.save(tag);

        template.setTags(new java.util.HashSet<>(java.util.Set.of(tag)));
        templateRepository.save(template);

        var results = templateRepository.findByTagNames(java.util.List.of("urgent"));
        assertThat(results).isNotEmpty();
    }
}
