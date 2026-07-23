# Grower Copilot — Architecture

## Service Identity

- **Service Name:** `grower-copilot-service`
- **Port:** `8102` (internal), `443` (gateway)
- **Protocol:** HTTP/2 (gRPC preferred for internal, REST for external)
- **Language:** Java 17 / Spring Boot 3.x
- **Deployment:** Docker / Kubernetes

## System Context

```
  ┌──────────────┐     ┌─────────────────────────────────────┐     ┌──────────────────┐
  │   Frontend   │────▶│         API Gateway (443)            │────▶│  Identity Service │
  │  (Web/Mobile)│     │  /v1/grower-copilot/*                │     │  (authn/authz)    │
  └──────────────┘     └────────────┬────────────────────────┘     └──────────────────┘
                                    │
                                    │ :8102
                                    ▼
                     ┌─────────────────────────────┐
                     │    Grower Copilot Service    │
                     │  ┌───────────────────────┐  │
                     │  │   Orchestration Layer  │  │
                     │  │   (Chat Controller)    │  │
                     │  └──────┬──────┬──────────┘  │
                     │         │      │             │
                     │  ┌──────▼──┐ ┌─▼────────────┐│
                     │  │ Prompt  │ │  Engine       ││
                     │  │ Manager │ │  Dispatcher   ││
                     │  └─────────┘ └──────┬────────┘│
                     │                     │          │
                     │    ┌────────────────▼──────┐   │
                     │    │     9 Engines          │   │
                     │    │  ┌──────────────────┐  │   │
                     │    │  │ DiseaseEngine     │  │   │
                     │    │  │ YieldPrediction   │  │   │
                     │    │  │ CultivationGuide  │  │   │
                     │    │  │ WeatherEngine     │  │   │
                     │    │  │ ScientificKnowledge│  │   │
                     │    │  │ PlanningEngine    │  │   │
                     │    │  │ RecommendationEng │  │   │
                     │    │  │ RiskEngine        │  │   │
                     │    │  │ ReportingEngine   │  │   │
                     │    │  └──────────────────┘  │   │
                     │    └────────────────────────┘   │
                     │                                 │
                     └──────────┬──────────────────────┘
                                │
              ┌─────────────────┼────────────────────┐
              ▼                 ▼                    ▼
   ┌──────────────────┐ ┌──────────────┐ ┌──────────────────┐
   │  Weather Provider │ │  Knowledge   │ │ Enterprise AI    │
   │  (abstracted)     │ │  Platform    │ │ Platform (LLM)   │
   │  ├─ Simulated     │ │  (REST)      │ │ (gRPC / REST)    │
   │  └─ Real (future) │ └──────────────┘ └──────────────────┘
   └──────────────────┘
```

## 9 Engines

| # | Engine | Responsibility |
|---|--------|----------------|
| 1 | **ChatController** | Orchestrates multi-turn conversation, manages context, routes to engines |
| 2 | **DiseaseEngine** | Symptom matching, disease diagnosis, treatment, escalation |
| 3 | **YieldPrediction** | Yield formulas per species, efficiency, revenue, risk scoring |
| 4 | **CultivationGuide** | Growth stage guidance, optimal conditions, troubleshooting |
| 5 | **WeatherEngine** | Weather data via abstracted provider, forecasts, climate risk |
| 6 | **ScientificKnowledge** | Query knowledge repository, citation formatting |
| 7 | **PlanningEngine** | Crop planning, calendar generation, rotation scheduling |
| 8 | **RecommendationEngine** | Cross-engine recommendation aggregation |
| 9 | **RiskEngine** | Composite risk scoring across disease, weather, economics |
| 10 | **ReportingEngine** | Summary generation, PDF/CSV export |

## Weather Abstraction

The `WeatherProvider` interface decouples weather ingestion from any specific vendor:

```
WeatherProvider (interface)
  ├── SimulatedWeatherProvider    (dev/staging)
  └── OpenMeteoProvider           (staging/production, planned)
  └── IMDWeatherProvider          (future — India Meteorological Dept.)
```

## Disease Advisory Architecture

```
  Symptom Input
       │
       ▼
  Symptom Normalizer ──► Disease Knowledge Base (15+ diseases)
       │
       ▼
  Matcher Engine ──► Confidence Scoring ──► Matched Diseases (ranked)
       │
       ▼
  Severity Classifier ──► Mild / Moderate / Severe / Critical
       │
       ▼
  Treatment Recommender ──► Chemical / Biological / Cultural / Integrated
       │
       ▼
  Escalation Evaluator ──► Escalate if: Severe/Critical, spread risk,
                            recurring, unknown symptoms
```

## Integration with Enterprise AI Platform

- **Model Routing:** The prompt manager delegates domain-specific queries to fine-tuned models via the Enterprise AI Platform.
- **Fallback:** When the platform is unavailable, the service operates in degraded mode using rule-based engines and cached knowledge.
- **Metering:** All LLM invocations are metered through the platform for cost attribution and quota management.

## Data Flow

```
  User Message
      │
      ▼
  ChatController.processMessage(text, context)
      │
      ├──► IntentClassifier (disease | yield | cultivation | weather | general)
      │
      ├──► [disease]     ──► DiseaseEngine.analyze(symptoms, images?)
      ├──► [yield]       ──► YieldPrediction.predict(species, params)
      ├──► [cultivation] ──► CultivationGuide.getGuidance(species, stage)
      ├──► [weather]     ──► WeatherEngine.getForecast(location)
      ├──► [planning]    ──► PlanningEngine.generatePlan(params)
      └──► [general]     ──► KnowledgeEngine.query(query)
              │
              ▼
  ResponseFormatter ──► ChatResponse
```

## Ports and Protocols

| Port | Protocol | Purpose |
|------|----------|---------|
| 8102 | HTTP/2 | Internal gRPC endpoints |
| 8103 | HTTP/1.1 | Health check (/healthz, /readyz) |
| 8104 | HTTP/1.1 | Metrics (/metrics, Prometheus) |
