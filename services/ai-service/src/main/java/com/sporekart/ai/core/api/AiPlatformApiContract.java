package com.sporekart.ai.core.api;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AI Platform", description = "Enterprise AI Platform API — Gateway, Providers, Prompts, RAG, Search, Chat, Content, Workflow, Monitoring")
public interface AiPlatformApiContract {

    String AI_HEALTH = "/ai/health";
    String AI_PROVIDERS = "/ai/providers";
    String AI_CHAT = "/ai/chat";
    String AI_SEARCH = "/ai/search";
    String AI_PROMPTS = "/ai/prompts";
    String AI_WORKFLOWS = "/ai/workflows";
    String AI_CONTENT = "/ai/content";
    String AI_RAG = "/ai/rag";
    String AI_METRICS = "/ai/metrics";
}
