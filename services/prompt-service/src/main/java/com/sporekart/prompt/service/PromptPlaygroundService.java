package com.sporekart.prompt.service;

import com.sporekart.prompt.dto.request.TestExecutionRequest;
import com.sporekart.prompt.dto.response.TestExecutionResponse;
import com.sporekart.prompt.entity.PromptUsageEntity;
import com.sporekart.prompt.repository.PromptUsageRepository;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Service
public class PromptPlaygroundService {

    private final PromptVersionRepository versionRepository;
    private final PreviewEngine previewEngine;
    private final TokenEstimator tokenEstimator;
    private final PromptUsageRepository usageRepository;

    public PromptPlaygroundService(PromptVersionRepository versionRepository,
                                   PreviewEngine previewEngine,
                                   TokenEstimator tokenEstimator,
                                   PromptUsageRepository usageRepository) {
        this.versionRepository = versionRepository;
        this.previewEngine = previewEngine;
        this.tokenEstimator = tokenEstimator;
        this.usageRepository = usageRepository;
    }

    @Transactional
    public TestExecutionResponse executeTest(TestExecutionRequest request) {
        var version = versionRepository.findById(request.versionId())
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + request.versionId()));

        var preview = previewEngine.preview(request.versionId(), request.variables());
        String renderedPrompt = preview.renderedPrompt();

        long startTime = System.currentTimeMillis();
        String response;
        boolean success;
        String error = null;

        try {
            response = simulateProviderCall(request.provider(), request.model(), renderedPrompt);
            success = true;
        } catch (Exception e) {
            response = null;
            success = false;
            error = e.getMessage();
        }

        long latencyMs = System.currentTimeMillis() - startTime;
        int promptTokens = tokenEstimator.estimatePromptTokens(renderedPrompt);
        int completionTokens = tokenEstimator.estimateCompletionTokens(response != null ? response : "");
        int totalTokens = promptTokens + completionTokens;
        BigDecimal cost = BigDecimal.valueOf(totalTokens * 0.000002);

        var usage = new PromptUsageEntity();
        usage.setTemplateId(version.getTemplateId());
        usage.setVersionId(version.getId());
        usage.setProvider(request.provider());
        usage.setModel(request.model());
        usage.setExecutionTimeMs(latencyMs);
        usage.setPromptTokens(promptTokens);
        usage.setCompletionTokens(completionTokens);
        usage.setTotalTokens(totalTokens);
        usage.setCost(cost);
        usage.setUserId(request.userId());
        usage.setLatencyMs(latencyMs);
        usage.setSuccess(success);
        usage.setErrorMessage(error);
        usage.setCreatedAt(OffsetDateTime.now());
        usageRepository.save(usage);

        return new TestExecutionResponse(
                renderedPrompt, response, cost, promptTokens,
                completionTokens, totalTokens, latencyMs, success, error);
    }

    private String simulateProviderCall(String provider, String model, String prompt) {
        return "[MOCK " + provider + "/" + model + " RESPONSE]\n\n"
                + "This is a simulated response for the prompt playground.\n"
                + "Prompt length: " + prompt.length() + " characters.\n"
                + "The actual provider integration will be handled by the AI Gateway service.";
    }
}
