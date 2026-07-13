# Statistics Components

## Overview

Fourteen components for displaying aggregated statistics: summary blocks, grids, and three formatting utilities. These are presentation-only — they receive formatted values and do not perform calculation.

---

## SummaryBlock

Multi-stat summary section for report headers and dashboard top sections.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `stats` | `SummaryStat[]` | — | Yes | Array of statistic items |
| `columns` | `2 \| 3 \| 4` | `4` | No | Number of columns |
| `title` | `string` | — | No | Section title |
| `size` | `'sm' \| 'md' \| 'lg'` | `'md'` | No | Stat card size |
| `className` | `string` | — | No | Additional CSS classes |

### SummaryStat

```ts
interface SummaryStat {
  label: string;
  value: string;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  icon?: ReactNode;
  subtitle?: string;
}
```

---

## StatisticGrid

Grid of statistic tiles with consistent sizing.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `items` | `StatGridItem[]` | — | Yes | Grid items |
| `columns` | `2 \| 3 \| 4` | `3` | No | Grid columns |
| `gap` | `string` | `'16px'` | No | Gap between items |
| `className` | `string` | — | No | Additional CSS classes |

### StatGridItem

```ts
interface StatGridItem {
  id: string;
  label: string;
  value: string;
  trend?: 'up' | 'down' | 'flat';
  color?: string;
  onClick?: () => void;
}
```

---

## NumberFormatter

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Value to format |
| `decimals` | `number` | `0` | No | Decimal places |
| `compact` | `boolean` | `false` | No | Compact notation (1.2K, 3.5M) |
| `prefix` | `string` | — | No | Text before value |
| `suffix` | `string` | — | No | Text after value |
| `locale` | `string` | `'en-US'` | No | Locale for formatting |

### Examples

```tsx
import { NumberFormatter } from '@sporekart/ui';

<NumberFormatter value={1234567} compact />        {/* 1.2M */}
<NumberFormatter value={98.5} decimals={1} suffix="%" />  {/* 98.5% */}
<NumberFormatter value={42000} />                         {/* 42,000 */}
```

---

## CurrencyFormatter

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Value to format |
| `currency` | `string` | `'USD'` | No | ISO currency code |
| `compact` | `boolean` | `false` | No | Compact notation ($1.2M) |
| `decimals` | `number` | `2` | No | Decimal places |
| `locale` | `string` | `'en-US'` | No | Locale |

### Examples

```tsx
import { CurrencyFormatter } from '@sporekart/ui';

<CurrencyFormatter value={1250000} compact />          {/* $1.25M */}
<CurrencyFormatter value={49.99} currency="EUR" />     {/* €49.99 */}
<CurrencyFormatter value={0.85} currency="BTC" decimals={8} />  {/* 0.85000000 BTC */}
```

---

## PercentageFormatter

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `value` | `number` | — | Yes | Value (0–100 or decimal) |
| `decimals` | `number` | `1` | No | Decimal places |
| `inputFormat` | `'percent' \| 'decimal'` | `'percent'` | No | Input interpretation |
| `showSign` | `boolean` | `false` | No | Always show + sign for positives |
| `colorize` | `boolean` | `false` | No | Color positive/negative |

### Examples

```tsx
import { PercentageFormatter } from '@sporekart/ui';

<PercentageFormatter value={87.4} />                       {/* 87.4% */}
<PercentageFormatter value={0.156} inputFormat="decimal" decimals={2} />  {/* 15.6% */}
<PercentageFormatter value={-3.2} showSign colorize />     {/* -3.2% (red) */}
```

---

## Usage Examples

### Summary block

```tsx
import { SummaryBlock } from '@sporekart/ui';

<SummaryBlock
  title="Q2 Overview"
  columns={4}
  stats={[
    { label: 'Revenue', value: '$2.4M', trend: 'up', trendValue: '+8.2%' },
    { label: 'Expenses', value: '$1.1M', trend: 'down', trendValue: '-3.1%' },
    { label: 'Profit Margin', value: '54.2%', trend: 'up', trendValue: '+2.1%' },
    { label: 'Active Customers', value: '3,420', trend: 'up', trendValue: '+12%' },
  ]}
/>
```

### Statistic grid

```tsx
import { StatisticGrid } from '@sporekart/ui';

<StatisticGrid
  columns={3}
  items={[
    { id: '1', label: 'Total Orders', value: '12,450', trend: 'up' },
    { id: '2', label: 'Avg Order Value', value: '$342' },
    { id: '3', label: 'Cancellation Rate', value: '2.1%', trend: 'down', color: 'var(--color-danger)' },
  ]}
/>
```

### Formatting utilities

```tsx
<NumberFormatter value={1500000} compact prefix="Active: " />
<CurrencyFormatter value={2500000} compact />
<PercentageFormatter value={0.089} inputFormat="decimal" decimals={2} colorize showSign />
```
