package com.sporekart.copilot.service;

import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.dto.RegisterCopilotRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CopilotRegistryServiceTest {

    @Mock
    private CapabilityRegistry capabilityRegistry;

    private CopilotRegistryService service;

    @BeforeEach
    void setUp() {
        service = new CopilotRegistryService(capabilityRegistry);
    }

    @Test
    void registerCopilotShouldCreateRegistration() {
        when(capabilityRegistry.findById("cap1")).thenReturn(
            new Capability("cap1", "Cap1", "desc", new CopilotType[]{CopilotType.CUSTOMER}, null, null));

        var request = new RegisterCopilotRequest("TestCopilot", "CUSTOMER", "1.0.0", "desc", null, List.of("cap1"));
        var registration = service.registerCopilot(request);

        assertNotNull(registration.id());
        assertEquals("TestCopilot", registration.name());
        assertEquals(CopilotType.CUSTOMER, registration.type());
        assertEquals(CopilotStatus.ACTIVE, registration.status());
        assertEquals(1, registration.capabilityIds().size());
    }

    @Test
    void registerCopilotShouldThrowOnInvalidType() {
        var request = new RegisterCopilotRequest("BadCopilot", "INVALID_TYPE", "1.0.0", "desc", null, List.of());

        assertThrows(IllegalArgumentException.class, () -> service.registerCopilot(request));
    }

    @Test
    void enableCopilotShouldActivateCopilot() {
        when(capabilityRegistry.findById("cap1")).thenReturn(
            new Capability("cap1", "Cap1", "desc", new CopilotType[]{CopilotType.CUSTOMER}, null, null));

        var request = new RegisterCopilotRequest("Test", "CUSTOMER", "1.0.0", "desc", null, List.of("cap1"));
        var registration = service.registerCopilot(request);

        service.disableCopilot(registration.id());
        assertEquals(CopilotStatus.INACTIVE, service.getCopilot(registration.id()).status());

        service.enableCopilot(registration.id());
        assertEquals(CopilotStatus.ACTIVE, service.getCopilot(registration.id()).status());
    }

    @Test
    void disableCopilotShouldDeactivateCopilot() {
        when(capabilityRegistry.findById("cap1")).thenReturn(
            new Capability("cap1", "Cap1", "desc", new CopilotType[]{CopilotType.CUSTOMER}, null, null));

        var request = new RegisterCopilotRequest("Test", "CUSTOMER", "1.0.0", "desc", null, List.of("cap1"));
        var registration = service.registerCopilot(request);

        service.disableCopilot(registration.id());
        assertEquals(CopilotStatus.INACTIVE, service.getCopilot(registration.id()).status());
    }

    @Test
    void listCopilotsShouldReturnAllRegistrations() {
        when(capabilityRegistry.findById("cap1")).thenReturn(
            new Capability("cap1", "Cap1", "desc", new CopilotType[]{CopilotType.CUSTOMER}, null, null));

        service.registerCopilot(new RegisterCopilotRequest("C1", "CUSTOMER", "1.0.0", null, null, List.of("cap1")));
        service.registerCopilot(new RegisterCopilotRequest("C2", "CUSTOMER", "1.0.0", null, null, List.of("cap1")));

        assertEquals(2, service.listCopilots().size());
    }

    @Test
    void getCopilotShouldThrowOnMissing() {
        assertThrows(java.util.NoSuchElementException.class, () -> service.getCopilot("nonexistent"));
    }
}
