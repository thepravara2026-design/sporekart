package com.sporekart.copilot.controller;

import com.sporekart.copilot.dto.HealthResponse;
import com.sporekart.copilot.service.CopilotHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/copilot/health")
public class CopilotHealthController {

    private final CopilotHealthService healthService;

    public CopilotHealthController(CopilotHealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(healthService.getHealth());
    }

    @GetMapping("/ready")
    public ResponseEntity<HealthResponse> readiness() {
        return ResponseEntity.ok(healthService.getReadiness());
    }

    @GetMapping("/live")
    public ResponseEntity<HealthResponse> liveness() {
        return ResponseEntity.ok(healthService.getLiveness());
    }
}
