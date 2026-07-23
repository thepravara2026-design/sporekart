package com.sporekart.grower.copilot.controller;

import com.sporekart.grower.copilot.domain.PageContext;
import com.sporekart.grower.copilot.domain.UserContext;
import com.sporekart.grower.copilot.dto.ChatRequest;
import com.sporekart.grower.copilot.dto.ChatResponse;
import com.sporekart.grower.copilot.dto.DiseaseRequest;
import com.sporekart.grower.copilot.dto.DiseaseResponse;
import com.sporekart.grower.copilot.dto.PlanningRequest;
import com.sporekart.grower.copilot.dto.PlanningResponse;
import com.sporekart.grower.copilot.dto.RecommendRequest;
import com.sporekart.grower.copilot.dto.RecommendResponse;
import com.sporekart.grower.copilot.dto.WeatherRequest;
import com.sporekart.grower.copilot.dto.WeatherResponse;
import com.sporekart.grower.copilot.dto.YieldRequest;
import com.sporekart.grower.copilot.dto.YieldResponse;
import com.sporekart.grower.copilot.service.GrowerCopilotOrchestrator;
import com.sporekart.grower.copilot.service.GrowerContextService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/copilot/grower")
public class GrowerCopilotController {

    private static final Logger log = LoggerFactory.getLogger(GrowerCopilotController.class);

    private final GrowerCopilotOrchestrator orchestrator;
    private final GrowerContextService contextService;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public GrowerCopilotController(GrowerCopilotOrchestrator orchestrator,
                                   GrowerContextService contextService) {
        this.orchestrator = orchestrator;
        this.contextService = contextService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("Chat request: sessionId={}, message={}", request.sessionId(), request.message());
        String sessionId = resolveSession(request);
        UserContext userContext = buildUserContext(request);
        PageContext pageContext = buildPageContext(request);
        ChatResponse response = orchestrator.processMessage(sessionId, request.message(), userContext, pageContext);
        contextService.addConversationMessage(sessionId, "user", request.message());
        contextService.addConversationMessage(sessionId, "assistant", response.message());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stream")
    public SseEmitter stream(@RequestBody ChatRequest request) {
        log.info("Stream request: sessionId={}, message={}", request.sessionId(), request.message());
        SseEmitter emitter = new SseEmitter(0L);
        String sessionId = resolveSession(request);
        UserContext userContext = buildUserContext(request);
        PageContext pageContext = buildPageContext(request);

        executor.execute(() -> {
            try {
                ChatResponse response = orchestrator.processMessage(sessionId, request.message(), userContext, pageContext);
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(response, MediaType.APPLICATION_JSON));
                emitter.send(SseEmitter.event()
                        .name("complete")
                        .data("{\"status\":\"done\"}", MediaType.APPLICATION_JSON));
                contextService.addConversationMessage(sessionId, "user", request.message());
                contextService.addConversationMessage(sessionId, "assistant", response.message());
                emitter.complete();
            } catch (IOException e) {
                log.error("SSE stream error for session {}", sessionId, e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @PostMapping("/recommend")
    public ResponseEntity<RecommendResponse> recommend(@RequestBody RecommendRequest request) {
        log.info("Recommend request: query={}, mushroomType={}", request.query(), request.mushroomType());
        var spawn = orchestrator.recommendSpawn(request.farmLocation());
        var substrate = orchestrator.recommendSubstrate(request.farmLocation());
        return ResponseEntity.ok(new RecommendResponse(
            "spawn_substrate",
            "Recommendations for " + request.mushroomType(),
            "Spawn and substrate recommendations generated",
            List.of(spawn, substrate),
            Map.of("query", request.query(), "difficulty", request.difficulty())
        ));
    }

    @PostMapping("/disease")
    public ResponseEntity<DiseaseResponse> diagnose(@RequestBody DiseaseRequest request) {
        log.info("Disease diagnosis request: symptoms={}", request.symptoms());
        var disease = orchestrator.diagnoseDisease("unknown", request.symptoms());
        return ResponseEntity.ok(new DiseaseResponse(
            disease.diseaseId(),
            disease.diseaseName(),
            disease.scientificName(),
            disease.category(),
            disease.symptoms(),
            disease.possibleCauses(),
            disease.probabilityScore(),
            disease.severity(),
            disease.treatmentPlan(),
            disease.preventionMethods(),
            disease.scientificReferences(),
            disease.requiresEscalation()
        ));
    }

    @PostMapping("/yield")
    public ResponseEntity<YieldResponse> predictYield(@RequestBody YieldRequest request) {
        log.info("Yield prediction request: mushroomType={}, area={}", request.mushroomType(), request.areaSquareMeters());
        var prediction = orchestrator.predictYield(request.farmLocation());
        return ResponseEntity.ok(new YieldResponse(
            prediction.predictionId(),
            prediction.expectedYieldKg(),
            prediction.yieldEfficiencyPercent(),
            prediction.estimatedRevenue(),
            prediction.productionCost(),
            prediction.profitMargin(),
            prediction.riskScore(),
            prediction.harvestWindowStart() + " to " + prediction.harvestWindowEnd(),
            prediction.recommendations(),
            prediction.confidenceLevel()
        ));
    }

    @PostMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(@RequestBody WeatherRequest request) {
        log.info("Weather request: location={}, days={}", request.location(), request.forecastDays());
        var weather = orchestrator.getWeather(request.location());
        return ResponseEntity.ok(new WeatherResponse(
            request.location(),
            weather,
            List.of(),
            weather.climateRisk(),
            Map.of("weatherImpact", weather.weatherImpact())
        ));
    }

    @PostMapping("/planning")
    public ResponseEntity<PlanningResponse> createPlan(@RequestBody PlanningRequest request) {
        log.info("Business planning request: mushroomType={}, budget={}", request.mushroomType(), request.budget());
        var plan = orchestrator.createBusinessPlan(request.location());
        return ResponseEntity.ok(new PlanningResponse(
            plan,
            List.of(),
            List.of(),
            "Business plan for " + request.mushroomType() + " generated"
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, String>>> getHistory(@RequestParam String sessionId) {
        log.debug("History request for session {}", sessionId);
        return ResponseEntity.ok(contextService.getConversationHistory(sessionId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "grower-copilot",
            "version", "1.0.0"
        ));
    }

    private String resolveSession(ChatRequest request) {
        if (request.sessionId() != null && !request.sessionId().isBlank()) {
            return request.sessionId();
        }
        return orchestrator.createSession("anonymous");
    }

    private UserContext buildUserContext(ChatRequest request) {
        return new UserContext(
            "anonymous",
            request.sessionId(),
            null,
            Map.of("farmLocation", request.farmLocation() != null ? request.farmLocation() : "")
        );
    }

    private PageContext buildPageContext(ChatRequest request) {
        return new PageContext(
            request.pageUrl(),
            request.pageTitle(),
            request.section(),
            request.entityType(),
            request.entityId()
        );
    }
}
