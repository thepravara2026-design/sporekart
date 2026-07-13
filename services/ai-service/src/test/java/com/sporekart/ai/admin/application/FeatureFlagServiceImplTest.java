package com.sporekart.ai.admin.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.domain.FeatureFlag;
import com.sporekart.ai.admin.infrastructure.persistence.FeatureFlagEntity;
import com.sporekart.ai.admin.infrastructure.persistence.FeatureFlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceImplTest {

    @Mock private FeatureFlagRepository featureFlagRepository;
    @Mock private AdministrationAuditService administrationAuditService;

    private FeatureFlagServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FeatureFlagServiceImpl(featureFlagRepository, administrationAuditService, new ObjectMapper());
    }

    private FeatureFlagEntity createEntity(UUID id, String key, boolean enabled) {
        var entity = new FeatureFlagEntity();
        entity.setId(id);
        entity.setKey(key);
        entity.setName(key);
        entity.setDescription("desc");
        entity.setEnabled(enabled);
        entity.setEnvironment("production");
        entity.setModule("governance");
        entity.setMetadata("{}");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void testGetFeatureFlag_Found() {
        var id = UUID.randomUUID();
        var entity = createEntity(id, "flag1", true);
        when(featureFlagRepository.findByKey("flag1")).thenReturn(Optional.of(entity));

        var result = service.getFeatureFlag("flag1");
        assertNotNull(result);
        assertEquals("flag1", result.key());
        assertTrue(result.enabled());
    }

    @Test
    void testGetFeatureFlag_NotFound() {
        when(featureFlagRepository.findByKey(any())).thenReturn(Optional.empty());
        assertNull(service.getFeatureFlag("nonexistent"));
    }

    @Test
    void testSetFeatureFlag_Create() {
        when(featureFlagRepository.findByKey(any())).thenReturn(Optional.empty());
        var savedEntity = createEntity(UUID.randomUUID(), "new-flag", true);
        when(featureFlagRepository.save(any())).thenReturn(savedEntity);

        var result = service.setFeatureFlag("new-flag", true, "production", "governance", Map.of(), UUID.randomUUID());
        assertNotNull(result);
        assertTrue(result.enabled());
        verify(administrationAuditService).recordAudit(eq("FEATURE_FLAG_CREATE"), any(), any(), any(), any(), any());
    }

    @Test
    void testSetFeatureFlag_Update() {
        var id = UUID.randomUUID();
        var existing = createEntity(id, "existing-flag", false);
        when(featureFlagRepository.findByKey("existing-flag")).thenReturn(Optional.of(existing));
        when(featureFlagRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.setFeatureFlag("existing-flag", true, "staging", "core", Map.of("k", "v"), UUID.randomUUID());
        assertTrue(result.enabled());
        verify(administrationAuditService).recordAudit(eq("FEATURE_FLAG_CHANGE"), any(), any(), any(), any(), any());
    }

    @Test
    void testIsFeatureEnabled_True() {
        var entity = createEntity(UUID.randomUUID(), "flag", true);
        when(featureFlagRepository.findByKey("flag")).thenReturn(Optional.of(entity));
        assertTrue(service.isFeatureEnabled("flag"));
    }

    @Test
    void testIsFeatureEnabled_False() {
        var entity = createEntity(UUID.randomUUID(), "flag", false);
        when(featureFlagRepository.findByKey("flag")).thenReturn(Optional.of(entity));
        assertFalse(service.isFeatureEnabled("flag"));
    }

    @Test
    void testIsFeatureEnabled_NotFound() {
        when(featureFlagRepository.findByKey(any())).thenReturn(Optional.empty());
        assertFalse(service.isFeatureEnabled("nonexistent"));
    }

    @Test
    void testGetAllFeatureFlags() {
        when(featureFlagRepository.findAll()).thenReturn(List.of(createEntity(UUID.randomUUID(), "f1", true)));
        assertEquals(1, service.getAllFeatureFlags().size());
    }

    @Test
    void testGetFeatureFlagsByModule() {
        when(featureFlagRepository.findByModule("governance")).thenReturn(List.of(createEntity(UUID.randomUUID(), "f1", true)));
        assertEquals(1, service.getFeatureFlagsByModule("governance").size());
    }

    @Test
    void testGetFeatureFlagsByEnvironment() {
        when(featureFlagRepository.findByEnvironment("production")).thenReturn(List.of(createEntity(UUID.randomUUID(), "f1", true)));
        assertEquals(1, service.getFeatureFlagsByEnvironment("production").size());
    }
}
