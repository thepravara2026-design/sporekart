package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptSearchServiceTest {

    @Mock
    private PromptTemplateRepository templateRepository;
    @Mock
    private PromptCategoryRepository categoryRepository;

    private PromptSearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new PromptSearchService(templateRepository, categoryRepository);
    }

    @Test
    void shouldSearchByQuery() {
        when(templateRepository.search("hello")).thenReturn(List.of(new PromptTemplateEntity()));
        assertEquals(1, searchService.search("hello").size());
    }

    @Test
    void shouldReturnAllWhenNoQuery() {
        when(templateRepository.findByIsDeletedFalse()).thenReturn(List.of(new PromptTemplateEntity()));
        assertEquals(1, searchService.search(null).size());
    }

    @Test
    void shouldFindByCategory() {
        UUID catId = UUID.randomUUID();
        when(templateRepository.findByCategoryIdAndIsDeletedFalse(catId)).thenReturn(List.of(new PromptTemplateEntity()));
        assertEquals(1, searchService.findByCategory(catId).size());
    }

    @Test
    void shouldFindPublished() {
        when(templateRepository.findByStatusAndIsDeletedFalse("PUBLISHED")).thenReturn(List.of(new PromptTemplateEntity()));
        assertEquals(1, searchService.findPublished().size());
    }

    @Test
    void shouldListCategories() {
        when(categoryRepository.findByIsDeletedFalseOrderByDisplayOrder())
                .thenReturn(List.of(new PromptCategoryEntity("Cat", null, null, 0)));
        assertEquals(1, searchService.listCategories().size());
    }
}
