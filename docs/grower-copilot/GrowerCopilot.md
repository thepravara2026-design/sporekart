# Grower Copilot

## Purpose

The Grower Copilot is an AI-powered advisory service for mushroom growers in India. It provides real-time, context-aware recommendations across disease management, yield optimization, cultivation guidance, weather adaptation, and scientific knowledge retrieval.

## Key Capabilities

- **Disease Advisory** — Symptom-based diagnosis of 15+ mushroom diseases with severity classification, treatment recommendations, and escalation triggers.
- **Yield Prediction** — Species-specific yield forecasting with efficiency analysis, revenue/cost estimation, risk scoring, and harvest window prediction.
- **Cultivation Guidance** — Step-by-step guidance across 5 species covering all growth stages, optimal condition tables, water management, and ventilation.
- **Weather Integration** — Provider-abstracted weather data with simulated fallback, regional forecasting, and climate risk assessment.
- **Scientific Knowledge** — Access to 40+ curated knowledge entries including peer-reviewed citations and government guidelines.
- **Crop Planning** — Multi-species planning with calendar generation, resource estimation, and rotation scheduling.

## Integration Points

| Integration | Protocol | Purpose |
|-------------|----------|---------|
| Enterprise AI Platform | gRPC / REST | Prompt orchestration, model routing, usage metering |
| Identity Service | REST (HTTP) | User authentication, session validation |
| Weather Provider (abstracted) | Pluggable adapter | Regional weather data ingestion |
| Knowledge Platform | REST (HTTP) | Scientific repository queries |
| Client Applications | WebSocket / REST | Real-time chat, async queries |

## Target Users

- **Smallholder mushroom farmers** in Maharashtra, Himachal Pradesh, Uttarakhand, Odisha, and Karnataka
- **Cooperative extension workers** assisting grower clusters
- **Agri-entrepreneurs** managing medium-scale spawn production and compost yards
- **Government horticulture officers** monitoring regional disease outbreaks

## Non-Goals

- Direct farm IoT hardware control
- Market price discovery or trading
- Soil or compost laboratory analysis
- Multi-language support (Phase 1: English and Hindi only)
