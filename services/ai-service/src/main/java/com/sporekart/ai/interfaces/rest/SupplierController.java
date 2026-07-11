package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.SupplierService;
import com.sporekart.ai.domain.model.Supplier;
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
@RequestMapping("/suppliers")
@Tag(name = "Supplier", description = "Supplier management endpoints")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @Operation(summary = "Register a new supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> registerSupplier(@RequestBody Map<String, String> payload) {
        Supplier supplier = supplierService.registerSupplier(
                payload.get("code"),
                payload.get("name"),
                payload.get("type"),
                payload.get("gstin"),
                payload.get("email"));
        return Map.of("id", supplier.getId(), "code", supplier.getSupplierCode(), "status", supplier.getStatus());
    }

    @PostMapping("/{supplierId}/approve")
    @Operation(summary = "Approve a supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier approved"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> approveSupplier(
            @Parameter(description = "Supplier ID") @PathVariable UUID supplierId,
            @RequestParam UUID userId) {
        supplierService.approveSupplier(supplierId, userId);
        return Map.of("success", true, "message", "Supplier approved");
    }

    @PostMapping("/{supplierId}/blacklist")
    @Operation(summary = "Blacklist a supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier blacklisted"),
        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> blacklistSupplier(
            @Parameter(description = "Supplier ID") @PathVariable UUID supplierId) {
        supplierService.blacklistSupplier(supplierId);
        return Map.of("success", true, "message", "Supplier blacklisted");
    }

    @PostMapping("/{supplierId}/rating")
    @Operation(summary = "Update supplier rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating updated"),
        @ApiResponse(responseCode = "400", description = "Invalid rating", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> updateRating(
            @Parameter(description = "Supplier ID") @PathVariable UUID supplierId,
            @RequestParam BigDecimal rating) {
        supplierService.updateRating(supplierId, rating);
        return Map.of("success", true, "message", "Rating updated");
    }

    @GetMapping
    @Operation(summary = "List suppliers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of suppliers"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getSuppliers(@RequestParam(required = false) String status) {
        return Map.of("suppliers", supplierService.getSuppliers(status));
    }

    @GetMapping("/{supplierId}")
    @Operation(summary = "Get supplier details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier details"),
        @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getSupplier(
            @Parameter(description = "Supplier ID") @PathVariable UUID supplierId) {
        Supplier supplier = supplierService.getSupplier(supplierId);
        return Map.of("name", supplier.getSupplierName(), "status", supplier.getStatus(), "rating",
                supplier.getRating());
    }
}
