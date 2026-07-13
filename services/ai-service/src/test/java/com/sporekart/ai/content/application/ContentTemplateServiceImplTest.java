package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentCategory;
import com.sporekart.ai.content.domain.ContentTemplate;
import com.sporekart.ai.content.infrastructure.persistence.ContentTemplateEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentTemplateRepository;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentTemplateServiceImplTest {

    @Mock private ContentTemplateRepository repository;

    private ContentTemplateServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentTemplateServiceImpl(repository);
    }

    @Test
    void shouldCreateTemplate() {
        var template = new ContentTemplate(null, "Blog Template", "A blog template",
                ContentCategory.BLOG, ContentType.TEXT, "Write about {{topic}}",
                List.of("topic"), true, 1, UUID.randomUUID(),
                null, null);
        when(repository.save(any())).thenAnswer(inv -> {
            var entity = inv.<ContentTemplateEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            entity.setCreatedAt(OffsetDateTime.now());
            entity.setUpdatedAt(OffsetDateTime.now());
            return entity;
        });

        var created = service.createTemplate(template);

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Blog Template", created.name());
        assertEquals(ContentCategory.BLOG, created.category());
        assertEquals(ContentType.TEXT, created.contentType());
        verify(repository).save(any());
    }

    @Test
    void shouldCreateTemplateWithActiveDefaults() {
        var template = new ContentTemplate(null, "Template", "desc",
                ContentCategory.GENERAL, ContentType.TEXT, "Content {{var}}",
                List.of("var"), true, 0, UUID.randomUUID(),
                null, null);
        when(repository.save(any())).thenAnswer(inv -> {
            var entity = inv.<ContentTemplateEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            entity.setCreatedAt(OffsetDateTime.now());
            entity.setUpdatedAt(OffsetDateTime.now());
            return entity;
        });

        var created = service.createTemplate(template);

        assertNotNull(created);
        assertTrue(created.isActive());
        assertEquals(1, created.version());
    }

    @Test
    void shouldUpdateTemplate() {
        var id = UUID.randomUUID();
        var entity = new ContentTemplateEntity();
        entity.setId(id);
        entity.setName("Original");
        entity.setDescription("Original desc");
        entity.setCategory("GENERAL");
        entity.setContentType("TEXT");
        entity.setTemplateContent("Original {{var}}");
        entity.setVariables("var");
        entity.setActive(true);
        entity.setVersion(1);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var updated = new ContentTemplate(id, "Updated", "Updated desc",
                ContentCategory.BLOG, ContentType.TEXT, "Updated {{var}}",
                List.of("var"), false, 1, UUID.randomUUID(),
                OffsetDateTime.now(), OffsetDateTime.now());
        var result = service.updateTemplate(updated);

        assertNotNull(result);
        assertEquals("Updated", result.name());
        assertEquals(ContentCategory.BLOG, result.category());
        assertEquals(2, result.version());
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentTemplate() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        var template = new ContentTemplate(id, "Name", "desc",
                ContentCategory.GENERAL, ContentType.TEXT, "content",
                List.of(), true, 1, UUID.randomUUID(),
                OffsetDateTime.now(), OffsetDateTime.now());

        assertThrows(ContentException.class, () -> service.updateTemplate(template));
    }

    @Test
    void shouldDeleteTemplate() {
        var id = UUID.randomUUID();
        var entity = new ContentTemplateEntity();
        entity.setId(id);
        entity.setDeleted(false);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        service.deleteTemplate(id);

        assertTrue(entity.isDeleted());
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTemplate() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(ContentException.class, () -> service.deleteTemplate(id));
    }

    @Test
    void shouldGetTemplate() {
        var id = UUID.randomUUID();
        var entity = new ContentTemplateEntity();
        entity.setId(id);
        entity.setName("Test Template");
        entity.setCategory("GENERAL");
        entity.setContentType("TEXT");
        entity.setTemplateContent("Content");
        entity.setActive(true);
        entity.setVersion(1);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = service.getTemplate(id);

        assertTrue(result.isPresent());
        assertEquals("Test Template", result.get().name());
    }

    @Test
    void shouldReturnEmptyForNonExistentTemplate() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        var result = service.getTemplate(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldListTemplatesByCategory() {
        var entity = new ContentTemplateEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Blog Template");
        entity.setCategory("BLOG");
        entity.setContentType("TEXT");
        entity.setTemplateContent("Content");
        entity.setActive(true);
        entity.setVersion(1);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByCategoryAndIsDeletedFalse("BLOG")).thenReturn(List.of(entity));

        var templates = service.listTemplates(ContentCategory.BLOG);

        assertEquals(1, templates.size());
        assertEquals("Blog Template", templates.get(0).name());
        assertEquals(ContentCategory.BLOG, templates.get(0).category());
    }

    @Test
    void shouldReturnEmptyListWhenNoTemplatesForCategory() {
        when(repository.findByCategoryAndIsDeletedFalse("BLOG")).thenReturn(List.of());

        var templates = service.listTemplates(ContentCategory.BLOG);

        assertTrue(templates.isEmpty());
    }

    @Test
    void shouldRenderTemplate() {
        var id = UUID.randomUUID();
        var entity = new ContentTemplateEntity();
        entity.setId(id);
        entity.setName("Render Template");
        entity.setCategory("GENERAL");
        entity.setContentType("TEXT");
        entity.setTemplateContent("Hello {{name}}, welcome to {{place}}!");
        entity.setVariables("name,place");
        entity.setActive(true);
        entity.setVersion(1);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var rendered = service.renderTemplate(id, Map.of("name", "John", "place", "Paris"));

        assertEquals("Hello John, welcome to Paris!", rendered.templateContent());
    }

    @Test
    void shouldRenderTemplateWithUnresolvedVariables() {
        var id = UUID.randomUUID();
        var entity = new ContentTemplateEntity();
        entity.setId(id);
        entity.setName("Template");
        entity.setCategory("GENERAL");
        entity.setContentType("TEXT");
        entity.setTemplateContent("Hello {{name}}, your id is {{id}}");
        entity.setVariables("name,id");
        entity.setActive(true);
        entity.setVersion(1);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var rendered = service.renderTemplate(id, Map.of("name", "John"));

        assertEquals("Hello John, your id is {{id}}", rendered.templateContent());
    }

    @Test
    void shouldThrowWhenRenderingNonExistentTemplate() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(ContentException.class, () -> service.renderTemplate(id, Map.of("key", "value")));
    }
}
