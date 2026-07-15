# Stock State Engine — 15-State Lifecycle

## State Definitions

| # | State | Color | Priority | Description |
|---|-------|-------|----------|-------------|
| 1 | Available | --color-success | normal | Stock available for sale or use |
| 2 | Reserved | --color-info | high | Soft-held for an active order/requisition |
| 3 | Incoming | --color-warning | normal | Expected from purchase order or transfer |
| 4 | Allocated | --color-primary | high | Committed to a specific order (hard allocation) |
| 5 | Damaged | --color-danger | critical | Physically damaged, requires disposition |
| 6 | Expired | --color-danger | critical | Past expiry date, requires disposal |
| 7 | Blocked | --color-neutral | high | Held for quality, compliance, or legal hold |
| 8 | Quarantine | --color-warning | critical | Under quality inspection or contamination check |
| 9 | Inspection | --color-info | high | Awaiting inspection results |
| 10 | Returned | --color-warning | normal | Returned from customer, pending disposition |
| 11 | Lost | --color-danger | critical | Inventory discrepancy — recorded as lost |
| 12 | Adjustment Pending | --color-warning | high | Quantity adjustment awaiting approval |
| 13 | Future Manufacturing | --color-info | low | Planned production output |
| 14 | Future Transit | --color-info | low | In-transit between warehouses |
| 15 | Future Consignment | --color-neutral | low | On consignment at customer location |

## State Groups
- **Active**: Available, Reserved, Incoming, Allocated
- **Problem**: Damaged, Expired, Blocked, Quarantine, Lost
- **Inspection**: Inspection, Returned
- **Adjustment**: Adjustment Pending
- **Future**: Future Manufacturing, Future Transit, Future Consignment

## Derivation
- **Availability** is derived from state quantities (available > 0 → available, incoming > 0 → pre_order, etc.)
- **Health** is derived as a weighted score combining damaged/expired/blocked ratios with available quantity
