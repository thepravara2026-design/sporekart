package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.FeatureFlagService;
import com.sporekart.ai.application.service.ProcurementService;
import com.sporekart.ai.application.service.SupplierService;
import com.sporekart.ai.application.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/operations")
@Tag(name = "Operations", description = "Operations dashboard and platform management endpoints")
public class OperationsController {
    private final WarehouseService warehouseService;
    private final ProcurementService procurementService;
    private final SupplierService supplierService;
    private final FeatureFlagService featureFlagService;

    public OperationsController(WarehouseService warehouseService,
            ProcurementService procurementService,
            SupplierService supplierService,
            FeatureFlagService featureFlagService) {
        this.warehouseService = warehouseService;
        this.procurementService = procurementService;
        this.supplierService = supplierService;
        this.featureFlagService = featureFlagService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get operations dashboard summary")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard summary"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("pendingPurchaseOrders", procurementService.getPurchaseOrders("SUBMITTED").size());
        dashboard.put("lowInventory", warehouseService.getWarehouses().size());
        dashboard.put("pendingReceipts", procurementService.getPurchaseOrders("RECEIVED").size());
        dashboard.put("warehouseCapacity", warehouseService.getWarehouses().size());
        dashboard.put("supplierPerformance", supplierService.getSuppliers(null).size());
        dashboard.put("operationalAlerts", 0);
        dashboard.put("inventoryAccuracy", 98.5);
        dashboard.put("featureFlags", featureFlagService.getFlags());
        return dashboard;
    }
}
