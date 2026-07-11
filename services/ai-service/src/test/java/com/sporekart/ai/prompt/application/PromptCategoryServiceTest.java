package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptCategoryServiceTest {

    @Mock
    private PromptCategoryRepository categoryRepository;
    @Mock
    private PromptAuditRepository auditRepository;

    private PromptCategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new PromptCategoryService(categoryRepository, auditRepository);
    }

    @Test
    void shouldListAllCategories() {
        when(categoryRepository.findByIsDeletedFalseOrderByDisplayOrder())
                .thenReturn(List.of(new PromptCategoryEntity("Cat1", null, null, 0)));
        assertEquals(1, categoryService.listAll().size());
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        PromptCategoryEntity entity = new PromptCategoryEntity("Test", "desc", "icon", 1);
        entity.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        assertEquals("Test", categoryService.findById(id).getName());
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(PromptNotFoundException.class, () -> categoryService.findById(UUID.randomUUID()));
    }

    @Test
    void shouldCreateCategory() {
        when(categoryRepository.existsByNameAndIsDeletedFalse("NewCat")).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(i -> {
            PromptCategoryEntity e = i.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });
        when(auditRepository.save(any())).thenReturn(null);

        PromptCategoryEntity created = categoryService.create("NewCat", "desc", "icon", 1, UUID.randomUUID());
        assertNotNull(created);
        assertEquals("NewCat", created.getName());
    }

    @Test
    void shouldThrowOnDuplicateCategory() {
        when(categoryRepository.existsByNameAndIsDeletedFalse("Dup")).thenReturn(true);
        assertThrows(PromptValidationException.class,
                () -> categoryService.create("Dup", null, null, 0, UUID.randomUUID()));
    }

    @Test
    void shouldUpdateCategory() {
        UUID id = UUID.randomUUID();
        PromptCategoryEntity entity = new PromptCategoryEntity("Old", "old", "icon", 1);
        entity.setId(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(categoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptCategoryEntity updated = categoryService.update(id, "New", "new desc", "new-icon", 2, true, UUID.randomUUID());
        assertEquals("New", updated.getName());
    }
}
