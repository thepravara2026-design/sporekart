package com.sporekart.ai.usagetracking.interfaces.rest.dto;

import com.sporekart.ai.usagetracking.domain.UsageSummary;

import java.util.List;
import java.util.Map;

public class UsageDashboardDto {

    private long totalRequests;
    private long totalSuccess;
    private long totalFailure;
    private long totalTokens;
    private List<UsageSummary> topProviders;
    private List<UsageSummary> topModels;
    private Map<String, Double> failureRates;

    public UsageDashboardDto() {
    }

    public long getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public long getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(long totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public long getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(long totalFailure) {
        this.totalFailure = totalFailure;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(long totalTokens) {
        this.totalTokens = totalTokens;
    }

    public List<UsageSummary> getTopProviders() {
        return topProviders;
    }

    public void setTopProviders(List<UsageSummary> topProviders) {
        this.topProviders = topProviders;
    }

    public List<UsageSummary> getTopModels() {
        return topModels;
    }

    public void setTopModels(List<UsageSummary> topModels) {
        this.topModels = topModels;
    }

    public Map<String, Double> getFailureRates() {
        return failureRates;
    }

    public void setFailureRates(Map<String, Double> failureRates) {
        this.failureRates = failureRates;
    }
}
