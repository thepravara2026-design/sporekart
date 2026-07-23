package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.dto.NaturalLanguageQueryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NaturalLanguageAnalyticsEngineTest {

    @InjectMocks
    private NaturalLanguageAnalyticsEngine engine;

    @Test
    void answerQueryWithRevenueQuestionReturnsRevenueData() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "show revenue of spawn products in June", true, true);
        assertNotNull(response);
        assertEquals("revenue_query", response.intent());
        assertNotNull(response.data());
        assertTrue(response.data().containsKey("revenue"));
    }

    @Test
    void answerQueryWithComparisonReturnsComparisonData() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "compare this month with last month", true, true);
        assertNotNull(response);
        assertEquals("comparison", response.intent());
        assertTrue(response.explanation().toLowerCase().contains("comparison")
                || response.explanation().toLowerCase().contains("vs")
                || response.explanation().toLowerCase().contains("difference"));
    }

    @Test
    void answerQueryWithMarginQuestionReturnsMarginData() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "what is our gross margin", true, true);
        assertNotNull(response);
        assertEquals("margin_query", response.intent());
        assertNotNull(response.data());
    }

    @Test
    void answerQueryWithPredictionReturnsForecastData() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "predict next month sales", true, true);
        assertNotNull(response);
        assertEquals("forecast", response.intent());
    }

    @Test
    void answerQueryReturnsVisualizationWhenRequested() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "show revenue of spawn products in June", true, false);
        assertNotNull(response);
        assertNotNull(response.visualization());
    }

    @Test
    void answerQueryReturnsInsights() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "show revenue of spawn products in June", false, true);
        assertNotNull(response.insights());
        assertFalse(response.insights().isEmpty());
    }

    @Test
    void generateFollowUpQuestionsReturnsQuestions() {
        String followUp = engine.generateFollowUpQuestions(
                "show revenue of spawn products in June");
        assertNotNull(followUp);
        assertFalse(followUp.isBlank());
    }

    @Test
    void answerQueryWithNoVisualizationReturnsNullViz() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "what is our gross margin", false, true);
        assertNull(response.visualization());
    }

    @Test
    void answerQueryIncludesSuggestedFollowUp() {
        NaturalLanguageQueryResponse response = engine.answerQuery(
                "compare this month with last month", true, true);
        assertNotNull(response.suggestedFollowUp());
        assertFalse(response.suggestedFollowUp().isBlank());
    }
}
