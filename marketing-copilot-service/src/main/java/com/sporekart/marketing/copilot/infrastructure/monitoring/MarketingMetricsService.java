package com.sporekart.marketing.copilot.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MarketingMetricsService {

    private final MeterRegistry meterRegistry;

    private final Counter contentRequests;
    private final Counter campaignRequests;
    private final Counter seoRequests;
    private final Counter aeogeoRequests;
    private final Counter socialRequests;
    private final Counter emailRequests;
    private final Counter whatsappRequests;
    private final Counter analyticsRequests;
    private final Counter brandRequests;
    private final Counter growthRequests;
    private final Counter strategyRequests;
    private final Counter totalRequests;
    private final Counter errors;

    public MarketingMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.contentRequests = Counter.builder("marketing.copilot.content.requests")
            .description("Content generation requests").register(meterRegistry);
        this.campaignRequests = Counter.builder("marketing.copilot.campaign.requests")
            .description("Campaign planning requests").register(meterRegistry);
        this.seoRequests = Counter.builder("marketing.copilot.seo.requests")
            .description("SEO analysis requests").register(meterRegistry);
        this.aeogeoRequests = Counter.builder("marketing.copilot.aeogeo.requests")
            .description("AEO/GEO analysis requests").register(meterRegistry);
        this.socialRequests = Counter.builder("marketing.copilot.social.requests")
            .description("Social media post requests").register(meterRegistry);
        this.emailRequests = Counter.builder("marketing.copilot.email.requests")
            .description("Email generation requests").register(meterRegistry);
        this.whatsappRequests = Counter.builder("marketing.copilot.whatsapp.requests")
            .description("WhatsApp message requests").register(meterRegistry);
        this.analyticsRequests = Counter.builder("marketing.copilot.analytics.requests")
            .description("Analytics requests").register(meterRegistry);
        this.brandRequests = Counter.builder("marketing.copilot.brand.requests")
            .description("Brand check requests").register(meterRegistry);
        this.growthRequests = Counter.builder("marketing.copilot.growth.requests")
            .description("Growth recommendation requests").register(meterRegistry);
        this.strategyRequests = Counter.builder("marketing.copilot.strategy.requests")
            .description("Strategy brief requests").register(meterRegistry);
        this.totalRequests = Counter.builder("marketing.copilot.total.requests")
            .description("Total marketing copilot requests").register(meterRegistry);
        this.errors = Counter.builder("marketing.copilot.error.count")
            .description("Marketing copilot error count").register(meterRegistry);
    }

    public void recordContentRequest() {
        contentRequests.increment();
        totalRequests.increment();
    }

    public void recordCampaignRequest() {
        campaignRequests.increment();
        totalRequests.increment();
    }

    public void recordSEORequest() {
        seoRequests.increment();
        totalRequests.increment();
    }

    public void recordAEOGEORequest() {
        aeogeoRequests.increment();
        totalRequests.increment();
    }

    public void recordSocialRequest() {
        socialRequests.increment();
        totalRequests.increment();
    }

    public void recordEmailRequest() {
        emailRequests.increment();
        totalRequests.increment();
    }

    public void recordWhatsAppRequest() {
        whatsappRequests.increment();
        totalRequests.increment();
    }

    public void recordAnalyticsRequest() {
        analyticsRequests.increment();
        totalRequests.increment();
    }

    public void recordBrandRequest() {
        brandRequests.increment();
        totalRequests.increment();
    }

    public void recordGrowthRequest() {
        growthRequests.increment();
        totalRequests.increment();
    }

    public void recordStrategyRequest() {
        strategyRequests.increment();
        totalRequests.increment();
    }

    public void recordError() {
        errors.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTimer(Timer.Sample sample, String operation) {
        sample.stop(Timer.builder("marketing.copilot." + operation + ".duration")
            .description("Marketing copilot " + operation + " duration")
            .register(meterRegistry));
    }

    public Counter getTotalRequests() {
        return totalRequests;
    }

    public Counter getErrors() {
        return errors;
    }
}
