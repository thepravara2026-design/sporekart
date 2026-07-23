package com.sporekart.trainer.copilot.engine;

import com.sporekart.trainer.copilot.domain.Assessment;
import com.sporekart.trainer.copilot.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentEngineTest {

    private AssessmentEngine engine;

    @BeforeEach
    void setUp() {
        engine = new AssessmentEngine();
    }

    @Test
    void generateMcqAssessmentHasValidQuestions() {
        Assessment assessment = engine.generateAssessment("M001", "Mushroom Biology Quiz", "MCQ", "EASY", 5, 30);

        assertEquals("MCQ", assessment.type());
        assertEquals(5, assessment.questions().size());
        assertTrue(assessment.totalMarks() > 0);
    }

    @Test
    void mcqQuestionsHaveFourOptions() {
        Assessment assessment = engine.generateAssessment("M001", "MCQ Test", "MCQ", "EASY", 3, 15);

        for (Question q : assessment.questions()) {
            assertEquals(4, q.options().size(), "Each MCQ must have 4 options");
        }
    }

    @Test
    void mcqQuestionsHaveCorrectAnswer() {
        Assessment assessment = engine.generateAssessment("M001", "MCQ Test", "MCQ", "EASY", 3, 15);

        for (Question q : assessment.questions()) {
            assertNotNull(q.correctAnswer());
            assertTrue(q.options().contains(q.correctAnswer()));
        }
    }

    @Test
    void generateShortAnswerAssessmentReturnsValid() {
        Assessment assessment = engine.generateAssessment("M002", "Short Answer Quiz", "SHORT_ANSWER", "INTERMEDIATE", 5, 30);

        assertEquals("SHORT_ANSWER", assessment.type());
        assertEquals(5, assessment.questions().size());
    }

    @Test
    void generateScenarioAssessmentReturnsValid() {
        Assessment assessment = engine.generateAssessment("M003", "Scenario Test", "SCENARIO", "ADVANCED", 2, 45);

        assertEquals("SCENARIO", assessment.type());
        assertFalse(assessment.questions().isEmpty());
    }

    @Test
    void generateCertificationAssessmentIncludesAllModules() {
        Assessment assessment = engine.generateAssessment("CERT-001", "Final Certification", "CERTIFICATION", "ADVANCED", 20, 120);

        assertEquals("CERTIFICATION", assessment.type());
        assertTrue(assessment.questions().size() >= 10);
        assertTrue(assessment.totalMarks() >= assessment.passingMarks());
    }

    @Test
    void generateAssessmentHasAnswerKey() {
        Assessment assessment = engine.generateAssessment("M004", "Integrated Test", "MCQ", "MEDIUM", 4, 20);

        assertNotNull(assessment.answerKey());
        assertFalse(assessment.answerKey().isEmpty());
    }

    @Test
    void answerKeyMatchesCorrectAnswers() {
        Assessment assessment = engine.generateAssessment("M005", "Answer Key Test", "MCQ", "EASY", 2, 10);

        for (Question q : assessment.questions()) {
            assertTrue(assessment.answerKey().contains(q.correctAnswer()));
        }
    }

    @Test
    void evaluateAssessmentReturnsScore() {
        Assessment assessment = engine.generateAssessment("M006", "Evaluation Test", "MCQ", "EASY", 5, 20);
        Map<String, String> answers = Map.of();
        for (Question q : assessment.questions()) {
            answers = Map.of(q.questionId(), q.correctAnswer());
        }

        Map<String, Object> result = engine.evaluateAssessment(assessment, answers);

        assertNotNull(result.get("score"));
        assertNotNull(result.get("total"));
        assertNotNull(result.get("percentage"));
    }

    @Test
    void evaluateAssessmentWithWrongAnswers() {
        Assessment assessment = engine.generateAssessment("M007", "Wrong Answers", "MCQ", "EASY", 3, 15);
        Map<String, String> wrongAnswers = Map.of();
        for (Question q : assessment.questions()) {
            String wrong = q.options().stream().filter(o -> !o.equals(q.correctAnswer())).findFirst().orElse("");
            wrongAnswers = Map.of(q.questionId(), wrong);
        }

        Map<String, Object> result = engine.evaluateAssessment(assessment, wrongAnswers);

        assertEquals(0, result.get("score"));
    }

    @Test
    void shortAnswerQuestionsHaveNoOptions() {
        Assessment assessment = engine.generateAssessment("M008", "Short Answer Only", "SHORT_ANSWER", "BEGINNER", 3, 20);

        for (Question q : assessment.questions()) {
            assertTrue(q.options() == null || q.options().isEmpty());
        }
    }
}
