package com.sporekart.grower.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KnowledgeRetrievalEngineTest {

    private KnowledgeRetrievalEngine engine;

    @BeforeEach
    void setUp() {
        engine = new KnowledgeRetrievalEngine();
    }

    @Test
    void search_ShouldReturnResultsWithCitations() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("oyster", 5);
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isLessThanOrEqualTo(5);
        KnowledgeRetrievalEngine.KnowledgeArticle first = results.get(0);
        assertThat(first.citation()).isNotBlank();
    }

    @Test
    void search_ForButton_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("button", 3);
        assertThat(results).isNotEmpty();
    }

    @Test
    void search_ForCompost_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("compost", 10);
        assertThat(results).isNotEmpty();
    }

    @Test
    void search_ForDisease_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("trichoderma", 5);
        assertThat(results).isNotEmpty();
    }

    @Test
    void search_ForEmptyQuery_ShouldReturnAll() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("", 100);
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isGreaterThan(30);
    }

    @Test
    void search_ForNonExistent_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("xyzzy_nonexistent", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void searchScientific_ShouldReturnOnlyScientific() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchScientific("mushroom", 10);
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "SCIENTIFIC".equals(a.category()));
    }

    @Test
    void searchScientific_WithSpecificTopic_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchScientific("nutrition", 5);
        assertThat(results).isNotEmpty();
    }

    @Test
    void searchScientific_ForNonExistent_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchScientific("nonexistent_topic", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void searchGovernmentDocuments_ShouldReturnOnlyGovernment() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchGovernmentDocuments("subsidy", 5);
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "GOVERNMENT".equals(a.category()));
    }

    @Test
    void searchGovernmentDocuments_ForExport_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchGovernmentDocuments("export", 5);
        assertThat(results).isNotEmpty();
    }

    @Test
    void searchGovernmentDocuments_ForNonExistent_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchGovernmentDocuments("nonexistent", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void searchSOPs_ShouldReturnOnlySOPs() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchSOPs("sterilization", 5);
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "SOP".equals(a.category()));
    }

    @Test
    void searchSOPs_ForInoculation_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchSOPs("inoculation", 5);
        assertThat(results).isNotEmpty();
    }

    @Test
    void searchSOPs_ForNonExistent_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchSOPs("nonexistent", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void searchFAQs_ShouldReturnOnlyFAQs() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchFAQs("temperature", 5);
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "FAQ".equals(a.category()));
    }

    @Test
    void searchFAQs_ForWater_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchFAQs("water", 3);
        assertThat(results).isNotEmpty();
    }

    @Test
    void searchFAQs_ForNonExistent_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.searchFAQs("nonexistent", 5);
        assertThat(results).isEmpty();
    }

    @Test
    void answerWithCitations_ShouldReturnAnswer() {
        Map<String, Object> result = engine.answerWithCitations("How to grow oyster mushrooms?");
        assertThat(result).isNotNull();
        assertThat(result).containsKey("answer");
        assertThat(result).containsKey("citations");
    }

    @Test
    void answerWithCitations_ShouldContainCitations() {
        Map<String, Object> result = engine.answerWithCitations("oyster substrate");
        assertThat(result).isNotNull();
        List<Map<String, Object>> citations = (List<Map<String, Object>>) result.get("citations");
        assertThat(citations).isNotEmpty();
        Map<String, Object> firstCitation = citations.get(0);
        assertThat(firstCitation).containsKey("citation");
        assertThat(firstCitation).containsKey("title");
    }

    @Test
    void answerWithCitations_ForUnknownQuestion_ShouldReturnDefault() {
        Map<String, Object> result = engine.answerWithCitations("xyznonexistent12345");
        assertThat(result).isNotNull();
        assertThat(result.get("answer")).toString().contains("could not find");
    }

    @Test
    void getCultivationKnowledge_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getCultivationKnowledge("spawn", "Oyster");
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "CULTIVATION".equals(a.category()));
    }

    @Test
    void getCultivationKnowledge_WithNullSpecies_ShouldReturnAll() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getCultivationKnowledge("substrate", null);
        assertThat(results).isNotEmpty();
    }

    @Test
    void getScientificReferences_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getScientificReferences("mushroom");
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "SCIENTIFIC".equals(a.category()));
    }

    @Test
    void getGovernmentGuidelines_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getGovernmentGuidelines("subsidy");
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "GOVERNMENT".equals(a.category()));
    }

    @Test
    void getCropHealthKnowledge_ShouldReturnResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getCropHealthKnowledge("green mold");
        assertThat(results).isNotEmpty();
    }

    @Test
    void getCropHealthKnowledge_ForUnknownSymptom_ShouldReturnEmpty() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getCropHealthKnowledge("nonexistent_symptom");
        assertThat(results).isEmpty();
    }

    @Test
    void getAllFAQs_ShouldReturnAllFAQs() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.getAllFAQs();
        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(a -> "FAQ".equals(a.category()));
    }

    @Test
    void answerWithCitations_CitationsShouldIncludeCategory() {
        Map<String, Object> result = engine.answerWithCitations("compost");
        List<Map<String, Object>> citations = (List<Map<String, Object>>) result.get("citations");
        assertThat(citations).isNotEmpty();
        assertThat(citations.get(0)).containsKey("category");
    }

    @Test
    void search_ShouldLimitResults() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results2 = engine.search("mushroom", 2);
        assertThat(results2.size()).isLessThanOrEqualTo(2);
    }

    @Test
    void search_WithQueryInTags_ShouldMatch() {
        List<KnowledgeRetrievalEngine.KnowledgeArticle> results = engine.search("spawn", 5);
        assertThat(results).isNotEmpty();
    }
}
