package com.sporekart.ai.analytics.infrastructure.persistence;

import com.sporekart.ai.analytics.domain.MetricType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GovernanceMetricRepositoryTest {

    @Autowired private GovernanceMetricRepository repository;

    @Test
    void testSaveAndFindByModule() {
        var entity = new GovernanceMetricEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test-metric");
        entity.setModule("module1");
        entity.setType(MetricType.COUNT.name());
        entity.setValue(42.0);
        entity.setLabels("{}");
        entity.setRecordedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());

        repository.save(entity);

        List<GovernanceMetricEntity> found = repository.findByModule("module1");

        assertEquals(1, found.size());
        assertEquals("test-metric", found.get(0).getName());
    }

    @Test
    void testSaveAndFindByName() {
        var entity = new GovernanceMetricEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("unique-name");
        entity.setModule("mod2");
        entity.setType(MetricType.RATE.name());
        entity.setValue(10.0);
        entity.setLabels("{}");
        entity.setRecordedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());

        repository.save(entity);

        List<GovernanceMetricEntity> found = repository.findByName("unique-name");

        assertEquals(1, found.size());
        assertEquals("unique-name", found.get(0).getName());
    }
}
