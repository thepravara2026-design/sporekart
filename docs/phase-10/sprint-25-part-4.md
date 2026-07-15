# Sprint 25 Part 4 — Enterprise Stock Management Engine & Inventory State Management

## Objective
Build the Enterprise Stock Management Engine & Inventory State Management Platform. Every Inventory Item created in Part 3 now maintains one or more Stock Records with lifecycle states. This Stock Engine becomes the foundation for Warehouse Operations, Order Management, Procurement, Manufacturing, Shipping, Returns, and all future inventory transactions.

## Scope
- New module: `admin/modules/stock/`
- Reuses certified components from Sprint 24 (Product Platform), Sprint 25 Parts 1–3 (Inventory Foundation, Warehouse, Inventory Items)
- Mock Mode only — no backend, database, or API
- Token CSS variables only (`--color-*`, `--radius-*`, `--text-*`, `--space-component-gap`)
- WCAG 2.2 AA compliance
- Responsive 320–1920px

## Key Concepts
| Concept | Description |
|---------|-------------|
| Stock Record | Tracks quantities across 15 states for one Inventory Item in one Warehouse |
| Stock State | 15-state lifecycle (Available, Reserved, Incoming, Allocated, Damaged, Expired, Blocked, Quarantine, Inspection, Returned, Lost, Adjustment Pending, Future Manufacturing, Future Transit, Future Consignment) |
| Availability Level | Derived state — available, limited, pre_order, out_of_stock, overstock, damaged |
| Stock Health | 5-level health score — healthy, low, critical, overstock, damaged, out_of_stock |
| Stock Timeline | Auditable log of every state transition with user attribution |
| Reservation | Soft-hold on quantity for future orders |
| Stock Record → Inventory Item | M:N relationship — one item can have stock at multiple warehouses |

## Module Structure
```
admin/modules/stock/
├── types.ts                  Stock types (StockRecord, StockState, StockHealth, AvailabilityLevel, Reservation, StockTimelineEvent, StockMetric, RecentActivity)
├── constants.ts              Sections (15), states (15), health levels, roles/permissions, filters, mock metrics, empty states
├── utils.ts                  Filter, sort, paginate, health/availability/compute helpers
├── contexts/                 StockWorkspaceContext (local provider + permissions)
├── hooks/                    useStockData, useStockFilters, useStockPermissions, useStockSearch, useStockResponsive
├── services/                 stockMockService (25 stock records, timeline events, reservations)
├── components/               StockTable, StockStateBadge, StockHealthBadge, AvailabilityBadge, StockTimeline, StockSummaryCards
├── layouts/                  StockWorkspaceLayout with 15-section sidebar, header, footer, search, banner
├── dashboard/                StockDashboard with metrics, health, distribution, quick actions, recent activity
├── workspace/                StockWorkspace rendering dashboard by default
├── pages/                    15 workspace pages (registry, all state pages, health, timeline, validation, reports, settings, help)
├── preview/                  6-tab preview app (dashboard, registry, profile, timeline, states, reports)
├── StockPage.tsx             Admin route entry point
└── index.ts                  Module barrel
```

## Routes
| Route | Component |
|-------|-----------|
| `/admin/stock` | `StockPage` → `StockWorkspaceLayout` |
| `/preview/stock/*` | `StockPreviewApp` (6 tabs) |

## Permissions
| Role | Permissions |
|------|-------------|
| viewer | view |
| inventory_operator | view, create, edit, reports |
| warehouse_operator | view, create, edit, reports |
| inventory_manager | view, create, edit, archive, restore, bulk_operations, reports, settings |
| administrator | view, create, edit, archive, restore, bulk_operations, reports, settings |

## Preview
Six routes at `/preview/stock/`:
- Dashboard — executive metrics, health, distribution
- Registry — searchable/filterable/sortable stock table with bulk select
- Profile — single stock record detail with timeline
- Timeline — chronological event log with search/filter
- States — 15-state visual reference with colors/descriptions
- Reports — health, availability, and distribution reports

## DO NOT MODIFY
- Customer Website, Auth, RBAC, Supabase, DB
- Sprint 24 Product Platform
- Sprint 25 Part 1 (Inventory Foundation)
- Sprint 25 Part 2 (Warehouse Platform)
- Sprint 25 Part 3 (Inventory Items)
- Enterprise Design System
