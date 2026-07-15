# Warehouse Allocation

## Overview
Warehouse allocation assigns received goods to specific storage locations within a warehouse. The framework supports a 5-level location hierarchy and 4 storage types.

## Location Hierarchy
```
Warehouse          → Main Warehouse - A, Cold Storage - B, etc.
  Zone             → Zone-1 through Zone-5
    Rack           → Rack-1 through Rack-15
      Shelf        → Shelf-A through Shelf-F
        Bin        → Bin-01 through Bin-30
```

## Storage Types
| Type | Description |
|------|-------------|
| `ambient` | Room temperature storage |
| `cold_storage` | Refrigerated storage |
| `dry` | Dry goods storage |
| `hazardous` | Hazardous materials storage |

## AllocationRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Allocation ID (ALC-001) |
| `warehouse` | string | Assigned warehouse |
| `zone` | string | Warehouse zone |
| `rack` | string | Rack number |
| `shelf` | string | Shelf letter |
| `bin` | string | Bin number |
| `storageType` | string | Storage category |
| `status` | AllocationStatus | 5 states |

## Allocation Page
- Full table with location hierarchy columns
- Status badge for allocation progress
- 20 mock allocation records

## Batch Association

### BatchAssignmentRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `batchCode` | string | Assigned batch code |
| `lotCode` | string | Lot number |
| `expiryDate` | string | Expiry date |
| `shelfLife` | string | Shelf life duration |
| `qualityStatus` | string | Quality status from batch |

### Batch Assignment Page
- Card-based layout showing batch assignments
- Each card shows: batch code, lot code, expiry date, shelf life, quality status
- 20 mock batch assignments

## Future Integration
- Automatic bin suggestion based on product/storage type
- Integration with warehouse management zones
- Real-time capacity tracking
- Cold chain compliance logging
- Auto batch creation from receipt data
