package com.sporekart.trainer.copilot.controller;

import com.sporekart.trainer.copilot.domain.Assessment;
import com.sporekart.trainer.copilot.domain.Lesson;
import com.sporekart.trainer.copilot.domain.PracticalGuide;
import com.sporekart.trainer.copilot.domain.TrainingBatch;
import com.sporekart.trainer.copilot.dto.*;
import com.sporekart.trainer.copilot.service.TrainerCopilotOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainerCopilotControllerTest {

    @Mock
    private TrainerCopilotOrchestrator orchestrator;

    @InjectMocks
    private TrainerCopilotController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testChatEndpointReturns200() throws Exception {
        ChatRequest request = new ChatRequest("Hello", "session-1", null, null, null, null, null);
        ChatResponse response = new ChatResponse("session-1", "Hello back", List.of(), Map.of(), false);
        when(orchestrator.processMessage(any(ChatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/copilot/trainer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"Hello\",\"sessionId\":\"session-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello back"));
    }

    @Test
    void testStreamEndpointReturnsSseEmitter() throws Exception {
        MockMvc mockMvcStream = MockMvcBuilders.standaloneSetup(new TrainerCopilotController() {
            @Override
            public SseEmitter streamChat(com.sporekart.trainer.copilot.dto.ChatRequest request) {
                return new SseEmitter(180000L);
            }
        }).build();

        mockMvcStream.perform(post("/api/v1/copilot/trainer/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"Hello\",\"sessionId\":\"session-1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBatchesReturns200WithBatchList() throws Exception {
        BatchListResponse batchList = new BatchListResponse(List.of(), 0, 0, 0, 0);
        when(orchestrator.getBatches()).thenReturn(batchList);

        mockMvc.perform(get("/api/v1/copilot/trainer/batches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    void testGetBatchByIdReturns200() throws Exception {
        BatchAnalyticsResponse analytics = new BatchAnalyticsResponse(
                "B001", "Batch 1", "Mushroom Cultivation", "ACTIVE",
                30, 25, 85.0, 72.0, 5, 8,
                Map.of(), List.of(), List.of()
        );
        when(orchestrator.getBatchAnalytics(anyString())).thenReturn(analytics);

        mockMvc.perform(get("/api/v1/copilot/trainer/batches/B001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchId").value("B001"));
    }

    @Test
    void testCreateLessonReturns200WithLesson() throws Exception {
        Lesson lesson = new Lesson("L001", "M001", "Introduction", "Content", "LECTURE",
                60, List.of(), List.of(), List.of(), List.of(), "BEGINNER");
        LessonResponse lessonResponse = new LessonResponse(List.of(lesson), "60 min", "AI", Map.of());
        when(orchestrator.getLesson(any(LessonRequest.class))).thenReturn(lessonResponse);

        mockMvc.perform(post("/api/v1/copilot/trainer/lesson")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"moduleId\":\"M001\",\"topic\":\"Introduction\",\"lessonType\":\"LECTURE\",\"difficulty\":\"BEGINNER\",\"durationMinutes\":60,\"learningObjectives\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessons[0].lessonId").value("L001"));
    }

    @Test
    void testCreateAssessmentReturns200WithAssessment() throws Exception {
        Assessment assessment = new Assessment("A001", "M001", "Quiz 1", "MCQ", "EASY",
                List.of(), 100, 60, 30, "key");
        AssessmentResponse assessmentResponse = new AssessmentResponse(assessment, "key");
        when(orchestrator.getAssessment(any(AssessmentRequest.class))).thenReturn(assessmentResponse);

        mockMvc.perform(post("/api/v1/copilot/trainer/assessment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"moduleId\":\"M001\",\"title\":\"Quiz 1\",\"type\":\"MCQ\",\"difficulty\":\"EASY\",\"numberOfQuestions\":5,\"durationMinutes\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assessment.assessmentId").value("A001"));
    }

    @Test
    void testGetAnalyticsReturns200WithSummary() throws Exception {
        AnalyticsSummaryResponse summary = new AnalyticsSummaryResponse(
                100, 5, 3, 2, 82.0, 74.0, 45, 12, 8, Map.of()
        );
        when(orchestrator.getAnalytics()).thenReturn(summary);

        mockMvc.perform(get("/api/v1/copilot/trainer/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalStudents").value(100));
    }

    @Test
    void testEvaluateCertificationReturns200() throws Exception {
        CertificationResponse certResponse = new CertificationResponse(
                null, "ELIGIBLE", List.of("Proceed to exam")
        );
        when(orchestrator.evaluateCertification(any(CertificationRequest.class))).thenReturn(certResponse);

        mockMvc.perform(post("/api/v1/copilot/trainer/certification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"batchId\":\"B001\",\"studentId\":\"S001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligibility").value("ELIGIBLE"));
    }

    @Test
    void testGetCultivationGuideByCategoryReturns200() throws Exception {
        PracticalGuide guide = new PracticalGuide("G001", "Spawn Prep", "SPAWN_PREPARATION", "EASY",
                "Content", List.of(), List.of(), List.of(), List.of(), List.of());
        when(orchestrator.getCultivationGuide(anyString())).thenReturn(guide);

        mockMvc.perform(get("/api/v1/copilot/trainer/cultivation/SPAWN_PREPARATION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guideId").value("G001"));
    }

    @Test
    void testSearchKnowledgeReturns200() throws Exception {
        Map<String, Object> result = Map.of("results", List.of(Map.of("title", "SOP Sterilization")));
        when(orchestrator.searchKnowledge(anyString())).thenReturn(result);

        mockMvc.perform(get("/api/v1/copilot/trainer/knowledge?query=sterilization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title").value("SOP Sterilization"));
    }

    @Test
    void testHealthEndpointReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/trainer/health"))
                .andExpect(status().isOk());
    }
}
