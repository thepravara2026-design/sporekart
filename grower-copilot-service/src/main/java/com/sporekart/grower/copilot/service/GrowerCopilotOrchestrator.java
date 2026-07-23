package com.sporekart.grower.copilot.service;

import com.sporekart.grower.copilot.domain.BusinessPlan;
import com.sporekart.grower.copilot.domain.CultivationStage;
import com.sporekart.grower.copilot.domain.DiseaseInfo;
import com.sporekart.grower.copilot.domain.GrowerProfile;
import com.sporekart.grower.copilot.domain.KnowledgeArticle;
import com.sporekart.grower.copilot.domain.PageContext;
import com.sporekart.grower.copilot.domain.SpawnRecommendation;
import com.sporekart.grower.copilot.domain.SubstrateRecommendation;
import com.sporekart.grower.copilot.domain.UserContext;
import com.sporekart.grower.copilot.domain.WeatherData;
import com.sporekart.grower.copilot.domain.YieldPrediction;
import com.sporekart.grower.copilot.dto.ChatResponse;
import com.sporekart.grower.copilot.dto.Suggestion;
import com.sporekart.grower.copilot.engine.CopilotEngine;
import com.sporekart.grower.copilot.engine.CropHealthAdvisorEngine;
import com.sporekart.grower.copilot.engine.CultivationAdvisorEngine;
import com.sporekart.grower.copilot.engine.DiseaseAdvisorEngine;
import com.sporekart.grower.copilot.engine.KnowledgeRetrievalEngine;
import com.sporekart.grower.copilot.engine.SpawnRecommendationEngine;
import com.sporekart.grower.copilot.engine.SubstrateAdvisorEngine;
import com.sporekart.grower.copilot.engine.WeatherIntegrationEngine;
import com.sporekart.grower.copilot.engine.YieldPredictionEngine;
import com.sporekart.grower.copilot.engine.BusinessPlanningEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GrowerCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(GrowerCopilotOrchestrator.class);

    private final CopilotEngine copilotEngine;
    private final CultivationAdvisorEngine cultivationAdvisorEngine;
    private final SpawnRecommendationEngine spawnRecommendationEngine;
    private final SubstrateAdvisorEngine substrateAdvisorEngine;
    private final DiseaseAdvisorEngine diseaseAdvisorEngine;
    private final YieldPredictionEngine yieldPredictionEngine;
    private final BusinessPlanningEngine businessPlanningEngine;
    private final WeatherIntegrationEngine weatherIntegrationEngine;
    private final KnowledgeRetrievalEngine knowledgeRetrievalEngine;
    private final CropHealthAdvisorEngine cropHealthAdvisorEngine;

    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public GrowerCopilotOrchestrator(
            CopilotEngine copilotEngine,
            CultivationAdvisorEngine cultivationAdvisorEngine,
            SpawnRecommendationEngine spawnRecommendationEngine,
            SubstrateAdvisorEngine substrateAdvisorEngine,
            DiseaseAdvisorEngine diseaseAdvisorEngine,
            YieldPredictionEngine yieldPredictionEngine,
            BusinessPlanningEngine businessPlanningEngine,
            WeatherIntegrationEngine weatherIntegrationEngine,
            KnowledgeRetrievalEngine knowledgeRetrievalEngine,
            CropHealthAdvisorEngine cropHealthAdvisorEngine) {
        this.copilotEngine = copilotEngine;
        this.cultivationAdvisorEngine = cultivationAdvisorEngine;
        this.spawnRecommendationEngine = spawnRecommendationEngine;
        this.substrateAdvisorEngine = substrateAdvisorEngine;
        this.diseaseAdvisorEngine = diseaseAdvisorEngine;
        this.yieldPredictionEngine = yieldPredictionEngine;
        this.businessPlanningEngine = businessPlanningEngine;
        this.weatherIntegrationEngine = weatherIntegrationEngine;
        this.knowledgeRetrievalEngine = knowledgeRetrievalEngine;
        this.cropHealthAdvisorEngine = cropHealthAdvisorEngine;
    }

    public String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userId);
        copilotEngine.createSession(sessionId);
        log.info("Created grower copilot session {} for user {}", sessionId, userId);
        return sessionId;
    }

    public void endSession(String sessionId) {
        sessions.remove(sessionId);
        copilotEngine.endSession(sessionId);
        log.info("Ended grower copilot session {}", sessionId);
    }

    public ChatResponse processMessage(String sessionId, String message, UserContext userContext, PageContext pageContext) {
        log.debug("Processing message for session {}: {}", sessionId, message);

        String intent = copilotEngine.detectIntent(sessionId, message);

        return switch (intent) {
            case "cultivation" -> {
                List<CultivationStage> stages = cultivationAdvisorEngine.getAdvice(userContext.growerId());
                yield return toChatResponse(sessionId, "Cultivation advice retrieved", stages);
            }
            case "spawn" -> {
                SpawnRecommendation spawn = spawnRecommendationEngine.recommend(userContext.growerId());
                yield return toChatResponse(sessionId, "Spawn recommendation generated", spawn);
            }
            case "substrate" -> {
                SubstrateRecommendation substrate = substrateAdvisorEngine.recommend(userContext.growerId());
                yield return toChatResponse(sessionId, "Substrate recommendation generated", substrate);
            }
            case "disease" -> {
                DiseaseInfo disease = diseaseAdvisorEngine.diagnose(userContext.growerId(), List.of());
                yield return toChatResponse(sessionId, "Disease diagnosis completed", disease);
            }
            case "yield" -> {
                YieldPrediction prediction = yieldPredictionEngine.predict(userContext.growerId());
                yield return toChatResponse(sessionId, "Yield prediction generated", prediction);
            }
            case "weather" -> {
                WeatherData weather = weatherIntegrationEngine.getCurrentWeather(
                    userContext.attributes().getOrDefault("location", "unknown").toString());
                yield return toChatResponse(sessionId, "Weather data retrieved", weather);
            }
            case "planning" -> {
                BusinessPlan plan = businessPlanningEngine.createPlan(userContext.growerId());
                yield return toChatResponse(sessionId, "Business plan created", plan);
            }
            case "knowledge" -> {
                List<KnowledgeArticle> articles = knowledgeRetrievalEngine.search(message);
                yield return toChatResponse(sessionId, "Knowledge search completed", articles);
            }
            case "health" -> {
                Map<String, Object> healthStatus = cropHealthAdvisorEngine.assess(userContext.growerId());
                yield return toChatResponse(sessionId, "Crop health assessment completed", healthStatus);
            }
            default -> copilotEngine.processMessage(sessionId, message);
        };
    }

    public ChatResponse processChat(String sessionId, String message) {
        return copilotEngine.processMessage(sessionId, message);
    }

    public List<CultivationStage> getCultivationAdvice(String growerId) {
        return cultivationAdvisorEngine.getAdvice(growerId);
    }

    public SpawnRecommendation recommendSpawn(String growerId) {
        return spawnRecommendationEngine.recommend(growerId);
    }

    public SubstrateRecommendation recommendSubstrate(String growerId) {
        return substrateAdvisorEngine.recommend(growerId);
    }

    public DiseaseInfo diagnoseDisease(String growerId, List<String> symptoms) {
        return diseaseAdvisorEngine.diagnose(growerId, symptoms);
    }

    public YieldPrediction predictYield(String growerId) {
        return yieldPredictionEngine.predict(growerId);
    }

    public WeatherData getWeather(String location) {
        return weatherIntegrationEngine.getCurrentWeather(location);
    }

    public BusinessPlan createBusinessPlan(String growerId) {
        return businessPlanningEngine.createPlan(growerId);
    }

    public List<KnowledgeArticle> searchKnowledge(String query) {
        return knowledgeRetrievalEngine.search(query);
    }

    public Map<String, Object> getCropHealth(String growerId) {
        return cropHealthAdvisorEngine.assess(growerId);
    }

    private ChatResponse toChatResponse(String sessionId, String message, Object data) {
        return new ChatResponse(
            sessionId,
            message,
            List.of(new Suggestion("learn_more", "Tell me more", "Get additional details")),
            Map.of("data", data),
            false
        );
    }
}
