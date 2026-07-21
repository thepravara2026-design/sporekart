package com.sporekart.ai.prompt.domain.valueobject;

import java.time.Instant;
import java.util.*;

public record PromptMetadata(
    String businessUnit,
    String department,
    String owner,
    String team,
    String createdBy,
    String approvedBy,
    Instant lastReviewed,
    ComplianceLevel complianceLevel,
    RiskLevel riskLevel,
    SensitivityLevel sensitivity,
    CostClassification costClassification,
    List<String> tags,
    Map<String, String> labels,
    String language,
    Set<String> modelCompatibility
) {
    public PromptMetadata {
        Objects.requireNonNull(createdBy, "createdBy must not be null");
        Objects.requireNonNull(complianceLevel, "complianceLevel must not be null");
        Objects.requireNonNull(riskLevel, "riskLevel must not be null");
        Objects.requireNonNull(sensitivity, "sensitivity must not be null");
        Objects.requireNonNull(costClassification, "costClassification must not be null");
        tags = tags == null ? List.of() : List.copyOf(tags);
        labels = labels == null ? Map.of() : Map.copyOf(labels);
        modelCompatibility = modelCompatibility == null ? Set.of() : Set.copyOf(modelCompatibility);
    }

    public static Builder builder() {
        return new Builder();
    }

    public PromptMetadata withLastReviewed(Instant lastReviewed) {
        return new PromptMetadata(businessUnit, department, owner, team, createdBy,
            approvedBy, lastReviewed, complianceLevel, riskLevel, sensitivity,
            costClassification, tags, labels, language, modelCompatibility);
    }

    public PromptMetadata withApprovedBy(String approvedBy) {
        return new PromptMetadata(businessUnit, department, owner, team, createdBy,
            approvedBy, lastReviewed, complianceLevel, riskLevel, sensitivity,
            costClassification, tags, labels, language, modelCompatibility);
    }

    public PromptMetadata withOwner(String owner) {
        return new PromptMetadata(businessUnit, department, owner, team, createdBy,
            approvedBy, lastReviewed, complianceLevel, riskLevel, sensitivity,
            costClassification, tags, labels, language, modelCompatibility);
    }

    public static class Builder {
        private String businessUnit;
        private String department;
        private String owner;
        private String team;
        private String createdBy;
        private String approvedBy;
        private Instant lastReviewed;
        private ComplianceLevel complianceLevel = ComplianceLevel.NONE;
        private RiskLevel riskLevel = RiskLevel.NONE;
        private SensitivityLevel sensitivity = SensitivityLevel.INTERNAL;
        private CostClassification costClassification = CostClassification.LOW;
        private List<String> tags = new ArrayList<>();
        private Map<String, String> labels = new HashMap<>();
        private String language = "en";
        private Set<String> modelCompatibility = new HashSet<>();

        public Builder businessUnit(String businessUnit) { this.businessUnit = businessUnit; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder owner(String owner) { this.owner = owner; return this; }
        public Builder team(String team) { this.team = team; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public Builder approvedBy(String approvedBy) { this.approvedBy = approvedBy; return this; }
        public Builder lastReviewed(Instant lastReviewed) { this.lastReviewed = lastReviewed; return this; }
        public Builder complianceLevel(ComplianceLevel complianceLevel) { this.complianceLevel = complianceLevel; return this; }
        public Builder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public Builder sensitivity(SensitivityLevel sensitivity) { this.sensitivity = sensitivity; return this; }
        public Builder costClassification(CostClassification costClassification) { this.costClassification = costClassification; return this; }
        public Builder tags(List<String> tags) { this.tags = tags; return this; }
        public Builder labels(Map<String, String> labels) { this.labels = labels; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder modelCompatibility(Set<String> modelCompatibility) { this.modelCompatibility = modelCompatibility; return this; }

        public PromptMetadata build() {
            return new PromptMetadata(businessUnit, department, owner, team, createdBy,
                approvedBy, lastReviewed, complianceLevel, riskLevel, sensitivity,
                costClassification, tags, labels, language, modelCompatibility);
        }
    }
}
