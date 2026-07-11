package com.sporekart.ai.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.application.service.ProcurementService;
import com.sporekart.ai.domain.model.PurchaseOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcurementController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProcurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProcurementService procurementService;

    @Test
    void shouldExposeProcurementAliasEndpoint() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder(UUID.randomUUID(), "PO-1001", UUID.randomUUID(),
                LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ONE);
        when(procurementService.getPurchaseOrders(null)).thenReturn(List.of(purchaseOrder));

        mockMvc.perform(get("/procurement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseOrders[0].poNumber").value("PO-1001"));
    }

    @Test
    void shouldCreateProcurementAliasRequest() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder(UUID.randomUUID(), "PO-2002", UUID.randomUUID(),
                LocalDate.now(),
                BigDecimal.TEN, BigDecimal.ONE);
        when(procurementService.createPurchaseOrder(any(UUID.class), any(LocalDate.class), any(BigDecimal.class),
                any(BigDecimal.class)))
                .thenReturn(purchaseOrder);

        Map<String, String> payload = Map.of(
                "supplierId", UUID.randomUUID().toString(),
                "poDate", LocalDate.now().toString(),
                "total", "10.00",
                "tax", "1.00");

        mockMvc.perform(post("/procurement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poNumber").value("PO-2002"));
    }
}
