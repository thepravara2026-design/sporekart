package com.sporekart.ai.compliance.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ComplianceFrameworkRepositoryTest {

    @Autowired private ComplianceFrameworkRepository repository;

    @Test
    void testSaveAndFindById() {
        ComplianceFrameworkEntity entity = new ComplianceFrameworkEntity();
        entity.setName("Test Framework");
        entity.setVersion("1.0.0");
        entity.setType("GDPR");
        entity.setDescription("Test description");
        entity.setAuthority("Test Authority");
        entity.setActive(true);

        ComplianceFrameworkEntity saved = repository.save(entity);
        assertNotNull(saved.getId());

        ComplianceFrameworkEntity found = repository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Framework", found.getName());
        assertTrue(found.getActive());
    }

    @Test
    void testFindAll() {
        ComplianceFrameworkEntity e1 = new ComplianceFrameworkEntity();
        e1.setName("FW1");
        e1.setVersion("1.0");
        e1.setType("GDPR");
        e1.setActive(true);

        ComplianceFrameworkEntity e2 = new ComplianceFrameworkEntity();
        e2.setName("FW2");
        e2.setVersion("2.0");
        e2.setType("ISO_27001");
        e2.setActive(true);

        repository.save(e1);
        repository.save(e2);

        List<ComplianceFrameworkEntity> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testDeleteById() {
        ComplianceFrameworkEntity entity = new ComplianceFrameworkEntity();
        entity.setName("To Delete");
        entity.setVersion("1.0");
        entity.setType("CUSTOM");
        entity.setActive(false);

        ComplianceFrameworkEntity saved = repository.save(entity);
        UUID id = saved.getId();

        repository.deleteById(id);
        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void testTimestampsAreSet() {
        ComplianceFrameworkEntity entity = new ComplianceFrameworkEntity();
        entity.setName("Timestamp Test");
        entity.setVersion("1.0");
        entity.setType("SOC_2");
        entity.setActive(true);

        ComplianceFrameworkEntity saved = repository.save(entity);

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }
}
