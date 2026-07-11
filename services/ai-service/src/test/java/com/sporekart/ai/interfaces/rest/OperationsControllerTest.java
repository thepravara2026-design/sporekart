package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.FeatureFlagService;
import com.sporekart.ai.application.service.ProcurementService;
import com.sporekart.ai.application.service.SupplierService;
import com.sporekart.ai.application.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperationsController.class)
@AutoConfigureMockMvc(addFilters = false)
class OperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private ProcurementService procurementService;

    @MockBean
    private SupplierService supplierService;

    @MockBean
    private FeatureFlagService featureFlagService;

    @Test
    void shouldReturnOperationsDashboard() throws Exception {
        when(procurementService.getPurchaseOrders("SUBMITTED")).thenReturn(List.of());
        when(procurementService.getPurchaseOrders("RECEIVED")).thenReturn(List.of());
        when(warehouseService.getWarehouses()).thenReturn(List.of());
        when(supplierService.getSuppliers(null)).thenReturn(List.of());
        when(featureFlagService.getFlags()).thenReturn(java.util.Map.of("Warehouse Enabled", true));

        mockMvc.perform(get("/operations/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pendingPurchaseOrders").value(0))
                .andExpect(jsonPath("$.warehouseCapacity").value(0))
                .andExpect(jsonPath("$.inventoryAccuracy").value(98.5));
    }
}
