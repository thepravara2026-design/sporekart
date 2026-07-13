package com.sporekart.ai.apiregistry.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.apiregistry.api.ApiDiscoveryService;
import com.sporekart.ai.apiregistry.api.ApiHealthService;
import com.sporekart.ai.apiregistry.api.ApiRegistryService;
import com.sporekart.ai.apiregistry.domain.ApiRegistryEntry;
import com.sporekart.ai.apiregistry.domain.HttpMethod;
import com.sporekart.ai.apiregistry.interfaces.rest.dto.ApiRegistryRequestDto;

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

@WebMvcTest(ApiRegistryController.class)
class ApiRegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApiRegistryService registryService;

    @MockitoBean
    private ApiHealthService healthService;

    @MockitoBean
    private ApiDiscoveryService discoveryService;

    @Test
    void registerReturns200() throws Exception {
        ApiRegistryEntry entry = new ApiRegistryEntry();
        entry.setApiId("a1");
        entry.setApiName("GetOrder");
        when(registryService.registerApi(any())).thenReturn(entry);

        ApiRegistryRequestDto request = new ApiRegistryRequestDto();
        request.setApiName("GetOrder");
        request.setApiPath("/orders");
        request.setHttpMethod(HttpMethod.GET);
        request.setModule("order");

        mockMvc.perform(post("/api/v1/api-registry/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void listReturns200() throws Exception {
        ApiRegistryEntry entry = new ApiRegistryEntry();
        entry.setApiId("a1");
        entry.setApiName("GetOrder");
        when(registryService.listApis()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/api-registry/list"))
                .andExpect(status().isOk());
    }
}
