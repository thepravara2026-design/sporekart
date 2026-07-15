# Expiry Management Framework

## Overview
The Expiry Management Engine tracks product freshness and shelf life across all batches. It provides a reusable framework for monitoring, alerting, and managing product expiry without implementing actual calculation logic.

## Expiry Fields
| Field | Description |
|-------|-------------|
| `productionDate` | Date of manufacture/production |
| `expiryDate` | End of shelf life (last usable date) |
| `bestBeforeDate` | Quality guarantee date (before expiry) |
| `manufacturingDate` | Manufacturing/packing date |
| `receivedDate` | Date received at warehouse |

## Expiry Status Framework (9 statuses)
| Status | Priority | Description |
|--------|----------|-------------|
| Fresh | 1 | Recently produced, full shelf life remaining |
| Healthy | 2 | Within optimal shelf life range |
| Monitor | 3 | Approaching mid-life, monitor regularly |
| Near Expiry | 4 | Close to expiry date |
| Critical | 5 | Critically close to expiry |
| Expired | 6 | Past expiry date |
| Blocked | 7 | Expiry blocked for compliance |
| Disposed | 8 | Disposed after expiry |
| Recalled | 9 | Recalled due to expiry concerns |

## Shelf Life Units
- Days, Weeks, Months, Years, Custom

## Remaining Days Calculation
```
remainingDays = Math.max(0, Math.ceil((expiryDate - now) / (1000 * 60 * 60 * 24)))
```

## Expiry Status Derivation
```
remaining <= 0  → expired
remaining <= 7  → critical
remaining <= 30 → near_expiry
remaining <= 60 → monitor
remaining <= 90 → healthy
remaining > 90  → fresh
```

## Future Extension Points
- Near Expiry Threshold (configurable days)
- Critical Expiry Threshold (configurable days)
- Auto-calculation of expiry from production date + shelf life
- Expiry notifications and alerts
- Bulk expiry date updates
