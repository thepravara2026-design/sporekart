# AreaChart

## Overview

AreaChart displays quantitative data with filled areas beneath the line. Supports single area, stacked areas, and gradient fills for emphasising magnitude over time.

---

## Variants

| Variant | Description | Use Case |
|---------|-------------|----------|
| Single | One area series with optional gradient | Total revenue, single metric over time |
| Stacked | Multiple areas stacked on top of each other | Part-to-whole across time periods |

---

## Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `data` | `Array<Record<string, unknown>>` | — | Yes | Chart data array |
| `categories` | `string[]` | — | Yes | Data keys for each area series |
| `indexKey` | `string` | — | Yes | Key for the X-axis |
| `stacked` | `boolean` | `false` | No | Stack multiple area series |
| `gradient` | `boolean` | `true` | No | Enable gradient fill |
| `gradientOpacity` | `{ top: number, bottom: number }` | `{ top: 0.3, bottom: 0 }` | No | Gradient opacity range |
| `curve` | `boolean` | `false` | No | Smooth curved area |
| `showDots` | `boolean` | `false` | No | Show data point markers |
| `colors` | `string[]` | theme colors | No | Series colors |
| `animate` | `boolean` | `true` | No | Enable enter animation |
| `height` | `number` | `400` | No | Chart height |
| `title` | `string` | — | No | Accessible chart title |
| `description` | `string` | — | No | Accessible chart description |
| `tooltipFormatter` | `(value: number) => string` | — | No | Tooltip value formatter |
| `onClick` | `(data: object) => void` | — | No | Click callback |
| `className` | `string` | — | No | Additional CSS classes |

---

## Usage Examples

### Single area with gradient

```tsx
import { AreaChart } from '@sporekart/ui';

const data = [
  { month: 'Jan', revenue: 4000 },
  { month: 'Feb', revenue: 3000 },
  { month: 'Mar', revenue: 5000 },
  { month: 'Apr', revenue: 4800 },
];

<AreaChart
  data={data}
  categories={['revenue']}
  indexKey="month"
  gradient
  title="Revenue Over Time"
  tooltipFormatter={(v) => `$${v.toLocaleString()}`}
/>
```

### Stacked area chart

```tsx
const trafficData = [
  { month: 'Jan', organic: 4000, paid: 2400, referral: 1800 },
  { month: 'Feb', organic: 3000, paid: 1398, referral: 2100 },
  { month: 'Mar', organic: 5000, paid: 3800, referral: 2500 },
];

<AreaChart
  data={trafficData}
  categories={['organic', 'paid', 'referral']}
  indexKey="month"
  stacked
  title="Web Traffic Sources"
  description="Monthly traffic breakdown by source"
/>
```

### Curved stacked area

```tsx
<AreaChart
  data={subscriberData}
  categories={['free', 'pro', 'enterprise']}
  indexKey="quarter"
  stacked
  curve
  gradient
  title="Subscribers by Plan"
/>
```

### Single area without gradient

```tsx
<AreaChart
  data={simpleData}
  categories={['value']}
  indexKey="date"
  gradient={false}
  showDots
  title="Daily Active Users"
/>
```

---

## Accessibility

- SVG root includes `role="img"` with `aria-label` and `aria-description`.
- A hidden data table is rendered alongside the chart for screen reader access.
- Stacked areas use distinct fill patterns (hatching, dots) in addition to color.
- Area fill opacity respects `prefers-reduced-motion` — animation is disabled when the user requests reduced motion.
- When `gradient` is enabled, the solid colour at the top of the gradient meets WCAG 3:1 contrast against the background.
