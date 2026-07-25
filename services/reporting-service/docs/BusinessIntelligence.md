# Business Intelligence Runtime

## Purpose
Aggregates data from upstream engines (Analytics, Prediction, Decision, Alert) to produce mocked enterprise business intelligence reports.

## Data Sources (mocked)
| Source Engine | Data Type |
|---|---|
| Analytics Engine | Revenue trends, customer segments, operational metrics, model performance |
| Prediction Engine | Revenue forecasts with confidence scores |
| Decision Engine | Recommendations, mitigation strategies, retention strategies |
| Alert Engine | Risk assessments, AI alerts, platform alerts, compliance alerts, inventory alerts |

## BI Reports
| Report | Category | Source |
|---|---|---|
| Executive Business Intelligence Dashboard | EXECUTIVE | All engines |
| Revenue Intelligence Report | REVENUE | Analytics + Prediction |
| Operational Intelligence Report | ORDERS | Analytics + Alert |
| Customer Intelligence Report | CUSTOMERS | Analytics + Decision |
| AI Platform Intelligence Report | AI_PLATFORM | Analytics + Alert |
| Platform Intelligence Report | PLATFORM_HEALTH | Analytics + Alert |
| Risk Intelligence Report | RISK | Alert + Decision |
| Compliance Intelligence Report | COMPLIANCE | Alert |

## Report Structure
```json
{
  "id": "uuid",
  "title": "Executive Business Intelligence Dashboard",
  "executiveSummary": "Enterprise performance is HEALTHY...",
  "businessHealth": "HEALTHY",
  "recommendations": [...],
  "riskFlags": [...],
  "aggregatedKpis": { "totalRevenue": 2840000.0, "revenueGrowth": 12.0, ... },
  "dataSources": [
    { "source": "Analytics Engine", "type": "revenue", "confidence": 0.95 },
    { "source": "Alert Engine", "type": "alerts", "criticalCount": 0 }
  ]
}
```
