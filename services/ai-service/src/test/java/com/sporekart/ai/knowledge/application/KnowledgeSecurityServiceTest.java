package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeAccessLogRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeSecurityServiceTest {

    @Mock private KnowledgeDocumentRepository documentRepository;
    @Mock private KnowledgeAccessLogRepository accessLogRepository;

    private KnowledgeSecurityService securityService;

    @BeforeEach
    void setUp() {
        securityService = new KnowledgeSecurityService(documentRepository, accessLogRepository);
    }

    @Test
    void shouldAdminViewAnyDocument() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setVisibility("CONFIDENTIAL");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertTrue(securityService.canViewDocument(docId, null, "ADMINISTRATOR"));
    }

    @Test
    void shouldViewPublicDocument() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setVisibility("PUBLIC");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertTrue(securityService.canViewDocument(docId, null, "USER"));
    }

    @Test
    void shouldDenyConfidentialToNonAdmin() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setVisibility("CONFIDENTIAL");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertFalse(securityService.canViewDocument(docId, null, "USER"));
    }

    @Test
    void shouldAdminEditAnyDocument() {
        UUID docId = UUID.randomUUID();
        assertTrue(securityService.canEditDocument(docId, null, "ADMINISTRATOR"));
    }

    @Test
    void shouldOwnerEditDocument() {
        UUID userId = UUID.randomUUID();
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setCreatedBy(userId);
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertTrue(securityService.canEditDocument(docId, userId, "USER"));
    }

    @Test
    void shouldDenyEditToNonOwner() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setCreatedBy(UUID.randomUUID());
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertFalse(securityService.canEditDocument(docId, UUID.randomUUID(), "USER"));
    }

    @Test
    void shouldCheckViewPermission() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setVisibility("CONFIDENTIAL");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertThrows(KnowledgeSecurityException.class,
                () -> securityService.checkViewPermission(docId, null, "USER"));
    }

    @Test
    void shouldLogAccess() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        securityService.logAccess(docId, UUID.randomUUID(), "USER", "VIEW", "127.0.0.1");
        verify(accessLogRepository).save(any());
    }
}
