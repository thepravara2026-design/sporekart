# Anomaly Engine Design

## Purpose
Detects business anomalies across 8 anomaly types with confidence scoring.

## Anomaly Types
| Type | Description |
|---|---|
| UNEXPECTED_DECLINE | Revenue drop below forecast |
| UNEXPECTED_GROWTH | Revenue spike (positive anomaly) |
| VOLUME_SPIKE | Order failure rate spike |
| TREND_BREAK | Customer login trend broken |
| PERFORMANCE_DROP | Inference error rate increase |
| OUTLIER | Inventory level outlier |
| OPERATIONAL_FAILURE | Workflow failure rate above threshold |
| SEASONALITY_CHANGE | Marketplace volume deviation |

## Detection Metrics
- **Expected vs Actual** value comparison
- **Deviation percentage** calculation
- **Confidence score** (0.0–1.0) based on deviation magnitude

## Key Methods
- `detectAllAnomalies()` — Detects 8+ anomalies across all types
- `detectAnomaliesByDomain(domain)` — Domain-specific detection
