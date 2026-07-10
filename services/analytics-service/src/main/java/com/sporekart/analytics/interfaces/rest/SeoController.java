package com.sporekart.analytics.interfaces.rest;

import com.sporekart.analytics.application.service.AnalyticsService;
import com.sporekart.analytics.domain.model.SeoMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seo")
public class SeoController {
    private final AnalyticsService analyticsService;

    public SeoController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/sitemap")
    public ResponseEntity<String> sitemap() {
        return ResponseEntity.ok("<?xml version=\"1.0\"?><urlset />\n");
    }

    @GetMapping("/robots")
    public ResponseEntity<String> robots() {
        return ResponseEntity.ok("User-agent: *\nDisallow: \n");
    }

    @GetMapping("/metadata")
    public ResponseEntity<List<SeoMetadata>> metadata() {
        return ResponseEntity.ok(analyticsService.getSeoMetadata());
    }

    @PostMapping("/metadata")
    public ResponseEntity<SeoMetadata> createMetadata(@RequestBody CreateMetadataRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analyticsService.createSeoMetadata(request.path(), request.title(), request.description()));
    }

    public record CreateMetadataRequest(String path, String title, String description) {
    }
}
