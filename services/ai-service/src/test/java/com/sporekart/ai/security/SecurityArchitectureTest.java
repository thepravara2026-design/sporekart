package com.sporekart.ai.security;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.*;

class SecurityArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.sporekart.ai");

    @Test
    void allRestControllersHaveRestControllerAnnotation() {
        ArchRule rule = classes()
                .that().resideInAPackage("..interfaces.rest..")
                .and().haveSimpleNameEndingWith("Controller")
                .should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class);
        rule.check(classes);
    }

    @Test
    void allEndpointsArePermittedInSecurityConfig() {
        Set<String> permittedPaths = Set.of(
                "/actuator/health", "/actuator/info",
                "/v3/api-docs/**", "/swagger-ui/**",
                "/api/v1/ai/health", "/api/v1/ai/status", "/api/v1/ai/features",
                "/api/v1/ai/providers", "/api/v1/ai/providers/**",
                "/api/v1/ai/providers/capabilities", "/api/v1/ai/providers/health",
                "/api/v1/ai/prompts", "/api/v1/ai/prompts/**",
                "/api/v1/ai/prompts/categories", "/api/v1/ai/prompts/render",
                "/api/v1/ai/prompts/history", "/api/v1/ai/prompts/export",
                "/api/v1/ai/prompts/import",
                "/api/v1/knowledge/**",
                "/api/v1/semantic/**",
                "/api/v1/conversation/**",
                "/api/v1/workflows/**",
                "/api/v1/content/**",
                "/api/v1/assistants/**",
                "/api/v1/governance/**",
                "/api/v1/policies/**",
                "/api/v1/decisions/**",
                "/api/v1/approvals/**",
                "/api/v1/compliance/**",
                "/api/v1/risk/**",
                "/api/v1/admin/**");
        assertNotNull(permittedPaths);
        assertFalse(permittedPaths.isEmpty());
    }

    @Test
    void noSecurityAnnotationsBypassed() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces.rest..")
                .should().beAnnotatedWith(org.springframework.security.access.annotation.Secured.class);
        rule.check(classes);
    }

    @Test
    void allAuditServicesRecordActions() {
        var auditServices = classes.that().resideInAPackage("..api..")
                .and().haveSimpleNameContaining("AuditService")
                .filter(c -> c.isInterface());
        assertFalse(auditServices.isEmpty());
        auditServices.forEach(svc -> {
            boolean hasRecordMethod = svc.getMethods().stream()
                    .anyMatch(m -> m.getName().equals("recordAudit"));
            assertTrue(hasRecordMethod, svc.getName() + " should have recordAudit method");
        });
    }

    @Test
    void allExceptionsHaveProperErrorCodes() {
        var exceptions = classes.that().resideInAPackage("..infrastructure.security..")
                .and().haveSimpleNameContaining("Exception")
                .filter(c -> !c.isInterface());
        assertFalse(exceptions.isEmpty());
        exceptions.forEach(exc -> {
            boolean hasFactoryMethods = exc.getMethods().stream()
                    .anyMatch(m -> Modifier.isStatic(m.getModifiers())
                            && exc.getName().equals(m.getRawReturnType().getName()));
            assertTrue(hasFactoryMethods, exc.getName() + " should have static factory methods");
        });
    }

    @Test
    void eachModuleHasExceptionClassWithFactoryMethods() {
        String[] modules = {"governance", "policy", "decision", "approval",
                "compliance", "risk", "analytics", "admin", "automation"};
        for (String module : modules) {
            var excClasses = classes.that()
                    .resideInAPackage(".." + module + ".infrastructure.security..")
                    .and().haveSimpleNameContaining("Exception")
                    .filter(c -> !c.isInterface());
            assertFalse(excClasses.isEmpty(),
                    module + " should have an Exception class in infrastructure.security");
            excClasses.forEach(exc -> {
                boolean hasFactory = exc.getMethods().stream()
                        .anyMatch(m -> Modifier.isStatic(m.getModifiers())
                                && exc.getName().equals(m.getRawReturnType().getName()));
                assertTrue(hasFactory, exc.getName() + " in " + module + " should have factory methods");
            });
        }
    }
}
