package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.domain.EnvironmentProfile;
import com.sporekart.ai.admin.domain.EnvironmentType;
import com.sporekart.ai.admin.infrastructure.persistence.EnvironmentProfileEntity;
import com.sporekart.ai.admin.infrastructure.persistence.EnvironmentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentManagerImplTest {

    @Mock private EnvironmentProfileRepository environmentProfileRepository;
    @Mock private AdministrationAuditService administrationAuditService;

    private EnvironmentManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new EnvironmentManagerImpl(environmentProfileRepository, administrationAuditService);
    }

    private EnvironmentProfileEntity createEntity(UUID id, String name, boolean active) {
        var entity = new EnvironmentProfileEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setType(EnvironmentType.PRODUCTION.name());
        entity.setDescription("desc");
        entity.setActive(active);
        entity.setConfigSource("db");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void testGetEnvironment_Found() {
        var id = UUID.randomUUID();
        var entity = createEntity(id, "production", true);
        when(environmentProfileRepository.findByName("production")).thenReturn(Optional.of(entity));

        var result = manager.getEnvironment("production");
        assertNotNull(result);
        assertEquals("production", result.name());
        assertTrue(result.active());
    }

    @Test
    void testGetEnvironment_NotFound() {
        when(environmentProfileRepository.findByName(any())).thenReturn(Optional.empty());
        assertNull(manager.getEnvironment("nonexistent"));
    }

    @Test
    void testCreateEnvironment() {
        var savedEntity = createEntity(UUID.randomUUID(), "staging", true);
        when(environmentProfileRepository.save(any())).thenReturn(savedEntity);

        var profile = new EnvironmentProfile(null, "staging", EnvironmentType.STAGING, "Staging env",
                true, "db", null, null);
        var result = manager.createEnvironment(profile);
        assertNotNull(result);
        assertEquals("staging", result.name());
        verify(administrationAuditService).recordAudit(eq("ENVIRONMENT_CREATE"), any(), any(), isNull(), any(), any());
    }

    @Test
    void testUpdateEnvironment() {
        var id = UUID.randomUUID();
        var existing = createEntity(id, "dev", false);
        when(environmentProfileRepository.findById(id)).thenReturn(Optional.of(existing));
        when(environmentProfileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var profile = new EnvironmentProfile(id, "development", EnvironmentType.DEVELOPMENT, "Updated desc",
                true, "file", null, null);
        var result = manager.updateEnvironment(id, profile);
        assertTrue(result.active());
        assertEquals("development", result.name());
        verify(administrationAuditService).recordAudit(eq("ENVIRONMENT_UPDATE"), any(), any(), isNull(), any(), any());
    }

    @Test
    void testUpdateEnvironment_NotFound() {
        when(environmentProfileRepository.findById(any())).thenReturn(Optional.empty());
        var profile = new EnvironmentProfile(UUID.randomUUID(), "x", null, null, false, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> manager.updateEnvironment(UUID.randomUUID(), profile));
    }

    @Test
    void testGetAllEnvironments() {
        when(environmentProfileRepository.findAll()).thenReturn(List.of(createEntity(UUID.randomUUID(), "prod", true)));
        assertEquals(1, manager.getAllEnvironments().size());
    }

    @Test
    void testSwitchEnvironment() {
        var prodId = UUID.randomUUID();
        var stagingId = UUID.randomUUID();
        var prod = createEntity(prodId, "production", true);
        var staging = createEntity(stagingId, "staging", false);

        when(environmentProfileRepository.findAll()).thenReturn(List.of(prod, staging));
        when(environmentProfileRepository.findByName("staging")).thenReturn(Optional.of(staging));
        when(environmentProfileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = manager.switchEnvironment("staging");
        assertTrue(result.active());
        assertEquals("staging", result.name());
        verify(administrationAuditService).recordAudit(eq("ENVIRONMENT_SWITCH"), any(), any(), isNull(), any(), any());
    }

    @Test
    void testSwitchEnvironment_NotFound() {
        when(environmentProfileRepository.findAll()).thenReturn(List.of());
        when(environmentProfileRepository.findByName(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> manager.switchEnvironment("nonexistent"));
    }
}
