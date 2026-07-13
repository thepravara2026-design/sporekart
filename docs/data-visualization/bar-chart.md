# BarChart

## Overview

Renders vertical, horizontal, grouped, stacked, or comparison bars. Suitable for category comparisons, part-to-whole relationships, and benchmark analysis.

---

## Variants

| Variant | Description | Use Case |
|---------|-------------|----------|
| `vertical` | Default. Bars extend upward from the X-axis | Comparing values across categories |
| `horizontal` | Bars extend rightward from the Y-axis | Many categories, long labels |
| `grouped` | Side-by-side bars per category | Comparing sub-groups across categories |
| `stacked` | Bars stacked on top of each other | Part-to-whole across categories |
| `comparison` | Two bars per category (e.g. actual vs target) | Benchmark or goal comparison |

---

## Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `data` | `Array<Record<string, unknown>>` | — | Yes | Chart data array |
| `categories` | `string[]` | — | Yes | Data keys for each bar series |
| `indexKey` | `string` | — | Yes | Key for the category axis |
| `variant` | `'vertical' \| 'horizontal' \| 'grouped' \| 'stacked' \| 'comparison'` | `'vertical'` | No | Bar variant |
| `colors` | `string[]` | theme colors | No | Series colors |
| `showValues` | `boolean` | `false` | No | Show value labels on bars |
| `valuePosition` | `'top' \| 'inside' \| 'right'` | `'top'` | No | Value label position |
| `barSize` | `number` | — | No | Bar width in px |
| `maxBarSize` | `number` | — | No | Maximum bar width |
| `layout` | `'vertical' \| 'horizontal'` | `'vertical'` | No | Bar orientation |
| `stacked` | `boolean` | `false` | No | Stack series (legacy, prefer `variant`) |
| `animate` | `boolean` | `true` | No | Enable enter animation |
| `height` | `number` | `400` | No | Chart height in px |
| `title` | `string` | — | No | Accessible chart title |
| `description` | `string` | — | No | Accessible chart description |
| `tooltipFormatter` | `(value: number) => string` | — | No | Tooltip value formatter |
| `onClick` | `(data: object) => void` | — | No | Click callback on bar |
| `className` | `string` | — | No | Additional CSS classes |

---

## Usage Examples

### Vertical bar chart

```tsx
import { BarChart } from '@sporekart/ui';

const data = [
  { product: 'Widget A', revenue: 12000 },
  { product: 'Widget B', revenue: 9000 },
  { product: 'Widget C', revenue: 15000 },
];

<BarChart
  data={data}
  categories={['revenue']}
  indexKey="product"
  variant="vertical"
  title="Product Revenue"
/>
```

### Grouped bar chart

```tsx
const quarterlyData = [
  { quarter: 'Q1', actual: 4000, target: 3800 },
  { quarter: 'Q2', actual: 3500, target: 4000 },
  { quarter: 'Q3', actual: 4200, target: 4100 },
];

<BarChart
  data={quarterlyData}
  categories={['actual', 'target']}
  indexKey="quarter"
  variant="grouped"
  title="Quarterly Actual vs Target"
  tooltipFormatter={(v) => `$${v.toLocaleString()}`}
/>
```

### Horizontal stacked bar

```tsx
<BarChart
  data={budgetData}
  categories={['marketing', 'engineering', 'operations']}
  indexKey="department"
  variant="stacked"
  layout="horizontal"
  showValues
  valuePosition="inside"
  title="Department Budget Allocation"
/>
```

### Comparison bars with click

```tsx
<BarChart
  data={salesData}
  categories={['achieved', 'goal']}
  indexKey="repName"
  variant="comparison"
  onClick={(item) => navigate(`/reps/${item.repName}`)}
  title="Sales Rep Performance"
  description="Individual sales achievements vs quarterly goals"
  showValues
/>
```

---

## Accessibility

- SVG root uses `role="img"` with accessible `aria-label` from the `title` prop.
- Each bar uses `role="graphics-symbol"` and includes an accessible `<title>` element with the value.
- A hidden data table is rendered automatically for screen readers.
- Patterns (hatching, dots) are applied to bars when `variant="grouped"` or `"comparison"` so color is not the sole differentiator.
- Value labels respect `prefers-contrast: high` for readability.
