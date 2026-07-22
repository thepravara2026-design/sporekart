package com.sporekart.prompt.repository;

import com.sporekart.prompt.entity.PromptUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PromptUsageRepository extends JpaRepository<PromptUsageEntity, UUID> {
    List<PromptUsageEntity> findByTemplateIdOrderByCreatedAtDesc(UUID templateId);

    @Query("SELECT COUNT(u) FROM PromptUsageEntity u WHERE u.templateId = :templateId")
    long countExecutionsByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT AVG(u.latencyMs) FROM PromptUsageEntity u WHERE u.templateId = :templateId AND u.isSuccess = true")
    Double averageLatencyByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT AVG(u.totalTokens) FROM PromptUsageEntity u WHERE u.templateId = :templateId AND u.isSuccess = true")
    Double averageTokensByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT AVG(u.cost) FROM PromptUsageEntity u WHERE u.templateId = :templateId AND u.isSuccess = true")
    Double averageCostByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT COUNT(u) FROM PromptUsageEntity u WHERE u.templateId = :templateId AND u.isSuccess = true")
    long countSuccessByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT COUNT(u) FROM PromptUsageEntity u WHERE u.templateId = :templateId AND u.isSuccess = false")
    long countFailureByTemplateId(@Param("templateId") UUID templateId);

    @Query("SELECT COUNT(u) FROM PromptUsageEntity u WHERE u.createdAt >= :since")
    long countExecutionsSince(@Param("since") OffsetDateTime since);

    @Query("SELECT u.templateId, COUNT(u) as cnt FROM PromptUsageEntity u GROUP BY u.templateId ORDER BY cnt DESC")
    List<Object[]> findMostUsedPrompts();

    @Query("SELECT u.templateId, COUNT(u) as cnt FROM PromptUsageEntity u GROUP BY u.templateId ORDER BY cnt ASC")
    List<Object[]> findLeastUsedPrompts();
}
