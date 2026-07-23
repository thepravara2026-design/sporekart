# SporeKart Enterprise Business Intelligence Copilot

## Overview

The Enterprise Business Intelligence (BI) Copilot is SporeKart's Decision Intelligence Platform that provides AI-powered analytics, forecasting, and executive reporting across all business domains. It operates as a standalone Spring Boot microservice on port 8104 and integrates with the Enterprise Copilot Framework and Unified Workspace.

## Decision Intelligence Platform

The platform transforms raw operational data into actionable business insights through 12 specialized engines:

| # | Engine | Purpose |
|---|--------|---------|
| 1 | **Revenue Analytics Engine** | Gross/net revenue, AOV, category/product/region breakdown, MoM/YoY growth, refund analysis |
| 2 | **Customer Analytics Engine** | Customer segments, retention/churn, CLV, repeat purchase patterns, top customers |
| 3 | **Product Analytics Engine** | Top/worst performers, fast/slow movers, category performance, profitability, conversion |
| 4 | **Inventory Analytics Engine** | Stock levels, turnover rates, restocking priorities, dead/low stock detection, risk scoring |
| 5 | **Training Analytics Engine** | Batch performance, enrollment trends, trainer effectiveness, certification rates, revenue by course |
| 6 | **Forecast Engine** | 4 forecasting methods (MA, ES, LR, Seasonal), auto-selection, accuracy (MAPE), confidence intervals |
| 7 | **Recommendation Engine** | Decision recommendation generation, priority classification, WHY + impact + confidence explanations |
| 8 | **Business Insights Engine** | Automated insight discovery across categories with severity classification |
| 9 | **Risk Assessment Engine** | Risk detection, probability/impact scoring, severity classification, recommended actions |
| 10 | **Visualization Engine** | Auto-visualization (line, bar, pie, heatmap, trend, forecast, KPI, scorecard, table) |
| 11 | **Natural Language Query Engine** | NLQ parsing, intent detection, metric/dimension extraction, auto-visualization |
| 12 | **Executive Reporting Engine** | Daily/weekly/monthly/quarterly/annual reports, company health score, dashboard definitions |

## Key Capabilities

- **Company Health Score**: Composite score (0-100) across revenue, customer, training, inventory, operations, and growth dimensions with configurable weights
- **Automated Insights**: Continuous monitoring and insight generation with severity classification (Critical, Important, Info)
- **Decision Support**: Actionable recommendations with priority (Critical, High, Medium, Low), confidence scoring, and supporting data
- **Executive Dashboards**: Curated views with KPI grids, trend charts, forecast visualizations, and scorecard summaries
- **Report Automation**: Scheduled and on-demand report generation in PDF, HTML, and JSON formats
- **Natural Language Query**: Conversational analytics via NLQ-to-metric translation with auto-visualization

## Integration

- **Enterprise Copilot Framework**: BI Copilot registers as a capability provider within the copilot service mesh; chat context is shared for cross-domain conversations
- **Unified Workspace**: Dashboards and reports are surfaced through the Unified Workspace UI with embedded visualization components
- **Identity Service**: All BI endpoints are secured via bearer JWT tokens validated against the Identity Service
