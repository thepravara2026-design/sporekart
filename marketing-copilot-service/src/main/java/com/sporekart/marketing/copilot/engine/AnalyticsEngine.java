package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.MarketingAnalytics;
import com.sporekart.marketing.copilot.dto.AnalyticsRequest;
import com.sporekart.marketing.copilot.dto.AnalyticsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AnalyticsEngine {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEngine.class);

    public AnalyticsResponse analyzeCampaign(AnalyticsRequest request) {
        log.info("Analyzing campaign: {}", request.campaignId());
        var metrics = new LinkedHashMap<String, Object>();
        metrics.put("totalImpressions", 150000);
        metrics.put("totalClicks", 7500);
        metrics.put("totalConversions", 375);
        metrics.put("totalSpend", 15000.0);
        metrics.put("totalRevenue", 112500.0);
        metrics.put("overallCTR", 5.0);
        metrics.put("overallConversionRate", 5.0);
        metrics.put("overallROAS", 7.5);
        metrics.put("cpc", 2.0);
        metrics.put("cpa", 40.0);

        var channelBreakdown = new LinkedHashMap<String, Double>();
        channelBreakdown.put("email", 3.5);
        channelBreakdown.put("social", 2.8);
        channelBreakdown.put("paid_ads", 1.2);
        channelBreakdown.put("organic", 0.0);

        var insights = List.of(
            "Campaign performing 25% above target ROAS",
            "Email channel has highest conversion rate at 5.2%",
            "Social media driving highest volume at 45% of total traffic",
            "Weekend campaigns show 30% higher engagement"
        );
        var recommendations = List.of(
            "Increase email marketing budget by 20%",
            "A/B test social media ad creatives",
            "Optimize landing pages for mobile traffic",
            "Create retargeting campaign for cart abandoners"
        );
        return new AnalyticsResponse(request.campaignId(), metrics, channelBreakdown, insights, recommendations);
    }

    public MarketingAnalytics getAggregatedAnalytics(List<String> campaignIds) {
        log.info("Getting aggregated analytics for {} campaigns", campaignIds != null ? campaignIds.size() : 0);
        var channelPerf = new LinkedHashMap<String, Double>();
        channelPerf.put("email", 4.2);
        channelPerf.put("social", 3.1);
        channelPerf.put("paid_ads", 1.8);
        channelPerf.put("organic", 0.5);

        var campaignPerf = new LinkedHashMap<String, Double>();
        if (campaignIds != null) {
            for (var id : campaignIds) {
                campaignPerf.put(id, Math.random() * 5.0 + 2.0);
            }
        }

        var campaignROAS = new LinkedHashMap<String, Double>();
        if (campaignIds != null) {
            for (var id : campaignIds) {
                campaignROAS.put(id, Math.random() * 10.0 + 3.0);
            }
        }

        var topContent = new LinkedHashMap<String, Integer>();
        topContent.put("blog_post_1", 5000);
        topContent.put("social_post_3", 4200);
        topContent.put("email_campaign_2", 3800);

        return new MarketingAnalytics(250000.0, 1250000.0, 5.0, 3.8, 4.2, 120.0, 480.0,
            channelPerf, campaignPerf, campaignROAS, topContent);
    }

    public double calculateROAS(double revenue, double spend) {
        if (spend <= 0) return 0.0;
        return Math.round((revenue / spend) * 100.0) / 100.0;
    }

    public double calculateCAC(double totalSpend, int newCustomers) {
        if (newCustomers <= 0) return 0.0;
        return Math.round((totalSpend / newCustomers) * 100.0) / 100.0;
    }

    public Map<String, Object> generateDashboardData(String campaignId) {
        log.debug("Generating dashboard data for campaign: {}", campaignId);
        var data = new LinkedHashMap<String, Object>();
        data.put("campaignId", campaignId);
        data.put("campaignName", "Q3 Campaign - " + campaignId.substring(0, 8));
        data.put("status", "ACTIVE");
        data.put("metrics", Map.of(
            "impressions", 150000,
            "clicks", 7500,
            "conversions", 375,
            "spend", 15000.0,
            "revenue", 112500.0
        ));
        data.put("roasTrend", List.of(3.5, 4.2, 5.8, 6.1, 7.5));
        return data;
    }
}
