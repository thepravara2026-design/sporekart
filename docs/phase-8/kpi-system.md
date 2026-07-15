# Enterprise KPI System

## KPICard

Reusable KPI indicator card.

```tsx
import { KPICard } from '../dashboard/kpi';

<KPICard kpi={kpi} />
```

### KPIData Interface
```tsx
interface KPIData {
  id: string;
  title: string;
  value: string;          // Formatted value (e.g., "$284,500")
  unit?: string;
  trend: 'up' | 'down' | 'neutral';
  percentage: number;      // Change percentage (e.g., 12.5)
  comparison: string;      // Comparison text (e.g., "vs last month")
  icon: string;
  color: string;           // Theme color token
  loading?: boolean;
}
```

### States
| State | Behavior |
|-------|----------|
| **Normal** | Full card with icon, value, trend |
| **Loading** | Skeleton animation (3 shimmer bars) |
| **Trend Up** | Green icon + "+X%" |
| **Trend Down** | Red icon + "-X%" |
| **Trend Neutral** | Grey icon + "0%" |

## KPIGrid

Responsive grid for KPI cards.

```tsx
<KPIGrid kpis={kpis} columns={4} />
```

- Uses CSS Grid with `repeat(columns, 1fr)`
- Falls back gracefully on smaller viewports
- Empty: renders nothing (no grid)

## Trend Calculation

```tsx
const trendIcon = kpi.trend === 'up' ? 'trending-up'
  : kpi.trend === 'down' ? 'trending-down' : 'minus';
const trendColor = kpi.trend === 'up' ? 'var(--color-success)'
  : kpi.trend === 'down' ? 'var(--color-error)'
  : 'var(--color-text-tertiary)';
```
