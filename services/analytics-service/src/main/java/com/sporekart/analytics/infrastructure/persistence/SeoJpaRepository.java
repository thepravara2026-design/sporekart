package com.sporekart.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeoJpaRepository extends JpaRepository<SeoMetadataEntity, String> {
}
