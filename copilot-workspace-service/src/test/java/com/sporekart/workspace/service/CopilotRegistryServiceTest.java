package com.sporekart.workspace.service;

import com.sporekart.workspace.domain.CopilotInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CopilotRegistryServiceTest {

    private CopilotRegistryService registryService;

    @BeforeEach
    void setUp() {
        registryService = new CopilotRegistryService();
    }

    @Test
    void register_ShouldRegisterCopilot() {
        CopilotInfo copilot = new CopilotInfo("copilot-1", "Customer Support", CopilotInfo.TYPE_CUSTOMER, "1.0",
                "http://localhost:8081", true, CopilotInfo.STATUS_ACTIVE, List.of("chat", "orders"),
                List.of("customer"), null, 0, 0, null);
        registryService.register(copilot);

        CopilotInfo found = registryService.getCopilot("copilot-1");
        assertNotNull(found);
        assertEquals("Customer Support", found.name());
    }

    @Test
    void register_ShouldUpdateExistingCopilot() {
        CopilotInfo copilot = new CopilotInfo("copilot-1", "Old Name", CopilotInfo.TYPE_CUSTOMER, "1.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null);
        registryService.register(copilot);

        CopilotInfo updated = new CopilotInfo("copilot-1", "New Name", CopilotInfo.TYPE_CUSTOMER, "2.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null);
        registryService.register(updated);

        assertEquals("New Name", registryService.getCopilot("copilot-1").name());
    }

    @Test
    void unregister_ShouldRemoveCopilot() {
        CopilotInfo copilot = new CopilotInfo("copilot-2", "Admin", CopilotInfo.TYPE_ADMIN, "1.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null);
        registryService.register(copilot);
        registryService.unregister("copilot-2");

        assertNull(registryService.getCopilot("copilot-2"));
    }

    @Test
    void getCopilot_ShouldReturnNull_WhenNotFound() {
        assertNull(registryService.getCopilot("nonexistent"));
    }

    @Test
    void getAllCopilots_ShouldReturnAllRegistered() {
        registryService.register(new CopilotInfo("c1", "C1", CopilotInfo.TYPE_CUSTOMER, "1.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null));
        registryService.register(new CopilotInfo("c2", "C2", CopilotInfo.TYPE_ADMIN, "1.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null));

        List<CopilotInfo> all = registryService.getAllCopilots();
        assertThat(all).hasSize(2);
    }

    @Test
    void getEnabledCopilots_ShouldReturnOnlyEnabled() {
        registryService.register(new CopilotInfo("c1", "Enabled", CopilotInfo.TYPE_CUSTOMER, "1.0",
                null, true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null));
        registryService.register(new CopilotInfo("c2", "Disabled", CopilotInfo.TYPE_ADMIN, "1.0",
                null, false, CopilotInfo.STATUS_INACTIVE, List.of(), List.of(), null, 0, 0, null));

        List<CopilotInfo> enabled = registryService.getEnabledCopilots();
        assertThat(enabled).hasSize(1);
        assertThat(enabled.get(0).copilotId()).isEqualTo("c1");
    }

    @Test
    void discoverCopilotsFromConfig_ShouldLoadFromConfiguration() {
        List<CopilotInfo> discovered = registryService.discoverCopilotsFromConfig();
        assertNotNull(discovered);
    }

    @Test
    void checkAllCopilotHealth_ShouldReturnHealthResults() {
        registryService.register(new CopilotInfo("c1", "Healthy", CopilotInfo.TYPE_CUSTOMER, "1.0",
                "http://localhost:8081", true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null));

        List<CopilotInfo> results = registryService.checkAllCopilotHealth();
        assertThat(results).isNotNull();
    }
}
