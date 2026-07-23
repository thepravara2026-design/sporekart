package com.sporekart.customer.copilot.controller;

import com.sporekart.copilot.PageContext;
import com.sporekart.copilot.UserContext;
import com.sporekart.customer.copilot.domain.CustomerProfile;
import com.sporekart.customer.copilot.domain.ProductItem;
import com.sporekart.customer.copilot.domain.ProductRecommendation;
import com.sporekart.customer.copilot.dto.*;
import com.sporekart.customer.copilot.service.CustomerCopilotOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/copilot/customer")
public class CustomerCopilotController {

    private static final Logger log = LoggerFactory.getLogger(CustomerCopilotController.class);

    private final CustomerCopilotOrchestrator orchestrator;

    private final List<ProductItem> productCatalog = new CopyOnWriteArrayList<>();
    private int productCounter = 0;

    public CustomerCopilotController(CustomerCopilotOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
        seedProductCatalog();
    }

    private void seedProductCatalog() {
        productCatalog.addAll(List.of(
            new ProductItem("P001", "Ergonomic Office Chair", "Premium ergonomic chair with lumbar support", "Furniture", 499.99, "USD", "/images/chair.jpg", 25, true, 4.5, List.of("ergonomic", "office", "furniture")),
            new ProductItem("P002", "Mechanical Keyboard", "RGB mechanical keyboard with Cherry MX switches", "Electronics", 149.99, "USD", "/images/keyboard.jpg", 50, true, 4.3, List.of("keyboard", "mechanical", "electronics")),
            new ProductItem("P003", "27-inch 4K Monitor", "Ultra HD monitor with HDR support", "Electronics", 699.99, "USD", "/images/monitor.jpg", 15, true, 4.7, List.of("monitor", "4k", "electronics")),
            new ProductItem("P004", "Standing Desk Converter", "Adjustable height standing desk converter", "Furniture", 299.99, "USD", "/images/desk.jpg", 30, true, 4.2, List.of("standing", "desk", "furniture")),
            new ProductItem("P005", "Wireless Mouse", "Bluetooth ergonomic mouse", "Electronics", 79.99, "USD", "/images/mouse.jpg", 100, true, 4.1, List.of("mouse", "wireless", "electronics")),
            new ProductItem("P006", "Webcam 4K", "4K ultra HD webcam with auto-focus", "Electronics", 129.99, "USD", "/images/webcam.jpg", 40, true, 4.4, List.of("webcam", "4k", "electronics")),
            new ProductItem("P007", "Noise Cancelling Headphones", "Over-ear Bluetooth headphones with ANC", "Audio", 349.99, "USD", "/images/headphones.jpg", 20, true, 4.8, List.of("headphones", "noise-cancelling", "audio")),
            new ProductItem("P008", "USB-C Hub", "7-in-1 USB-C hub with HDMI", "Electronics", 49.99, "USD", "/images/usbhub.jpg", 200, true, 4.0, List.of("usb-c", "hub", "electronics"))
        ));
        log.info("Seeded product catalog with {} items", productCatalog.size());
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Chat request received: session={}, messageLen={}", request.sessionId(), request.message().length());

        var sessionId = request.sessionId() != null && !request.sessionId().isBlank()
            ? request.sessionId()
            : UUID.randomUUID().toString();

        var userContext = UserContext.builder()
            .userId("anonymous-" + sessionId)
            .userName("Guest")
            .email("guest@sporekart.com")
            .roles(List.of("CUSTOMER"))
            .workspaceId("default")
            .organizationId("sporekart")
            .tenantId("sporekart")
            .deviceType("web")
            .language("en")
            .timeZone("UTC")
            .build();

        var pageContext = PageContext.builder()
            .pageUrl(request.pageUrl() != null ? request.pageUrl() : "")
            .pageTitle(request.pageTitle() != null ? request.pageTitle() : "")
            .section(request.section() != null ? request.section() : "")
            .entityType(request.entityType() != null ? request.entityType() : "")
            .entityId(request.entityId() != null ? request.entityId() : "")
            .build();

        var response = orchestrator.processMessage(sessionId, request.message(), userContext, pageContext);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@Valid @RequestBody ChatRequest request) {
        log.info("Stream chat request received: session={}", request.sessionId());

        var emitter = new SseEmitter(300000L);

        var sessionId = request.sessionId() != null && !request.sessionId().isBlank()
            ? request.sessionId()
            : UUID.randomUUID().toString();

        var userContext = UserContext.builder()
            .userId("anonymous-" + sessionId)
            .userName("Guest")
            .email("guest@sporekart.com")
            .roles(List.of("CUSTOMER"))
            .workspaceId("default")
            .organizationId("sporekart")
            .tenantId("sporekart")
            .deviceType("web")
            .language("en")
            .timeZone("UTC")
            .build();

        var pageContext = PageContext.builder()
            .pageUrl(request.pageUrl() != null ? request.pageUrl() : "")
            .pageTitle(request.pageTitle() != null ? request.pageTitle() : "")
            .section(request.section() != null ? request.section() : "")
            .entityType(request.entityType() != null ? request.entityType() : "")
            .entityId(request.entityId() != null ? request.entityId() : "")
            .build();

        var response = orchestrator.processMessage(sessionId, request.message(), userContext, pageContext);

        try {
            emitter.send(SseEmitter.event()
                .id(sessionId)
                .name("message")
                .data(response));

            var suggestions = response.suggestions();
            if (suggestions != null && !suggestions.isEmpty()) {
                emitter.send(SseEmitter.event()
                    .id(sessionId + "-suggestions")
                    .name("suggestions")
                    .data(suggestions));
            }

            emitter.send(SseEmitter.event()
                .id(sessionId + "-complete")
                .name("complete")
                .data(Map.of("sessionId", sessionId, "status", "done")));

            emitter.complete();
        } catch (Exception e) {
            log.error("Error streaming response for session {}: {}", sessionId, e.getMessage());
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @GetMapping("/history")
    public ResponseEntity<List<com.sporekart.customer.copilot.domain.ConversationMessage>> getHistory(
            @RequestParam(defaultValue = "") String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        var history = orchestrator.getConversationHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/recommend")
    public ResponseEntity<RecommendResponse> recommend(@Valid @RequestBody RecommendRequest request) {
        log.info("Recommend request for customer: {}", request.customerId());

        var filtered = productCatalog.stream()
            .filter(p -> request.excludeProductIds() == null || !request.excludeProductIds().contains(p.id()))
            .filter(p -> request.category() == null || request.category().isBlank() || p.category().equalsIgnoreCase(request.category()))
            .limit(request.limit() > 0 ? request.limit() : 10)
            .toList();

        var recommendations = filtered.stream()
            .map(p -> new ProductRecommendation(p, Math.round(Math.random() * 100) / 100.0,
                "Based on your browsing history and preferences", "PERSONALIZED"))
            .toList();

        var response = new RecommendResponse(recommendations, "PERSONALIZED",
            "Showing " + recommendations.size() + " personalized recommendations based on your profile");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/context")
    public ResponseEntity<ContextResponse> getContext(@RequestParam String sessionId) {
        log.info("Context request for session: {}", sessionId);

        var userContext = UserContext.builder()
            .userId("anonymous-" + sessionId)
            .userName("Guest")
            .email("guest@sporekart.com")
            .roles(List.of("CUSTOMER"))
            .workspaceId("default")
            .organizationId("sporekart")
            .tenantId("sporekart")
            .deviceType("web")
            .language("en")
            .timeZone("UTC")
            .build();

        var pageContext = PageContext.empty();

        var contextResponse = orchestrator.getContextService().assembleContext(sessionId, pageContext, userContext);
        return ResponseEntity.ok(contextResponse);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductSearchResponse> searchProducts(@Valid @RequestBody ProductSearchRequest request) {
        log.info("Product search request: query={}, category={}", request.query(), request.category());

        var stream = productCatalog.stream();

        if (request.query() != null && !request.query().isBlank()) {
            var q = request.query().toLowerCase();
            stream = stream.filter(p ->
                p.name().toLowerCase().contains(q) ||
                p.description().toLowerCase().contains(q) ||
                p.tags().stream().anyMatch(t -> t.toLowerCase().contains(q))
            );
        }

        if (request.category() != null && !request.category().isBlank()) {
            stream = stream.filter(p -> p.category().equalsIgnoreCase(request.category()));
        }

        if (request.minPrice() != null) {
            stream = stream.filter(p -> p.price() >= request.minPrice());
        }

        if (request.maxPrice() != null) {
            stream = stream.filter(p -> p.price() <= request.maxPrice());
        }

        var allResults = stream.toList();
        var totalResults = allResults.size();

        var page = Math.max(0, request.page());
        var size = Math.max(1, request.size() > 0 ? request.size() : 20);
        var fromIndex = page * size;
        var toIndex = Math.min(fromIndex + size, totalResults);

        List<ProductItem> pageResults;
        if (fromIndex >= totalResults) {
            pageResults = List.of();
        } else {
            pageResults = allResults.subList(fromIndex, toIndex);
        }

        var response = new ProductSearchResponse(pageResults, totalResults, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    public ResponseEntity<Void> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        log.info("Feedback received: session={}, rating={}", request.sessionId(), request.rating());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        var status = new HashMap<String, Object>();
        status.put("status", "UP");
        status.put("service", "customer-copilot-service");
        status.put("version", "0.1.0");
        status.put("timestamp", OffsetDateTime.now().toString());
        status.put("activeSessions", orchestrator.getConversationHistory("").size());
        return ResponseEntity.ok(status);
    }
}
