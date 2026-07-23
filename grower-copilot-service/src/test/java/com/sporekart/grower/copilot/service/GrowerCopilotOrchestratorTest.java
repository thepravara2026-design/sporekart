package com.sporekart.grower.copilot.service;

import com.sporekart.grower.copilot.domain.BusinessPlan;
import com.sporekart.grower.copilot.domain.DiseaseInfo;
import com.sporekart.grower.copilot.domain.GrowerProfile;
import com.sporekart.grower.copilot.domain.KnowledgeArticle;
import com.sporekart.grower.copilot.domain.SpawnRecommendation;
import com.sporekart.grower.copilot.domain.SubstrateRecommendation;
import com.sporekart.grower.copilot.domain.WeatherData;
import com.sporekart.grower.copilot.domain.YieldPrediction;
import com.sporekart.grower.copilot.dto.ChatRequest;
import com.sporekart.grower.copilot.dto.ChatResponse;
import com.sporekart.grower.copilot.dto.DiseaseRequest;
import com.sporekart.grower.copilot.dto.DiseaseResponse;
import com.sporekart.grower.copilot.dto.PlanningRequest;
import com.sporekart.grower.copilot.dto.PlanningResponse;
import com.sporekart.grower.copilot.dto.RecommendRequest;
import com.sporekart.grower.copilot.dto.RecommendResponse;
import com.sporekart.grower.copilot.dto.Suggestion;
import com.sporekart.grower.copilot.dto.WeatherRequest;
import com.sporekart.grower.copilot.dto.WeatherResponse;
import com.sporekart.grower.copilot.dto.YieldRequest;
import com.sporekart.grower.copilot.dto.YieldResponse;
import com.sporekart.grower.copilot.engine.BusinessPlanningEngine;
import com.sporekart.grower.copilot.engine.CropHealthAdvisorEngine;
import com.sporekart.grower.copilot.engine.CultivationAdvisorEngine;
import com.sporekart.grower.copilot.engine.DiseaseAdvisorEngine;
import com.sporekart.grower.copilot.engine.KnowledgeRetrievalEngine;
import com.sporekart.grower.copilot.engine.SpawnRecommendationEngine;
import com.sporekart.grower.copilot.engine.SubstrateAdvisorEngine;
import com.sporekart.grower.copilot.engine.WeatherIntegrationEngine;
import com.sporekart.grower.copilot.engine.YieldPredictionEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrowerCopilotOrchestratorTest {

    @Mock
    private CultivationAdvisorEngine cultivationEngine;

    @Mock
    private SpawnRecommendationEngine spawnEngine;

    @Mock
    private SubstrateAdvisorEngine substrateEngine;

    @Mock
    private DiseaseAdvisorEngine diseaseEngine;

    @Mock
    private YieldPredictionEngine yieldEngine;

    @Mock
    private BusinessPlanningEngine planningEngine;

    @Mock
    private WeatherIntegrationEngine weatherEngine;

    @Mock
    private KnowledgeRetrievalEngine knowledgeEngine;

    @Mock
    private CropHealthAdvisorEngine cropHealthEngine;

    private GrowerCopilotOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new GrowerCopilotOrchestrator(
                cultivationEngine, spawnEngine, substrateEngine,
                diseaseEngine, yieldEngine, planningEngine,
                weatherEngine, knowledgeEngine, cropHealthEngine);
    }

    @Test
    void processMessage_ShouldReturnChatResponse() {
        when(cultivationEngine.answerCultivationQuestion(anyString(), anyString())).thenReturn("Cultivation answer");
        when(spawnEngine.recommendSpawn(anyString())).thenReturn(List.of());
        when(knowledgeEngine.search(anyString(), anyInt())).thenReturn(List.of());

        ChatRequest request = new ChatRequest("How to grow oyster mushrooms?", null, null, null, null, null, null, null);
        ChatResponse response = orchestrator.processMessage(request);

        assertThat(response).isNotNull();
        assertThat(response.sessionId()).isNotBlank();
        assertThat(response.message()).isNotBlank();
    }

    @Test
    void processMessage_WithExistingSession_ShouldReturnResponse() {
        when(cultivationEngine.answerCultivationQuestion(anyString(), anyString())).thenReturn("Answer for session");

        ChatRequest request1 = new ChatRequest("Hello", null, null, null, null, null, null, null);
        ChatResponse first = orchestrator.processMessage(request1);

        ChatRequest request2 = new ChatRequest("Tell me more", first.sessionId(), null, null, null, null, null, null);
        ChatResponse second = orchestrator.processMessage(request2);

        assertThat(second.sessionId()).isEqualTo(first.sessionId());
    }

    @Test
    void processMessage_WithSpawningQuestion_ShouldIncludeSpawnRecommendations() {
        when(spawnEngine.recommendSpawn(anyString())).thenReturn(List.of(
                new SpawnRecommendation("S1", "Oyster", "PL-27", "Grain", "Easy",
                        20, 30, 70, 85, 15, 5, 20, 5.0, "Tropical", "High",
                        List.of("Training 1"), "Oyster mushroom", List.of("Fast growth"), List.of())));
        when(cultivationEngine.answerCultivationQuestion(anyString(), anyString())).thenReturn("Spawn info");

        ChatRequest request = new ChatRequest("What spawn should I use?", null, null, null, null, "spawn", null, null);
        ChatResponse response = orchestrator.processMessage(request);

        assertThat(response).isNotNull();
    }

    @Test
    void processMessage_WithSubstrateQuestion_ShouldIncludeSubstrateInfo() {
        when(substrateEngine.recommendSubstrate(anyString())).thenReturn(List.of(
                new SubstrateRecommendation("SUB1", "Wheat Straw", "Agricultural", "Standard", 68.0,
                        "Steam", 3, 5.0, 1.2, List.of("Oyster"), List.of("Chop", "Soak"),
                        List.of("Cheap"), List.of("Bulky"), List.of("Use fresh straw"))));
        when(cultivationEngine.answerCultivationQuestion(anyString(), anyString())).thenReturn("Substrate info");

        ChatRequest request = new ChatRequest("What substrate for oyster?", null, null, null, null, null, null, null);
        ChatResponse response = orchestrator.processMessage(request);

        assertThat(response).isNotNull();
    }

    @Test
    void createSession_ShouldReturnSessionId() {
        String sessionId = orchestrator.createSession();
        assertThat(sessionId).isNotBlank();
    }

    @Test
    void createSession_Multiple_ShouldReturnDifferentIds() {
        String s1 = orchestrator.createSession();
        String s2 = orchestrator.createSession();
        assertThat(s1).isNotEqualTo(s2);
    }

    @Test
    void endSession_ShouldInvalidateSession() {
        ChatRequest request = new ChatRequest("Hello", null, null, null, null, null, null, null);
        ChatResponse response = orchestrator.processMessage(request);
        String sessionId = response.sessionId();

        orchestrator.endSession(sessionId);
        List<ChatResponse> history = orchestrator.getHistory(sessionId);
        assertThat(history).isEmpty();
    }

    @Test
    void endSession_WithInvalidId_ShouldNotThrow() {
        orchestrator.endSession("nonexistent");
    }

    @Test
    void recommendSpawn_ShouldReturnRecommendResponse() {
        when(spawnEngine.recommendSpawn(anyString())).thenReturn(List.of(
                new SpawnRecommendation("S1", "Oyster", "PL-27", "Grain", "Easy",
                        20, 30, 70, 85, 15, 5, 20, 5.0, "Tropical", "High",
                        List.of(), "Oyster", List.of(), List.of())));

        RecommendRequest request = new RecommendRequest("oyster", null, "spawn", null, null, null);
        RecommendResponse response = orchestrator.recommendSpawn(request);

        assertThat(response).isNotNull();
        assertThat(response.recommendationType()).isEqualTo("spawn");
    }

    @Test
    void recommendSpawn_WithDifficulty_ShouldFilterByDifficulty() {
        when(spawnEngine.recommendSpawnByDifficulty(anyString())).thenReturn(List.of(
                new SpawnRecommendation("S2", "Shiitake", "DK-01", "Sawdust", "Expert",
                        15, 25, 75, 85, 60, 10, 30, 3.0, "Temperate", "Moderate",
                        List.of(), "Shiitake", List.of(), List.of())));

        RecommendRequest request = new RecommendRequest("shiitake", null, "spawn", "expert", null, null);
        RecommendResponse response = orchestrator.recommendSpawn(request);

        assertThat(response).isNotNull();
    }

    @Test
    void recommendSpawn_WithClimate_ShouldFilterByClimate() {
        when(spawnEngine.recommendSpawnByClimate(anyString())).thenReturn(List.of(
                new SpawnRecommendation("S3", "Milky", "CI-01", "Grain", "Easy",
                        30, 38, 80, 88, 18, 8, 15, 6.0, "Tropical", "High",
                        List.of(), "Milky", List.of(), List.of())));

        RecommendRequest request = new RecommendRequest("milky", null, "spawn", null, "tropical", null);
        RecommendResponse response = orchestrator.recommendSpawn(request);

        assertThat(response).isNotNull();
    }

    @Test
    void diagnoseDisease_ShouldReturnDiseaseResponse() {
        DiseaseAdvisorEngine.DiseaseResponse diseaseResult = new DiseaseAdvisorEngine.DiseaseResponse(
                "diag-1",
                List.of(new DiseaseInfo("D001", "Green Mold", "Trichoderma spp.", "Fungal",
                        List.of("Green spots"), List.of("Poor sterilization"), 0.85,
                        "Remove substrate", List.of("Sterilize"), List.of("Ref1"), true, "HIGH", List.of())),
                "Green Mold", "Treatment plan", true);
        when(diseaseEngine.diagnoseBySymptoms(anyList(), anyString(), anyString())).thenReturn(diseaseResult);

        DiseaseRequest request = new DiseaseRequest(List.of("green spots", "foul odor"), "oyster", "fruiting", null, null);
        DiseaseResponse response = orchestrator.diagnoseDisease(request);

        assertThat(response).isNotNull();
        assertThat(response.diseaseName()).isEqualTo("Green Mold");
    }

    @Test
    void diagnoseDisease_WithEnvironment_ShouldIncludeContext() {
        DiseaseAdvisorEngine.DiseaseResponse diseaseResult = new DiseaseAdvisorEngine.DiseaseResponse(
                "diag-2", List.of(), "No match", "Monitor", false);
        when(diseaseEngine.diagnoseBySymptoms(anyList(), anyString(), anyString())).thenReturn(diseaseResult);

        DiseaseRequest request = new DiseaseRequest(List.of("unknown symptom"), "oyster", "fruiting", "High humidity 95%", List.of());
        DiseaseResponse response = orchestrator.diagnoseDisease(request);

        assertThat(response).isNotNull();
    }

    @Test
    void predictYield_ShouldReturnYieldResponse() {
        when(yieldEngine.predictYield(anyString(), anyString(), anyDouble(), anyInt(), anyString(), any()))
                .thenReturn(new YieldPrediction("pred-1", "Oyster", "Wheat Straw", 100.0, 200,
                        500.0, 85.0, 250000.0, 150000.0, 40.0, 15.0,
                        "2026-08-01", "2026-09-15", 90, "Maintain humidity"));
        when(yieldEngine.calculateEfficiency(any())).thenCallRealMethod();

        YieldRequest request = new YieldRequest("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        YieldResponse response = orchestrator.predictYield(request);

        assertThat(response).isNotNull();
        assertThat(response.expectedYieldKg()).isEqualTo(500.0);
    }

    @Test
    void predictYield_WithAdditionalParams_ShouldPassThem() {
        when(yieldEngine.predictYield(anyString(), anyString(), anyDouble(), anyInt(), anyString(), any()))
                .thenReturn(new YieldPrediction("pred-2", "Button", "Compost", 200.0, 400,
                        300.0, 75.0, 450000.0, 300000.0, 33.0, 25.0,
                        "2026-10-01", "2026-12-15", 85, "Monitor temperature"));

        YieldRequest request = new YieldRequest("Button", "Compost", 200.0, 400, "Himachal Pradesh",
                Map.of("compost_type", "phase_II"));
        YieldResponse response = orchestrator.predictYield(request);

        assertThat(response).isNotNull();
    }

    @Test
    void getWeather_ShouldReturnWeatherResponse() {
        when(weatherEngine.getCurrentWeather(anyString()))
                .thenReturn(new WeatherIntegrationEngine.WeatherData("Punjab", 28.5, 44.0, 12.0, 0.0, "Clear", java.time.LocalDate.now()));
        when(weatherEngine.getForecast(anyString(), anyInt())).thenReturn(List.of());
        when(weatherEngine.getClimateRisk(anyString(), anyString())).thenReturn("LOW");

        WeatherRequest request = new WeatherRequest("Punjab", 5);
        WeatherResponse response = orchestrator.getWeather(request);

        assertThat(response).isNotNull();
        assertThat(response.location()).isEqualTo("Punjab");
    }

    @Test
    void getWeather_WithForecast_ShouldIncludeForecast() {
        when(weatherEngine.getCurrentWeather(anyString()))
                .thenReturn(new WeatherIntegrationEngine.WeatherData("Karnataka", 31.2, 72.0, 8.5, 12.0, "Cloudy", java.time.LocalDate.now()));
        when(weatherEngine.getForecast(anyString(), anyInt())).thenReturn(List.of(
                new WeatherIntegrationEngine.WeatherData("Karnataka", 30.0, 75.0, 10.0, 5.0, "Sunny", java.time.LocalDate.now().plusDays(1))));
        when(weatherEngine.getClimateRisk(anyString(), anyString())).thenReturn("MODERATE");

        WeatherRequest request = new WeatherRequest("Karnataka", 3);
        WeatherResponse response = orchestrator.getWeather(request);

        assertThat(response).isNotNull();
    }

    @Test
    void createBusinessPlan_ShouldReturnPlanningResponse() {
        when(planningEngine.createBusinessPlan(anyString(), anyDouble(), anyDouble(), anyString(), anyString()))
                .thenReturn(new BusinessPlan("plan-1", "Oyster", 200000.0, 350000.0, 45.0, 12,
                        List.of(), List.of("Growing demand"), List.of("Oct-Mar peak"), List.of("Rs 150-200/kg"),
                        "Vacuum pack", "2-4°C", "Refrigerated"));
        when(planningEngine.generateProductionPlan(anyString(), anyDouble(), anyInt()))
                .thenReturn(List.of());
        when(planningEngine.getMarketInsights(anyString(), anyString())).thenReturn(List.of("Growing demand in metros"));

        PlanningRequest request = new PlanningRequest("Oyster", 500.0, 200000.0, "beginner", "Punjab");
        PlanningResponse response = orchestrator.createBusinessPlan(request);

        assertThat(response).isNotNull();
        assertThat(response.summary()).isNotBlank();
    }

    @Test
    void createBusinessPlan_WithHighBudget_ShouldIncludeMoreDetail() {
        when(planningEngine.createBusinessPlan(anyString(), anyDouble(), anyDouble(), anyString(), anyString()))
                .thenReturn(new BusinessPlan("plan-2", "Button", 500000.0, 900000.0, 60.0, 18,
                        List.of(), List.of(), List.of(), List.of(), "", "", ""));
        when(planningEngine.getMarketInsights(anyString(), anyString())).thenReturn(List.of("Premium market"));

        PlanningRequest request = new PlanningRequest("Button", 1000.0, 500000.0, "intermediate", "Himachal Pradesh");
        PlanningResponse response = orchestrator.createBusinessPlan(request);

        assertThat(response).isNotNull();
    }

    @Test
    void searchKnowledge_ShouldReturnResults() {
        when(knowledgeEngine.search(anyString(), anyInt())).thenReturn(List.of(
                new KnowledgeArticle("art-1", "Oyster Cultivation", "Content about oyster...",
                        "CULTIVATION", "SporeKart Manual", List.of("oyster", "cultivation"),
                        "SporeKart Manual v2.3", 0.95, "https://example.com/oyster")));

        List<KnowledgeArticle> results = orchestrator.searchKnowledge("oyster cultivation", 5);
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).title()).contains("Oyster Cultivation");
    }

    @Test
    void searchKnowledge_WithEmptyQuery_ShouldReturnEmpty() {
        when(knowledgeEngine.search(anyString(), anyInt())).thenReturn(List.of());

        List<KnowledgeArticle> results = orchestrator.searchKnowledge("", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void streamMessage_ShouldReturnSseEmitter() {
        when(cultivationEngine.answerCultivationQuestion(anyString(), anyString())).thenReturn("Streamed response");

        ChatRequest request = new ChatRequest("Tell me about oyster farming", null, null, null, null, null, null, null);
        SseEmitter emitter = orchestrator.streamMessage(request);

        assertThat(emitter).isNotNull();
    }

    @Test
    void streamMessage_WithExistingSession_ShouldReturnEmitter() {
        ChatRequest request1 = new ChatRequest("Start", null, null, null, null, null, null, null);
        ChatResponse first = orchestrator.processMessage(request1);

        ChatRequest request2 = new ChatRequest("Continue", first.sessionId(), null, null, null, null, null, null);
        SseEmitter emitter = orchestrator.streamMessage(request2);

        assertThat(emitter).isNotNull();
    }

    @Test
    void getHistory_ShouldReturnSessionHistory() {
        ChatRequest request1 = new ChatRequest("First message", null, null, null, null, null, null, null);
        ChatResponse first = orchestrator.processMessage(request1);

        ChatRequest request2 = new ChatRequest("Second message", first.sessionId(), null, null, null, null, null, null);
        orchestrator.processMessage(request2);

        List<ChatResponse> history = orchestrator.getHistory(first.sessionId());
        assertThat(history).hasSize(2);
    }

    @Test
    void getHistory_AllSessions_ShouldReturnAll() {
        orchestrator.processMessage(new ChatRequest("Session 1 msg", null, null, null, null, null, null, null));
        orchestrator.processMessage(new ChatRequest("Session 2 msg", null, null, null, null, null, null, null));

        List<ChatResponse> allHistory = orchestrator.getHistory(null);
        assertThat(allHistory).hasSize(2);
    }
}
