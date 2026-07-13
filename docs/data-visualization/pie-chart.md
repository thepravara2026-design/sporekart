# PieChart & Radial Components

## Overview

PieChart renders pie, donut, and semi-circle variants for proportional data. Companion components include RadialProgress (circular progress), CircularKPI (value with label), and Gauge (speedometer-style).

---

## PieChart

### Variants

| Variant | Description | Use Case |
|---------|-------------|----------|
| `pie` | Full circle pie | Category share of total |
| `donut` | Pie with centre cutout | Similar to pie, with room for centre label |
| `semi-circle` | Half-circle pie (180°) | Dashboard widgets, limited space |

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `data` | `Array<{name: string, value: number, color?: string}>` | — | Yes | Slice data |
| `variant` | `'pie' \| 'donut' \| 'semi-circle'` | `'donut'` | No | Visual variant |
| `innerRadius` | `number` | `60` | No | Donut hole radius (donut variant) |
| `outerRadius` | `number` | — | No | Outer radius (auto-calculated) |
| `showLabels` | `boolean` | `true` | No | Show slice labels |
| `showValues` | `boolean` | `true` | No | Show value text |
| `showPercentages` | `boolean` | `true` | No | Show percentage labels |
| `valueFormatter` | `(value: number) => string` | — | No | Value format function |
| `labelFormatter` | `(name: string) => string` | — | No | Label format function |
| `centerLabel` | `string` | — | No | Content for the centre of donut |
| `animate` | `boolean` | `true` | No | Enable enter animation |
| `height` | `number` | `400` | No | Chart height |
| `title` | `string` | — | No | Accessible chart title |
| `description` | `string` | — | No | Accessible chart description |
| `onClick` | `(slice: object) => void` | — | No | Click callback on slice |
| `className` | `string` | — | No | Additional CSS classes |

---

## RadialProgress

Circular progress indicator.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Current value (0–100) |
| `size` | `number` | `120` | No | Diameter in px |
| `strokeWidth` | `number` | `8` | No | Progress ring thickness |
| `color` | `string` | theme primary | No | Progress color |
| `trackColor` | `string` | theme border | No | Track color |
| `showLabel` | `boolean` | `true` | No | Show percentage label in centre |
| `labelFormatter` | `(value: number) => string` | — | No | Custom label format |
| `animate` | `boolean` | `true` | No | Enable animation |

---

## CircularKPI

Circular KPI with value, label, and optional trend.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Metric value |
| `label` | `string` | — | Yes | Metric label |
| `max` | `number` | `100` | No | Maximum value |
| `size` | `number` | `140` | No | Diameter in px |
| `unit` | `string` | — | No | Unit suffix (e.g. `"%"`, `"kg"`) |
| `trend` | `'up' \| 'down' \| 'flat'` | — | No | Trend direction |
| `trendValue` | `string` | — | No | Trend text (e.g. `"+12%"`) |
| `color` | `string` | theme primary | No | Main color |

---

## Gauge

Speedometer-style gauge.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Current value |
| `min` | `number` | `0` | No | Minimum value |
| `max` | `number` | `100` | No | Maximum value |
| `segments` | `Array<{threshold: number, color: string}>` | — | No | Color segments (e.g. green→yellow→red) |
| `size` | `number` | `200` | No | Diameter in px |
| `showValue` | `boolean` | `true` | No | Show current value |
| `label` | `string` | — | No | Gauge label |
| `unit` | `string` | — | No | Unit of measurement |

---

## Usage Examples

### Donut pie chart

```tsx
import { PieChart } from '@sporekart/ui';

const data = [
  { name: 'Marketing', value: 35 },
  { name: 'Engineering', value: 40 },
  { name: 'Operations', value: 25 },
];

<PieChart
  data={data}
  variant="donut"
  centerLabel="Total"
  title="Department Budget Split"
/>
```

### Semi-circle gauge-style pie

```tsx
<PieChart
  data={[
    { name: 'Completed', value: 75 },
    { name: 'Remaining', value: 25 },
  ]}
  variant="semi-circle"
  showLabels={false}
  title="Project Completion"
  centerLabel="75%"
/>
```

### Radial progress

```tsx
import { RadialProgress } from '@sporekart/ui';

<RadialProgress
  value={68}
  size={100}
  color="var(--color-success)"
  labelFormatter={(v) => `${v}%`}
/>
```

### Circular KPI with trend

```tsx
import { CircularKPI } from '@sporekart/ui';

<CircularKPI
  value={87}
  max={100}
  label="Customer Satisfaction"
  unit="%"
  trend="up"
  trendValue="+5%"
/>
```

### Gauge with color segments

```tsx
import { Gauge } from '@sporekart/ui';

<Gauge
  value={72}
  min={0}
  max={100}
  segments={[
    { threshold: 33, color: 'var(--color-danger)' },
    { threshold: 66, color: 'var(--color-warning)' },
    { threshold: 100, color: 'var(--color-success)' },
  ]}
  label="System Health"
  unit="%"
/>
```

---

## Accessibility

- SVG root elements include `role="img"` with descriptive `aria-label` and `aria-description`.
- Each pie slice has `role="graphics-symbol"` with an accessible `<title>` containing the name, value, and percentage.
- A hidden data table is rendered alongside pie charts with columns for category, value, and percentage.
- Pattern fills (hatching) are available as a `patterns` prop for color-independent identification.
- RadialProgress and CircularKPI include `role="progressbar"` with `aria-valuenow`, `aria-valuemin`, and `aria-valuemax`.
- Gauge includes `aria-valuenow`, `aria-valuemin`, `aria-valuemax`, and a text description of the gauge reading.
