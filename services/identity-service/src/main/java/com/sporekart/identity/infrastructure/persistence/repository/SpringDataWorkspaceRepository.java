package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.infrastructure.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataWorkspaceRepository extends JpaRepository<WorkspaceEntity, String> {
    List<WorkspaceEntity> findByOwnerId(String ownerId);
    boolean existsByName(String name);
}
