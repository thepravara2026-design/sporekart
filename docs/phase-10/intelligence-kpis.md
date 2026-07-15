# Intelligence KPI Framework

## Executive KPI Data
The `ExecutiveKpiData` type aggregates top-level inventory metrics:
- inventoryItems (5,842)
- warehouses (5)
- stockRecords (12,430)
- inventoryValue ($8.4M)
- goodsReceipts (245)
- transfers (187)
- batchCount (450)
- nearExpiry (38)
- expiredCount (12)
- rejectedGoods (23)
- pendingInspection (31)
- warehouseCapacity (78%)

## KPI Metrics Display
8 KPI metric cards displayed on the Overview and Executive KPIs pages:
| Metric | Value | Trend | Variant |
|--------|-------|-------|---------|
| Inventory Items | 5,842 | ↑ | Success |
| Warehouses | 5 | → | Info |
| Stock Records | 12,430 | ↑ | Success |
| Inventory Value | $8.4M | ↑ | Info |
| Near Expiry | 38 | ↑ | Warning |
| Expired | 12 | ↓ | Danger |
| Batch Count | 450 | ↑ | Success |
| Warehouse Capacity | 78% | ↑ | Warning |

## Health Scores
6 health metrics scored 0–100:
- Inventory Health (87)
- Stock Health (82)
- Batch Health (74)
- Warehouse Health (91)
- Movement Health (79)
- Receiving Health (85)

Score thresholds: ≥80 green (success), ≥60 yellow (warning), <60 red (danger).
