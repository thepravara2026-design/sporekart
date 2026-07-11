package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.GSTService;
import com.sporekart.ai.domain.model.GSTTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/gst")
@Tag(name = "GST", description = "GST transaction processing and filing endpoints")
public class GSTController {
    private final GSTService gstService;

    public GSTController(GSTService gstService) {
        this.gstService = gstService;
    }

    @PostMapping("/transactions")
    @Operation(summary = "Create a GST transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> createTransaction(@RequestBody Map<String, String> payload) {
        GSTTransaction tx = gstService.createTransaction(
                payload.get("transactionType"),
                LocalDate.parse(payload.get("date")),
                new BigDecimal(payload.get("amount")),
                payload.get("supplierGSTIN"),
                payload.get("buyerGSTIN"));
        return Map.of(
                "id", tx.getId(),
                "number", tx.getTransactionNumber(),
                "totalTax", tx.getTotalTax(),
                "grandTotal", tx.getGrandTotal());
    }

    @PostMapping("/transactions/{txId}/process")
    @Operation(summary = "Process a GST transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction processed"),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> processTransaction(@PathVariable UUID txId) {
        gstService.processTransaction(txId);
        return Map.of("success", true, "message", "Transaction processed");
    }

    @PostMapping("/transactions/{txId}/file")
    @Operation(summary = "File a GST return")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction filed"),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> fileTransaction(@PathVariable UUID txId) {
        gstService.fileReturn(txId);
        return Map.of("success", true, "message", "Transaction filed");
    }

    @GetMapping("/reports")
    @Operation(summary = "Get GST report for a date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "GST report"),
        @ApiResponse(responseCode = "400", description = "Invalid date range", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getGSTReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return gstService.getGSTReport(startDate, endDate);
    }
}