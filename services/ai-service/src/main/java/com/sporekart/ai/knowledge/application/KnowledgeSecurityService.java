package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeAccessLogEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeAccessLogRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class KnowledgeSecurityService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeSecurityService.class);
    private static final Set<String> ADMIN_ROLES = Set.of("ADMINISTRATOR", "KNOWLEDGE_MANAGER");
    private static final Set<String> EDITOR_ROLES = Set.of("ADMINISTRATOR", "KNOWLEDGE_MANAGER", "CONTENT_EDITOR");
    private static final Set<String> VIEWER_ROLES = Set.of("ADMINISTRATOR", "KNOWLEDGE_MANAGER", "CONTENT_EDITOR", "USER");

    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeAccessLogRepository accessLogRepository;

    public KnowledgeSecurityService(KnowledgeDocumentRepository documentRepository,
                                    KnowledgeAccessLogRepository accessLogRepository) {
        this.documentRepository = documentRepository;
        this.accessLogRepository = accessLogRepository;
    }

    public boolean canViewDocument(UUID documentId, UUID userId, String userRole) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(documentId)
                .orElse(null);
        if (doc == null) return false;
        if (ADMIN_ROLES.contains(userRole)) return true;
        if ("PUBLIC".equals(doc.getVisibility())) return true;
        if ("INTERNAL".equals(doc.getVisibility()) && VIEWER_ROLES.contains(userRole)) return true;
        if ("RESTRICTED".equals(doc.getVisibility()) && EDITOR_ROLES.contains(userRole)) return true;
        return "CONFIDENTIAL".equals(doc.getVisibility()) && ADMIN_ROLES.contains(userRole);
    }

    public boolean canEditDocument(UUID documentId, UUID userId, String userRole) {
        if (ADMIN_ROLES.contains(userRole)) return true;
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(documentId).orElse(null);
        if (doc == null) return false;
        if (EDITOR_ROLES.contains(userRole)) return true;
        return doc.getCreatedBy() != null && doc.getCreatedBy().equals(userId);
    }

    public boolean canDeleteDocument(UUID documentId, UUID userId, String userRole) {
        if (ADMIN_ROLES.contains(userRole)) return true;
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(documentId).orElse(null);
        return doc != null && doc.getCreatedBy() != null && doc.getCreatedBy().equals(userId);
    }

    public void checkViewPermission(UUID documentId, UUID userId, String userRole) {
        if (!canViewDocument(documentId, userId, userRole)) {
            log.warn("Access denied: user {} role {} cannot view document {}", userId, userRole, documentId);
            throw new KnowledgeSecurityException("Access denied: insufficient permissions to view document");
        }
    }

    public void checkEditPermission(UUID documentId, UUID userId, String userRole) {
        if (!canEditDocument(documentId, userId, userRole)) {
            log.warn("Access denied: user {} role {} cannot edit document {}", userId, userRole, documentId);
            throw new KnowledgeSecurityException("Access denied: insufficient permissions to edit document");
        }
    }

    public void checkDeletePermission(UUID documentId, UUID userId, String userRole) {
        if (!canDeleteDocument(documentId, userId, userRole)) {
            log.warn("Access denied: user {} role {} cannot delete document {}", userId, userRole, documentId);
            throw new KnowledgeSecurityException("Access denied: insufficient permissions to delete document");
        }
    }

    public void logAccess(UUID documentId, UUID userId, String userRole, String action, String ipAddress) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(documentId).orElse(null);
        if (doc == null) return;
        KnowledgeAccessLogEntity logEntry = new KnowledgeAccessLogEntity(doc, action);
        logEntry.setUserId(userId);
        logEntry.setUserRole(userRole);
        logEntry.setIpAddress(ipAddress);
        accessLogRepository.save(logEntry);
    }

    public List<KnowledgeAccessLogEntity> getAccessLogs(UUID documentId) {
        return accessLogRepository.findByDocumentIdOrderByTimestampDesc(documentId);
    }

    public long countAccessByAction(String action) {
        return accessLogRepository.countByAction(action);
    }
}
