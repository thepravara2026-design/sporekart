# BI Copilot Architecture

## Overview

The BI Copilot is a Spring Boot 3 microservice deployed on port 8104. It follows a layered architecture with engine-based domain isolation, stateless API endpoints, and seed-data-driven analytics that simulate production data for development and testing.

## Service Details

| Attribute | Value |
|-----------|-------|
| **Service Name** | bi-copilot-service |
| **Port** | 8104 |
| **Framework** | Spring Boot 3.x |
| **Java** | 21 |
| **Auth** | Bearer JWT (validated via Identity Service) |
| **Config Prefix** | `sporekart.bi` |
| **API Docs** | Swagger UI at `/swagger-ui/`, OpenAPI at `/v3/api-docs/` |

## Component Diagram

```
+----------------------------------------------------------------------+
|                        BI Copilot Service (8104)                      |
|                                                                       |
|  +------------------+  +------------------+  +--------------------+  |
|  |   REST Layer     |  |   Websocket      |  |   Event Listener   |  |
|  |   (Controllers)  |  |   (Streaming)    |  |   (Async Updates)  |  |
|  +--------+---------+  +--------+---------+  +---------+----------+  |
|           |                      |                      |             |
|  +--------v----------------------v----------------------v----------+  |
|  |                    Service / Orchestration Layer                |  |
|  +--------+----------+----------+----------+----------+-----------+  |
|           |          |          |          |          |              |
|  +--------v-+  +-----v----+  +--v------+  +--v------+  +--v------+  |
|  | Revenue  |  | Customer |  | Product |  | Invent- |  | Train-  |  |
|  |Analytics |  |Analytics |  |Analytics|  |  ory    |  |  ing    |  |
|  | Engine   |  | Engine   |  | Engine  |  |Analytics|  |Analytics|  |
|  +----------+  +----------+  +---------+  +---------+  +---------+  |
|                                                                       |
|  +--------+  +----------+  +----------+  +--------+  +-----------+  |
|  |Forecast|  | Recommend|  | Insights |  |  Risk  |  |Visual-   |  |
|  | Engine |  |  Engine  |  |  Engine  |  | Engine |  |ization   |  |
|  +--------+  +----------+  +----------+  +--------+  | Engine   |  |
|                                                       +-----------+  |
|  +----------+  +-----------------+                                   |
|  | Natural  |  | Executive       |                                   |
|  | Language |  | Reporting       |                                   |
|  | Query    |  | Engine          |                                   |
|  +----------+  +-----------------+                                   |
+----------------------------------------------------------------------+
         |                      |                      |
         v                      v                      v
+------------------+  +------------------+  +----------------------+
|  Identity        |  |  Enterprise      |  |  Unified Workspace  |
|  Service         |  |  Copilot         |  |  (Frontend)         |
|  (Auth / JWT)    |  |  Framework       |  |                     |
+------------------+  +------------------+  +----------------------+
```

## Data Flow — Analytics Pipeline

```
User Request (REST / Chat / NLQ)
        |
        v
[Authentication & Authorization] -- JWT validated against Identity Service
        |
        v
[Request Routing] -- Controller dispatches to appropriate engine
        |
        v
[Domain Engine] -- e.g. RevenueAnalyticsEngine
        |
        +--> [Seed Data / In-Memory Store]
        |         |
        |         v
        |    Monthly Snapshots (24 months of revenue data)
        |    Customer Records (5000 customers across segments)
        |    Product Records (39 products across 5 categories)
        |    Inventory State (per-product stock, velocity, turnover)
        |    Training Batches (96 batches, 10 trainers)
        |
        +--> [Business Logic Layer]
        |         - Aggregation / filtering / period resolution
        |         - Metric calculations (growth rates, ratios, trends)
        |         - Seasonality adjustments
        |
        +--> [Recommendation / Insight Generation]
        |         - Rule-based insight detection
        |         - Priority classification
        |         - Explanation assembly (WHY + impact + confidence)
        |
        +--> [Visualization Selection]
                  - Data shape analysis (categorical, temporal, KPIs)
                  - Chart type mapping (line, bar, pie, heatmap, etc.)
                  - Color scheme and label generation
        |
        v
[Response Assembly] -- Domain object + VisualizationConfig list
        |
        v
[Client Response]
```

## 12 Engines Architecture

Each engine is a Spring `@Component` with isolated responsibility, seed data generation, and public query methods:

| Engine | Seed Data | Data Volume |
|--------|-----------|-------------|
| RevenueAnalyticsEngine | 24 months of monthly snapshots | 5 categories, 39 products, 5 regions, 4 segments, 5 channels |
| CustomerAnalyticsEngine | 5,000 customers with purchase history | 4 segments, 5 regions, 12 months |
| ProductAnalyticsEngine | 12 months of product performance | 39 products across 5 categories |
| InventoryAnalyticsEngine | Per-product stock/velocity values | 48 products across 5 categories |
| TrainingAnalyticsEngine | 96 batches across 12 months | 5 courses, 10 trainers, 500 students |

## Configuration

Key properties (prefix `sporekart.bi`):

| Property | Default | Description |
|----------|---------|-------------|
| `data-retention-days` | 365 | Days to retain analytics data |
| `forecast-default-horizon` | 90 | Default forecast horizon in days |
| `dashboard-refresh-seconds` | 300 | Dashboard auto-refresh interval |
| `executive-report-format` | pdf,html,json | Supported report export formats |
| `health-score-weights` | (configurable map) | Weight per dimension for health score |

## Security

- All endpoints (except actuator, swagger, and docs) require JWT bearer authentication
- Stateless session management
- CORS enabled for frontend integration
- Role-based access control via `@PreAuthorize` annotations
