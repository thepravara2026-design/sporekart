package com.sporekart.ai.admin.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminConfigurationRepositoryTest {

    @Autowired
    private AdminConfigurationRepository repository;

    @Test
    void testSaveAndFindById() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("test.key");
        entity.setValue("test-value");
        entity.setModule("governance");
        entity.setEnvironment("production");
        entity.setDescription("Test config");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());

        var saved = repository.save(entity);
        assertNotNull(saved.getId());

        var found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test.key", found.get().getKey());
    }

    @Test
    void testFindByKeyAndModuleAndEnvironment() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("my.key");
        entity.setValue("my-value");
        entity.setModule("module-a");
        entity.setEnvironment("staging");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        repository.save(entity);

        var found = repository.findByKeyAndModuleAndEnvironment("my.key", "module-a", "staging");
        assertTrue(found.isPresent());
        assertEquals("my-value", found.get().getValue());
    }

    @Test
    void testFindByKeyAndModuleAndEnvironment_NotFound() {
        var found = repository.findByKeyAndModuleAndEnvironment("nonexistent", "mod", "env");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByModule() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("k1");
        entity.setValue("v1");
        entity.setModule("module-b");
        entity.setEnvironment("dev");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        repository.save(entity);

        var results = repository.findByModule("module-b");
        assertEquals(1, results.size());
    }

    @Test
    void testFindByEnvironment() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("k2");
        entity.setValue("v2");
        entity.setModule("mod");
        entity.setEnvironment("production");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        repository.save(entity);

        var results = repository.findByEnvironment("production");
        assertEquals(1, results.size());
    }

    @Test
    void testFindByKey() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("unique-key");
        entity.setValue("unique-value");
        entity.setModule("mod");
        entity.setEnvironment("env");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        repository.save(entity);

        var found = repository.findByKey("unique-key");
        assertTrue(found.isPresent());
        assertEquals("unique-value", found.get().getValue());
    }

    @Test
    void testDelete() {
        var entity = new AdminConfigurationEntity();
        entity.setKey("del-key");
        entity.setValue("del-value");
        entity.setModule("mod");
        entity.setEnvironment("env");
        entity.setStatus("ACTIVE");
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        var saved = repository.save(entity);

        repository.delete(saved);
        assertFalse(repository.findById(saved.getId()).isPresent());
    }
}
