package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ModulithArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void governanceOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("governance");
    }

    @Test
    void policyOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("policy");
    }

    @Test
    void decisionOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("decision");
    }

    @Test
    void approvalOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("approval");
    }

    @Test
    void complianceOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("compliance");
    }

    @Test
    void riskOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("risk");
    }

    @Test
    void analyticsOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("analytics");
    }

    @Test
    void adminOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("admin");
    }

    @Test
    void automationOnlyDependsOnApiDomainInfrastructure() {
        assertModuleDependsOnlyOn("automation");
    }

    @Test
    void governanceModulesDoNotDependOnEachOther() {
        String[] modules = {"governance", "policy", "decision", "approval",
                "compliance", "risk", "analytics", "admin", "automation"};
        for (String module : modules) {
            for (String other : modules) {
                if (!module.equals(other)) {
                    ArchRule rule = noClasses()
                            .that().resideInAPackage(".." + module + "..")
                            .should().dependOnClassesThat()
                            .resideInAPackage(".." + other + ".application..")
                            .orShould().dependOnClassesThat()
                            .resideInAPackage(".." + other + ".infrastructure..")
                            .orShould().dependOnClassesThat()
                            .resideInAPackage(".." + other + ".interfaces..");
                    rule.check(classes);
                }
            }
        }
    }

    @Test
    void allModulesDependOnlyOnSharedKernel() {
        String[] modules = {"governance", "policy", "decision", "approval",
                "compliance", "risk", "analytics", "admin", "automation"};
        for (String module : modules) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage(".." + module + "..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..core..", "..infrastructure.config..",
                            "..infrastructure.security..", "..infrastructure.persistence..");
            rule.check(classes);
        }
    }

    private void assertModuleDependsOnlyOn(String module) {
        String[] forbidden = {"gateway", "provider", "prompt", "rag", "search",
                "chat", "content", "workflow", "monitoring", "conversation",
                "knowledge", "semantic", "assistant"};
        for (String other : forbidden) {
            if (!other.equals(module)) {
                ArchRule rule = noClasses()
                        .that().resideInAPackage(".." + module + "..")
                        .should().dependOnClassesThat()
                        .resideInAPackage(".." + other + "..");
                rule.check(classes);
            }
        }
    }
}
