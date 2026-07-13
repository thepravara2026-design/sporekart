package com.sporekart.ai.capabilitydiscovery.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.capabilitydiscovery.api.CapabilityDiscoveryService;
import com.sporekart.ai.capabilitydiscovery.api.CapabilityHealthService;
import com.sporekart.ai.capabilitydiscovery.api.CapabilityRegistryService;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityEntry;
import com.sporekart.ai.capabilitydiscovery.domain.CapabilityType;
import com.sporekart.ai.capabilitydiscovery.interfaces.rest.dto.CapabilityRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CapabilityDiscoveryController.class)
class CapabilityDiscoveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CapabilityRegistryService registryService;

    @MockitoBean
    private CapabilityDiscoveryService discoveryService;

    @MockitoBean
    private CapabilityHealthService healthService;

    @Test
    void registerReturns201() throws Exception {
        CapabilityEntry entry = new CapabilityEntry();
        entry.setCapabilityId("c1");
        entry.setCapabilityName("Vision");
        when(registryService.registerCapability(any())).thenReturn(entry);

        CapabilityRequestDto request = new CapabilityRequestDto();
        request.setCapabilityName("Vision");
        request.setCapabilityType(CapabilityType.AI_MODEL);
        request.setModule("vision");

        mockMvc.perform(post("/api/v1/capability-discovery/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void listReturns200() throws Exception {
        CapabilityEntry entry = new CapabilityEntry();
        entry.setCapabilityId("c1");
        entry.setCapabilityName("Vision");
        when(registryService.listCapabilities()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/capability-discovery/list"))
                .andExpect(status().isOk());
    }
}
