package com.sporekart.ai.apiregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiHealthRepository extends JpaRepository<ApiHealthEntity, UUID> {

    List<ApiHealthEntity> findByApiIdOrderByCheckedAtDesc(String apiId);

    java.util.Optional<ApiHealthEntity> findTopByApiIdOrderByCheckedAtDesc(String apiId);

    @Query("SELECT h FROM ApiHealthEntity h WHERE h.apiId = :apiId "
            + "AND h.checkedAt = (SELECT MAX(h2.checkedAt) FROM ApiHealthEntity h2 WHERE h2.apiId = :apiId)")
    java.util.Optional<ApiHealthEntity> findLatestByApiId(@Param("apiId") String apiId);

    void deleteByApiId(String apiId);
}
