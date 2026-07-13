package com.sporekart.ai.configregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigSnapshotRepository extends JpaRepository<ConfigSnapshotEntity, String> {
}
