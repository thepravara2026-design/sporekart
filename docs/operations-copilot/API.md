# Operations Copilot API Reference

Base URL: `http://localhost:8108/api/v1/copilot/operations`

## Unified Query
### POST /query
Route any operations intent to the appropriate engine.

```json
{
  "query": "string",
  "intent": "inventory|procurement|orders|warehouse|logistics|forecast|supply_chain|kpi|decision|cost",
  "warehouseId": "string (optional)",
  "region": "string (optional)",
  "category": "string (optional)"
}
```

## Inventory
### POST /inventory
Get inventory status by warehouse and category.

**Parameters:** `warehouseId` (query), `category` (query)

**Response:** InventoryStatusResponse with SKU counts, values, and item details.

## Procurement
### POST /procurement
Get procurement recommendations.
```json
{
  "query": "string",
  "intent": "procurement",
  "vendorId": "string (optional)",
  "sku": "string (optional)",
  "urgency": "standard|expedited|emergency"
}
```

## Orders
### POST /orders
Analyze order queue.
**Parameters:** `warehouseId`, `status`

## Warehouse
### POST /warehouse
Analyze warehouse operations.
**Parameters:** `warehouseId`

## Logistics
### POST /logistics
Plan shipment and get courier options.
```json
{
  "query": "string",
  "intent": "logistics",
  "origin": "string (optional)",
  "destination": "string (optional)",
  "pincode": "string (optional)",
  "weight": 0 (optional)
}
```

## Forecast
### POST /forecast
Get demand forecast for a product.
```json
{
  "productId": "string",
  "region": "string (optional)",
  "forecastDays": 30 (optional)
}
```

## Dashboard
### GET /dashboard
Get operations overview dashboard.

## Alerts
### GET /alerts
Get active operations alerts.
**Parameters:** `category` (query, optional)

## Supply Chain
### POST /supply-chain
Monitor supply chain health.
```json
{
  "query": "string",
  "intent": "supply_chain",
  "vendorId": "string (optional)",
  "region": "string (optional)"
}
```

## KPIs
### POST /kpi
Get operational KPIs.
```json
{
  "category": "Fulfillment|Inventory|Warehouse|Logistics|Cost (optional)",
  "period": "string (optional)"
}
```

## Decision Support
### POST /decision
Get data-driven operational recommendations.
```json
{
  "query": "string",
  "intent": "increase_procurement|reduce_procurement|transfer_inventory|launch_promotions",
  "category": "string (optional)",
  "sku": "string (optional)"
}
```

## Cost Optimization
### POST /cost-optimization
Analyze operational costs.
```json
{
  "category": "string (optional)",
  "period": "string (optional)"
}
```

## Health
### GET /health
Health check — no auth required.
**Response:** `{"success": true, "message": "Operations Copilot is operational"}`
