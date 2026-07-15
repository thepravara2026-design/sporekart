# Sprint 24 Part 6 — Enterprise Product Organization, Taxonomy & Hierarchy

## Overview

Establishes the permanent Product Organization and Taxonomy foundation for the SporeKart Enterprise PIM platform. All data is mock. No backend, no database.

## Files Created

### Organization Module (`src/admin/modules/products/organization/`)

| File | Purpose |
|------|---------|
| `types.ts` | Domain types: OrgCategory, OrgCollection, OrgBrand, OrgTag, CategoryNode, OrgActivityEvent, filters, sorts |
| `permissions.ts` | 4-tier role matrix (viewer → administrator) with `canOrg()` |
| `OrganizationPage.tsx` | Main page with 11-section workspace, permission gating |
| `Organization.css` | Responsive grid/sidebar layout, hover/focus states |

### Mock Data (`mock/`)

| File | Data |
|------|------|
| `mockCategories.ts` | 32 categories across 6 roots, up to 2 levels deep, with codes, icons, featured flags |
| `mockCollections.ts` | 16 collections across 7 types (seasonal, featured, trending, recommended, campaign, product, administrator) |
| `mockBrands.ts` | 11 brands with status, country, manufacturer, priority, featured flags |
| `mockTags.ts` | 29 tags across 8 types (keyword, search, seo, marketing, campaign, internal, product, color) |
| `mockActivity.ts` | 15 timeline events |

### State (`state/`)

| File | Purpose |
|------|---------|
| `useOrganizationState.ts` | Central hook: section nav, search/filter/sort, category tree with expand/collapse, selection, preview |
| `useAnalytics.ts` | Derived analytics: category/brand/collection KPIs, health scores, distribution charts |
| `OrganizationNav.tsx` | Config-driven workspace navigation (11 sections) |

### Components (`components/`)

| Component | Purpose |
|-----------|---------|
| `OrganizationDashboard` | Overview with KPIs, health bars, collection distribution, quick stats, activity feed |
| `CategoryManager` | Tree + detail panel layout |
| `CategoryTree` | Recursive tree with expand/collapse, expand all/collapse all, depth indentation |
| `CollectionManager` | Card grid with type chips, detail panel on select |
| `BrandManager` | Table view with brand/manufacturer/country/products/status columns |
| `TagManager` | Grouped by type, detail panel on tag click, color indicators |
| `CategoryAnalytics` | KPI cards (total/active/inactive/archived/featured), largest categories, unused categories, health score |
| `BrandAnalyticsDashboard` | Brand KPIs, top brands by products, recently added |
| `CollectionExplorer` | Card grid with type filtering, detail panel |
| `ProductAssignment` | Single/bulk assignment UI with edit mode, add/remove placeholders |
| `BulkOrganization` | 10 bulk operations in 3 groups: Assignments, Organization, Management |
| `ActivityTimeline` | Vertical timeline with icons, color-coded by event type |
| `DetailPanel` | Reusable metadata panel with header and rows |
| `OrganizationToolbar` | Search + sort + selection bar |
| `OrganizationEmptyStates` | No search results, no data, permission denied |
| `OrganizationLoading` | Skeleton tree, table, and card loading states |

### Preview Routes

| Route | Content |
|-------|---------|
| `/preview/products/organization/workspace` | Full organization workspace |
| `/preview/products/organization/hierarchy` | Interactive category tree |
| `/preview/products/organization/collections` | Collection explorer |
| `/preview/products/organization/info` | Architecture documentation |

Also available within the existing product preview at `/preview/products/organization/*`.

## Architecture

- **Taxonomy Model**: 6 root categories → 26 children (max 2 levels deep). Categories support codes (FRESH, DRIED, SPAWN, etc.), icons, visibility/featured flags, display order, and product counts.
- **Collection Model**: 7 types (seasonal, featured, trending, recommended, campaign, product, administrator). Supports date ranges for seasonal/campaign collections.
- **Brand Model**: 11 brands with full metadata (country, manufacturer, website, priority, notes, status).
- **Tag Model**: 29 tags in 8 types with usage counts and color support.
- **Assignment Model**: Single and bulk assignment UI with edit mode showing available/assigned entities.

## Quality Gate

- Organization Workspace ✓
- Category Management ✓
- Category Tree (expand/collapse) ✓
- Collection Management ✓
- Brand Management ✓
- Tag Management ✓
- Product Assignment ✓
- Bulk Organization ✓
- Category Analytics ✓
- Brand Analytics ✓
- Collection Explorer ✓
- Responsive (grid → single column at 1023px) ✓
- Accessibility (aria-current, aria-label, role, keyboard) ✓
- Performance (React.memo, useMemo, useCallback) ✓
- Existing modules unaffected ✓
- Documentation ✓

## Recommendations for Sprint 24 Part 7

- Product Pricing Engine
- Commercial Rules Framework
- Tax Configuration
- Discount & Promotion Management
- Price Optimization Dashboard
