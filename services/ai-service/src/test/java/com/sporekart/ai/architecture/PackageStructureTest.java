package com.sporekart.ai.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    void verifyTestPackageStructure() {
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/memory")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/runtime")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/shared")));
        assertTrue(Files.exists(Paths.get("src/test/java/com/sporekart/ai/architecture")));
    }

    @Test
    void verifyConfigFiles() {
        assertTrue(Files.exists(Paths.get("src/main/resources/application.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-local.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-dev.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-test.yml")));
        assertTrue(Files.exists(Paths.get("src/main/resources/application-prod.yml")));
    }
}
