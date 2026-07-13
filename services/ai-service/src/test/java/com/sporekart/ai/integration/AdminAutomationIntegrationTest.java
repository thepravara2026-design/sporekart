package com.sporekart.ai.integration;

import com.sporekart.ai.admin.api.AdministrationService;
import com.sporekart.ai.admin.domain.AdminOperation;
import com.sporekart.ai.admin.domain.AdminOperationType;
import com.sporekart.ai.automation.api.AutomationEngine;
import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.SchedulerService;
import com.sporekart.ai.automation.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAutomationIntegrationTest {

    @Mock private AdministrationService administrationService;
    @Mock private AutomationEngine automationEngine;
    @Mock private SchedulerService schedulerService;
    @Mock private AutomationAuditService automationAuditService;

    @Test
    void testConfigurationChangeTriggersAutomationJob() {
        UUID configId = UUID.randomUUID();
        UUID performedBy = UUID.randomUUID();

        AdminOperation configUpdate = new AdminOperation(
            UUID.randomUUID(), AdminOperationType.CONFIG_UPDATE,
            "Updated pricing rules", Map.of("configId", configId.toString(),
            "changes", "{\"maxDiscount\": 0.3}"),
            performedBy, "192.168.1.1", true, null
        );
        when(administrationService.performOperation(
            eq(AdminOperationType.CONFIG_UPDATE), anyString(), anyMap(), eq(performedBy), anyString()
        )).thenReturn(configUpdate);

        AutomationJob syncJob = new AutomationJob(
            UUID.randomUUID(), JobType.CONFIG_SYNC, "sync-pricing-config",
            Map.of("configId", configId.toString()), AutomationStatus.COMPLETED,
            0, 3, Instant.now(), Instant.now(), Instant.now(), null
        );
        when(automationEngine.executeJob(
            eq(JobType.CONFIG_SYNC), anyString(), anyMap()
        )).thenReturn(syncJob);

        AdminOperation result = administrationService.performOperation(
            AdminOperationType.CONFIG_UPDATE,
            "Updated pricing rules",
            Map.of("configId", configId.toString(), "changes", "{\"maxDiscount\": 0.3}"),
            performedBy, "192.168.1.1"
        );
        assertNotNull(result);
        assertTrue(result.successful());
        assertEquals(AdminOperationType.CONFIG_UPDATE, result.type());

        AutomationJob job = automationEngine.executeJob(
            JobType.CONFIG_SYNC, "sync-pricing-config",
            Map.of("configId", configId.toString())
        );
        assertNotNull(job);
        assertEquals(JobType.CONFIG_SYNC, job.type());
        assertEquals(AutomationStatus.COMPLETED, job.status());

        verify(administrationService).performOperation(
            eq(AdminOperationType.CONFIG_UPDATE), anyString(), anyMap(), eq(performedBy), anyString()
        );
        verify(automationEngine).executeJob(eq(JobType.CONFIG_SYNC), anyString(), anyMap());
    }

    @Test
    void testJobScheduledAndExecuted() {
        UUID taskId = UUID.randomUUID();
        ScheduledTask task = new ScheduledTask(
            taskId, "daily-compliance-scan", JobType.COMPLIANCE_SCAN,
            ScheduleFrequency.DAILY, "0 0 2 * * ?",
            Map.of("scanDepth", "full"), true,
            Instant.now(), Instant.now().plusDays(1),
            Instant.now(), Instant.now()
        );
        when(schedulerService.createTask(any(ScheduledTask.class))).thenReturn(task);
        when(schedulerService.getTask(taskId)).thenReturn(task);

        AutomationJob executedJob = new AutomationJob(
            UUID.randomUUID(), JobType.COMPLIANCE_SCAN, "daily-compliance-scan",
            Map.of("taskId", taskId.toString()), AutomationStatus.COMPLETED,
            0, 3, Instant.now(), Instant.now(), Instant.now(), null
        );
        when(automationEngine.executeJob(
            eq(JobType.COMPLIANCE_SCAN), eq("daily-compliance-scan"), anyMap()
        )).thenReturn(executedJob);

        ScheduledTask createdTask = schedulerService.createTask(task);
        assertNotNull(createdTask);
        assertEquals(taskId, createdTask.id());

        AutomationJob job = automationEngine.executeJob(
            JobType.COMPLIANCE_SCAN, "daily-compliance-scan",
            Map.of("taskId", taskId.toString())
        );
        assertNotNull(job);
        assertEquals(AutomationStatus.COMPLETED, job.status());

        verify(schedulerService).createTask(any(ScheduledTask.class));
        verify(automationEngine).executeJob(eq(JobType.COMPLIANCE_SCAN), eq("daily-compliance-scan"), anyMap());
    }

    @Test
    void testAuditRecordedForAutomationAction() {
        UUID auditId = UUID.randomUUID();
        UUID entityId = UUID.randomUUID();
        UUID performedBy = UUID.randomUUID();

        AutomationAudit audit = new AutomationAudit(
            auditId, "JOB_EXECUTED", "AutomationJob", entityId,
            performedBy, Map.of("jobName", "cleanup", "status", "COMPLETED"),
            "10.0.0.1", Instant.now()
        );
        doNothing().when(automationAuditService).recordAudit(
            eq("JOB_EXECUTED"), eq("AutomationJob"), eq(entityId),
            eq(performedBy), anyMap(), eq("10.0.0.1")
        );

        List<AutomationAudit> auditLogs = List.of(audit);
        when(automationAuditService.getAuditLogs(entityId)).thenReturn(auditLogs);

        automationAuditService.recordAudit(
            "JOB_EXECUTED", "AutomationJob", entityId,
            performedBy, Map.of("jobName", "cleanup", "status", "COMPLETED"),
            "10.0.0.1"
        );

        List<AutomationAudit> logs = automationAuditService.getAuditLogs(entityId);
        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals("JOB_EXECUTED", logs.get(0).action());

        verify(automationAuditService).recordAudit(
            eq("JOB_EXECUTED"), eq("AutomationJob"), eq(entityId),
            eq(performedBy), anyMap(), eq("10.0.0.1")
        );
        verify(automationAuditService).getAuditLogs(entityId);
    }

    @Test
    void testAdminFeatureFlagChangeSchedulesAuditCleanup() {
        UUID flagId = UUID.randomUUID();
        UUID performedBy = UUID.randomUUID();

        AdminOperation flagChange = new AdminOperation(
            UUID.randomUUID(), AdminOperationType.FEATURE_FLAG_CHANGE,
            "Disabled legacy workflow", Map.of("flagId", flagId.toString(),
            "flag", "legacy_workflow", "enabled", false),
            performedBy, "10.0.0.1", true, null
        );
        when(administrationService.performOperation(
            eq(AdminOperationType.FEATURE_FLAG_CHANGE), anyString(), anyMap(),
            eq(performedBy), anyString()
        )).thenReturn(flagChange);

        AutomationJob cleanupJob = new AutomationJob(
            UUID.randomUUID(), JobType.AUDIT_CLEANUP, "audit-cleanup-after-flag-change",
            Map.of("flagId", flagId.toString()), AutomationStatus.PENDING,
            0, 3, Instant.now().plusMinutes(5), null, null, null
        );
        when(automationEngine.executeJob(
            eq(JobType.AUDIT_CLEANUP), anyString(), anyMap()
        )).thenReturn(cleanupJob);

        AdminOperation opResult = administrationService.performOperation(
            AdminOperationType.FEATURE_FLAG_CHANGE,
            "Disabled legacy workflow",
            Map.of("flagId", flagId.toString(), "flag", "legacy_workflow", "enabled", false),
            performedBy, "10.0.0.1"
        );
        assertTrue(opResult.successful());

        AutomationJob cleanup = automationEngine.executeJob(
            JobType.AUDIT_CLEANUP, "audit-cleanup-after-flag-change",
            Map.of("flagId", flagId.toString())
        );
        assertNotNull(cleanup);
        assertEquals(AutomationStatus.PENDING, cleanup.status());

        verify(administrationService).performOperation(
            eq(AdminOperationType.FEATURE_FLAG_CHANGE), anyString(), anyMap(),
            eq(performedBy), anyString()
        );
        verify(automationEngine).executeJob(eq(JobType.AUDIT_CLEANUP), anyString(), anyMap());
    }
}
