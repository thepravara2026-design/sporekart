package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class DependencyRuleTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void domainClassesOnlyDependOnJavaAndDomain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..domain..");
        rule.check(classes);
    }

    @Test
    void apiClassesOnlyDependOnDomain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..api..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..domain..");
        rule.check(classes);
    }

    @Test
    void applicationClassesOnlyDependOnApiDomainAndInfrastructurePersistence() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..api..", "..domain..",
                        "..infrastructure.persistence..", "..config..", "..engine..");
        rule.check(classes);
    }

    @Test
    void infrastructureClassesMayDependOnApiDomainAndSpring() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..api..", "..domain..",
                        "org.springframework..", "..config..",
                        "com.fasterxml.jackson..", "lombok..", "org.slf4j..");
        rule.check(classes);
    }

    @Test
    void interfacesClassesOnlyDependOnApiApplicationAndDto() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..api..", "..application..",
                        "..dto..", "..domain..", "org.springframework..",
                        "lombok..", "..infrastructure..");
        rule.check(classes);
    }

    @Test
    void noCyclicDependenciesBetweenPackages() {
        ArchRule rule = slices()
                .matching("com.sporekart.ai.(*)..")
                .should().beFreeOfCycles();
        rule.check(classes);
    }

    @Test
    void governanceModulesDoNotDependOnEachOthersInternalClasses() {
        String[] modules = {"governance", "policy", "decision", "approval",
                "compliance", "risk", "analytics", "admin", "automation"};
        for (String module : modules) {
            for (String other : modules) {
                if (!module.equals(other)) {
                    ArchRule rule = noClasses()
                            .that().resideInAPackage(".." + module + "..")
                            .should().dependOnClassesThat()
                            .resideInAPackage(".." + other + "..");
                    rule.check(classes);
                }
            }
        }
    }
}
