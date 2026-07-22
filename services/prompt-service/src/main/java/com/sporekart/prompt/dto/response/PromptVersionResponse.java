package com.sporekart.prompt.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PromptVersionResponse(
        UUID id,
        UUID templateId,
        Integer version,
        String promptBody,
        String systemPrompt,
        String developerPrompt,
        String userPrompt,
        String fewShotExamples,
        String conversationInstructions,
        String safetyConstraints,
        String providerMetadata,
        String variablesJson,
        String providerConstraints,
        BigDecimal temperature,
        BigDecimal topP,
        Integer maxTokens,
        boolean isPublished,
        OffsetDateTime createdAt,
        UUID createdBy,
        String changeNotes
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private UUID templateId;
        private Integer version;
        private String promptBody;
        private String systemPrompt;
        private String developerPrompt;
        private String userPrompt;
        private String fewShotExamples;
        private String conversationInstructions;
        private String safetyConstraints;
        private String providerMetadata;
        private String variablesJson;
        private String providerConstraints;
        private BigDecimal temperature;
        private BigDecimal topP;
        private Integer maxTokens;
        private boolean isPublished;
        private OffsetDateTime createdAt;
        private UUID createdBy;
        private String changeNotes;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder templateId(UUID t) { this.templateId = t; return this; }
        public Builder version(Integer v) { this.version = v; return this; }
        public Builder promptBody(String p) { this.promptBody = p; return this; }
        public Builder systemPrompt(String s) { this.systemPrompt = s; return this; }
        public Builder developerPrompt(String d) { this.developerPrompt = d; return this; }
        public Builder userPrompt(String u) { this.userPrompt = u; return this; }
        public Builder fewShotExamples(String f) { this.fewShotExamples = f; return this; }
        public Builder conversationInstructions(String c) { this.conversationInstructions = c; return this; }
        public Builder safetyConstraints(String s) { this.safetyConstraints = s; return this; }
        public Builder providerMetadata(String p) { this.providerMetadata = p; return this; }
        public Builder variablesJson(String v) { this.variablesJson = v; return this; }
        public Builder providerConstraints(String p) { this.providerConstraints = p; return this; }
        public Builder temperature(BigDecimal t) { this.temperature = t; return this; }
        public Builder topP(BigDecimal t) { this.topP = t; return this; }
        public Builder maxTokens(Integer m) { this.maxTokens = m; return this; }
        public Builder isPublished(boolean p) { this.isPublished = p; return this; }
        public Builder createdAt(OffsetDateTime c) { this.createdAt = c; return this; }
        public Builder createdBy(UUID c) { this.createdBy = c; return this; }
        public Builder changeNotes(String c) { this.changeNotes = c; return this; }

        public PromptVersionResponse build() {
            return new PromptVersionResponse(id, templateId, version, promptBody, systemPrompt,
                    developerPrompt, userPrompt, fewShotExamples, conversationInstructions,
                    safetyConstraints, providerMetadata, variablesJson, providerConstraints,
                    temperature, topP, maxTokens, isPublished, createdAt, createdBy, changeNotes);
        }
    }
}
