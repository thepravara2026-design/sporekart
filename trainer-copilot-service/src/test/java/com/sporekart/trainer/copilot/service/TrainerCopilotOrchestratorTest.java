package com.sporekart.trainer.copilot.service;

import com.sporekart.trainer.copilot.domain.Assessment;
import com.sporekart.trainer.copilot.domain.Certification;
import com.sporekart.trainer.copilot.domain.Lesson;
import com.sporekart.trainer.copilot.domain.PracticalGuide;
import com.sporekart.trainer.copilot.domain.StudentProgress;
import com.sporekart.trainer.copilot.domain.TrainingBatch;
import com.sporekart.trainer.copilot.dto.*;
import com.sporekart.trainer.copilot.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerCopilotOrchestratorTest {

    @Mock
    private TrainingPlannerEngine trainingPlannerEngine;

    @Mock
    private LessonGeneratorEngine lessonGeneratorEngine;

    @Mock
    private AssessmentEngine assessmentEngine;

    @Mock
    private StudentAnalyticsEngine studentAnalyticsEngine;

    @Mock
    private CertificationEngine certificationEngine;

    @Mock
    private PracticalCultivationEngine practicalCultivationEngine;

    @Mock
    private KnowledgeRetrievalEngine knowledgeRetrievalEngine;

    @Mock
    private BatchManagementEngine batchManagementEngine;

    @InjectMocks
    private TrainerCopilotOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
    }

    @Test
    void processMessageReturnsChatResponse() {
        ChatRequest request = new ChatRequest("Hello", "session-1", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request);

        assertNotNull(response);
        assertNotNull(response.message());
    }

    @Test
    void createSessionReturnsSessionId() {
        String sessionId = orchestrator.createSession();

        assertNotNull(sessionId);
        assertFalse(sessionId.isEmpty());
    }

    @Test
    void endSessionReturnsTrue() {
        String sessionId = orchestrator.createSession();
        boolean result = orchestrator.endSession(sessionId);

        assertTrue(result);
    }

    @Test
    void getLessonDelegatesToLessonGenerator() {
        Lesson lesson = new Lesson("L001", "M001", "Introduction", "Content", "LECTURE",
                60, List.of(), List.of(), List.of(), List.of(), "BEGINNER");
        LessonResponse expected = new LessonResponse(List.of(lesson), "60 min", "AI", Map.of());
        when(lessonGeneratorEngine.generateLesson(anyString(), anyString(), anyString(), anyString(), anyInt(), anyList()))
                .thenReturn(lesson);

        LessonRequest request = new LessonRequest("M001", "Introduction", "LECTURE", "BEGINNER", 60, List.of());
        LessonResponse actual = orchestrator.getLesson(request);

        assertNotNull(actual);
        assertEquals("L001", actual.lessons().get(0).lessonId());
    }

    @Test
    void getAssessmentDelegatesToAssessmentEngine() {
        Assessment assessment = new Assessment("A001", "M001", "Quiz", "MCQ", "EASY",
                List.of(), 100, 60, 30, "key");
        when(assessmentEngine.generateAssessment(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(assessment);

        AssessmentRequest request = new AssessmentRequest("M001", "Quiz", "MCQ", "EASY", 5, 30);
        AssessmentResponse actual = orchestrator.getAssessment(request);

        assertNotNull(actual);
        assertEquals("A001", actual.assessment().assessmentId());
    }

    @Test
    void getBatchesReturnsBatchList() {
        List<TrainingBatch> batches = List.of(
                new TrainingBatch("B001", "Batch 1", "Mushroom", "2026-01-01", "2026-03-01",
                        "ACTIVE", 30, 25, "Trainer A", "Lab 1", List.of("M001"), Map.of()),
                new TrainingBatch("B002", "Batch 2", "Mushroom", "2026-02-01", "2026-04-01",
                        "ACTIVE", 25, 20, "Trainer B", "Lab 2", List.of("M002"), Map.of())
        );
        when(batchManagementEngine.getAllBatches()).thenReturn(batches);

        BatchListResponse response = orchestrator.getBatches();

        assertNotNull(response);
        assertEquals(2, response.totalCount());
    }

    @Test
    void getBatchAnalyticsReturnsAnalytics() {
        when(studentAnalyticsEngine.getAttendanceTrends(anyString())).thenReturn(Map.of("batchId", "B001", "trends", List.of()));
        when(studentAnalyticsEngine.getPerformanceTrends(anyString())).thenReturn(Map.of("batchId", "B001", "averageScores", List.of()));

        BatchAnalyticsResponse response = orchestrator.getBatchAnalytics("B001");

        assertNotNull(response);
        assertEquals("B001", response.batchId());
    }

    @Test
    void getAnalyticsReturnsSummary() {
        AnalyticsSummaryResponse response = orchestrator.getAnalytics();

        assertNotNull(response);
        assertTrue(response.totalStudents() >= 0);
    }

    @Test
    void evaluateCertificationReturnsCertificationResponse() {
        when(certificationEngine.evaluateEligibility(anyString(), anyString())).thenReturn(Map.of("eligible", true, "criteria", List.of()));
        when(certificationEngine.calculateOverallScore(anyString(), anyString())).thenReturn(Map.of("overallScore", 85.0));
        when(certificationEngine.generateRecommendation(anyString(), anyString(), anyDouble())).thenReturn("PASS");
        when(certificationEngine.generateCertificate(anyString(), anyString(), anyString(), anyString(), anyString(), anyDouble(), anyDouble(), anyDouble(), anyString()))
                .thenReturn(new Certification("C001", "S001", "John", "B001", "Mushroom", 85.0, 80.0, 90.0, 85.0, "PASS", "ISSUED", List.of("Good job"), "2026-07-23", "2027-07-23"));

        CertificationRequest request = new CertificationRequest("B001", "S001");
        CertificationResponse response = orchestrator.evaluateCertification(request);

        assertNotNull(response);
        assertNotNull(response.eligibility());
    }

    @Test
    void getCultivationGuideReturnsGuide() {
        PracticalGuide guide = new PracticalGuide("G001", "Spawn Prep", "SPAWN_PREPARATION", "EASY",
                "Content", List.of("Step 1", "Step 2"), List.of("Materials"), List.of(), List.of(), List.of());
        when(practicalCultivationEngine.getCultivationGuide(anyString())).thenReturn(guide);

        PracticalGuide result = orchestrator.getCultivationGuide("SPAWN_PREPARATION");

        assertNotNull(result);
        assertEquals("SPAWN_PREPARATION", result.category());
    }

    @Test
    void searchKnowledgeReturnsResults() {
        Map<String, Object> expected = Map.of("query", "sterilization", "results", List.of(Map.of("title", "SOP Sterilization", "citation", "Manual v2.3")));
        when(knowledgeRetrievalEngine.searchKnowledge(anyString())).thenReturn(expected);

        Map<String, Object> result = orchestrator.searchKnowledge("sterilization");

        assertNotNull(result);
        assertEquals("sterilization", result.get("query"));
    }
}
