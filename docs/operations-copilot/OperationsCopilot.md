# Enterprise Operations Copilot — AI COO

## Overview
The Enterprise Operations Copilot (AI COO) is SporeKart's operations intelligence platform that continuously monitors operational health — inventory, procurement, warehouse, logistics, demand planning, supply chain, KPIs, cost optimization, and decision support.

## Architecture
Microservice on port 8108, route `/api/v1/copilot/operations/`. Integrates with existing SporeKart platform (Gateway, AI Platform, Prompt Platform, Knowledge Platform, RAG).

## Capabilities

| Capability | Engine | Description |
|---|---|---|
| Inventory Intelligence | InventoryEngine | Stock monitoring, alerts, risk scoring, restock suggestions |
| Procurement Planning | ProcurementEngine | Purchase planning, vendor selection, EOQ calculation |
| Order Operations | OrderOperationsEngine | Queue analysis, bottleneck detection, priority queuing |
| Warehouse Intelligence | WarehouseEngine | Storage optimization, picking efficiency, capacity monitoring |
| Logistics Planning | LogisticsEngine | Shipment planning, courier recommendation, cost analysis |
| Demand Planning | DemandPlanningEngine | Product/seasonal/regional/festival demand forecasting |
| Supply Chain Monitoring | SupplyChainMonitorEngine | Delay/vendor/risk detection, health scoring |
| Operational KPIs | OperationalKPIEngine | Fulfillment/warehouse/logistics metrics tracking |
| Decision Support | DecisionSupportEngine | Procurement/transfer/promotion recommendations |
| Cost Optimization | CostOptimizationEngine | Cost analysis, process improvement, savings opportunities |

## Engines Detail

### InventoryEngine
- Real-time inventory status by warehouse and category
- Automated low/critical/out-of-stock alerts
- Safety stock and reorder point calculation
- Dead stock detection and restock suggestions

### ProcurementEngine
- Multi-vendor comparison and selection
- Economic Order Quantity (EOQ) calculation
- Purchase order generation
- Vendor risk analysis

### OrderOperationsEngine
- Full order queue analysis by status
- Bottleneck detection (picking/packing/shipping)
- Fulfillment recommendations
- Dynamic priority queue generation

### WarehouseEngine
- Storage capacity and bin utilization analysis
- Picking efficiency and worker productivity
- ABC analysis for storage optimization
- Zone-level health monitoring

### LogisticsEngine
- Courier comparison (Delhivery, BlueDart, DTDC, India Post, ShadowFax)
- Delivery time and cost estimation
- Route optimization recommendations
- Regional performance tracking

### DemandPlanningEngine
- 30/90-day demand forecasting
- Seasonal factor calculation (Diwali, Holi, Pongal, etc.)
- Regional demand distribution
- Inventory buffer prediction

### SupplyChainMonitorEngine
- Real-time supply chain health scoring
- Anomaly detection (demand spikes, lead time deviations)
- Vendor risk assessment
- Automated alert generation

### OperationalKPIEngine
- 10 core operational KPIs tracked
- Fulfillment/warehouse/logistics/cost categories
- SLA compliance monitoring
- Trend analysis (improving/declining/stable/critical)

### DecisionSupportEngine
- Data-driven recommendations with confidence scores
- Decision simulation with impact analysis
- Prioritized alternatives
- Risk assessment for each decision

### CostOptimizationEngine
- Full operational cost breakdown
- Identified savings opportunities
- Process optimization suggestions
- Cost trend analysis

## API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | /api/v1/copilot/operations/query | Unified operations query routing |
| POST | /api/v1/copilot/operations/inventory | Inventory status |
| POST | /api/v1/copilot/operations/procurement | Procurement recommendations |
| POST | /api/v1/copilot/operations/orders | Order queue analysis |
| POST | /api/v1/copilot/operations/warehouse | Warehouse analysis |
| POST | /api/v1/copilot/operations/logistics | Shipment planning |
| POST | /api/v1/copilot/operations/forecast | Demand forecast |
| GET | /api/v1/copilot/operations/dashboard | Operations dashboard |
| GET | /api/v1/copilot/operations/alerts | Active operations alerts |
| POST | /api/v1/copilot/operations/supply-chain | Supply chain monitoring |
| POST | /api/v1/copilot/operations/kpi | Operational KPIs |
| POST | /api/v1/copilot/operations/decision | Decision support |
| POST | /api/v1/copilot/operations/cost-optimization | Cost optimization |
| GET | /api/v1/copilot/operations/health | Health check |

## Getting Started
- Build: `mvn -f operations-copilot-service/pom.xml compile`
- Test: `mvn -f operations-copilot-service/pom.xml test`
- Run: `mvn spring-boot:run -f operations-copilot-service/pom.xml`
- API Docs: http://localhost:8108/swagger-ui.html
