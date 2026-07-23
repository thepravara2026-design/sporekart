package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.MarketIntelligence;
import com.sporekart.executive.copilot.domain.MarketIntelligence.CompetitorInfo;
import com.sporekart.executive.copilot.dto.MarketRequest;
import com.sporekart.executive.copilot.dto.MarketResponse;
import com.sporekart.executive.copilot.dto.MarketResponse.CompetitorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarketIntelligenceEngine {

    private static final Logger log = LoggerFactory.getLogger(MarketIntelligenceEngine.class);

    public MarketResponse analyzeMarket(MarketRequest request) {
        log.info("Analyzing market segment: {} region: {}", request.segment(), request.region());
        var segment = request.segment() != null ? request.segment() : "mushroom_cultivation";
        var competitors = List.of(
            new CompetitorItem("GreenMush Solutions", "22%", "Strong distribution network", "Limited product range"),
            new CompetitorItem("FungiFarm Technologies", "18%", "Advanced R&D capabilities", "Premium pricing"),
            new CompetitorItem("MushroomIndia Pvt Ltd", "15%", "Wide geographic reach", "Inconsistent quality"),
            new CompetitorItem("OrganicSpores Co", "10%", "Organic certification", "Small scale operations")
        );
        var trends = List.of(
            "Growing demand for organic mushroom spawn (25% YoY)",
            "Rise of home cultivation kits post-pandemic",
            "Increasing commercial mushroom farming in tier-2 cities",
            "Technology adoption in mushroom farming automation"
        );
        var opportunities = List.of(
            "Untapped market in Eastern India",
            "B2B training and consultancy services",
            "Premium exotic mushroom varieties",
            "Subscription model for regular customers"
        );
        return new MarketResponse(segment, 500000000.0, 18.5, competitors, trends, opportunities);
    }

    public MarketIntelligence getDetailedIntelligence(String segment) {
        log.debug("Getting detailed market intelligence for segment: {}", segment);
        return new MarketIntelligence(segment, 500000000.0, 18.5,
            List.of(
                new CompetitorInfo("GreenMush Solutions", "22%", "Distribution", "Product range"),
                new CompetitorInfo("FungiFarm Technologies", "18%", "R&D", "Pricing")
            ),
            List.of("Organic demand growing", "Home kits trending"),
            List.of("East India expansion", "B2B training"));
    }

    public Map<String, Object> benchmarkCompetitors(String metric) {
        log.debug("Benchmarking competitors on metric: {}", metric);
        var benchmark = new LinkedHashMap<String, Object>();
        benchmark.put("metric", metric);
        benchmark.put("sporekart", Map.of("value", 85.0, "rank", 2));
        benchmark.put("industryAverage", Map.of("value", 72.0, "rank", "-"));
        benchmark.put("topPerformer", Map.of("name", "GreenMush Solutions", "value", 90.0));
        benchmark.put("gapToClose", 5.0);
        benchmark.put("recommendations", List.of("Invest in distribution network", "Improve product range breadth"));
        return benchmark;
    }

    public List<String> getIndustryTrends(String sector) {
        log.debug("Getting industry trends for sector: {}", sector);
        return List.of(
            "Precision fermentation gaining traction",
            "Vertical farming integration with mushroom cultivation",
            "Blockchain for supply chain transparency",
            "AI-powered crop monitoring for commercial farms",
            "Sustainable packaging becoming table stakes"
        );
    }
}
