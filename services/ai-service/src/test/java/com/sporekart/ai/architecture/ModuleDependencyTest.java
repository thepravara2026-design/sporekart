package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ModuleDependencyTest {

    @Test
    void verifyMemoryModuleDoesNotDependOnOtherModules() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.memory..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.sporekart.ai.runtime..",
                "com.sporekart.ai.assistant..",
                "com.sporekart.ai.governance..");
        rule.check(classes);
    }

    @Test
    void verifyRuntimeModuleDoesNotDependOnOtherModules() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.runtime..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.sporekart.ai.assistant..",
                "com.sporekart.ai.governance..",
                "com.sporekart.ai.analytics..");
        rule.check(classes);
    }

    @Test
    void verifySharedModuleHasNoDependenciesOnApplicationModules() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sporekart.ai");
        ArchRule rule = noClasses()
            .that().resideInAnyPackage("com.sporekart.ai.shared..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.sporekart.ai.memory..",
                "com.sporekart.ai.runtime..",
                "com.sporekart.ai.gateway..",
                "com.sporekart.ai.provider..",
                "com.sporekart.ai.prompt..");
        rule.check(classes);
    }
}
