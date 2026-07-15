# Movement Workspace & Navigation

## Overview
The Movement Workspace provides 14 sections for managing all inventory movement operations, accessible via a sidebar within the `/admin/movements` route.

## Workspace Sections

| # | Section ID | Label | Description |
|---|-----------|-------|-------------|
| 1 | `overview` | Overview | Movement dashboard with metrics and recent activity |
| 2 | `transactions` | Transactions | Full transaction registry (sortable, paginated, selectable table) |
| 3 | `movements` | Movements | Transactions grouped by movement type |
| 4 | `transfers` | Transfers | Warehouse transfer requests with source→destination view |
| 5 | `adjustments` | Adjustments | Stock adjustments with type and quantity difference |
| 6 | `goods-receipt` | Goods Receipt | Goods receipt with accepted/rejected/pending quantities |
| 7 | `goods-issue` | Goods Issue | Goods issue with reason and destination tracking |
| 8 | `history` | History | Filterable movement timeline |
| 9 | `validation` | Validation | Transaction validation and quality checks (7 rules) |
| 10 | `analytics` | Analytics | Movement analytics with 7 KPI cards |
| 11 | `reports` | Reports | Operational reports with 8 report type tabs |
| 12 | `audit` | Audit Trail | Full transaction audit trail (field-level changes) |
| 13 | `settings` | Settings | Movement preferences (3 groups of toggle/select/number) |
| 14 | `help` | Help | FAQ and documentation |

## Layout Components

### MovementWorkspaceLayout
- Header: Title, search input (by reference/product/warehouse), Filter/Export/New Movement buttons
- Sidebar: 14-section nav with active state highlighting
- Main content area: Renders active section page
- Responsive: Sidebar hides on mobile with hamburger toggle, overlay on mobile

### MovementWorkspace
- Reads `activeSection` from `MovementWorkspaceContext`
- Maps section IDs to page components via `PAGE_MAP`
- Wraps everything in `MovementWorkspaceLayout`

## Search
- Located in the layout header
- Searches by: transaction ID, reference number, product, SKU, batch code, warehouse
- Results are passed to the active section page (when applicable)

## Role-Based Access
6 roles with graduated permissions: `viewer` → `inventory_operator` → `warehouse_operator` → `inventory_manager` → `warehouse_manager` → `administrator`. Each role has a defined set of `MovementPermission` entries (view, create, approve, reject, archive, restore, reports, analytics, audit).
