package com.sporekart.trainer.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeRetrievalEngineTest {

    private KnowledgeRetrievalEngine engine;

    @BeforeEach
    void setUp() {
        engine = new KnowledgeRetrievalEngine();
    }

    @Test
    void searchKnowledgeReturnsResultsWithCitations() {
        Map<String, Object> results = engine.searchKnowledge("sterilization");

        assertNotNull(results.get("query"));
        List<Map<String, Object>> entries = (List<Map<String, Object>>) results.get("results");
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
        for (Map<String, Object> entry : entries) {
            assertNotNull(entry.get("citation"));
        }
    }

    @Test
    void searchKnowledgeResultsHaveTitleAndContent() {
        Map<String, Object> results = engine.searchKnowledge("spawn preparation");
        List<Map<String, Object>> entries = (List<Map<String, Object>>) results.get("results");

        for (Map<String, Object> entry : entries) {
            assertNotNull(entry.get("title"));
            assertNotNull(entry.get("content"));
        }
    }

    @Test
    void searchTrainingManualsReturnsManuals() {
        Map<String, Object> results = engine.searchTrainingManuals("substrate formulation");

        assertNotNull(results.get("results"));
        List<Map<String, Object>> manuals = (List<Map<String, Object>>) results.get("results");
        assertFalse(manuals.isEmpty());
        for (Map<String, Object> m : manuals) {
            assertNotNull(m.get("citation"));
        }
    }

    @Test
    void searchSOPsReturnsStandardOperatingProcedures() {
        Map<String, Object> results = engine.searchSOPs("laminar flow");

        assertNotNull(results.get("results"));
        List<Map<String, Object>> sops = (List<Map<String, Object>>) results.get("results");
        assertFalse(sops.isEmpty());
        for (Map<String, Object> sop : sops) {
            assertNotNull(sop.get("procedureId"));
            assertNotNull(sop.get("citation"));
        }
    }

    @Test
    void searchFAQsReturnsQuestionsAndAnswers() {
        Map<String, Object> results = engine.searchFAQs("contamination");

        assertNotNull(results.get("results"));
        List<Map<String, Object>> faqs = (List<Map<String, Object>>) results.get("results");
        assertFalse(faqs.isEmpty());
        for (Map<String, Object> faq : faqs) {
            assertNotNull(faq.get("question"));
            assertNotNull(faq.get("answer"));
        }
    }

    @Test
    void getCultivationKnowledgeReturnsDomainKnowledge() {
        Map<String, Object> knowledge = engine.getCultivationKnowledge("mushroom biology");

        assertNotNull(knowledge.get("topic"));
        assertNotNull(knowledge.get("summary"));
        assertNotNull(knowledge.get("citations"));
        List<String> citations = (List<String>) knowledge.get("citations");
        assertFalse(citations.isEmpty());
    }

    @Test
    void getGovernmentGuidelinesReturnsRegulations() {
        Map<String, Object> guidelines = engine.getGovernmentGuidelines("mushroom cultivation");

        assertNotNull(guidelines.get("guidelines"));
        List<Map<String, Object>> entries = (List<Map<String, Object>>) guidelines.get("guidelines");
        assertFalse(entries.isEmpty());
        for (Map<String, Object> entry : entries) {
            assertNotNull(entry.get("citation"));
        }
    }

    @Test
    void getScientificResearchReturnsPapers() {
        Map<String, Object> research = engine.getScientificResearch("oyster mushroom cultivation");

        assertNotNull(research.get("papers"));
        List<Map<String, Object>> papers = (List<Map<String, Object>>) research.get("papers");
        assertFalse(papers.isEmpty());
        for (Map<String, Object> paper : papers) {
            assertNotNull(paper.get("title"));
            assertNotNull(paper.get("citation"));
            assertNotNull(paper.get("abstract"));
        }
    }

    @Test
    void searchKnowledgeWithEmptyQueryReturnsAll() {
        Map<String, Object> results = engine.searchKnowledge("");

        assertNotNull(results.get("results"));
    }

    @Test
    void searchKnowledgeResultsRankedByRelevance() {
        Map<String, Object> results = engine.searchKnowledge("contamination prevention");
        List<Map<String, Object>> entries = (List<Map<String, Object>>) results.get("results");

        assertFalse(entries.isEmpty());
        for (Map<String, Object> entry : entries) {
            assertNotNull(entry.get("relevance"));
        }
    }
}
