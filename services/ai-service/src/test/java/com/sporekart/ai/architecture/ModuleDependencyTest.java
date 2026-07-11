package com.sporekart.ai.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ModuleDependencyTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void coreShouldNotDependOnAnyOtherModule() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..gateway..", "..provider..", "..prompt..", "..rag..",
                        "..search..", "..chat..", "..content..", "..workflow..", "..monitoring..");
        rule.check(classes);
    }

    @Test
    void coreShouldNotDependOnGateway() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat()
                .resideInAPackage("..gateway..");
        rule.check(classes);
    }

    @Test
    void modulesShouldNotDependOnEachOther() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..gateway..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..provider..", "..prompt..", "..rag..", "..search..",
                        "..chat..", "..content..", "..workflow..", "..monitoring..");
        rule.check(classes);
    }

    @Test
    void modulesShouldOnlyDependOnCore() {
        String[] modules = {"gateway", "provider", "prompt", "rag", "search", "chat", "content", "workflow", "monitoring"};
        for (String module : modules) {
            ArchRule rule = noClasses()
                    .that().resideInAPackage(".." + module + "..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..application.service..", "..infrastructure..", "..interfaces..");
            rule.check(classes);
        }
    }

    @Test
    void gatewayShouldNotDependOnApplicationServiceInfrastructureOrInterfacesFromOtherModules() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..gateway..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application.service..", "..infrastructure..", "..interfaces..");
        rule.check(classes);
    }

    @Test
    void gatewayShouldOnlyDependOnCoreApiCoreApplicationAndCoreDomain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..gateway..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "..provider..", "..prompt..", "..rag..", "..search..",
                        "..chat..", "..content..", "..workflow..", "..monitoring..",
                        "..core.config..",
                        "..application.service..", "..infrastructure..", "..interfaces..");
        rule.check(classes);
    }

    @Test
    void noCyclicDependencies() {
        ArchRule rule = slices()
                .matching("com.sporekart.ai.(*)..")
                .should().beFreeOfCycles();
        rule.check(classes);
    }

    @Test
    void domainShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infrastructure..", "..springframework..", "..javax..", "..jakarta..");
        rule.check(classes);
    }
}
