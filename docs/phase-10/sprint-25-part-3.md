# Sprint 25 Part 3 — Enterprise Inventory Item Management Platform

## Objective
Build the Enterprise Inventory Item Management Platform. Every Product, Variant and SKU from Sprint 24 becomes an Inventory Item. This is the permanent Inventory Item Foundation that future Stock Management, Warehouse Operations, Procurement, Sales Orders and Returns will reference.

## Scope
- New module: `admin/modules/inventory-items/`
- Reuses certified components from Sprint 24 (Product Platform), Sprint 25 Part 1 (Inventory Foundation), and Sprint 25 Part 2 (Warehouse Platform)
- Mock Mode only — no backend, database, or API
- Token CSS variables only (`--color-*`, `--radius-*`, `--text-*`, `--space-component-gap`)
- WCAG 2.2 AA compliance
- Responsive 320–1920px

## Key Concepts
| Concept | Description |
|---------|-------------|
| Inventory Item Record | Mapped from a Product + Variant + SKU combination |
| Product Mapping | Links Sprint 24 Products to one or more Inventory Items |
| Variant Mapping | Links Sprint 24 Variants to Inventory Items |
| SKU Association | Each SKU maps to exactly one Inventory Item |
| Classification | Type (raw_material → supplies) + Grade (A+ → C) taxonomy |
| Lifecycle | draft → pending_approval → approved → active → frozen → suspended → discontinued → archived |
| Unit Management | Weight, volume, quantity units with conversion factors |

## Module Structure
```
admin/modules/inventory-items/
├── types.ts                  Inventory item types (Record, Classification, Lifecycle, Mapping)
├── constants.ts              Sections, roles/permissions, mock data, filters, units
├── utils.ts                  Filter, sort, paginate, variant helpers
├── contexts/                 InventoryItemWorkspaceContext (local provider + permissions)
├── hooks/                    useInventoryItemData, useInventoryItemFilters, etc.
├── services/                 inventoryItemMockService (25 mock items, product/variant/SKU mappings)
├── components/               Registry table, LifecycleTimeline, ClassificationBadge
├── layouts/                  Workspace layout with section nav, search, header/footer
├── dashboard/                Executive metrics, health scores, quick actions, recent activity
├── workspace/                Workspace container rendering dashboard
├── pages/                    11 workspace pages (registry, products, variants, SKU, units, etc.)
├── preview/                  6-tab preview app (dashboard, registry, profile, mapping, SKU, classification)
├── InventoryItemPage.tsx     Admin route entry point
└── index.ts                  Module barrel
```

## Routes
| Route | Component |
|-------|-----------|
| `/admin/inventory-items` | `InventoryItemPage` → `InventoryItemWorkspaceLayout` |
| `/preview/inventory/items/*` | `InventoryItemPreviewApp` (6 tabs) |

## Permissions
| Role | Permissions |
|------|-------------|
| viewer | view |
| inventory_operator | view, create, edit, reports |
| inventory_manager | view, create, edit, archive, restore, bulk_operations, reports, settings |
| warehouse_manager | view, create, edit, archive, reports |
| administrator | view, create, edit, archive, restore, bulk_operations, reports, settings |

## Preview
Six routes at `/preview/inventory/items/`:
- Dashboard — executive metrics and health
- Registry — searchable/filterable/sortable table with bulk select
- Profile — single item detail with lifecycle timeline
- Mapping — product-to-item mapping cards
- SKU — SKU association table
- Classification — type and grade taxonomy display

## DO NOT MODIFY
- Customer Website, Auth, RBAC, Supabase, DB
- Sprint 24 Product Platform
- Sprint 25 Part 1 (Inventory Foundation)
- Sprint 25 Part 2 (Warehouse Platform)
- Enterprise Design System
