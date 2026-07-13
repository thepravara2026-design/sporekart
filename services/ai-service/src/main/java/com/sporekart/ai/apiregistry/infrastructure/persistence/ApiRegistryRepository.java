package com.sporekart.ai.apiregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiRegistryRepository extends JpaRepository<ApiRegistryEntity, UUID> {

    List<ApiRegistryEntity> findByModule(String module);

    List<ApiRegistryEntity> findByApiPathContaining(String path);

    List<ApiRegistryEntity> findByOwner(String owner);

    java.util.Optional<ApiRegistryEntity> findByApiId(String apiId);
}
