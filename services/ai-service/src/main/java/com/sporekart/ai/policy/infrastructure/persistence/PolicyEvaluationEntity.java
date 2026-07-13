package com.sporekart.ai.policy.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_policy_evaluations")
public class PolicyEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "decision", length = 50)
    private String decision;

    @Column(name = "violations", columnDefinition = "TEXT")
    private String violations;

    @Column(name = "context", columnDefinition = "TEXT")
    private String context;

    @Column(name = "evaluation_time_ms")
    private Long evaluationTimeMs;

    @Column(name = "rules_evaluated")
    private Integer rulesEvaluated;

    @Column(name = "rules_passed")
    private Integer rulesPassed;

    @Column(name = "rules_failed")
    private Integer rulesFailed;

    @Column(name = "matched")
    private Boolean matched;

    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public PolicyEvaluationEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getRequestId() { return requestId; }
    public void setRequestId(UUID requestId) { this.requestId = requestId; }
    public UUID getPolicyId() { return policyId; }
    public void setPolicyId(UUID policyId) { this.policyId = policyId; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getViolations() { return violations; }
    public void setViolations(String violations) { this.violations = violations; }
    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
    public Long getEvaluationTimeMs() { return evaluationTimeMs; }
    public void setEvaluationTimeMs(Long evaluationTimeMs) { this.evaluationTimeMs = evaluationTimeMs; }
    public Integer getRulesEvaluated() { return rulesEvaluated; }
    public void setRulesEvaluated(Integer rulesEvaluated) { this.rulesEvaluated = rulesEvaluated; }
    public Integer getRulesPassed() { return rulesPassed; }
    public void setRulesPassed(Integer rulesPassed) { this.rulesPassed = rulesPassed; }
    public Integer getRulesFailed() { return rulesFailed; }
    public void setRulesFailed(Integer rulesFailed) { this.rulesFailed = rulesFailed; }
    public Boolean getMatched() { return matched; }
    public void setMatched(Boolean matched) { this.matched = matched; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
