package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.WarehouseService;
import com.sporekart.ai.domain.model.Warehouse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/warehouses")
@Tag(name = "Warehouse", description = "Warehouse management endpoints")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @Operation(summary = "Create a new warehouse")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Warehouse created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> createWarehouse(@RequestBody Map<String, String> payload) {
        Warehouse warehouse = warehouseService.createWarehouse(
                payload.get("code"),
                payload.get("name"),
                payload.get("type"),
                new BigDecimal(payload.get("capacity")));
        return Map.of("id", warehouse.getId(), "code", warehouse.getWarehouseCode(), "type",
                warehouse.getWarehouseType());
    }

    @PostMapping("/{warehouseId}/stock")
    @Operation(summary = "Add stock to a warehouse")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock added"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "Warehouse not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> addStock(
            @Parameter(description = "Warehouse ID") @PathVariable UUID warehouseId,
            @RequestBody Map<String, String> payload) {
        warehouseService.addStock(
                warehouseId,
                UUID.fromString(payload.get("productId")),
                new BigDecimal(payload.get("quantity")),
                new BigDecimal(payload.getOrDefault("reorderLevel", "10")));
        return Map.of("success", true, "message", "Stock added");
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer stock between warehouses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock transferred"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "Warehouse not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> transferStock(@RequestBody Map<String, String> payload) {
        warehouseService.transferStock(
                UUID.fromString(payload.get("fromWarehouseId")),
                UUID.fromString(payload.get("toWarehouseId")),
                UUID.fromString(payload.get("productId")),
                new BigDecimal(payload.get("quantity")));
        return Map.of("success", true, "message", "Stock transferred");
    }

    @GetMapping("/{warehouseId}/stock")
    @Operation(summary = "Get stock levels for a warehouse")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock levels returned"),
        @ApiResponse(responseCode = "404", description = "Warehouse not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getStockLevel(
            @Parameter(description = "Warehouse ID") @PathVariable UUID warehouseId) {
        return warehouseService.getStockLevel(warehouseId);
    }

    @GetMapping
    @Operation(summary = "List all warehouses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of warehouses"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getWarehouses() {
        return Map.of("warehouses", warehouseService.getWarehouses());
    }
}
