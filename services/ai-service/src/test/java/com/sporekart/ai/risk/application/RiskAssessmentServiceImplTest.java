package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAssessmentEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskAssessmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class RiskAssessmentServiceImplTest {

    @Mock
    private RiskAssessmentRepository riskAssessmentRepository;
    @Mock
    private ObjectMapper objectMapper;

    private RiskAssessmentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RiskAssessmentServiceImpl(riskAssessmentRepository, objectMapper);
    }

    @Test
    void createAssessmentShouldReturnCreatedAssessment() {
        var module = "prompt";
        var action = "generate";
        var context = Map.<String, Object>of("key", "value");

        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(module);
        entity.setAction(action);
        entity.setStatus(RiskAssessmentStatus.PENDING.name());
        entity.setContext("{\"key\":\"value\"}");
        entity.setAssessedAt(LocalDateTime.now());

        when(riskAssessmentRepository.save(any(RiskAssessmentEntity.class))).thenReturn(entity);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"key\":\"value\"}");
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(context);

        var result = service.createAssessment(module, action, context);

        assertNotNull(result);
        assertEquals(module, result.module());
        assertEquals(action, result.action());
        assertEquals(RiskAssessmentStatus.PENDING, result.status());
        assertEquals(context, result.context());

        verify(riskAssessmentRepository).save(any(RiskAssessmentEntity.class));
    }

    @Test
    void getAssessmentShouldReturnAssessmentWhenFound() {
        var id = UUID.randomUUID();
        var entity = new RiskAssessmentEntity();
        entity.setId(id);
        entity.setModule("test");
        entity.setAction("run");
        entity.setStatus(RiskAssessmentStatus.COMPLETED.name());
        entity.setContext("{}");
        entity.setAssessedAt(LocalDateTime.now());

        when(riskAssessmentRepository.findById(id)).thenReturn(Optional.of(entity));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var result = service.getAssessment(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("test", result.module());
    }

    @Test
    void getAssessmentShouldThrowWhenNotFound() {
        var id = UUID.randomUUID();
        when(riskAssessmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getAssessment(id));
    }

    @Test
    void getAssessmentsByModuleShouldReturnList() {
        var module = "prompt";
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(module);
        entity.setAction("run");
        entity.setStatus(RiskAssessmentStatus.COMPLETED.name());
        entity.setContext("{}");
        entity.setAssessedAt(LocalDateTime.now());

        when(riskAssessmentRepository.findByModule(module)).thenReturn(List.of(entity));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var results = service.getAssessmentsByModule(module);

        assertEquals(1, results.size());
        assertEquals(module, results.get(0).module());
    }

    @Test
    void getAssessmentsByStatusShouldReturnFilteredList() {
        var status = RiskAssessmentStatus.PENDING;
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule("test");
        entity.setAction("run");
        entity.setStatus(status.name());
        entity.setContext("{}");
        entity.setAssessedAt(LocalDateTime.now());

        when(riskAssessmentRepository.findByStatus(status.name())).thenReturn(List.of(entity));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var results = service.getAssessmentsByStatus(status);

        assertEquals(1, results.size());
        assertEquals(status, results.get(0).status());
    }

    @Test
    void getAssessmentsByModuleShouldReturnEmptyListWhenNone() {
        when(riskAssessmentRepository.findByModule("unknown")).thenReturn(List.of());
        assertTrue(service.getAssessmentsByModule("unknown").isEmpty());
    }

    @Test
    void getAssessmentsByStatusShouldReturnEmptyListWhenNone() {
        when(riskAssessmentRepository.findByStatus(RiskAssessmentStatus.CANCELLED.name())).thenReturn(List.of());
        assertTrue(service.getAssessmentsByStatus(RiskAssessmentStatus.CANCELLED).isEmpty());
    }
}
