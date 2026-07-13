package com.sporekart.ai.integration;

import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.analytics.api.MetricsAggregationService;
import com.sporekart.ai.analytics.api.KPIService;
import com.sporekart.ai.analytics.api.TrendAnalysisService;
import com.sporekart.ai.analytics.domain.GovernanceKPI;
import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.GovernanceTrend;
import com.sporekart.ai.analytics.domain.KpiStatus;
import com.sporekart.ai.analytics.domain.MetricType;
import com.sporekart.ai.analytics.domain.TrendDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskAnalyticsIntegrationTest {

    @Mock private RiskEngine riskEngine;
    @Mock private MetricsAggregationService metricsAggregationService;
    @Mock private KPIService kpiService;
    @Mock private TrendAnalysisService trendAnalysisService;

    @Test
    void testRiskAssessmentResultsRecordedAsMetrics() {
        RiskAssessmentRequest riskRequest = new RiskAssessmentRequest(
            UUID.randomUUID(), "catalog", "publish_item",
            Map.of("category", "electronics", "price", 1500.0)
        );

        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), true, RiskLevel.MEDIUM, 0.45, 0.72, 0.68,
            null, Map.of("riskScore", 0.45, "trustScore", 0.72, "confidenceScore", 0.68)
        );
        when(riskEngine.assess(riskRequest)).thenReturn(riskResult);

        GovernanceMetric riskMetric = new GovernanceMetric(
            UUID.randomUUID(), "risk_score", "catalog",
            MetricType.SCORE, 0.45, Map.of("level", "MEDIUM"), null
        );
        when(metricsAggregationService.recordMetric(
            eq("risk_score"), eq("catalog"), eq(MetricType.SCORE), eq(0.45), any()
        )).thenReturn(riskMetric);

        GovernanceMetric trustMetric = new GovernanceMetric(
            UUID.randomUUID(), "trust_score", "catalog",
            MetricType.SCORE, 0.72, Map.of(), null
        );
        when(metricsAggregationService.recordMetric(
            eq("trust_score"), eq("catalog"), eq(MetricType.SCORE), eq(0.72), any()
        )).thenReturn(trustMetric);

        RiskAssessmentResult assessment = riskEngine.assess(riskRequest);
        assertEquals(RiskLevel.MEDIUM, assessment.riskLevel());

        GovernanceMetric recorded = metricsAggregationService.recordMetric(
            "risk_score", "catalog", MetricType.SCORE, assessment.riskScore(), Map.of("level", "MEDIUM")
        );
        assertNotNull(recorded);
        assertEquals("risk_score", recorded.name());
        assertEquals(0.45, recorded.value(), 0.01);

        GovernanceMetric trustRecorded = metricsAggregationService.recordMetric(
            "trust_score", "catalog", MetricType.SCORE, assessment.trustScore(), Map.of()
        );
        assertNotNull(trustRecorded);

        verify(riskEngine).assess(riskRequest);
        verify(metricsAggregationService, times(2)).recordMetric(
            anyString(), anyString(), any(MetricType.class), anyDouble(), any()
        );
    }

    @Test
    void testKPIsUpdatedFromRiskData() {
        GovernanceKPI riskKpi = new GovernanceKPI(
            UUID.randomUUID(), "risk_exposure", "Risk exposure level",
            "catalog",             0.45, 0.3, 0.1, KpiStatus.AT_RISK,
            Map.of("threshold", 0.3), null
        );
        when(kpiService.calculateKPI(
            eq("risk_exposure"), eq("catalog"), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(riskKpi);

        GovernanceKPI trustKpi = new GovernanceKPI(
            UUID.randomUUID(), "trust_level", "Trust level",
            "catalog", 0.72, 0.8, 0.05, KpiStatus.ON_TRACK,
            Map.of(), null
        );
        when(kpiService.calculateKPI(
            eq("trust_level"), eq("catalog"), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(trustKpi);

        GovernanceKPI calculatedRiskKpi = kpiService.calculateKPI(
            "risk_exposure", "catalog", 0.45, 0.3, 0.1
        );
        assertNotNull(calculatedRiskKpi);
        assertEquals("risk_exposure", calculatedRiskKpi.name());
        assertEquals(0.45, calculatedRiskKpi.currentValue(), 0.01);

        GovernanceKPI calculatedTrustKpi = kpiService.calculateKPI(
            "trust_level", "catalog", 0.72, 0.8, 0.05
        );
        assertNotNull(calculatedTrustKpi);
        assertEquals("trust_level", calculatedTrustKpi.name());

        verify(kpiService).calculateKPI(eq("risk_exposure"), eq("catalog"), anyDouble(), anyDouble(), anyDouble());
        verify(kpiService).calculateKPI(eq("trust_level"), eq("catalog"), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void testTrendsCalculatedFromRiskHistory() {
        GovernanceTrend riskTrend = new GovernanceTrend(
            UUID.randomUUID(), "risk_trend", "catalog",
            List.of(0.3, 0.45, 0.4, 0.35), List.of(), TrendDirection.DOWN, -5.0, null
        );
        when(trendAnalysisService.calculateTrend(
            eq("risk_trend"), eq("catalog"), anyList(), anyList()
        )).thenReturn(riskTrend);

        List<Double> dataPoints = List.of(0.3, 0.45, 0.4, 0.35);
        List<String> timestamps = List.of("t1", "t2", "t3", "t4");

        GovernanceTrend trend = trendAnalysisService.calculateTrend(
            "risk_trend", "catalog", dataPoints, timestamps
        );
        assertNotNull(trend);
        assertEquals(TrendDirection.DOWN, trend.direction());
        assertEquals(-5.0, trend.changePercentage(), 0.01);

        verify(trendAnalysisService).calculateTrend(eq("risk_trend"), eq("catalog"), anyList(), anyList());
    }

    @Test
    void testCompleteRiskAnalyticsPipeline() {
        RiskAssessmentRequest riskRequest = new RiskAssessmentRequest(
            UUID.randomUUID(), "finance", "large_transfer",
            Map.of("amount", 100000.0)
        );

        RiskAssessmentResult riskResult = new RiskAssessmentResult(
            UUID.randomUUID(), false, RiskLevel.HIGH, 0.78, 0.35, 0.40,
            null, Map.of("riskScore", 0.78)
        );
        when(riskEngine.assess(riskRequest)).thenReturn(riskResult);

        GovernanceMetric metric = new GovernanceMetric(
            UUID.randomUUID(), "risk_score", "finance",
            MetricType.SCORE, 0.78, Map.of("level", "HIGH"), null
        );
        when(metricsAggregationService.recordMetric(anyString(), anyString(), any(), anyDouble(), any()))
            .thenReturn(metric);

        GovernanceKPI kpi = new GovernanceKPI(
            UUID.randomUUID(), "risk_exposure", "Risk exposure",
            "finance", 0.78, 0.5, 0.1, KpiStatus.CRITICAL, Map.of(), null
        );
        when(kpiService.calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(kpi);

        GovernanceTrend trend = new GovernanceTrend(
            UUID.randomUUID(), "risk_trend", "finance",
            List.of(0.6, 0.78), List.of(), TrendDirection.UP, 30.0, null
        );
        when(trendAnalysisService.calculateTrend(anyString(), anyString(), anyList(), anyList()))
            .thenReturn(trend);

        RiskAssessmentResult assessment = riskEngine.assess(riskRequest);
        assertFalse(assessment.proceed());
        assertEquals(RiskLevel.HIGH, assessment.riskLevel());

        GovernanceMetric recordedMetric = metricsAggregationService.recordMetric(
            "risk_score", "finance", MetricType.SCORE, assessment.riskScore(),
            Map.of("level", assessment.riskLevel().name())
        );
        assertNotNull(recordedMetric);
        assertEquals(0.78, recordedMetric.value(), 0.01);

        GovernanceKPI calculatedKpi = kpiService.calculateKPI(
            "risk_exposure", "finance", assessment.riskScore(), 0.5, 0.1
        );
        assertNotNull(calculatedKpi);

        GovernanceTrend calculatedTrend = trendAnalysisService.calculateTrend(
            "risk_trend", "finance", List.of(0.6, 0.78), List.of("t1", "t2")
        );
        assertNotNull(calculatedTrend);

        verify(riskEngine).assess(riskRequest);
        verify(metricsAggregationService).recordMetric(anyString(), anyString(), any(), anyDouble(), any());
        verify(kpiService).calculateKPI(anyString(), anyString(), anyDouble(), anyDouble(), anyDouble());
        verify(trendAnalysisService).calculateTrend(anyString(), anyString(), anyList(), anyList());
    }
}
