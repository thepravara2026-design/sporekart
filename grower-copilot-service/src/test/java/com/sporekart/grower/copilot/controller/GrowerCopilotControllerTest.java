package com.sporekart.grower.copilot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.sporekart.grower.copilot.service.GrowerCopilotOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GrowerCopilotController.class)
class GrowerCopilotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GrowerCopilotOrchestrator orchestrator;

    @Test
    void chat_ShouldReturn200() throws Exception {
        ChatRequest request = new ChatRequest("What mushrooms grow in Punjab?", null, null, null, null, null, null, null);
        ChatResponse response = new ChatResponse("sess-1", "Oyster mushrooms grow well in Punjab...",
                List.of(new Suggestion("Learn more", "open", "Details")), Map.of(), false);
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("sess-1"))
                .andExpect(jsonPath("$.message").value("Oyster mushrooms grow well in Punjab..."));
    }

    @Test
    void chat_WithSessionId_ShouldReturn200() throws Exception {
        ChatRequest request = new ChatRequest("Tell me more", "sess-1", null, null, null, null, null, null);
        ChatResponse response = new ChatResponse("sess-1", "Here are more details...",
                List.of(), Map.of(), false);
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("sess-1"));
    }

    @Test
    void chat_WithPageContext_ShouldReturn200() throws Exception {
        ChatRequest request = new ChatRequest("Explain this", null, "/grower/dashboard", "Dashboard", "overview", null, null, null);
        ChatResponse response = new ChatResponse("sess-2", "Dashboard explanation...",
                List.of(), Map.of("page", "dashboard"), false);
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.context.page").value("dashboard"));
    }

    @Test
    void chat_WithEntityContext_ShouldReturn200() throws Exception {
        ChatRequest request = new ChatRequest("About this spawn", null, null, null, null, "spawn", "spawn-1", null);
        ChatResponse response = new ChatResponse("sess-3", "This spawn is Oyster...",
                List.of(), Map.of(), false);
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("sess-3"));
    }

    @Test
    void stream_ShouldReturnSseEmitter() throws Exception {
        SseEmitter emitter = new SseEmitter();
        when(orchestrator.streamMessage(any(ChatRequest.class))).thenReturn(emitter);

        ChatRequest request = new ChatRequest("Tell me about oyster", null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/copilot/grower/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted());
    }

    @Test
    void stream_WithSession_ShouldReturnSseEmitter() throws Exception {
        SseEmitter emitter = new SseEmitter();
        when(orchestrator.streamMessage(any(ChatRequest.class))).thenReturn(emitter);

        ChatRequest request = new ChatRequest("More details", "sess-1", null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/copilot/grower/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted());
    }

    @Test
    void recommend_ShouldReturn200() throws Exception {
        RecommendRequest request = new RecommendRequest("oyster", null, "spawn", "easy", "Punjab", "beginner");
        RecommendResponse response = new RecommendResponse("spawn", "Oyster Mushroom", "Recommended for beginners...",
                List.of(), Map.of("difficulty", "easy"));
        when(orchestrator.recommendSpawn(any(RecommendRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationType").value("spawn"))
                .andExpect(jsonPath("$.title").value("Oyster Mushroom"));
    }

    @Test
    void recommend_WithQueryOnly_ShouldReturn200() throws Exception {
        RecommendRequest request = new RecommendRequest("button mushroom", null, null, null, null, null);
        RecommendResponse response = new RecommendResponse("spawn", "Button Mushroom", "Agaricus bisporus...",
                List.of(), Map.of());
        when(orchestrator.recommendSpawn(any(RecommendRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Button Mushroom"));
    }

    @Test
    void recommend_WithCategorySubstrate_ShouldReturn200() throws Exception {
        RecommendRequest request = new RecommendRequest(null, null, "substrate", null, null, null);
        RecommendResponse response = new RecommendResponse("substrate", "Wheat Straw", "Standard substrate...",
                List.of(), Map.of());
        when(orchestrator.recommendSubstrate(any(RecommendRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationType").value("substrate"));
    }

    @Test
    void disease_WithSymptoms_ShouldReturn200() throws Exception {
        DiseaseRequest request = new DiseaseRequest(List.of("green sporulation", "foul odor"), "oyster", "fruiting", null, null);
        DiseaseResponse response = new DiseaseResponse("D001", "Green Mold", "Trichoderma spp.", "Fungal",
                List.of("green sporulation", "foul odor"), List.of("Contaminated spawn"), 0.85,
                "HIGH", "Remove infected substrate...", List.of("Use sterilized spawn"),
                List.of("Singh et al. 2021"), true);
        when(orchestrator.diagnoseDisease(any(DiseaseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diseaseName").value("Green Mold"))
                .andExpect(jsonPath("$.requiresEscalation").value(true));
    }

    @Test
    void disease_WithEnvironmentDescription_ShouldReturn200() throws Exception {
        DiseaseRequest request = new DiseaseRequest(List.of("brown spots"), "button", "fruiting", "High humidity 90%", null);
        DiseaseResponse response = new DiseaseResponse("D002", "Bacterial Blotch", "Pseudomonas tolaasii", "Bacterial",
                List.of("brown spots"), List.of("High humidity"), 0.75, "MODERATE",
                "Reduce humidity...", List.of("Avoid overhead watering"), List.of("Fletcher et al. 1989"), false);
        when(orchestrator.diagnoseDisease(any(DiseaseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diseaseName").value("Bacterial Blotch"));
    }

    @Test
    void disease_WithEmptySymptoms_ShouldReturn400() throws Exception {
        DiseaseRequest request = new DiseaseRequest(List.of(), "oyster", "fruiting", null, null);

        mockMvc.perform(post("/api/v1/copilot/grower/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void yield_ShouldReturn200() throws Exception {
        YieldRequest request = new YieldRequest("Oyster", "Wheat Straw", 100.0, 200, "Punjab", Map.of());
        YieldResponse response = new YieldResponse("pred-1", 500.0, 85.0, 250000.0, 150000.0, 40.0, 15.0,
                "2026-08-01 to 2026-09-15", "Maintain humidity at 80%", 90);
        when(orchestrator.predictYield(any(YieldRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/yield")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedYieldKg").value(500.0))
                .andExpect(jsonPath("$.confidenceLevel").value(90));
    }

    @Test
    void yield_WithButtonMushroom_ShouldReturn200() throws Exception {
        YieldRequest request = new YieldRequest("Button", "Compost", 200.0, 400, "Himachal Pradesh", Map.of("compost_type", "phase_II"));
        YieldResponse response = new YieldResponse("pred-2", 300.0, 75.0, 450000.0, 300000.0, 33.0, 25.0,
                "2026-10-01 to 2026-12-15", "Monitor compost temperature", 85);
        when(orchestrator.predictYield(any(YieldRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/yield")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.predictionId").value("pred-2"));
    }

    @Test
    void yield_WithAdditionalParameters_ShouldReturn200() throws Exception {
        YieldRequest request = new YieldRequest("Shiitake", "Sawdust", 50.0, 100, "Karnataka",
                Map.of("log_diameter", "10cm", "species", "Lentinula edodes"));
        YieldResponse response = new YieldResponse("pred-3", 150.0, 70.0, 180000.0, 120000.0, 33.0, 20.0,
                "2027-01-15 to 2027-03-01", "Soak logs for 24 hours before fruiting", 80);
        when(orchestrator.predictYield(any(YieldRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/yield")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.harvestWindow").value("2027-01-15 to 2027-03-01"));
    }

    @Test
    void weather_ShouldReturn200() throws Exception {
        WeatherRequest request = new WeatherRequest("Punjab", 5);
        WeatherResponse response = new WeatherResponse("Punjab", null, List.of(), "LOW", Map.of());
        when(orchestrator.getWeather(any(WeatherRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Punjab"));
    }

    @Test
    void weather_WithForecast_ShouldReturn200() throws Exception {
        WeatherRequest request = new WeatherRequest("Karnataka", 3);
        WeatherResponse response = new WeatherResponse("Karnataka", null, List.of(), "MODERATE", Map.of());
        when(orchestrator.getWeather(any(WeatherRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.climateRisk").value("MODERATE"));
    }

    @Test
    void weather_WithInvalidForecastDays_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/copilot/grower/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new WeatherRequest("Punjab", -1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void planning_ShouldReturn200() throws Exception {
        PlanningRequest request = new PlanningRequest("Oyster", 500.0, 200000.0, "beginner", "Punjab");
        PlanningResponse response = new PlanningResponse(null, List.of("Month 1: Setup", "Month 2: Spawning"),
                List.of("Market price fluctuation"), "Business plan for Oyster mushroom in Punjab");
        when(orchestrator.createBusinessPlan(any(PlanningRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/planning")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").value("Business plan for Oyster mushroom in Punjab"))
                .andExpect(jsonPath("$.milestones[0]").value("Month 1: Setup"));
    }

    @Test
    void planning_WithButtonMushroom_ShouldReturn200() throws Exception {
        PlanningRequest request = new PlanningRequest("Button", 1000.0, 500000.0, "intermediate", "Himachal Pradesh");
        PlanningResponse response = new PlanningResponse(null, List.of(), List.of(), "Button mushroom plan");
        when(orchestrator.createBusinessPlan(any(PlanningRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/planning")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").value("Button mushroom plan"));
    }

    @Test
    void history_ShouldReturn200() throws Exception {
        List<ChatResponse> history = List.of(
                new ChatResponse("sess-1", "Hello", List.of(), Map.of(), false),
                new ChatResponse("sess-1", "How to grow oyster?", List.of(), Map.of(), false));
        when(orchestrator.getHistory("sess-1")).thenReturn(history);

        mockMvc.perform(get("/api/v1/copilot/grower/history")
                        .param("sessionId", "sess-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void history_EmptySession_ShouldReturn200() throws Exception {
        when(orchestrator.getHistory("sess-empty")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/copilot/grower/history")
                        .param("sessionId", "sess-empty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void history_WithoutSessionId_ShouldReturn200() throws Exception {
        List<ChatResponse> allHistory = List.of(
                new ChatResponse("sess-1", "Message 1", List.of(), Map.of(), false));
        when(orchestrator.getHistory(null)).thenReturn(allHistory);

        mockMvc.perform(get("/api/v1/copilot/grower/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void health_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/grower/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("UP"));
    }

    @Test
    void chat_WithEmptyMessage_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/copilot/grower/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChatRequest("", null, null, null, null, null, null, null))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void disease_WithImages_ShouldReturn200() throws Exception {
        DiseaseRequest request = new DiseaseRequest(List.of("green spots"), "oyster", "spawn run", "Wet conditions",
                List.of("image1.jpg", "image2.jpg"));
        DiseaseResponse response = new DiseaseResponse("D001", "Green Mold", "Trichoderma spp.", "Fungal",
                List.of("green spots"), List.of("Contaminated spawn"), 0.9, "HIGH",
                "Remove infected substrate...", List.of("Use sterilized spawn"),
                List.of("Singh et al. 2021"), true);
        when(orchestrator.diagnoseDisease(any(DiseaseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/grower/disease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.probabilityScore").value(0.9));
    }
}
