# Enterprise Product Analytics & Intelligence Platform

> Reference architecture for the SporeKart Enterprise PIM Analytics module. Part of Phase 9, Sprint 24 Part 11.

## Overview

The Analytics & Intelligence Platform provides administrators with comprehensive visibility into the product catalog ecosystem. It aggregates data across all other PIM modules (organization, pricing, variants, SEO, marketplace, publishing, validation, compliance) into dashboards, KPIs, charts, insights, and reports.

## Module Architecture

```
AnalyticsPage (workspace shell)
├── AnalyticsToolbar (search + filters)
├── AnalyticsNav (16-section sidebar)
└── Content Area (section-switched)
    ├── ExecutiveDashboard
    ├── CatalogHealthDashboard
    ├── ProductQuality
    ├── CategoryAnalyticsView
    ├── BrandAnalyticsView
    ├── VariantAnalyticsView
    ├── KpiCenter
    ├── SeoAnalyticsView
    ├── MarketplaceAnalyticsView
    ├── PublishingAnalyticsView
    ├── InsightsPanel
    ├── ReportCenter
    ├── AnalyticsSettings
    └── AnalyticsHelp
```

## Chart Components

### Design Principles

1. **Pure SVG** — All 8 chart types are implemented using raw SVG elements (`<svg>`, `<path>`, `<rect>`, `<circle>`, `<text>`, `<line>`). No canvas, no WebGL, no external libraries.
2. **Theme compliance** — Every color reference uses CSS variable tokens (`var(--color-chart-1)` through `var(--color-chart-7)`, `var(--color-text-primary)`, etc.). Dark/light mode works without code changes.
3. **Accessibility** — Every chart has `role="img"`, `aria-label` describing the chart, semantic structure.
4. **Responsive** — Width is 100% with `viewBox` for scaling. Height is configurable via props.
5. **Performance** — `React.memo` wraps every component. `useMemo` caches derived data (segments, scales, paths).
6. **TypeScript** — Every chart exports a prop interface. No implicit any.

### Component Reference

#### LineChart (`config: ChartConfig`, `height?: number`, `title?: string`)
- SVG polyline with circle dots at each data point
- Auto-scaled Y-axis with 5 tick marks
- X-axis labels from config.labels
- Legend for each series
- Light grid lines for readability

#### BarChart (`config: ChartConfig`, `height?: number`, `title?: string`, `horizontal?: boolean`)
- Grouped bars (multiple series) or single series
- Optional horizontal layout (swap axes)
- Labels on both axes
- Legend

#### AreaChart (`config: ChartConfig`, `height?: number`, `title?: string`)
- Line chart with filled area below the line
- Gradient fill using SVG `<linearGradient>` + `<stop>` elements
- Dots at each data point
- Auto-scaled Y-axis

#### PieChart (`data: ChartDataPoint[]`, `size?: number`, `title?: string`, `showLegend?: boolean`)
- SVG arc segments with `path` commands
- Percentage labels on each segment
- Auto-color palette if no color specified
- Optional legend below

#### DonutChart (`data?: ChartDataPoint[]`, `config?: ChartConfig`, `size?: number`, `title?: string`, `centerLabel?: string`, `centerValue?: string`)
- Pie chart with a hole (donut)
- Accepts either `data[]` (label/value/color) or `config` (ChartConfig format)
- Center label + value for summary display
- Segment labels in legend below

#### ProgressChart (`value: number`, `max?: number`, `label: string`, `color?: string`, `size?: 'sm'|'md'|'lg'`, `showLabel?: boolean`)
- Circular progress ring
- Color changes based on thresholds: green (≥80), orange (≥50), red (<50)
- sm=80px, md=120px, lg=160px diameter
- Label + percentage text below

#### ScoreCard (`label: string`, `value: number|string`, `unit?: string`, `trend?: 'up'|'down'|'neutral'`, `trendValue?: string`, `icon: string`, `color: string`)
- Card layout with icon, label, large value, optional unit
- Optional trend indicator (arrow + value)
- Background tinted with the color at low opacity
- Compact, fits in grid layouts

#### HeatMap (`data: {row, col, value}[]`, `rows: string[]`, `cols: string[]`, `title?: string`)
- Grid of colored cells
- Color intensity from green (low) through yellow to red (high) using RGB interpolation
- Row labels (left) and column labels (top)
- Hover tooltip showing row, column, and value

## Data Flow

```
useAnalyticsState() ─── mock data (static imports)
       │
       ├── section ──────► AnalyticsNav ──────► renderSection()
       ├── search ───────► AnalyticsToolbar
       ├── filters ──────► (future: filter logic)
       ├── kpiCards ─────► ExecutiveDashboard, KpiCenter
       ├── catalogHealth ─► CatalogHealthDashboard
       ├── qualityMetrics ► ProductQuality
       ├── category... ───► CategoryAnalyticsView
       ├── brand... ─────► BrandAnalyticsView
       ├── variant... ───► VariantAnalyticsView
       ├── seo... ───────► SeoAnalyticsView
       ├── marketplace... ► MarketplaceAnalyticsView
       ├── publishing... ► PublishingAnalyticsView
       ├── chart... ─────► LineChart, BarChart, DonutChart
       ├── insights ─────► InsightsPanel
       ├── reports ──────► ReportCenter
       └── activity ─────► (future: activity feed)
```

## State Management

`useAnalyticsState()` is a standalone hook (not a React Context) that provides:

- **Navigation**: `section` + `setSection` for the 16-section sidebar
- **Search**: `search` + `setSearch` for the toolbar
- **Filters**: `filters` + `setFilters` with `dateRange`, `category`, `brand`, `status`
- **Derived**: `activeFilterCount` — number of active filters; `clearAllFilters` — reset to defaults
- **Mock data**: All 11 mock datasets returned as read-only values

## File Count: 27 files

- 1 types.ts, 1 permissions.ts
- 4 mock data files
- 2 state files (hook + nav)
- 8 chart components
- 15 business components (13 dashboards + toolbar + loading/empty)
- 1 CSS, 1 page, 1 preview

## Future Roadmap

| Feature | Description |
|---------|-------------|
| Real-time data | WebSocket or polling for live updates |
| BI integration | Power BI / Tableau embed |
| Scheduled reports | Cron-based PDF/CSV generation and email |
| AI insights | NLP-based anomaly detection and recommendations |
| Custom dashboards | Drag-and-drop chart builder |
| Export | PDF, CSV, Excel with configurable columns |
| Multi-warehouse | Regional inventory analytics |
| Supplier analytics | Supplier performance, lead times, defect rates |
| Trend forecasting | Time-series prediction for catalog growth |
| User analytics | Per-user dashboard customization and favorites |

## Related Documents

- [Sprint 24 Part 11](./sprint-24-part-11.md) — Sprint documentation
- [Validation Framework](./validation-framework.md) — Validation data source for health metrics
- [Publishing Workflow](./publishing-workflow.md) — Publishing data source for publishing analytics
- [Organization Taxonomy](./organization-taxonomy.md) — Category/brand data sources
- [Pricing Architecture](./pricing-architecture.md) — Pricing data source
- [SEO & Marketplace](./seo-marketplace.md) — SEO/marketplace data sources
