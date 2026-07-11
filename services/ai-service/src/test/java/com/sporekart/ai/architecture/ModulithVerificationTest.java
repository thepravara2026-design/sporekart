package com.sporekart.ai.architecture;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithVerificationTest {

    private final ApplicationModules modules = ApplicationModules.of(com.sporekart.ai.AiServiceApplication.class);

    @Test
    void verifyModulithStructure() {
        modules.verify();
    }

    @Test
    void createModulithDocumentation() {
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }
}
