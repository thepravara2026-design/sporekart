package com.sporekart.memory.repository;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.Visibility;
import com.sporekart.memory.persistence.MemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoryRepository extends JpaRepository<MemoryEntity, UUID> {
    Optional<MemoryEntity> findByIdAndIsDeletedFalse(UUID id);
    List<MemoryEntity> findByIsDeletedFalse();

    List<MemoryEntity> findByOwnerIdAndIsDeletedFalse(UUID ownerId);
    List<MemoryEntity> findByOwnerTypeAndOwnerIdAndIsDeletedFalse(String ownerType, UUID ownerId);
    List<MemoryEntity> findByWorkspaceAndIsDeletedFalse(String workspace);
    List<MemoryEntity> findByMemoryTypeAndIsDeletedFalse(MemoryType memoryType);
    List<MemoryEntity> findBySourceAndIsDeletedFalse(String source);
    List<MemoryEntity> findByConversationIdAndIsDeletedFalse(UUID conversationId);
    List<MemoryEntity> findByEntityTypeAndEntityIdAndIsDeletedFalse(String entityType, UUID entityId);
    List<MemoryEntity> findByPriorityAndIsDeletedFalse(Priority priority);
    List<MemoryEntity> findByVisibilityAndIsDeletedFalse(Visibility visibility);

    @Query("SELECT m FROM MemoryEntity m JOIN m.tags t WHERE t.name IN :tags AND m.isDeleted = false")
    List<MemoryEntity> findByTagNames(@Param("tags") List<String> tags);

    @Query("SELECT m FROM MemoryEntity m WHERE m.importance >= :minImportance AND m.isDeleted = false")
    List<MemoryEntity> findByMinImportance(@Param("minImportance") int minImportance);

    @Query("SELECT m FROM MemoryEntity m WHERE m.createdAt >= :since AND m.isDeleted = false")
    List<MemoryEntity> findByCreatedSince(@Param("since") OffsetDateTime since);

    @Query("SELECT m FROM MemoryEntity m WHERE m.expiresAt IS NOT NULL AND m.expiresAt <= :now AND m.isDeleted = false")
    List<MemoryEntity> findExpired(@Param("now") OffsetDateTime now);

    @Query("SELECT m FROM MemoryEntity m WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.summary) LIKE LOWER(CONCAT('%', :query, '%')) AND m.isDeleted = false")
    List<MemoryEntity> search(@Param("query") String query);

    @Query("SELECT m FROM MemoryEntity m WHERE m.workspace = :workspace AND m.department = :department AND m.isDeleted = false")
    List<MemoryEntity> findByWorkspaceAndDepartment(@Param("workspace") String workspace, @Param("department") String department);

    long countByOwnerIdAndIsDeletedFalse(UUID ownerId);
    long countByWorkspaceAndIsDeletedFalse(String workspace);
    long countByIsDeletedFalse();
}
