package com.sporekart.ai.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.test.ApplicationModuleTest;

@ApplicationModuleTest
class GatewayArchitectureTest {

    @Test
    void shouldBeCompliantWithModulith() {
        var modules = ApplicationModules.of(com.sporekart.ai.AiServiceApplication.class);
        modules.verify();
    }

    @Test
    void shouldContainGatewayModule() {
        var modules = ApplicationModules.of(com.sporekart.ai.AiServiceApplication.class);
        modules.stream()
            .filter(m -> m.getName().contains("gateway"))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Gateway module not found"));
    }
}
