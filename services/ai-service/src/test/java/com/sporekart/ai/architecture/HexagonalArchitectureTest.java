package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void domainLayerDoesNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void domainLayerDoesNotDependOnSpring() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta..");
        rule.check(classes);
    }

    @Test
    void applicationLayerDoesNotDependOnInterfaces() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");
        rule.check(classes);
    }

    @Test
    void infrastructureDependsOnApiPorts() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");
        rule.check(classes);
    }

    @Test
    void infrastructureMayDependOnApi() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..api..", "..domain..",
                        "org.springframework..", "..config..",
                        "com.fasterxml.jackson..", "lombok..", "org.slf4j..",
                        "jakarta..");
        rule.check(classes);
    }

    @Test
    void interfacesDependsOnApplication() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages("java..", "..api..", "..application..",
                        "..dto..", "..domain..", "org.springframework..",
                        "lombok..", "..infrastructure..");
        rule.check(classes);
    }

    @Test
    void noLayerSkipInterfacesToDomainDirectly() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces..")
                .should().dependOnClassesThat()
                .resideInAPackage("..domain..");
        rule.check(classes);
    }

    @Test
    void noLayerSkipApplicationToInfrastructureDirectly() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");
        rule.check(classes);
    }
}
