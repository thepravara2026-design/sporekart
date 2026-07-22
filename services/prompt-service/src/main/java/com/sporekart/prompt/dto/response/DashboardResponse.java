package com.sporekart.prompt.dto.response;

import java.util.List;

public record DashboardResponse(
        long totalTemplates,
        long publishedTemplates,
        long draftTemplates,
        long pendingApprovals,
        long totalExecutions,
        List<PromptTemplateResponse> recentTemplates,
        List<PromptTemplateResponse> mostUsedPrompts,
        List<PromptTemplateResponse> leastUsedPrompts
) {}
