package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleBoundaryTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void governanceHasAllRequiredPackages() {
        assertModuleHasPackages("governance");
    }

    @Test
    void policyHasAllRequiredPackages() {
        assertModuleHasPackages("policy");
    }

    @Test
    void decisionHasAllRequiredPackages() {
        assertModuleHasPackages("decision");
    }

    @Test
    void approvalHasAllRequiredPackages() {
        assertModuleHasPackages("approval");
    }

    @Test
    void complianceHasAllRequiredPackages() {
        assertModuleHasPackages("compliance");
    }

    @Test
    void riskHasAllRequiredPackages() {
        assertModuleHasPackages("risk");
    }

    @Test
    void analyticsHasAllRequiredPackages() {
        assertModuleHasPackages("analytics");
    }

    @Test
    void adminHasAllRequiredPackages() {
        assertModuleHasPackages("admin");
    }

    @Test
    void automationHasAllRequiredPackages() {
        assertModuleHasPackages("automation");
    }

    @Test
    void governanceDomainHasEnumsAndRecords() {
        assertPackageContainsTypes("governance", "domain", Set.of("enum", "record"));
    }

    @Test
    void governanceApiHasInterfaces() {
        assertPackageContainsInterfaces("governance", "api");
    }

    @Test
    void governanceApplicationHasServices() {
        assertPackageContainsServices("governance", "application");
    }

    @Test
    void governanceInfrastructureHasRequiredSubPackages() {
        assertInfrastructureHasSubPackages("governance");
    }

    @Test
    void governanceInterfacesHasRestAndDtos() {
        assertPackageExists("governance", "interfaces.rest");
        assertPackageExists("governance", "interfaces.rest.dto");
    }

    private void assertModuleHasPackages(String module) {
        assertPackageExists(module, "domain");
        assertPackageExists(module, "api");
        assertPackageExists(module, "application");
        assertPackageExists(module, "infrastructure");
        assertPackageExists(module, "interfaces");
        assertPackageExists(module, "config");
    }

    private void assertPackageExists(String module, String pkg) {
        assertFalse(classes.that().resideInAPackage(".." + module + "." + pkg + "..").isEmpty(),
                module + "/" + pkg + " should have classes");
    }

    private void assertPackageContainsTypes(String module, String pkg, Set<String> kinds) {
        var classNames = classes.that().resideInAPackage(".." + module + "." + pkg + "..")
                .stream().map(c -> c.getSimpleName()).toList();
        assertFalse(classNames.isEmpty(), module + "/" + pkg + " should exist");
        boolean hasEnumOrRecord = classNames.stream().anyMatch(n -> n.endsWith("Record") || n.endsWith("Enum")
                || n.contains("Status") || n.contains("Type") || n.contains("Mode"));
        assertTrue(hasEnumOrRecord, module + "/" + pkg + " should have enums or records");
    }

    private void assertPackageContainsInterfaces(String module, String pkg) {
        var interfaces = classes.that().resideInAPackage(".." + module + "." + pkg + "..")
                .stream().filter(c -> c.isInterface()).toList();
        assertFalse(interfaces.isEmpty(), module + "/" + pkg + " should have interfaces");
    }

    private void assertPackageContainsServices(String module, String pkg) {
        var services = classes.that().resideInAPackage(".." + module + "." + pkg + "..")
                .stream().filter(c -> !c.isInterface()).toList();
        assertFalse(services.isEmpty(), module + "/" + pkg + " should have service implementations");
    }

    private void assertInfrastructureHasSubPackages(String module) {
        assertPackageExists(module, "infrastructure.persistence");
        assertPackageExists(module, "infrastructure.redis");
        assertPackageExists(module, "infrastructure.kafka");
        assertPackageExists(module, "infrastructure.monitoring");
        assertPackageExists(module, "infrastructure.security");
    }
}
