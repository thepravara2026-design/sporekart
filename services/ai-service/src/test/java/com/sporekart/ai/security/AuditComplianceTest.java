package com.sporekart.ai.security;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AuditComplianceTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void governanceModuleHasAuditService() {
        assertModuleHasAuditService("governance");
    }

    @Test
    void policyModuleHasAuditService() {
        assertModuleHasAuditService("policy");
    }

    @Test
    void decisionModuleHasAuditService() {
        assertModuleHasAuditService("decision");
    }

    @Test
    void approvalModuleHasAuditService() {
        assertModuleHasAuditService("approval");
    }

    @Test
    void complianceModuleHasAuditService() {
        assertModuleHasAuditService("compliance");
    }

    @Test
    void riskModuleHasAuditService() {
        assertModuleHasAuditService("risk");
    }

    @Test
    void analyticsModuleHasAuditService() {
        assertModuleHasAuditService("analytics");
    }

    @Test
    void adminModuleHasAuditService() {
        assertModuleHasAuditService("admin");
    }

    @Test
    void automationModuleHasAuditService() {
        assertModuleHasAuditService("automation");
    }

    @Test
    void eachAuditServiceImplementsRecordAudit() {
        String[] modules = {"governance", "policy", "decision", "approval",
                "compliance", "risk", "analytics", "admin", "automation"};
        for (String module : modules) {
            var auditServices = classes.that()
                    .resideInAPackage(".." + module + ".api..")
                    .and().haveSimpleNameContaining("AuditService")
                    .filter(c -> c.isInterface());
            assertFalse(auditServices.isEmpty(),
                    module + " should have AuditService interface in api package");
            auditServices.forEach(svc -> {
                boolean hasRecordAudit = svc.getMethods().stream()
                        .anyMatch(m -> m.getName().equals("recordAudit"));
                assertTrue(hasRecordAudit,
                        svc.getName() + " in " + module + " should implement recordAudit()");
            });
        }
    }

    private void assertModuleHasAuditService(String module) {
        var auditServices = classes.that()
                .resideInAPackage(".." + module + ".api..")
                .and().haveSimpleNameContaining("AuditService")
                .filter(c -> c.isInterface());
        assertFalse(auditServices.isEmpty(),
                module + " should have an AuditService interface");
    }
}
