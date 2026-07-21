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
    void verifyProviderPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/adapter")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/client")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/model")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/capability")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/catalog")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/discovery")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/lifecycle")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/activation")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/metadata")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/capability")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/monitoring")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/health")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/validator")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/selector")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/cache")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/persistence")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/audit")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/synchronization")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/registry/versioning")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/factory")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/selector")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/selector/strategies")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/lifecycle")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/validator")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/discovery")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/metadata")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/monitoring")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/exception")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/contracts")));
        assertTrue(Files.exists(Paths.get("src/main/java/com/sporekart/ai/providers/config")));
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
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/providers")));
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
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/provider-architecture.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/provider-lifecycle.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/provider-selection.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/provider-contracts.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/README.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-architecture.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-lifecycle.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-catalog.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-discovery.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-activation.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-versioning.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-capability.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-cache.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-monitoring.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-health.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-audit.md")));
        assertTrue(Files.exists(Paths.get("docs/phase-13/providers/registry/registry-validator.md")));
    }
}
