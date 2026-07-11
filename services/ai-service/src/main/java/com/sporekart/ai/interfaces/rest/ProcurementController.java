package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.ProcurementService;
import com.sporekart.ai.domain.model.PurchaseOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping({ "/purchase-orders", "/procurement" })
@Tag(name = "Procurement", description = "Purchase order and procurement management endpoints")
public class ProcurementController {
    private final ProcurementService procurementService;

    public ProcurementController(ProcurementService procurementService) {
        this.procurementService = procurementService;
    }

    @PostMapping
    @Operation(summary = "Create a purchase order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchase order created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> createPurchaseOrder(@RequestBody Map<String, String> payload) {
        PurchaseOrder po = procurementService.createPurchaseOrder(
                UUID.fromString(payload.get("supplierId")),
                LocalDate.parse(payload.get("poDate")),
                new BigDecimal(payload.get("total")),
                new BigDecimal(payload.getOrDefault("tax", "0")));
        return Map.of(
                "id", po.getId(),
                "poNumber", po.getPoNumber(),
                "status", po.getStatus(),
                "grandTotal", po.getGrandTotal());
    }

    @PostMapping("/{poId}/submit")
    @Operation(summary = "Submit a purchase order for approval")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PO submitted"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> submitPurchaseOrder(
            @Parameter(description = "Purchase order ID") @PathVariable UUID poId) {
        procurementService.submitPurchaseOrder(poId);
        return Map.of("success", true, "message", "PO submitted");
    }

    @PostMapping("/{poId}/approve")
    @Operation(summary = "Approve a purchase order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PO approved"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> approvePurchaseOrder(
            @Parameter(description = "Purchase order ID") @PathVariable UUID poId,
            @RequestParam UUID userId) {
        procurementService.approvePurchaseOrder(poId, userId);
        return Map.of("success", true, "message", "PO approved");
    }

    @PostMapping("/{poId}/reject")
    @Operation(summary = "Reject a purchase order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PO rejected"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> rejectPurchaseOrder(
            @Parameter(description = "Purchase order ID") @PathVariable UUID poId) {
        procurementService.rejectPurchaseOrder(poId);
        return Map.of("success", true, "message", "PO rejected");
    }

    @PostMapping("/{poId}/mark-received")
    @Operation(summary = "Mark a purchase order as received")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PO marked as received"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> markReceived(
            @Parameter(description = "Purchase order ID") @PathVariable UUID poId) {
        procurementService.markAsReceived(poId);
        return Map.of("success", true, "message", "PO marked as received");
    }

    @GetMapping
    @Operation(summary = "List purchase orders")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of purchase orders"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getPurchaseOrders(@RequestParam(required = false) String status) {
        return Map.of("purchaseOrders", procurementService.getPurchaseOrders(status));
    }

    @GetMapping("/{poId}")
    @Operation(summary = "Get purchase order details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchase order details"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getPurchaseOrder(
            @Parameter(description = "Purchase order ID") @PathVariable UUID poId) {
        PurchaseOrder po = procurementService.getPurchaseOrder(poId);
        return Map.of("poNumber", po.getPoNumber(), "status", po.getStatus(), "grandTotal", po.getGrandTotal());
    }
}
