package com.sporekart.ai.compliance.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceFrameworkTest {

    @Test
    void testRecordConstruction() {
        UUID id = UUID.randomUUID();
        String name = "Test Framework";
        String version = "1.0.0";
        ComplianceFrameworkType type = ComplianceFrameworkType.ISO_27001;
        String description = "Test description";
        String authority = "Test Authority";
        ComplianceControl control = new ComplianceControl(
            UUID.randomUUID(), "CTRL-001", "Access Control", "Control access",
            ControlType.PREVENTIVE, Map.of("key", "value"), true, Instant.now()
        );
        List<ComplianceControl> controls = List.of(control);
        boolean active = true;

        ComplianceFramework framework = new ComplianceFramework(
            id, name, version, type, description, authority, controls, active
        );

        assertEquals(id, framework.id());
        assertEquals(name, framework.name());
        assertEquals(version, framework.version());
        assertEquals(type, framework.type());
        assertEquals(description, framework.description());
        assertEquals(authority, framework.authority());
        assertEquals(controls, framework.controls());
        assertTrue(framework.active());
    }

    @Test
    void testRecordEquality() {
        UUID id = UUID.randomUUID();
        ComplianceFramework f1 = new ComplianceFramework(id, "FW", "1.0", ComplianceFrameworkType.GDPR,
            "desc", "auth", List.of(), true);
        ComplianceFramework f2 = new ComplianceFramework(id, "FW", "1.0", ComplianceFrameworkType.GDPR,
            "desc", "auth", List.of(), true);

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
