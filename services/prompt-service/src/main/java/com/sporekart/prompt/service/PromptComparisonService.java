package com.sporekart.prompt.service;

import com.sporekart.prompt.dto.response.ComparisonResponse;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PromptComparisonService {

    private final PromptVersionRepository versionRepository;
    private final TokenEstimator tokenEstimator;

    public PromptComparisonService(PromptVersionRepository versionRepository, TokenEstimator tokenEstimator) {
        this.versionRepository = versionRepository;
        this.tokenEstimator = tokenEstimator;
    }

    public ComparisonResponse compare(UUID templateId, int versionA, int versionB) {
        var vA = versionRepository.findByTemplateIdAndVersion(templateId, versionA)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionA));
        var vB = versionRepository.findByTemplateIdAndVersion(templateId, versionB)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionB));

        var diff = computeDiff(vA.getPromptBody(), vB.getPromptBody());

        int tokensA = tokenEstimator.estimatePromptTokens(vA.getPromptBody());
        int tokensB = tokenEstimator.estimatePromptTokens(vB.getPromptBody());

        var costPerToken = 0.000002;
        double costA = tokensA * costPerToken;
        double costB = tokensB * costPerToken;

        var diffA = new ComparisonResponse.VersionDiff(vA.getVersion(), truncate(vA.getPromptBody()), tokensA, java.math.BigDecimal.valueOf(costA));
        var diffB = new ComparisonResponse.VersionDiff(vB.getVersion(), truncate(vB.getPromptBody()), tokensB, java.math.BigDecimal.valueOf(costB));

        return new ComparisonResponse(diffA, diffB, diff.addedLines(), diff.removedLines(),
                tokensB - tokensA, java.math.BigDecimal.valueOf(costB - costA),
                "Version " + versionB + " vs " + versionA);
    }

    private DiffResult computeDiff(String oldText, String newText) {
        var oldLines = oldText != null ? oldText.split("\n") : new String[0];
        var newLines = newText != null ? newText.split("\n") : new String[0];
        var oldSet = new java.util.HashSet<>(List.of(oldLines));
        var newSet = new java.util.HashSet<>(List.of(newLines));

        var added = new ArrayList<String>();
        var removed = new ArrayList<String>();

        for (var line : newLines) {
            if (!oldSet.contains(line)) {
                added.add(line);
            }
        }
        for (var line : oldLines) {
            if (!newSet.contains(line)) {
                removed.add(line);
            }
        }

        return new DiffResult(added, removed);
    }

    private record DiffResult(List<String> addedLines, List<String> removedLines) {}

    private String truncate(String text) {
        if (text == null) return "";
        return text.length() > 200 ? text.substring(0, 200) + "..." : text;
    }
}
