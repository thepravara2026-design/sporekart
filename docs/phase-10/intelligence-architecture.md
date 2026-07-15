# Inventory Intelligence Architecture

## Overview
The Intelligence module provides executive-level operational visibility across all inventory domains. It aggregates data from Inventory, Warehouse, Stock, Batch, Movement, and Receiving modules into a unified analytics, KPI, and reporting layer.

## Data Flow
```
Inventory Domain → Executive KPIs → Dashboard Overview
Warehouse Domain → Warehouse Analytics → Health Scores
Stock Domain     → Stock Analytics → Health Distribution
Batch Domain     → Batch Analytics → Quality Distribution
Movement Domain  → Movement Analytics → Volume Trends
Receiving Domain → Receiving Analytics → Inbound Metrics
All Domains      → Forecasting → Insights → Reports → Alerts
```

## Service Boundaries
- All data is mock-generated — no backend, database, or API dependencies
- Each analytics domain maps to a dedicated mock data set
- Chart generators transform aggregate data into visualization-ready formats

## Mock Data Architecture
- 12 exported data sets covering all intelligence domains
- 7 chart generator functions for visualization-ready data
- Aggregate object types (not arrays) for domain analytics
- Sub-arrays for multi-entity data (topWarehouses, byWarehouse, topProducts)
