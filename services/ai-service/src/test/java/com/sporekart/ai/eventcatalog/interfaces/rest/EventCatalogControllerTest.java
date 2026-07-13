package com.sporekart.ai.eventcatalog.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.eventcatalog.api.EventCatalogService;
import com.sporekart.ai.eventcatalog.api.EventSubscriptionService;
import com.sporekart.ai.eventcatalog.api.EventVisualizationService;
import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventRetryStrategy;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventCatalogRequestDto;

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

@WebMvcTest(EventCatalogController.class)
class EventCatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventCatalogService catalogService;

    @MockitoBean
    private EventSubscriptionService subscriptionService;

    @MockitoBean
    private EventVisualizationService visualizationService;

    @Test
    void registerReturns200() throws Exception {
        EventCatalogEntry entry = new EventCatalogEntry();
        entry.setEventId("e1");
        entry.setEventName("OrderCreated");
        when(catalogService.registerEvent(any())).thenReturn(entry);

        EventCatalogRequestDto request = new EventCatalogRequestDto();
        request.setEventId("e1");
        request.setEventName("OrderCreated");
        request.setModule("order");
        request.setProducer("order-service");
        request.setRetryStrategy(EventRetryStrategy.LINEAR);

        mockMvc.perform(post("/api/v1/event-catalog/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void listEventsReturns200() throws Exception {
        EventCatalogEntry entry = new EventCatalogEntry();
        entry.setEventId("e1");
        entry.setEventName("OrderCreated");
        when(catalogService.listEvents()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/event-catalog/list"))
                .andExpect(status().isOk());
    }
}
