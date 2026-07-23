package com.sporekart.trainer.copilot.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sporekart.copilot.CopilotEngine;
import com.sporekart.copilot.CopilotResponse;
import com.sporekart.copilot.CopilotSDK;
import com.sporekart.copilot.CopilotStatus;
import com.sporekart.copilot.CopilotType;
import com.sporekart.copilot.PageContext;
import com.sporekart.copilot.SessionId;
import com.sporekart.copilot.UserContext;
import com.sporekart.trainer.copilot.domain.PracticalGuide;
import com.sporekart.trainer.copilot.dto.AnalyticsSummaryResponse;
import com.sporekart.trainer.copilot.dto.AssessmentRequest;
import com.sporekart.trainer.copilot.dto.AssessmentResponse;
import com.sporekart.trainer.copilot.dto.BatchAnalyticsResponse;
import com.sporekart.trainer.copilot.dto.BatchListResponse;
import com.sporekart.trainer.copilot.dto.CertificationRequest;
import com.sporekart.trainer.copilot.dto.CertificationResponse;
import com.sporekart.trainer.copilot.dto.ChatResponse;
import com.sporekart.trainer.copilot.dto.LessonRequest;
import com.sporekart.trainer.copilot.dto.LessonResponse;
import com.sporekart.trainer.copilot.engine.AssessmentEngine;
import com.sporekart.trainer.copilot.engine.BatchManagementEngine;
import com.sporekart.trainer.copilot.engine.CertificationEngine;
import com.sporekart.trainer.copilot.engine.KnowledgeRetrievalEngine;
import com.sporekart.trainer.copilot.engine.LessonGeneratorEngine;
import com.sporekart.trainer.copilot.engine.PracticalCultivationEngine;
import com.sporekart.trainer.copilot.engine.StudentAnalyticsEngine;
import com.sporekart.trainer.copilot.engine.TrainingPlannerEngine;
import com.sporekart.trainer.copilot.infrastructure.knowledge.KnowledgeServiceClient.KnowledgeResult;

@Service
public class TrainerCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(TrainerCopilotOrchestrator.class);

    private final CopilotEngine copilotEngine;
    private final TrainingPlannerEngine trainingPlannerEngine;
    private final LessonGeneratorEngine lessonGeneratorEngine;
    private final AssessmentEngine assessmentEngine;
    private final StudentAnalyticsEngine studentAnalyticsEngine;
    private final CertificationEngine certificationEngine;
    private final PracticalCultivationEngine practicalCultivationEngine;
    private final BatchManagementEngine batchManagementEngine;
    private final KnowledgeRetrievalEngine knowledgeRetrievalEngine;

    private final Map<String, SessionId> sessions = new ConcurrentHashMap<>();

    public TrainerCopilotOrchestrator(
            TrainingPlannerEngine trainingPlannerEngine,
            LessonGeneratorEngine lessonGeneratorEngine,
            AssessmentEngine assessmentEngine,
            StudentAnalyticsEngine studentAnalyticsEngine,
            CertificationEngine certificationEngine,
            PracticalCultivationEngine practicalCultivationEngine,
            BatchManagementEngine batchManagementEngine,
            KnowledgeRetrievalEngine knowledgeRetrievalEngine) {
        this.copilotEngine = CopilotSDK.createDefaultEngine();
        this.trainingPlannerEngine = trainingPlannerEngine;
        this.lessonGeneratorEngine = lessonGeneratorEngine;
        this.assessmentEngine = assessmentEngine;
        this.studentAnalyticsEngine = studentAnalyticsEngine;
        this.certificationEngine = certificationEngine;
        this.practicalCultivationEngine = practicalCultivationEngine;
        this.batchManagementEngine = batchManagementEngine;
        this.knowledgeRetrievalEngine = knowledgeRetrievalEngine;
        log.info("TrainerCopilotOrchestrator initialized");
    }

    public ChatResponse processMessage(String sessionId, String message, UserContext userContext, PageContext pageContext) {
        log.info("Processing message for sessionId='{}': {}", sessionId, message);
        String intent = detectIntent(message);
        return switch (intent) {
            case "lesson" -> {
                log.info("Routing to LessonGeneratorEngine");
                yield new ChatResponse(sessionId, "Lesson generation request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "assessment" -> {
                log.info("Routing to AssessmentEngine");
                yield new ChatResponse(sessionId, "Assessment request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "training_plan" -> {
                log.info("Routing to TrainingPlannerEngine");
                yield new ChatResponse(sessionId, "Training plan request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "analytics" -> {
                log.info("Routing to StudentAnalyticsEngine");
                yield new ChatResponse(sessionId, "Analytics request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "certification" -> {
                log.info("Routing to CertificationEngine");
                yield new ChatResponse(sessionId, "Certification request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "cultivation" -> {
                log.info("Routing to PracticalCultivationEngine");
                yield new ChatResponse(sessionId, "Cultivation guide request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "batch" -> {
                log.info("Routing to BatchManagementEngine");
                yield new ChatResponse(sessionId, "Batch management request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            case "knowledge" -> {
                log.info("Routing to KnowledgeRetrievalEngine");
                yield new ChatResponse(sessionId, "Knowledge search request routed to engine", Collections.emptyList(), Map.of(), false);
            }
            default -> processChat(sessionId, message, userContext, pageContext);
        };
    }

    public ChatResponse processChat(String sessionId, String message, UserContext userContext, PageContext pageContext) {
        log.info("Processing general chat for sessionId='{}'", sessionId);
        SessionId sid = sessions.get(sessionId);
        if (sid == null) {
            log.warn("Session not found: {}, creating new session", sessionId);
            sid = createSession(userContext);
            sessions.put(sessionId, sid);
        }
        try {
            CopilotResponse response = copilotEngine.processMessage(sid, message, userContext, pageContext).get();
            log.info("Copilot response received for sessionId='{}'", sessionId);
            return new ChatResponse(
                sessionId,
                response.message(),
                response.suggestions() != null ? response.suggestions() : Collections.emptyList(),
                response.context() != null ? response.context() : Collections.emptyMap(),
                response.streaming()
            );
        } catch (Exception e) {
            log.error("Error processing chat for sessionId='{}'", sessionId, e);
            return new ChatResponse(sessionId, "I encountered an error processing your request. Please try again.", Collections.emptyList(), Map.of(), false);
        }
    }

    public String createSession(UserContext userContext) {
        SessionId sessionId = new SessionId();
        copilotEngine.startSession(sessionId, CopilotType.TRAINER, userContext);
        sessions.put(sessionId.toString(), sessionId);
        log.info("Session created: {}", sessionId);
        return sessionId.toString();
    }

    public void endSession(String sessionIdStr) {
        SessionId sid = sessions.remove(sessionIdStr);
        if (sid != null) {
            copilotEngine.endSession(sid);
            log.info("Session ended: {}", sessionIdStr);
        } else {
            log.warn("Session not found for endSession: {}", sessionIdStr);
        }
    }

    public List<Map<String, Object>> getConversationHistory() {
        log.info("getConversationHistory called - returning empty list");
        return Collections.emptyList();
    }

    public LessonResponse getLesson(LessonRequest request) {
        log.info("Delegating lesson generation to LessonGeneratorEngine: topic='{}'", request.topic());
        Map<String, Object> result = lessonGeneratorEngine.generateLesson(
            request.moduleId(), request.topic(), request.lessonType(),
            request.difficulty(), request.durationMinutes(), request.learningObjectives());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sections = (List<Map<String, Object>>) result.getOrDefault("sections", Collections.emptyList());
        return new LessonResponse(
            Collections.emptyList(),
            request.durationMinutes() + " min",
            "ai-generated",
            Map.of("sections", sections, "summary", result.getOrDefault("summary", ""), "keyTakeaways", result.getOrDefault("keyTakeaways", Collections.emptyList()))
        );
    }

    public AssessmentResponse getAssessment(AssessmentRequest request) {
        log.info("Delegating assessment generation to AssessmentEngine: title='{}'", request.title());
        Map<String, Object> result = assessmentEngine.generateAssessment(
            request.moduleId(), request.title(), request.type(),
            request.difficulty(), request.numberOfQuestions(), request.durationMinutes());
        return new AssessmentResponse(
            null,
            (String) result.getOrDefault("answerKey", "")
        );
    }

    public BatchListResponse getBatches() {
        log.info("Delegating batch listing to BatchManagementEngine");
        return batchManagementEngine.getAllBatches();
    }

    public BatchAnalyticsResponse getBatchAnalytics(String batchId) {
        log.info("Delegating batch analytics to StudentAnalyticsEngine and BatchManagementEngine for batchId='{}'", batchId);
        Map<String, Object> performanceTrends = studentAnalyticsEngine.getPerformanceTrends(batchId);
        Map<String, Object> attendanceTrends = studentAnalyticsEngine.getAttendanceTrends(batchId);
        Map<String, Object> batchInfo = batchManagementEngine.getBatchInfo(batchId);
        return new BatchAnalyticsResponse(
            batchId,
            (String) batchInfo.getOrDefault("batchName", ""),
            (String) batchInfo.getOrDefault("courseName", ""),
            (String) batchInfo.getOrDefault("status", ""),
            (int) batchInfo.getOrDefault("capacity", 0),
            (int) batchInfo.getOrDefault("enrolledCount", 0),
            (double) attendanceTrends.getOrDefault("overallAverage", 0.0),
            (double) performanceTrends.getOrDefault("overallAverage", 0.0),
            0,
            0,
            Collections.emptyMap(),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    public AnalyticsSummaryResponse getAnalytics() {
        log.info("Delegating analytics summary to StudentAnalyticsEngine");
        return studentAnalyticsEngine.getSummary();
    }

    public CertificationResponse evaluateCertification(CertificationRequest request) {
        log.info("Delegating certification evaluation to CertificationEngine: studentId='{}'", request.studentId());
        Map<String, Object> eligibility = certificationEngine.evaluateEligibility(request.studentId(), request.batchId());
        return new CertificationResponse(
            null,
            (String) eligibility.getOrDefault("recommendation", "Unknown"),
            Collections.emptyList()
        );
    }

    public PracticalGuide getCultivationGuide(String category) {
        log.info("Delegating cultivation guide to PracticalCultivationEngine: category='{}'", category);
        return practicalCultivationEngine.getGuide(category);
    }

    public List<KnowledgeResult> searchKnowledge(String query) {
        log.info("Delegating knowledge search to KnowledgeRetrievalEngine: query='{}'", query);
        return knowledgeRetrievalEngine.search(query);
    }

    private String detectIntent(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("lesson") || lower.contains("curriculum") || lower.contains("syllabus")) {
            return "lesson";
        }
        if (lower.contains("assessment") || lower.contains("quiz") || lower.contains("test") || lower.contains("exam")) {
            return "assessment";
        }
        if (lower.contains("plan") || lower.contains("schedule") || lower.contains("timetable") || lower.contains("roadmap")) {
            return "training_plan";
        }
        if (lower.contains("analytics") || lower.contains("report") || lower.contains("performance") || lower.contains("progress")) {
            return "analytics";
        }
        if (lower.contains("certification") || lower.contains("certificate") || lower.contains("eligible")) {
            return "certification";
        }
        if (lower.contains("cultivation") || lower.contains("grow") || lower.contains("guide") || lower.contains("practical")) {
            return "cultivation";
        }
        if (lower.contains("batch") || lower.contains("group") || lower.contains("cohort")) {
            return "batch";
        }
        if (lower.contains("knowledge") || lower.contains("search") || lower.contains("find") || lower.contains("how to")) {
            return "knowledge";
        }
        return "general";
    }
}
