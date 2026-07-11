package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.ERPIntegrationService;
import com.sporekart.ai.domain.model.ERPSyncLog;
import com.sporekart.ai.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/erp")
@Tag(name = "ERP Integration", description = "ERP integration and synchronization endpoints")
public class ERPIntegrationController {

    private final ERPIntegrationService erpIntegrationService;

    public ERPIntegrationController(ERPIntegrationService erpIntegrationService) {
        this.erpIntegrationService = erpIntegrationService;
    }

    @GetMapping("/providers")
    @Operation(summary = "List available ERP providers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of ERP providers"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getProviders() {
        return Map.of("providers", List.of("TALLY_PRIME", "ZOHO_BOOKS", "ERPNEXT", "SAP", "ORACLE"));
    }

    @PostMapping("/configuration")
    @Operation(summary = "Configure an ERP provider")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ERP provider configured"),
        @ApiResponse(responseCode = "400", description = "Invalid configuration", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ERPConfigureResponse configureERPConfiguration(
            @RequestBody ERPConfigureRequest payload) {
        erpIntegrationService.configureERPProvider(payload.getProvider(), payload.getEndpoint(), payload.getApiKey());
        return new ERPConfigureResponse(true, "ERP provider configured");
    }

    @PostMapping("/configure")
    @Operation(summary = "Configure an ERP provider (alias)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ERP provider configured"),
        @ApiResponse(responseCode = "400", description = "Invalid configuration", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ERPConfigureResponse configureERP(
            @RequestBody ERPConfigureRequest payload) {
        erpIntegrationService.configureERPProvider(
                payload.getProvider(),
                payload.getEndpoint(),
                payload.getApiKey());
        return new ERPConfigureResponse(true, "ERP provider configured");
    }

    @PostMapping("/sync")
    @Operation(summary = "Initiate an ERP synchronization")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sync initiated"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ERPSyncResponse initiateSync(
            @RequestBody ERPSyncRequest payload) {
        ERPSyncLog log = erpIntegrationService.initiateSync(
                payload.getSyncType(),
                payload.getProvider());
        return new ERPSyncResponse(log.getId(), log.getSyncStatus());
    }

    @PostMapping("/sync/{syncId}/complete")
    @Operation(summary = "Mark an ERP sync as completed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sync marked as completed"),
        @ApiResponse(responseCode = "404", description = "Sync log not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public SyncCompleteResponse completeSync(
            @Parameter(description = "Sync log ID") @PathVariable UUID syncId,
            @RequestParam Integer synced,
            @RequestParam Integer failed) {
        erpIntegrationService.completeSyncLog(syncId, synced, failed);
        return new SyncCompleteResponse(true, "Sync completed");
    }

    @PostMapping("/sync/{syncId}/fail")
    @Operation(summary = "Mark an ERP sync as failed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sync marked as failed"),
        @ApiResponse(responseCode = "404", description = "Sync log not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public SyncCompleteResponse failSync(
            @Parameter(description = "Sync log ID") @PathVariable UUID syncId,
            @RequestParam String error) {
        erpIntegrationService.failSyncLog(syncId, error);
        return new SyncCompleteResponse(true, "Sync failed");
    }

    @GetMapping("/status")
    @Operation(summary = "Get ERP provider status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ERP provider status"),
        @ApiResponse(responseCode = "400", description = "Invalid provider", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ERPStatusResponse getStatus(@RequestParam String provider) {
        return new ERPStatusResponse(erpIntegrationService.getERPStatus(provider));
    }

    @GetMapping("/sync-history")
    @Operation(summary = "Get ERP sync history for a provider")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ERP sync history"),
        @ApiResponse(responseCode = "400", description = "Invalid provider", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ERPSyncHistoryResponse getSyncHistory(@RequestParam String provider) {
        return new ERPSyncHistoryResponse(
                erpIntegrationService.getSyncHistory(provider));
    }
}
