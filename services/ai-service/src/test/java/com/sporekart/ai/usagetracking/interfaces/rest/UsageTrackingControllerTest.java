package com.sporekart.ai.usagetracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.usagetracking.api.UsageAggregationService;
import com.sporekart.ai.usagetracking.api.UsageDashboardService;
import com.sporekart.ai.usagetracking.api.UsageTrackingService;
import com.sporekart.ai.usagetracking.domain.UsageRecord;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.UsageRecordRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsageTrackingController.class)
class UsageTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsageTrackingService usageTrackingService;

    @MockitoBean
    private UsageAggregationService usageAggregationService;

    @MockitoBean
    private UsageDashboardService usageDashboardService;

    @Test
    void recordReturns201() throws Exception {
        UsageRecord record = new UsageRecord();
        record.setProviderId("p1");
        record.setModelId("m1");
        when(usageTrackingService.recordUsage(any())).thenReturn(record);

        UsageRecordRequestDto request = new UsageRecordRequestDto();
        request.setProviderId("p1");
        request.setModelId("m1");
        request.setPromptTokens(10);
        request.setCompletionTokens(5);

        mockMvc.perform(post("/api/v1/usage-tracking/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void listReturns200() throws Exception {
        UsageRecord record = new UsageRecord();
        record.setProviderId("p1");
        record.setModelId("m1");
        when(usageTrackingService.listUsage(anyInt())).thenReturn(List.of(record));

        mockMvc.perform(get("/api/v1/usage-tracking/list"))
                .andExpect(status().isOk());
    }
}
