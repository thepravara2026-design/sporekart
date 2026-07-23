package com.sporekart.workspace.service;

import com.sporekart.workspace.domain.CopilotInfo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CopilotRegistryService {

    private static final Logger log = LoggerFactory.getLogger(CopilotRegistryService.class);

    private final ConcurrentHashMap<String, CopilotInfo> registry = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostConstruct
    public void init() {
        discoverCopilotsFromConfig();
    }

    public CopilotInfo registerCopilot(CopilotInfo copilotInfo) {
        registry.put(copilotInfo.copilotId(), copilotInfo);
        log.info("Registered copilot: {} ({})", copilotInfo.name(), copilotInfo.copilotId());
        return copilotInfo;
    }

    public void unregisterCopilot(String copilotId) {
        registry.remove(copilotId);
        log.info("Unregistered copilot: {}", copilotId);
    }

    public Optional<CopilotInfo> getCopilot(String copilotId) {
        return Optional.ofNullable(registry.get(copilotId));
    }

    public List<CopilotInfo> getAllCopilots() {
        return new ArrayList<>(registry.values());
    }

    public List<CopilotInfo> getEnabledCopilots() {
        return registry.values().stream()
                .filter(CopilotInfo::enabled)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getCopilotHealth(String copilotId) {
        CopilotInfo copilot = registry.get(copilotId);
        if (copilot == null) {
            return Map.of("status", "UNKNOWN", "error", "Copilot not found");
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(copilot.baseUrl() + "/health"))
                    .GET()
                    .timeout(java.time.Duration.ofSeconds(5))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> health = new HashMap<>();
            health.put("status", response.statusCode() == 200 ? "UP" : "DOWN");
            health.put("httpStatus", response.statusCode());
            health.put("copilotId", copilotId);
            health.put("checkedAt", OffsetDateTime.now().toString());
            return health;
        } catch (Exception e) {
            return Map.of("status", "DOWN", "error", e.getMessage(), "copilotId", copilotId);
        }
    }

    public Map<String, Map<String, Object>> checkAllCopilotHealth() {
        Map<String, Map<String, Object>> results = new ConcurrentHashMap<>();
        registry.keySet().parallelStream().forEach(copilotId -> {
            results.put(copilotId, getCopilotHealth(copilotId));
        });
        return results;
    }

    public List<CopilotInfo> discoverCopilotsFromConfig() {
        List<CopilotInfo> seed = List.of(
                new CopilotInfo(
                        "copilot-customer",
                        "Customer Copilot",
                        CopilotInfo.TYPE_CUSTOMER,
                        "1.0.0",
                        "http://localhost:8099/api/v1/copilot/customer",
                        true,
                        CopilotInfo.STATUS_ACTIVE,
                        List.of("order_inquiry", "product_search", "recommendation", "support"),
                        List.of("customer"),
                        Map.of("port", 8099, "description", "Handles customer inquiries, orders, and product searches"),
                        0,
                        0.0,
                        OffsetDateTime.now()
                ),
                new CopilotInfo(
                        "copilot-admin",
                        "Admin Copilot",
                        CopilotInfo.TYPE_ADMIN,
                        "1.0.0",
                        "http://localhost:8100/api/v1/copilot/admin",
                        true,
                        CopilotInfo.STATUS_ACTIVE,
                        List.of("dashboard", "analytics", "user_management", "revenue_report"),
                        List.of("admin"),
                        Map.of("port", 8100, "description", "Handles administrative tasks, dashboards, and analytics"),
                        0,
                        0.0,
                        OffsetDateTime.now()
                ),
                new CopilotInfo(
                        "copilot-trainer",
                        "Trainer Copilot",
                        CopilotInfo.TYPE_TRAINER,
                        "1.0.0",
                        "http://localhost:8101/api/v1/copilot/trainer",
                        true,
                        CopilotInfo.STATUS_ACTIVE,
                        List.of("training_info", "student_progress", "lesson_plan", "certification"),
                        List.of("trainer"),
                        Map.of("port", 8101, "description", "Handles training, courses, certifications, and student management"),
                        0,
                        0.0,
                        OffsetDateTime.now()
                ),
                new CopilotInfo(
                        "copilot-grower",
                        "Grower Copilot",
                        CopilotInfo.TYPE_GROWER,
                        "1.0.0",
                        "http://localhost:8102/api/v1/copilot/grower",
                        true,
                        CopilotInfo.STATUS_ACTIVE,
                        List.of("cultivation_advice", "disease_diagnosis", "yield_prediction", "weather"),
                        List.of("grower"),
                        Map.of("port", 8102, "description", "Handles cultivation advice, disease diagnosis, and yield predictions"),
                        0,
                        0.0,
                        OffsetDateTime.now()
                )
        );

        for (CopilotInfo copilot : seed) {
            registry.put(copilot.copilotId(), copilot);
        }

        log.info("Discovered and registered {} copilots from configuration", seed.size());
        return seed;
    }
}
