# KPI Components

## Overview

Six KPI components for displaying metrics, trends, growth, comparisons, and target progress. Designed for dashboard tiles, report headers, and executive summaries.

---

## When to Use Each

| Component | Purpose | Use When |
|-----------|---------|----------|
| `MetricTile` | Single metric with label | Any dashboard tile showing one number |
| `TrendIndicator` | Direction arrow | Showing whether a metric moved up, down, or stayed flat |
| `GrowthIndicator` | Growth rate with direction | Period-over-period growth (e.g. "+12.5%") |
| `PercentageChange` | Percentage difference | Change calculation between two values |
| `ComparisonMetric` | Side-by-side metrics | Comparing two related metrics (e.g. actual vs target) |
| `TargetProgress` | Progress toward a goal | Showing X of Y completed |

---

## MetricTile

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `string \| number` | — | Yes | Metric value |
| `label` | `string` | — | Yes | Metric label |
| `icon` | `ReactNode` | — | No | Leading icon |
| `trend` | `'up' \| 'down' \| 'flat'` | — | No | Trend direction |
| `trendValue` | `string` | — | No | Trend text (e.g. `"+5.2%"`) |
| `format` | `'number' \| 'currency' \| 'percentage'` | — | No | Value formatting |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Tile size |
| `loading` | `boolean` | `false` | No | Show skeleton |
| `onClick` | `() => void` | — | No | Click handler |
| `className` | `string` | — | No | Additional CSS classes |

---

## TrendIndicator

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `direction` | `'up' \| 'down' \| 'flat'` | — | Yes | Trend direction |
| `value` | `string` | — | No | Display value |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Size |
| `color` | `'positive' \| 'negative' \| 'neutral'` | — | No | Override color |
| `className` | `string` | — | No | Additional CSS classes |

---

## GrowthIndicator

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Growth percentage |
| `period` | `string` | — | No | Period label (e.g. `"vs last month"`) |
| `inverted` | `boolean` | `false` | No | Invert positive/negative colors (for cost metrics) |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Size |
| `className` | `string` | — | No | Additional CSS classes |

---

## PercentageChange

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `current` | `number` | — | Yes | Current value |
| `previous` | `number` | — | Yes | Previous value |
| `showValue` | `boolean` | `true` | No | Show current/previous values |
| `label` | `string` | — | No | Metric label |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Size |
| `className` | `string` | — | No | Additional CSS classes |

---

## ComparisonMetric

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `primary` | `{ label: string, value: string \| number }` | — | Yes | Primary metric |
| `secondary` | `{ label: string, value: string \| number }` | — | Yes | Comparison metric |
| `difference` | `string` | — | No | Calculated difference |
| `direction` | `'row' \| 'col'` | `'row'` | No | Layout direction |
| `className` | `string` | — | No | Additional CSS classes |

---

## TargetProgress

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `current` | `number` | — | Yes | Current value |
| `target` | `number` | — | Yes | Target value |
| `label` | `string` | — | No | Metric label |
| `unit` | `string` | — | No | Unit (e.g. `"units"`, `"$"`) |
| `showPercentage` | `boolean` | `true` | No | Show percentage text |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Size |
| `color` | `string` | — | No | Override progress color |
| `className` | `string` | — | No | Additional CSS classes |

---

## Usage Examples

### Dashboard metric tile

```tsx
import { MetricTile } from '@sporekart/ui';

<MetricTile
  value="$1,284,500"
  label="Total Revenue"
  trend="up"
  trendValue="+12.3%"
  format="currency"
  onClick={() => navigate('/reports/revenue')}
/>
```

### Growth indicator

```tsx
import { GrowthIndicator } from '@sporekart/ui';

<GrowthIndicator value={-3.2} period="vs last quarter" />
```

### Percentage change

```tsx
import { PercentageChange } from '@sporekart/ui';

<PercentageChange
  current={1520}
  previous={1340}
  label="Active Users"
/>
```

### Side-by-side comparison

```tsx
import { ComparisonMetric } from '@sporekart/ui';

<ComparisonMetric
  primary={{ label: 'Actual', value: '$425K' }}
  secondary={{ label: 'Target', value: '$500K' }}
  difference="-$75K (-15%)"
/>
```

### Target progress

```tsx
import { TargetProgress } from '@sporekart/ui';

<TargetProgress
  current={780}
  target={1000}
  label="Units Sold"
  unit="units"
  size="lg"
/>
```

### Metric tile in loading state

```tsx
<MetricTile
  value="—"
  label="Pending Approvals"
  loading
/>
```
