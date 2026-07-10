package com.sporekart.analytics.interfaces.rest;

import com.sporekart.analytics.application.service.AnalyticsService;
import com.sporekart.analytics.domain.model.ReportRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final AnalyticsService analyticsService;

    public ReportController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<List<ReportRequest>> listReports() {
        return ResponseEntity.ok(analyticsService.getReports());
    }

    @PostMapping("/export")
    public ResponseEntity<ReportRequest> export(@RequestBody CreateReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analyticsService.createReport(request.reportType(), request.format()));
    }

    public record CreateReportRequest(String reportType, String format) {
    }
}
