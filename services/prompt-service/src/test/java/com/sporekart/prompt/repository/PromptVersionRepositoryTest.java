package com.sporekart.prompt.repository;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptScope;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.entity.PromptTemplateEntity;
import com.sporekart.prompt.entity.PromptVersionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PromptVersionRepositoryTest {

    @Autowired
    private PromptTemplateRepository templateRepository;

    @Autowired
    private PromptVersionRepository versionRepository;

    private UUID templateId;

    @BeforeEach
    void setUp() {
        var template = new PromptTemplateEntity();
        template.setName("Test");
        template.setSlug("test-slug-" + UUID.randomUUID());
        template.setCategory(PromptCategory.COMMERCE);
        template.setScope(PromptScope.GLOBAL);
        template.setStatus(PromptStatus.DRAFT);
        template.setOwner(UUID.randomUUID());
        template.setCreatedBy(UUID.randomUUID());
        template.setCreatedAt(OffsetDateTime.now());
        template.setUpdatedAt(OffsetDateTime.now());
        template.setActive(true);
        template.setDeleted(false);
        templateId = templateRepository.save(template).getId();
    }

    @Test
    void shouldCreateVersion() {
        var version = createVersion(1);
        assertThat(version.getId()).isNotNull();
    }

    @Test
    void shouldFindVersionsByTemplateId() {
        createVersion(1);
        createVersion(2);

        var versions = versionRepository.findByTemplateIdOrderByVersionDesc(templateId);
        assertThat(versions).hasSize(2);
        assertThat(versions.get(0).getVersion()).isEqualTo(2);
    }

    @Test
    void shouldFindByTemplateAndVersion() {
        createVersion(1);
        var found = versionRepository.findByTemplateIdAndVersion(templateId, 1);
        assertThat(found).isPresent();
    }

    @Test
    void shouldFindPublishedVersion() {
        var v = createVersion(1);
        v.setPublished(true);
        versionRepository.save(v);

        var published = versionRepository.findTopByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId);
        assertThat(published).isPresent();
    }

    @Test
    void shouldCountVersions() {
        createVersion(1);
        createVersion(2);
        assertThat(versionRepository.countByTemplateId(templateId)).isEqualTo(2);
    }

    private PromptVersionEntity createVersion(int ver) {
        var entity = new PromptVersionEntity();
        entity.setTemplateId(templateId);
        entity.setVersion(ver);
        entity.setPromptBody("Test prompt body");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(UUID.randomUUID());
        return versionRepository.save(entity);
    }
}
