package com.sporekart.risk.interfaces.rest;

import com.sporekart.risk.application.dto.CreateRiskAssessmentRequest;
import com.sporekart.risk.application.dto.RiskAssessmentResponse;
import com.sporekart.risk.application.service.RiskService;
import com.sporekart.risk.domain.model.RiskLevel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/risk-assessments")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskAssessmentResponse> create(@Valid @RequestBody CreateRiskAssessmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(riskService.assessRisk(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskAssessmentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(riskService.getAssessment(id));
    }

    @GetMapping
    public ResponseEntity<List<RiskAssessmentResponse>> list(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) RiskLevel riskLevel) {
        if (entityType != null && entityId != null) {
            return ResponseEntity.ok(riskService.getAssessmentsByEntity(entityType, entityId));
        }
        if (riskLevel != null) {
            return ResponseEntity.ok(riskService.getAssessmentsByLevel(riskLevel));
        }
        return ResponseEntity.ok(riskService.listAll());
    }

    @PostMapping("/{id}/mitigate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskAssessmentResponse> mitigate(@PathVariable String id) {
        return ResponseEntity.ok(riskService.mitigateRisk(id));
    }

    @PostMapping("/{id}/escalate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskAssessmentResponse> escalate(@PathVariable String id,
            @RequestBody Map<String, String> body) {
        String reason = body.getOrDefault("reason", "");
        return ResponseEntity.ok(riskService.escalateRisk(id, reason));
    }
}