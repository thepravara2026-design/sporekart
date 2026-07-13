# LineChart

## Overview

Displays data points connected by straight, smooth, or stepped lines. Supports multi-series, area overlays, zoom/pan, and accessible data tables.

---

## Variants

| Variant | Description | Use Case |
|---------|-------------|----------|
| `straight` | Default. Points connected by straight line segments | Standard time-series data |
| `smooth` | Curved line through data points using spline interpolation | Flowing trends, financial data |
| `stepped` | Step-like connection between points | Discrete changes, inventory levels |
| `area` | Line with filled area underneath (gradient-supported) | Emphasizing magnitude + trend |

---

## Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `data` | `Array<Record<string, unknown>>` | — | Yes | Chart data array |
| `categories` | `string[]` | — | Yes | Data keys for each line series |
| `indexKey` | `string` | — | Yes | Key for the X-axis (e.g. `"month"`) |
| `variant` | `'straight' \| 'smooth' \| 'stepped' \| 'area'` | `'straight'` | No | Line variant |
| `colors` | `string[]` | theme colors | No | Series colors |
| `showDots` | `boolean` | `true` | No | Show data point dots |
| `dotSize` | `number` | `4` | No | Data point dot radius |
| `curve` | `boolean` | `false` | No | Enable curved lines (alias for smooth) |
| `gradient` | `boolean` | `false` | No | Enable gradient fill (area variant) |
| `animate` | `boolean` | `true` | No | Enable enter animation |
| `height` | `number` | `400` | No | Chart height in px |
| `title` | `string` | — | No | Accessible chart title |
| `description` | `string` | — | No | Accessible chart description |
| `tooltipFormatter` | `(value: number) => string` | — | No | Tooltip value formatter |
| `onClick` | `(data: object) => void` | — | No | Click callback on data point |
| `className` | `string` | — | No | Additional CSS classes |

---

## Usage Examples

### Basic line chart

```tsx
import { LineChart } from '@sporekart/ui';

const data = [
  { month: 'Jan', revenue: 4000, expenses: 2400 },
  { month: 'Feb', revenue: 3000, expenses: 1398 },
  { month: 'Mar', revenue: 2000, expenses: 9800 },
];

<LineChart
  data={data}
  categories={['revenue', 'expenses']}
  indexKey="month"
  title="Monthly Financials"
/>
```

### Smooth variant with gradient area

```tsx
<LineChart
  data={data}
  categories={['revenue']}
  indexKey="month"
  variant="area"
  smooth
  gradient
  title="Revenue Trend"
  tooltipFormatter={(v) => `$${v.toLocaleString()}`}
/>
```

### Stepped line for discrete data

```tsx
<LineChart
  data={inventoryData}
  categories={['stockLevel']}
  indexKey="week"
  variant="stepped"
  showDots={false}
  title="Inventory Levels"
/>
```

### Multi-series with click handler

```tsx
<LineChart
  data={salesData}
  categories={['north', 'south', 'east', 'west']}
  indexKey="quarter"
  onClick={(point) => navigate(`/sales/${point.quarter}`)}
  title="Regional Sales"
  description="Quarterly sales by region for the current fiscal year"
/>
```

---

## Accessibility

- The root SVG element includes `role="img"`, `aria-label` (from `title`), and `aria-description` (from `description`).
- Each data point has `role="graphics-symbol"`.
- A hidden `<table>` with the chart data is rendered for screen readers.
- Color is never the sole differentiator — series use distinct stroke patterns (dashed, dotted) in addition to color.
- Line width meets minimum 3px for visibility.

### Data table example (rendered automatically)

```html
<table class="sr-only">
  <caption>Monthly Financials</caption>
  <thead>
    <tr>
      <th>month</th>
      <th>revenue</th>
      <th>expenses</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Jan</td>
      <td>4000</td>
      <td>2400</td>
    </tr>
  </tbody>
</table>
```
