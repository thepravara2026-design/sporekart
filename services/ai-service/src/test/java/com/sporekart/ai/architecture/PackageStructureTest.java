package com.sporekart.ai.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageStructureTest {

    @Test
    void verifyMemoryPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/api")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/application")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/config")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/domain")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/infrastructure")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/memory/interfaces")));
    }

    @Test
    void verifyRuntimePackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/api")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/application")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/config")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/domain")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/infrastructure")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/runtime/interfaces")));
    }

    @Test
    void verifySharedPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/shared/constants")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/shared/enums")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/shared/interfaces")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/shared/util")));
    }

    @Test
    void verifyEventsPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/events")));
    }

    @Test
    void verifyConfigurationPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/api")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/domain")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/model/provider")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/model/feature")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/model/environment")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/model/secret")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/validation")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/config")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/configuration/binder")));
    }

    @Test
    void verifyGatewayPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/pipeline/stages")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/pipeline")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/router")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/router/strategies")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/contract/request")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/contract/response")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/contract/message")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/security/hooks")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/exception")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/health")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/observability")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/facade")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/config")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/gateway/domain")));
    }

    @Test
    void verifyTestPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/memory")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/runtime")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/shared")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/architecture")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/configuration")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/gateway")));
    }

    @Test
    void verifyConfigFiles() {
        assertTrue(Files.exists(Paths.get("src/main/resources/application.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-local.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-dev.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-test.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-prod.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-stage.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-docker.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-cloud.yml")));
    }

    @Test
    void verifyPhase13Docs() {
        assertTrue(Files.exists(Paths.get("docs/phase-13/configuration-platform.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/configuration-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/feature-flag-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/environment-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/provider-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/secret-management-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/deployment-configuration-guide.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/configuration-matrix.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/configuration-lifecycle.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-architecture.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-pipeline.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-routing.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-contracts.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-error-handling.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-security.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-observability.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-lifecycle.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/gateway-health.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/gateway/README.md")));
    }
}
