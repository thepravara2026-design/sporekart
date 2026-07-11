package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.gateway.application.GatewayApplicationService;
import com.sporekart.ai.gateway.application.GatewayResponseBuilder;
import com.sporekart.ai.gateway.domain.AIExecutionRequest;
import com.sporekart.ai.gateway.domain.AIExecutionResponse;
import com.sporekart.ai.gateway.domain.AIHealthResponse;
import com.sporekart.ai.gateway.domain.GatewayStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI Gateway", description = "Enterprise AI Gateway API — execute, validate, health, status, features")
public class GatewayController {
    private final GatewayApplicationService gatewayService;
    private final GatewayResponseBuilder responseBuilder;

    public GatewayController(GatewayApplicationService gatewayService, GatewayResponseBuilder responseBuilder) {
        this.gatewayService = gatewayService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute an AI request", description = "Routes AI request through the gateway pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "AI execution completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded"),
        @ApiResponse(responseCode = "503", description = "AI Gateway unavailable")
    })
    public ResponseEntity<ResponseEnvelope<AIExecutionResponse>> execute(@RequestBody AIExecutionRequest request) {
        AIExecutionResponse response = gatewayService.executeRequest(request);
        return ResponseEntity.ok(responseBuilder.fromExecutionResponse(response));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate an AI request", description = "Validates request without execution")
    public ResponseEntity<ResponseEnvelope<AIExecutionResponse>> validate(@RequestBody AIExecutionRequest request) {
        AIExecutionResponse response = gatewayService.validateRequest(request);
        return ResponseEntity.ok(responseBuilder.fromExecutionResponse(response));
    }

    @GetMapping("/health")
    @Operation(summary = "AI Gateway health check", description = "Returns gateway health status")
    public ResponseEntity<ResponseEnvelope<AIHealthResponse>> health() {
        AIHealthResponse health = gatewayService.getHealth();
        return ResponseEntity.ok(responseBuilder.buildSuccess(health));
    }

    @GetMapping("/status")
    @Operation(summary = "AI Gateway status", description = "Returns detailed gateway status including metrics and feature flags")
    public ResponseEntity<ResponseEnvelope<GatewayStatus>> status() {
        GatewayStatus status = gatewayService.getStatus();
        return ResponseEntity.ok(responseBuilder.buildSuccess(status));
    }

    @GetMapping("/features")
    @Operation(summary = "Get feature flags", description = "Returns current state of all AI feature flags")
    public ResponseEntity<ResponseEnvelope<Map<String, Boolean>>> features() {
        Map<String, Boolean> features = gatewayService.getFeatures();
        return ResponseEntity.ok(responseBuilder.buildSuccess(features));
    }
}
