package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class RegistryModulesArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    private static final String[] MODULES = {
            "providerregistry",
            "promptregistry",
            "knowledgeregistry",
            "usagetracking",
            "configregistry",
            "eventcatalog",
            "apiregistry",
            "capabilitydiscovery"
    };

    @Test
    void restControllersAreAnnotatedWithRestController() {
        for (String module : MODULES) {
            ArchRule rule = classes()
                    .that().resideInAPackage(".." + module + ".interfaces.rest..")
                    .and().haveSimpleNameEndingWith("Controller")
                    .should().beAnnotatedWith(RestController.class);
            rule.check(classes);
        }
    }

    @Test
    void servicesResideInApplicationLayer() {
        for (String module : MODULES) {
            ArchRule rule = classes()
                    .that().areAnnotatedWith(Service.class)
                    .and().resideInAPackage(".." + module + "..")
                    .should().resideInAPackage(".." + module + ".application..");
            rule.check(classes);
        }
    }

    @Test
    void restLayerDoesNotDependOnPersistence() {
        for (String module : MODULES) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage(".." + module + ".interfaces.rest..")
                    .should().dependOnClassesThat()
                    .resideInAPackage(".." + module + ".infrastructure.persistence..");
            rule.check(classes);
        }
    }

    @Test
    void persistenceLayerDoesNotDependOnRest() {
        for (String module : MODULES) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage(".." + module + ".infrastructure.persistence..")
                    .should().dependOnClassesThat()
                    .resideInAPackage(".." + module + ".interfaces.rest..");
            rule.check(classes);
        }
    }
}
