# Stock Reports & Health Analysis

## Reports Section
The Reports section provides three pre-configured report views:

### 1. Stock Health Report
- Table of all stock records sorted by health score (ascending)
- Color-coded health badges
- Quick breakdown of damaged/expired/blocked quantities
- Export-ready (downloadable data structure)

### 2. Availability Report
- Table filtered to show current availability status per item/warehouse
- Availability badges with quantity breakdown
- Sorted by least available first
- Action buttons for low-stock items

### 3. Distribution Report
- Pie/bar representation of stock across warehouses
- Quantity distribution by state group (Active, Problem, Inspection, Adjustment, Future)
- Warehouse-level breakdown
- Responsive chart layout

## Stock Health Levels
| Level | Threshold | Color | Action Required |
|-------|-----------|-------|-----------------|
| healthy | ≥60% and no damage | --color-success | None |
| low | 21–59% available | --color-warning | Reorder soon |
| critical | ≤20% available | --color-danger | Reorder immediately |
| overstock | >80% full | --color-orange | Consider reduction |
| damaged | any damaged/expired | --color-danger | Investigate |
| out_of_stock | total = 0 | --color-neutral | Restock |

## Health Score Calculation
```
healthScore = Math.round(((total - (damaged + expired + blocked)) / total) * 100)
```
- Ranges from 0 (worst) to 100 (best)
- Only problem states (damaged, expired, blocked) reduce the score
- Future states (manufacturing, transit, consignment) are excluded from health calculation
