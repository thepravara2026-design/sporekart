# Sprint 24 Part 11 — Enterprise Product Analytics, Intelligence & Catalog Insights

> Companion to [analytics-intelligence.md](./analytics-intelligence.md). Mock Mode.

## Overview

Part 11 establishes the Enterprise Product Analytics, Intelligence & Catalog Insights layer for the SporeKart PIM platform. Administrators gain visibility into product health, catalog completeness, quality metrics, category/brand/variant distribution, SEO readiness, marketplace readiness, publishing status, and actionable insights — all through a pure-SVG chart system with zero external dependencies. All data is mock. No persistence, no real-time data, no BI tool integration.

## Architecture

### 16-Section Navigation (AnalyticsPage workspace)

The `AnalyticsPage` workspace provides a 16-section sidebar navigation:

| # | Section | Component |
|---|---------|-----------|
| 1 | Executive Overview | `ExecutiveDashboard` |
| 2 | Catalog Health | `CatalogHealthDashboard` |
| 3 | Product Quality | `ProductQuality` |
| 4 | Category Analytics | `CategoryAnalyticsView` |
| 5 | Brand Analytics | `BrandAnalyticsView` |
| 6 | Variant Analytics | `VariantAnalyticsView` |
| 7 | Pricing KPI | `KpiCenter` |
| 8 | SEO Analytics | `SeoAnalyticsView` |
| 9 | Marketplace Analytics | `MarketplaceAnalyticsView` |
| 10 | Publishing Analytics | `PublishingAnalyticsView` |
| 11 | Validation Health | `CatalogHealthDashboard` |
| 12 | Compliance Health | `CatalogHealthDashboard` |
| 13 | KPI Center | `KpiCenter` |
| 14 | Insights Panel | `InsightsPanel` |
| 15 | Report Center | `ReportCenter` |
| 16 | Settings / Help | `AnalyticsSettings` / `AnalyticsHelp` |

### File Layout

```
analytics/
├── types.ts                       — 30 interfaces, 16 section IDs, 4-tier roles, filters/sorts
├── permissions.ts                 — 4-tier role-based access
├── mock/
│   ├── mockAnalytics.ts           — 12 KPI cards, catalog health, 8 quality metrics, 6 categories, 8 brands, variant/SEO/marketplace/publishing analytics, product KPI, 6 chart configs
│   ├── mockInsights.ts            — 10 insights/recommendations
│   ├── mockReports.ts             — 9 analytics reports
│   └── mockActivity.ts            — 10 activity events
├── state/
│   ├── useAnalyticsState.ts        — Central state hook with mock data, search, filters, section navigation
│   └── AnalyticsNav.tsx            — 16-section sidebar navigation component
├── components/
│   ├── charts/                    — 8 pure-SVG chart components (see below)
│   ├── ExecutiveDashboard.tsx      — KPI grid, growth LineChart, recent activity
│   ├── CatalogHealthDashboard.tsx  — Overall score ring + 9 dimension ProgressChart bars
│   ├── ProductQuality.tsx          — Quality metric cards with severity coloring
│   ├── CategoryAnalyticsView.tsx   — Category table, BarChart, highlights
│   ├── BrandAnalyticsView.tsx      — Brand table with status badges, summary stat cards
│   ├── VariantAnalyticsView.tsx    — Stats bar, DonutChart, BarChart, attribute usage, SKU coverage
│   ├── SeoAnalyticsView.tsx        — Avg score ring, 6 coverage bars
│   ├── MarketplaceAnalyticsView.tsx — Channel BarChart, avg score ring, missing reqs + warnings
│   ├── PublishingAnalyticsView.tsx — DonutChart, health ring, status count grid
│   ├── KpiCenter.tsx               — 8 enterprise KPI ScoreCards
│   ├── InsightsPanel.tsx           — Severity-sorted insights with type filter tabs
│   ├── ReportCenter.tsx            — Report card grid with type badges
│   ├── AnalyticsToolbar.tsx        — Search input + clear filters
│   ├── AnalyticsLoading.tsx        — 3 skeleton variants (dashboard, chart, KPI)
│   └── AnalyticsEmptyStates.tsx    — 6 empty state variants
├── AnalyticsPage.tsx              — Main workspace page (16-section switch)
├── Analytics.css                  — Workspace layout, sidebar, responsive grid
└── preview/
    └── AnalyticsPreviewApp.tsx    — 6 preview routes (workspace, KPI, insights, reports, info)
```

### Chart System (8 Pure-SVG Components)

| Component | Purpose | Props |
|-----------|---------|-------|
| `LineChart` | Growth/trend lines | `config: ChartConfig`, `height`, `title` |
| `BarChart` | Category/KPI comparisons | `config`, `height`, `title`, `horizontal` |
| `AreaChart` | Cumulative/filled trends | `config`, `height`, `title` |
| `PieChart` | Distribution segments | `data[]`, `size`, `title`, `showLegend` |
| `DonutChart` | Status/health distribution | `data[]` or `config`, `size`, `title`, `centerLabel`, `centerValue` |
| `ProgressChart` | Single-metric completion | `value`, `max`, `label`, `color`, `size` (sm/md/lg) |
| `ScoreCard` | KPI card with trend | `label`, `value`, `unit`, `trend`, `trendValue`, `icon`, `color` |
| `HeatMap` | Multi-dimension intensity | `data[]`, `rows[]`, `cols[]`, `title` |

All charts use `React.memo`, `useMemo`, CSS var tokens, `role="img"` + `aria-label`, TypeScript interfaces. Zero external dependencies.

### Mock Data

| Entity | Count | Details |
|--------|-------|---------|
| KPI Cards | 12 | Total products, published, draft, archived, categories, brands, variants, avg score, marketplace ready, SEO ready, compliance ready, recently updated |
| Catalog Health | 1 | 9 dimensions: completion, validation, publishing, SEO, media, compliance, marketplace, accessibility, AI readiness |
| Quality Metrics | 8 | Missing info, images, variants, SEO, pricing, categories, review, compliance |
| Category Analytics | 6 | Fresh Produce, Dried Goods, Grow Kits, Spawn, Equipment, Training |
| Brand Analytics | 8 | FungaFarm, MycoLogic, SporeSwift, EcoMycelium, TruffleTrove, MushroomMate, GourmetGrow, BioFungi |
| Chart Configs | 6 | Growth, completion, variants, health, publishing, marketplace |
| Insights | 10 | SEO, images, publishing, marketplace, quality, duplicate |
| Reports | 9 | Catalog health, quality, SEO, marketplace, compliance, publishing, KPI scorecard, insights, brand |
| Activity Events | 10 | Dashboard views, exports, report generation, filter updates |

### Permissions (4-Tier)

| Role | Permissions |
|------|------------|
| `viewer` | View analytics |
| `manager` | View + generate reports |
| `business_analyst` | View + generate + export reports |
| `administrator` | All + dashboard customization |

## Preview Routes

| Route | Preview |
|-------|---------|
| `/preview/products/analytics/workspace` | Full 16-section workspace |
| `/preview/products/analytics/kpi` | KPI Center (8 KPIs) |
| `/preview/products/analytics/insights` | Insights Panel (10 insights) |
| `/preview/products/analytics/reports` | Report Center (9 reports) |
| `/preview/products/analytics/info` | Architecture notes |

## Key Design Decisions

1. **Pure SVG charts** — No external charting library (Chart.js, D3, Recharts). All 8 components are hand-built SVG.
2. **CSS var token theming** — All charts use the enterprise design system tokens `var(--color-*)`, `var(--text-*)`, `var(--space-*)`, `var(--radius-*)`. Dark/light theme compatible.
3. **Shared AnalyticsPage shell** — 16-section workspace with sidebar navigation, toolbar, search, filters. Consistent with Parts 6–10 pattern.
4. **Dual DonutChart API** — Accepts both `data[]` (label/value/color) and `config` (ChartConfig with labels/series). Backward compatible with simple and complex uses.
5. **Mock-first** — All data statically defined. No backend, no API calls, no persistence.

## Integration Points (Future)

- Real-time analytics via WebSocket/Polling
- BI tool integration (Power BI, Tableau)
- Automated report scheduling + email delivery
- PDF/CSV data export
- AI-powered insight generation
- Custom dashboard builder (drag-and-drop)
- Catalog trend prediction and forecasting
- Product lifecycle analytics
- Supplier performance analytics
- Multi-warehouse inventory analytics
