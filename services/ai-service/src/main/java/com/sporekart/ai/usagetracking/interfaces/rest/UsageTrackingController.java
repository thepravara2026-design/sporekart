package com.sporekart.ai.usagetracking.interfaces.rest;

import com.sporekart.ai.usagetracking.api.UsageAggregationService;
import com.sporekart.ai.usagetracking.api.UsageDashboardService;
import com.sporekart.ai.usagetracking.api.UsageTrackingService;
import com.sporekart.ai.usagetracking.domain.DailyUsage;
import com.sporekart.ai.usagetracking.domain.MonthlyUsage;
import com.sporekart.ai.usagetracking.domain.UsageRecord;
import com.sporekart.ai.usagetracking.domain.UsageSummary;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.DailyUsageDto;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.MonthlyUsageDto;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.UsageDashboardDto;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.UsageRecordRequestDto;
import com.sporekart.ai.usagetracking.interfaces.rest.dto.UsageRecordResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usage-tracking")
public class UsageTrackingController {

    private final UsageTrackingService usageTrackingService;
    private final UsageAggregationService usageAggregationService;
    private final UsageDashboardService usageDashboardService;

    public UsageTrackingController(UsageTrackingService usageTrackingService,
                                  UsageAggregationService usageAggregationService,
                                  UsageDashboardService usageDashboardService) {
        this.usageTrackingService = usageTrackingService;
        this.usageAggregationService = usageAggregationService;
        this.usageDashboardService = usageDashboardService;
    }

    @PostMapping("/record")
    public ResponseEntity<UsageRecordResponseDto> record(@Valid @RequestBody UsageRecordRequestDto request) {
        UsageRecord saved = usageTrackingService.recordUsage(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsageRecordResponseDto> get(@PathVariable("id") String id) {
        return usageTrackingService.getUsage(id)
                .map(record -> ResponseEntity.ok(toResponse(record)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<UsageRecordResponseDto>> list(@RequestParam(name = "limit", defaultValue = "100") int limit) {
        List<UsageRecordResponseDto> body = usageTrackingService.listUsage(limit).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyUsageDto> daily(@RequestParam("date") String date) {
        return ResponseEntity.ok(toDto(usageAggregationService.getDailyUsage(date)));
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyUsageDto> monthly(@RequestParam("yearMonth") String yearMonth) {
        return ResponseEntity.ok(toDto(usageAggregationService.getMonthlyUsage(yearMonth)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<UsageSummary>> byProvider(@PathVariable("providerId") String providerId) {
        return ResponseEntity.ok(usageAggregationService.getByProvider(providerId));
    }

    @GetMapping("/model/{modelId}")
    public ResponseEntity<List<UsageSummary>> byModel(@PathVariable("modelId") String modelId) {
        return ResponseEntity.ok(usageAggregationService.getByModel(modelId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<UsageDashboardDto> dashboard() {
        Map<String, Object> data = usageDashboardService.getDashboard();
        UsageDashboardDto dto = new UsageDashboardDto();
        dto.setTotalRequests(asLong(data.get("totalRequests")));
        dto.setTotalSuccess(asLong(data.get("totalSuccess")));
        dto.setTotalFailure(asLong(data.get("totalFailure")));
        dto.setTotalTokens(asLong(data.get("totalTokens")));
        dto.setTopProviders(usageDashboardService.getTopProviders());
        dto.setTopModels(usageDashboardService.getTopModels());
        dto.setFailureRates(usageDashboardService.getFailureRates());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/top-providers")
    public ResponseEntity<List<UsageSummary>> topProviders() {
        return ResponseEntity.ok(usageDashboardService.getTopProviders());
    }

    @GetMapping("/top-models")
    public ResponseEntity<List<UsageSummary>> topModels() {
        return ResponseEntity.ok(usageDashboardService.getTopModels());
    }

    @GetMapping("/failures")
    public ResponseEntity<Map<String, Double>> failures() {
        return ResponseEntity.ok(usageDashboardService.getFailureRates());
    }

    private long asLong(Object value) {
        return value instanceof Number number ? number.longValue() : 0L;
    }

    private UsageRecord toDomain(UsageRecordRequestDto dto) {
        UsageRecord record = new UsageRecord();
        record.setRequestId(dto.getRequestId());
        record.setProviderId(dto.getProviderId());
        record.setModelId(dto.getModelId());
        record.setPromptTokens(dto.getPromptTokens());
        record.setCompletionTokens(dto.getCompletionTokens());
        record.setTotalTokens(dto.getTotalTokens());
        record.setExecutionTimeMs(dto.getExecutionTimeMs());
        record.setSuccess(dto.isSuccess());
        record.setFailureReason(dto.getFailureReason());
        record.setUserId(dto.getUserId());
        record.setSessionId(dto.getSessionId());
        record.setModule(dto.getModule());
        return record;
    }

    private UsageRecordResponseDto toResponse(UsageRecord record) {
        UsageRecordResponseDto dto = new UsageRecordResponseDto();
        dto.setUsageId(record.getUsageId());
        dto.setRequestId(record.getRequestId());
        dto.setProviderId(record.getProviderId());
        dto.setModelId(record.getModelId());
        dto.setPromptTokens(record.getPromptTokens());
        dto.setCompletionTokens(record.getCompletionTokens());
        dto.setTotalTokens(record.getTotalTokens());
        dto.setExecutionTimeMs(record.getExecutionTimeMs());
        dto.setSuccess(record.isSuccess());
        dto.setFailureReason(record.getFailureReason());
        dto.setTimestamp(record.getTimestamp());
        dto.setUserId(record.getUserId());
        dto.setSessionId(record.getSessionId());
        dto.setModule(record.getModule());
        return dto;
    }

    private DailyUsageDto toDto(DailyUsage usage) {
        DailyUsageDto dto = new DailyUsageDto();
        dto.setUsageDate(usage.usageDate());
        dto.setSummaries(usage.summaries());
        dto.setTotals(usage.totals());
        return dto;
    }

    private MonthlyUsageDto toDto(MonthlyUsage usage) {
        MonthlyUsageDto dto = new MonthlyUsageDto();
        dto.setYearMonth(usage.yearMonth());
        dto.setSummaries(usage.summaries());
        dto.setTotals(usage.totals());
        return dto;
    }
}
