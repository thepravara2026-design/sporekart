package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

class ArchitectureTest {

    @Test
    void verifyNoCircularDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = noClasses()
            .should().dependOnEachOther();
        rule.check(classes);
    }

    @Test
    void verifyNoFieldInjection() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = fields()
            .that().areDeclaredInClassesThat()
            .resideInAnyPackage("com.sporekart.ai..")
            .should().notBeAnnotatedWith("org.springframework.beans.factory.annotation.Autowired");
        rule.check(classes);
    }

    @Test
    void verifyDomainLayerHasNoFrameworkDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai..domain");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void verifyModuleStructure() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("..interfaces..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void verifyConfigurationDomainHasNoSpringDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.configuration.domain");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.configuration.domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void verifyConfigurationApiHasNoInfrastructureDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.configuration");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("..configuration.api..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void verifyConfigurationModelHasNoSpringDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.configuration.model");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.configuration.model..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void verifyConfigurationBinderHasNoInfrastructureDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.configuration");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("..configuration.binder..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void verifyGatewayDomainHasNoFrameworkDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.gateway.domain");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.gateway.domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }

    @Test
    void verifyGatewayContractHasNoInfrastructureDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.gateway.contract");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("..gateway.contract..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void verifyGatewayExceptionHasNoSpringDependencies() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai.gateway.exception");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.gateway.exception..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..");
        rule.check(classes);
    }
}
