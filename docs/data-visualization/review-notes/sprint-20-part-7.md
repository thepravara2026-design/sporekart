# Review Notes — Sprint 20 Part 7: Data Visualization & Analytics

## Routes to Check

| Route | What to Review |
|-------|----------------|
| `/design-system/charts` | All chart types: line, area, bar, pie, radial, scatter, heatmap |
| `/design-system/kpis` | MetricTile, TrendIndicator, GrowthIndicator, PercentageChange, ComparisonMetric, TargetProgress |
| `/design-system/timelines` | Timeline, ActivityTimeline, OrderTimeline, TrainingTimeline, AuditTimeline |
| `/design-system/calendars` | CalendarMonth, CalendarWeek, CalendarAgenda, CalendarDateRange |
| `/design-system/data-filters` | DatePicker, DateRangePicker, FilterChips, SearchFilter, QuickFilter |
| `/design-system/export` | ExportMenu + export/print hooks |

---

## Validation Checklist

### General

- [ ] All components render without console errors
- [ ] All components render without ESLint warnings
- [ ] TypeScript compilation: 0 errors (`npm run typecheck`)
- [ ] ESLint: 0 errors (`npm run lint`)
- [ ] Design tokens used everywhere — no hardcoded colors, spacing, or typography
- [ ] Light and dark mode both render correctly
- [ ] Loading, empty, and error states all show correct UI (not blank)

### Responsive

- [ ] Charts resize when viewport changes
- [ ] Mobile viewport: no overflow, legends stack or hide, axes simplify
- [ ] Tablet viewport: compact layout, touch targets ≥ 44px
- [ ] Desktop viewport: full feature set visible

### Accessibility

- [ ] SVG charts have `role="img"` with `aria-label` / `aria-description`
- [ ] Hidden data tables exist alongside all charts
- [ ] Color is never the sole differentiator (patterns, labels, shapes)
- [ ] `/design-system/calendars` keyboard navigation works
- [ ] `prefers-reduced-motion: reduce` — no animations play
- [ ] `prefers-contrast: high` — fills visible, text readable
- [ ] Tab order follows visual order on interactive charts
- [ ] Focus indicators visible on all interactive elements

### Per-Chart

#### LineChart

- [ ] All four variants render: straight, smooth, stepped, area
- [ ] Animation plays on first render (unless reduced motion)
- [ ] Area overlay shows gradient by default
- [ ] Hidden data table contains correct values

#### BarChart

- [ ] All five variants render: vertical, horizontal, grouped, stacked, comparison
- [ ] Value labels on bars (when `showValues` is true)
- [ ] Pattern fills visible on grouped/comparison variants
- [ ] Click handler fires with correct data

#### PieChart + Radial

- [ ] Pie, donut, semi-circle all render
- [ ] Donut centre label renders
- [ ] RadialProgress fills correctly
- [ ] CircularKPI shows trend indicator
- [ ] Gauge segments color at correct thresholds
- [ ] Hidden data table for pie chart

#### AreaChart

- [ ] Single area with gradient renders
- [ ] Stacked areas render
- [ ] Gradient respects opacity props

#### Timelines

- [ ] Timeline renders with correct orientation and alignment
- [ ] ActivityTimeline shows at least activity type icons
- [ ] OrderTimeline steps reflect order status correctly
- [ ] TrainingTimeline shows module progress and status
- [ ] AuditTimeline filter/search works
- [ ] Empty timeline shows appropriate message

#### Calendars

- [ ] CalendarMonth navigates months, shows events
- [ ] CalendarWeek shows time slots, renders events
- [ ] CalendarAgenda groups by date, limits items
- [ ] CalendarDateRange selection works, presets functional
- [ ] Keyboard navigation: Arrow keys, Page Up/Down, Enter/Space, Escape

#### KPI

- [ ] MetricTile renders with/without trend
- [ ] GrowthIndicator colors positive/negative correctly
- [ ] PercentageChange calculates correctly
- [ ] ComparisonMetric row/col layout works
- [ ] TargetProgress bar fills proportionally

#### Statistics

- [ ] SummaryBlock renders grid at correct columns
- [ ] StatisticGrid responsive columns
- [ ] NumberFormatter compact/locale options work
- [ ] CurrencyFormatter handles different currencies
- [ ] PercentageFormatter `inputFormat` works

#### Export

- [ ] ExportMenu shows all format options
- [ ] PNG export downloads an image
- [ ] CSV export downloads correct data
- [ ] Print opens print dialog

### Data Filters (Preview Only — Part 8)

- [ ] DatePicker opens/closes
- [ ] DateRangePicker selects range
- [ ] FilterChips renders and removable
- [ ] SearchFilter input works
- [ ] QuickFilter buttons highlight correctly

---

## What to Look for in Each Preview

1. **Does the component render?** — If the page or component is blank, check the browser console for errors.
2. **Are the props working?** — Toggle props via the Storybook-style controls or by editing the preview code.
3. **Does it look like the design?** — Compare against the Figma reference if available. Check spacing, color, typography.
4. **Does it respond?** — Resize the browser. Check mobile, tablet, and desktop layouts.
5. **Is it accessible?** — Tab through interactive elements. Verify screen reader announcements. Test with high contrast mode and reduced motion.
6. **Is it performant?** — Check for layout shifts, jank during resize, and console performance warnings.
7. **Are the tokens correct?** — Inspect CSS custom property values. No hardcoded colors or spacing should be present.
