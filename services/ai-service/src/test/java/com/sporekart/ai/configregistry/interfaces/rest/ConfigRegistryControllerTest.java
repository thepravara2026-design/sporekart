package com.sporekart.ai.configregistry.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.configregistry.api.ConfigRegistryService;
import com.sporekart.ai.configregistry.api.ConfigSnapshotService;
import com.sporekart.ai.configregistry.domain.ConfigSnapshot;
import com.sporekart.ai.configregistry.interfaces.rest.dto.ConfigSnapshotRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConfigRegistryController.class)
class ConfigRegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConfigRegistryService configRegistryService;

    @MockitoBean
    private ConfigSnapshotService snapshotService;

    @Test
    void createSnapshotReturns201() throws Exception {
        ConfigSnapshot snapshot = new ConfigSnapshot();
        snapshot.setSnapshotId("s1");
        snapshot.setName("snap1");
        when(snapshotService.createSnapshot(anyString(), anyString(), anyString())).thenReturn(snapshot);

        ConfigSnapshotRequestDto request = new ConfigSnapshotRequestDto();
        request.setName("snap1");
        request.setDescription("desc");
        request.setCreatedBy("me");

        mockMvc.perform(post("/api/v1/config-registry/snapshot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void listSnapshotsReturns200() throws Exception {
        ConfigSnapshot snapshot = new ConfigSnapshot();
        snapshot.setSnapshotId("s1");
        snapshot.setName("snap1");
        when(snapshotService.listSnapshots()).thenReturn(List.of(snapshot));

        mockMvc.perform(get("/api/v1/config-registry/snapshots"))
                .andExpect(status().isOk());
    }
}
