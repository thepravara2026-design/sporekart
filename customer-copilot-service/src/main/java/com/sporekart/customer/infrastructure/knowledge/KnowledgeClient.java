package com.sporekart.customer.infrastructure.knowledge;

import com.sporekart.customer.copilot.domain.KnowledgeArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class KnowledgeClient {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeClient.class);

    private final List<KnowledgeArticle> knowledgeBase = new CopyOnWriteArrayList<>();

    public KnowledgeClient() {
        log.info("Initializing KnowledgeClient with simulated knowledge base");
        knowledgeBase.add(new KnowledgeArticle("KB-001", "Getting Started with Mushroom Cultivation",
            "A comprehensive beginner's guide to growing mushrooms at home.", buildContent("beginner"),
            "Cultivation", "SporeKart KB", 0.95, "/knowledge/getting-started"));
        knowledgeBase.add(new KnowledgeArticle("KB-002", "Oyster Mushroom Growing Guide",
            "Step-by-step instructions for growing Pleurotus ostreatus.", buildContent("oyster"),
            "Cultivation", "SporeKart KB", 0.92, "/knowledge/oyster-guide"));
        knowledgeBase.add(new KnowledgeArticle("KB-003", "Shiitake Log Cultivation",
            "How to grow shiitake mushrooms on hardwood logs.", buildContent("shiitake"),
            "Cultivation", "SporeKart KB", 0.88, "/knowledge/shiitake-log"));
        knowledgeBase.add(new KnowledgeArticle("KB-004", "Mushroom Nutrition Facts",
            "Nutritional benefits of incorporating mushrooms into your diet.", buildContent("nutrition"),
            "Nutrition", "SporeKart KB", 0.85, "/knowledge/nutrition"));
        knowledgeBase.add(new KnowledgeArticle("KB-005", "Seasonal Mushroom Growing Calendar",
            "What mushrooms to grow each season for optimal yields.", buildContent("seasonal"),
            "Seasonal", "SporeKart KB", 0.90, "/knowledge/seasonal-calendar"));
        knowledgeBase.add(new KnowledgeArticle("KB-006", "Common Pests and Diseases",
            "Identifying and treating common mushroom cultivation issues.", buildContent("pests"),
            "Cultivation", "SporeKart KB", 0.82, "/knowledge/pests-diseases"));
        knowledgeBase.add(new KnowledgeArticle("KB-007", "Mushroom Growing Equipment Guide",
            "Essential equipment for successful mushroom cultivation.", buildContent("equipment"),
            "Equipment", "SporeKart KB", 0.87, "/knowledge/equipment-guide"));
        knowledgeBase.add(new KnowledgeArticle("KB-008", "Lion's Mane Mushroom Benefits",
            "Cognitive and neurological benefits of Hericium erinaceus.", buildContent("lions-mane"),
            "Health", "SporeKart KB", 0.91, "/knowledge/lions-mane-benefits"));
        knowledgeBase.add(new KnowledgeArticle("KB-009", "Mushroom Substrate Recipes",
            "Different substrate formulations for various mushroom species.", buildContent("substrate"),
            "Cultivation", "SporeKart KB", 0.84, "/knowledge/substrate-recipes"));
        knowledgeBase.add(new KnowledgeArticle("KB-010", "Harvesting and Storage Best Practices",
            "When and how to harvest mushrooms and store them properly.", buildContent("harvesting"),
            "Cultivation", "SporeKart KB", 0.89, "/knowledge/harvesting-storage"));
        knowledgeBase.add(new KnowledgeArticle("KB-011", "Government Mushroom Farming Subsidies",
            "Available agricultural subsidies for mushroom farmers.", buildContent("subsidies"),
            "Government", "Ministry of Agriculture", 0.78, "/knowledge/government/subsidies"));
        knowledgeBase.add(new KnowledgeArticle("KB-012", "Organic Certification for Mushrooms",
            "Steps to obtain organic certification for mushroom cultivation.", buildContent("organic"),
            "Government", "Food Safety Authority", 0.76, "/knowledge/government/organic-cert"));
        knowledgeBase.add(new KnowledgeArticle("KB-013", "Mushroom Market Trends 2026",
            "Current market prices and demand trends for specialty mushrooms.", buildContent("market"),
            "Business", "SporeKart Insights", 0.83, "/knowledge/market-trends"));
        knowledgeBase.add(new KnowledgeArticle("KB-014", "Reishi Mushroom Cultivation",
            "Growing Ganoderma lucidum for medicinal use.", buildContent("reishi"),
            "Cultivation", "SporeKart KB", 0.86, "/knowledge/reishi-guide"));
        knowledgeBase.add(new KnowledgeArticle("KB-015", "Indoor Mushroom Farming Setup",
            "Setting up a home-based mushroom farm with minimal space.", buildContent("indoor"),
            "Cultivation", "SporeKart KB", 0.93, "/knowledge/indoor-farming"));
        knowledgeBase.add(new KnowledgeArticle("KB-016", "Mushroom Pairing and Cooking Tips",
            "Culinary guide to matching mushrooms with wines and dishes.", buildContent("cooking"),
            "Lifestyle", "SporeKart KB", 0.80, "/knowledge/cooking-tips"));
        log.info("KnowledgeClient initialized with {} articles", knowledgeBase.size());
    }

    private String buildContent(String topic) {
        return "Detailed content about " + topic
            + ". This article provides comprehensive information based on "
            + "expert knowledge and industry best practices. For more details, "
            + "please refer to the full article on our knowledge base.";
    }

    public List<KnowledgeArticle> searchKnowledge(String query, String category) {
        log.debug("KnowledgeClient.searchKnowledge called with query='{}', category='{}'", query, category);
        if (query == null && category == null) {
            return knowledgeBase;
        }
        return knowledgeBase.stream()
            .filter(a -> {
                boolean matchQuery = query == null || query.isBlank()
                    || a.title().toLowerCase().contains(query.toLowerCase())
                    || a.snippet().toLowerCase().contains(query.toLowerCase());
                boolean matchCategory = category == null || category.isBlank()
                    || a.category().equalsIgnoreCase(category);
                return matchQuery && matchCategory;
            })
            .toList();
    }

    public List<KnowledgeArticle> getFAQs(String category) {
        log.debug("KnowledgeClient.getFAQs called for category='{}'", category);
        return knowledgeBase.stream()
            .filter(a -> category == null || a.category().equalsIgnoreCase(category))
            .limit(5)
            .toList();
    }

    public Map<String, Object> getGrowingAdvice(String mushroomType) {
        log.debug("KnowledgeClient.getGrowingAdvice called for mushroomType='{}'", mushroomType);
        if (mushroomType == null || mushroomType.isBlank()) {
            mushroomType = "oyster";
        }
        return Map.of(
            "mushroomType", mushroomType,
            "difficulty", mushroomType.equalsIgnoreCase("oyster") ? "Easy" : "Intermediate",
            "temperature", "18-24°C",
            "humidity", "85-95%",
            "lighting", "Indirect sunlight",
            "growingCycleDays", mushroomType.equalsIgnoreCase("shiitake") ? 90 : 30,
            "commonIssues", List.of("Contamination", "Poor airflow", "Incorrect moisture"),
            "tips", List.of(
                "Maintain sterile conditions",
                "Monitor humidity levels daily",
                "Ensure proper air exchange"
            )
        );
    }

    public List<KnowledgeArticle> getSeasonalContent(String season) {
        log.debug("KnowledgeClient.getSeasonalContent called for season='{}'", season);
        return knowledgeBase.stream()
            .filter(a -> a.category().equalsIgnoreCase("Seasonal")
                || a.title().toLowerCase().contains(season != null ? season.toLowerCase() : ""))
            .toList();
    }
}
