# Sprint 18 Part 6 — Enterprise AI Risk Assessment & Trust Framework

**Lead:** Enterprise AI Platform Engineering Team
**Module:** ai-service
**Type:** Risk Assessment & Trust Scoring

## Objective

Build the Enterprise AI Risk Assessment & Trust Framework — the centralized system for identifying risk factors, calculating risk scores, determining risk levels, computing trust scores, estimating confidence, and generating recommendations across all AI operations.

## Architecture Position

```
Business Module → Conversation → Workflow → Governance → Policy → Decision → Approval → Compliance → Risk → Prompt → Knowledge → Semantic → Gateway → Provider
```

## Modules Created

| Module | Responsibility |
|--------|---------------|
| risk-core | Shared domain enums, records |
| risk-api | Port interfaces for risk assessment services |
| risk-application | Application service implementations |
| risk-engine | Trust score calculator, confidence engine, risk result models |
| risk-infrastructure | JPA entities, repositories, Redis, Kafka, monitoring, security |
| risk-interfaces | REST controller, DTOs |

## Domain Model

### Enums (6)

- `RiskLevel` — LOW, MEDIUM, HIGH, CRITICAL
- `RiskAssessmentStatus` — PENDING, IN_PROGRESS, COMPLETED, FAILED
- `RiskCategory` — PROVIDER, PROMPT, KNOWLEDGE, COMPLIANCE, PERFORMANCE, SECURITY, OPERATIONAL
- `RecommendationType` — ALLOW, REVIEW, ESCALATE, BLOCK, FALLBACK
- `TrustFactor` — PROVIDER_RELIABILITY, KNOWLEDGE_QUALITY, SEMANTIC_CONFIDENCE, PROMPT_VALIDATION, HISTORICAL_ACCURACY, POLICY_COMPLIANCE, WORKFLOW_SUCCESS, CONTEXT_COMPLETENESS, OUTPUT_VALIDATION
- `ConfidenceFactor` — KNOWLEDGE_MATCH, SEMANTIC_SIMILARITY, PROMPT_QUALITY, CONVERSATION_CONTEXT, WORKFLOW_SUCCESS, PROVIDER_METADATA

### Records (14)

- `RiskAssessment` — Aggregate root with status, level, scores
- `RiskScore` — Overall risk score (0-100) with breakdown
- `RiskFactor` — Individual risk factor with category, score, weight
- `RiskRule` — Risk rule definition with expression, threshold, severity
- `RiskEvidence` — Evidence collected for risk evaluation
- `RiskDecision` — Decision record with recommendation type
- `TrustAssessment` — Trust evaluation result with per-factor scores
- `ConfidenceScore` — Confidence calculation with per-factor breakdown
- `RiskRecommendation` — Recommendation with reasons and actions
- `RiskAudit` — Immutable audit record
- `RiskThreshold` — Configurable threshold configuration
- `RiskMetadata` — Request metadata for risk evaluation
- `RiskHistory` — Historical risk assessment record
- `RiskAssessmentRequest` — Request record for initiating assessment

## API Interfaces (11)

| Interface | Methods |
|-----------|---------|
| RiskEngine | assess, assessAsync, getStatus, cancelAssessment |
| RiskAssessmentService | createAssessment, submitAssessment, getAssessment |
| RiskScoringService | calculateRiskScore, getScoreBreakdown, getWeightedFactors |
| RiskClassificationService | classifyRiskLevel, getRiskLevel, getThresholds |
| TrustEngine | evaluateTrust, getTrustScore, getFactorBreakdown |
| TrustScoreService | calculateTrustScore, getTrustFactors, getTrustReasons |
| ConfidenceCalculator | calculateConfidence, getConfidenceFactors, getConfidenceExplanation |
| RiskRecommendationService | generateRecommendation, getRecommendation, getRecommendationHistory |
| RiskAuditService | recordAudit, queryAudit, exportAudit |
| RiskMetricsService | recordMetric, getStatistics, getDashboard |
| RiskConfigurationService | getConfig, setConfig, reloadConfig, getThresholds |

## Application Services (11)

Service implementations matching all 11 API interfaces.

## Engine Classes (3)

- `TrustScoreCalculator` — Evaluates 9 trust factors (Provider Reliability, Knowledge Quality, Semantic Confidence, Prompt Validation, Historical Accuracy, Policy Compliance, Workflow Success, Context Completeness, Output Validation), each scored 0-100, weighted average for overall trust score
- `ConfidenceCalculatorEngine` — Evaluates 6 confidence factors (Knowledge Match, Semantic Similarity, Prompt Quality, Conversation Context, Workflow Success, Provider Metadata), each scored 0-100, weighted average with explanation
- `RiskResult` — Result model with risk level, risk score, trust score, confidence score, recommendation, evidence references

## Persistence

### Flyway V25 — 8 Tables

- `risk_assessments` — Assessment records with status, level, scores
- `risk_factors` — Individual risk factor evaluations
- `risk_rules` — Risk rule definitions
- `risk_evidence` — Evidence collected for risk evaluation
- `risk_decisions` — Decision outputs with recommendations
- `risk_trust_scores` — Trust assessment results
- `risk_confidence_scores` — Confidence calculation results
- `risk_audit` — Immutable audit trail

### JPA (8 Entities + 8 Repositories)

- UUID PKs, TEXT columns, soft deletes, audit timestamps
- Soft-delete-aware queries, findByAssessmentId, findByStatus, findByLevel

### 11 Indexes

Performance indexes on assessment status, risk level, factor references, trust identifiers, confidence identifiers, audit timestamps.

## REST API (10 Endpoints)

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/v1/risk/assess | Initiate a risk assessment |
| GET | /api/v1/risk/assessments | List risk assessments |
| GET | /api/v1/risk/assessments/{id} | Get assessment details |
| POST | /api/v1/risk/assessments/{id}/factors | Add risk factors to assessment |
| GET | /api/v1/risk/assessments/{id}/factors | List factors for assessment |
| POST | /api/v1/risk/assess/{id}/trust | Evaluate trust for assessment |
| GET | /api/v1/risk/assess/{id}/trust | Get trust evaluation |
| POST | /api/v1/risk/assess/{id}/confidence | Calculate confidence for assessment |
| GET | /api/v1/risk/assess/{id}/confidence | Get confidence score |
| GET | /api/v1/risk/health | Health check |

**DTOs (13):** RiskAssessmentRequest, RiskAssessmentResponse, RiskFactorRequest, RiskFactorResponse, RiskLevelResponse, TrustEvaluationRequest, TrustEvaluationResponse, ConfidenceCalculationRequest, ConfidenceCalculationResponse, RecommendationResponse, HealthResponse, StatisticsResponse, ErrorResponse

## Kafka (7 Event Types)

Topic: `risk-events`

| Event Type | Description |
|------------|-------------|
| RiskAssessmentStarted | Assessment initiated |
| RiskAssessmentCompleted | Assessment completed |
| RiskAssessmentFailed | Assessment failed with error |
| RiskFactorIdentified | Risk factor identified |
| TrustEvaluated | Trust evaluation completed |
| ConfidenceCalculated | Confidence calculation completed |
| RecommendationGenerated | Recommendation produced |

## Redis (5 Namespaces)

| Namespace | TTL | Purpose |
|-----------|-----|---------|
| risk:assessment:* | 300s | Active assessment state |
| risk:factors:* | 180s | Resolved risk factors |
| risk:trust:* | 300s | Trust evaluation results |
| risk:confidence:* | 300s | Confidence calculation results |
| risk:config:* | 300s | Configuration cache |

## Trust Scoring (9 Factors)

| Factor | Description | Weight |
|--------|-------------|--------|
| Provider Reliability | Provider uptime, error rate, latency history | 15% |
| Knowledge Quality | Source authority, freshness, citation validity | 15% |
| Semantic Confidence | Embedding similarity score, vector distance | 12% |
| Prompt Validation | Injection detection, template compliance | 12% |
| Historical Accuracy | Past response accuracy for similar queries | 12% |
| Policy Compliance | Adherence to governance policies | 10% |
| Workflow Success | Previous workflow completion rate | 8% |
| Context Completeness | Required context fields present and valid | 8% |
| Output Validation | Response format, length, content checks | 8% |

Each factor scored 0-100. Overall trust score = weighted average (0-100).

## Confidence Engine (6 Factors)

| Factor | Description | Weight |
|--------|-------------|--------|
| Knowledge Match | Relevance of retrieved knowledge to query | 25% |
| Semantic Similarity | Embedding cosine similarity score | 20% |
| Prompt Quality | Prompt template match, variable completeness | 18% |
| Conversation Context | Conversation history relevance and recency | 15% |
| Workflow Success | Similar workflow completion statistics | 12% |
| Provider Metadata | Provider capability match to request | 10% |

Each factor scored 0-100. Overall confidence = weighted average with explanation string.

## Out of Scope (Deferred to Later Sprints)

- PII Detection
- Prompt Injection Detection
- Jailbreak Detection
- Content Moderation
- Hallucination Detection
- Cost Management
- Model Evaluation
