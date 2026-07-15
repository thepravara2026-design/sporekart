# Inventory Items Workspace

## Overview
The Inventory Items Workspace is the central UI hub for the Inventory Item Management Platform. It provides 12 sections accessible via a sidebar navigation, with a consistent layout pattern matching the Sprint 25 Part 2 Warehouse Workspace.

## Layout Structure
```
┌─────────────────────────────────────────────────┐
│ Header (title, subtitle, role switcher, actions) │
├─────────────────────────────────────────────────┤
│ Breadcrumb: Admin / Inventory Items / Section     │
├─────────────────────────────────────────────────┤
│ Search bar + Workspace banner                    │
├──────────────────┬──────────────────────────────┤
│ Sidebar (12      │ Main content area             │
│ sections)        │ (section-specific page)       │
├──────────────────┴──────────────────────────────┤
│ Activity feed placeholder                        │
├─────────────────────────────────────────────────┤
│ Footer (Mock Mode indicator, profile button)     │
└─────────────────────────────────────────────────┘
```

## Workspace Sections
| Section | Page | Description |
|---------|------|-------------|
| Overview | Dashboard | Executive metrics and health |
| Inventory Items | Registry | Searchable/filterable item table |
| Products | Product Mapping | Product-to-item link cards |
| Variants | Variant Mapping | Variant-to-item link table |
| SKU Mapping | SKU Association | SKU mapping table |
| Units | Unit Management | Unit config cards with conversions |
| Classification | Classification | Type and grade taxonomy display |
| Lifecycle | Lifecycle Management | Stage distribution + timeline |
| Validation | Validation | Validation metrics + placeholder |
| Reports | Reports | Report template cards with export |
| History | History | Change audit trail |
| Settings | Settings | Configuration section cards |

## Workspace Provider
Local `InventoryItemWorkspaceProvider` (not using global `PermissionProvider`):
- Manages current role, active section, search query
- Provides `can()` permission check function
- Initial role: `administrator`

## Responsive Behavior
- Desktop (≥1024px): sidebar + main content side by side
- Tablet (768–1023px): collapsible sidebar
- Mobile (<768px): stacked layout, full-width sections

## Reused Components
From Sprint 25 Part 1 (Inventory Foundation):
- `SectionHeader`, `WorkspaceBanner`, `SearchComponent`
- `MetricCard`, `StatisticsGrid`, `SummaryCard`, `QuickActionCard`, `RecentActivityCard`

From Design System:
- `Icon`, `StatusBadge`
- Token CSS variables for all spacing, sizing, and colors
