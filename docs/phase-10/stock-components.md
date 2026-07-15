# Stock Engine — Component Library

## Components

### StockTable
Reusable data table for stock records with:
- Sortable columns (sku, warehouse, availability, health, quantities)
- Column visibility toggle
- Bulk selection with select-all checkbox
- Row click → navigate to stock profile
- Skeleton loading state
- Empty state with action button
- Responsive: collapses to card layout on mobile

### StockStateBadge
Renders a colored badge for any of the 15 stock states:
- Each state has a unique color (`--color-{state}`)
- Shows state label with dot indicator
- Vanilla CSS, no icon library dependency

### StockHealthBadge
Renders health level with color coding:
- healthy → green, low → yellow, critical → red
- overstock → orange, damaged → darkred, out_of_stock → gray

### AvailabilityBadge
Renders availability level with distinct visual:
- available → green check, limited → yellow clock
- pre_order → blue arrow, out_of_stock → red x
- overstock → orange, damaged → red alert

### StockTimeline
Vertical timeline component rendering events chronologically:
- Icon per event type
- User attribution
- Quantity delta display
- Timestamp formatting
- Filterable by event type
- Lazy-loads 50 events at a time

### StockSummaryCards
Row of metric cards showing:
- Total Stock Items
- Total Quantity
- Reserved
- Incoming
- Damaged
- Health Score (avg)
