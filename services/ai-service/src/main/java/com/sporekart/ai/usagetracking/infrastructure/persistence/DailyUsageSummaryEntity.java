package com.sporekart.ai.usagetracking.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ut_daily_usage_summary")
public class DailyUsageSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "usage_date", length = 10)
    private String usageDate;

    @Column(name = "provider_id", length = 128)
    private String providerId;

    @Column(name = "model_id", length = 128)
    private String modelId;

    @Column(name = "total_requests")
    private int totalRequests;

    @Column(name = "total_success")
    private int totalSuccess;

    @Column(name = "total_failure")
    private int totalFailure;

    @Column(name = "total_tokens")
    private long totalTokens;

    @Column(name = "avg_execution_time_ms")
    private double avgExecutionTimeMs;

    public DailyUsageSummaryEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(String usageDate) {
        this.usageDate = usageDate;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public int getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(int totalFailure) {
        this.totalFailure = totalFailure;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(long totalTokens) {
        this.totalTokens = totalTokens;
    }

    public double getAvgExecutionTimeMs() {
        return avgExecutionTimeMs;
    }

    public void setAvgExecutionTimeMs(double avgExecutionTimeMs) {
        this.avgExecutionTimeMs = avgExecutionTimeMs;
    }
}
