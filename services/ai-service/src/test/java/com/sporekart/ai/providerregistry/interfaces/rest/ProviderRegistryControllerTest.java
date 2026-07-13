package com.sporekart.ai.providerregistry.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.providerregistry.api.ProviderDiscoveryService;
import com.sporekart.ai.providerregistry.api.ProviderHealthService;
import com.sporekart.ai.providerregistry.api.ProviderRegistryService;
import com.sporekart.ai.providerregistry.domain.ProviderRegistryEntry;
import com.sporekart.ai.providerregistry.domain.ProviderType;
import com.sporekart.ai.providerregistry.interfaces.rest.dto.ProviderRegistryRequestDto;

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

@WebMvcTest(ProviderRegistryController.class)
class ProviderRegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProviderRegistryService registryService;

    @MockitoBean
    private ProviderHealthService healthService;

    @MockitoBean
    private ProviderDiscoveryService discoveryService;

    @Test
    void listReturns200() throws Exception {
        ProviderRegistryEntry entry = new ProviderRegistryEntry();
        entry.setProviderName("OpenAI");
        entry.setProviderType(ProviderType.CLOUD);
        when(registryService.listProviders()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/provider-registry/list"))
                .andExpect(status().isOk());
    }

    @Test
    void registerReturns201() throws Exception {
        ProviderRegistryEntry entry = new ProviderRegistryEntry();
        entry.setProviderName("OpenAI");
        entry.setProviderType(ProviderType.CLOUD);
        when(registryService.registerProvider(any())).thenReturn(entry);

        ProviderRegistryRequestDto request = new ProviderRegistryRequestDto();
        request.setProviderName("OpenAI");
        request.setProviderType(ProviderType.CLOUD);

        mockMvc.perform(post("/api/v1/provider-registry/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
