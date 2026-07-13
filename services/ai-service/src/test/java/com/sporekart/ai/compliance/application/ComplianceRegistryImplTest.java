package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceFrameworkRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceRegistryImplTest {

    @Mock private ComplianceFrameworkRepository frameworkRepository;
    @Mock private ComplianceRuleRepository ruleRepository;
    @Mock private ComplianceAuditService complianceAuditService;

    private ComplianceRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ComplianceRegistryImpl(frameworkRepository, ruleRepository, complianceAuditService);
    }

    @Test
    void testRegisterFramework() {
        ComplianceFramework framework = new ComplianceFramework(
            UUID.randomUUID(), "Test", "1.0", ComplianceFrameworkType.INTERNAL_AI_GOVERNANCE,
            "desc", "auth", List.of(), true
        );
        when(frameworkRepository.save(any())).thenReturn(framework);

        ComplianceFramework result = registry.registerFramework(framework);

        assertNotNull(result);
        assertEquals("Test", result.name());
        verify(complianceAuditService).recordAudit(
            eq("FRAMEWORK_REGISTER"), eq("FRAMEWORK"), any(), isNull(), anyMap(), eq("registry")
        );
    }

    @Test
    void testUpdateFramework() {
        ComplianceFramework framework = new ComplianceFramework(
            UUID.randomUUID(), "Updated", "2.0", ComplianceFrameworkType.ISO_27001,
            "updated desc", "auth", List.of(), true
        );
        when(frameworkRepository.save(any())).thenReturn(framework);

        ComplianceFramework result = registry.updateFramework(framework);

        assertEquals("Updated", result.name());
        assertEquals("2.0", result.version());
        verify(complianceAuditService).recordAudit(
            eq("FRAMEWORK_UPDATE"), eq("FRAMEWORK"), any(), isNull(), anyMap(), eq("registry")
        );
    }

    @Test
    void testGetFramework() {
        UUID id = UUID.randomUUID();
        ComplianceFramework framework = new ComplianceFramework(
            id, "Test", "1.0", ComplianceFrameworkType.GDPR,
            "desc", "auth", List.of(), true
        );
        when(frameworkRepository.findById(id)).thenReturn(Optional.of(framework));

        Optional<ComplianceFramework> result = registry.getFramework(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void testGetAllFrameworks() {
        List<ComplianceFramework> frameworks = List.of(
            new ComplianceFramework(UUID.randomUUID(), "FW1", "1.0", ComplianceFrameworkType.GDPR,
                "desc", "auth", List.of(), true),
            new ComplianceFramework(UUID.randomUUID(), "FW2", "1.0", ComplianceFrameworkType.ISO_27001,
                "desc", "auth", List.of(), true)
        );
        when(frameworkRepository.findAll()).thenReturn(frameworks);

        List<ComplianceFramework> result = registry.getAllFrameworks();

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteFramework() {
        UUID id = UUID.randomUUID();
        registry.deleteFramework(id);

        verify(frameworkRepository).deleteById(id);
        verify(complianceAuditService).recordAudit(
            eq("FRAMEWORK_DELETE"), eq("FRAMEWORK"), eq(id), isNull(), anyMap(), eq("registry")
        );
    }

    @Test
    void testRegisterRule() {
        ComplianceRule rule = new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
            RiskLevel.MEDIUM, "expr", true, Instant.now(), Instant.now().plusSeconds(86400)
        );
        when(ruleRepository.save(any())).thenReturn(rule);

        ComplianceRule result = registry.registerRule(rule);

        assertNotNull(result);
        assertEquals("R1", result.ruleId());
        verify(complianceAuditService).recordAudit(
            eq("RULE_REGISTER"), eq("RULE"), any(), isNull(), anyMap(), eq("registry")
        );
    }

    @Test
    void testUpdateRule() {
        ComplianceRule rule = new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "R1", "Updated", "desc", "cat",
            RiskLevel.HIGH, "expr", true, Instant.now(), Instant.now().plusSeconds(86400)
        );
        when(ruleRepository.save(any())).thenReturn(rule);

        ComplianceRule result = registry.updateRule(rule);

        assertEquals("Updated", result.name());
        verify(complianceAuditService).recordAudit(
            eq("RULE_UPDATE"), eq("RULE"), any(), isNull(), anyMap(), eq("registry")
        );
    }

    @Test
    void testGetRule() {
        UUID id = UUID.randomUUID();
        ComplianceRule rule = new ComplianceRule(
            id, UUID.randomUUID(), "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "expr", true, Instant.now(), Instant.now().plusSeconds(86400)
        );
        when(ruleRepository.findById(id)).thenReturn(Optional.of(rule));

        Optional<ComplianceRule> result = registry.getRule(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void testGetRulesByFramework() {
        UUID frameworkId = UUID.randomUUID();
        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule1", "desc", "cat",
                RiskLevel.LOW, "expr", true, Instant.now(), Instant.now().plusSeconds(86400))
        );
        when(ruleRepository.findByFrameworkId(frameworkId)).thenReturn(rules);

        List<ComplianceRule> result = registry.getRulesByFramework(frameworkId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetActiveRules() {
        UUID frameworkId = UUID.randomUUID();
        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule1", "desc", "cat",
                RiskLevel.LOW, "expr", true, Instant.now(), Instant.now().plusSeconds(86400))
        );
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        List<ComplianceRule> result = registry.getActiveRules(frameworkId);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteRule() {
        UUID id = UUID.randomUUID();
        registry.deleteRule(id);

        verify(ruleRepository).deleteById(id);
        verify(complianceAuditService).recordAudit(
            eq("RULE_DELETE"), eq("RULE"), eq(id), isNull(), anyMap(), eq("registry")
        );
    }
}
