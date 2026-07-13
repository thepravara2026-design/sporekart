package com.sporekart.ai.eventcatalog.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCatalogRepository extends JpaRepository<EventCatalogEntity, String> {

    List<EventCatalogEntity> findByModule(String module);

    List<EventCatalogEntity> findByConsumersContaining(String consumer);
}
