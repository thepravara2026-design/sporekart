# Card Component

## Overview

The Card component is a surface-level container for grouping related content. The system provides 14 variant cards plus the base Card component, each tailored for specific content patterns.

| Variant | Purpose |
|---------|---------|
| Card | Base container — generic grouped content |
| StatCard | Single metric with label, trend, and sparkline |
| MetricCard | Multiple metrics in a compact card |
| InfoCard | Key-value information pairs |
| ProfileCard | User/grower profile summary |
| FeatureCard | Feature showcase with icon and description |
| PricingCard | Pricing tier with feature list and CTA |
| ProductCard | Product listing with image, price, rating |
| OrderCard | Order summary with status, items, timeline |
| SummaryCard | Aggregated summary with breakdown sections |
| StatusCard | Status display with icon, label, timestamp |
| NotificationCard | Notification with icon, message, action |
| QuickActionCard | Action shortcut with icon and label |
| MediaCard | Media (image/video) with overlay content |
| TrainingCard | Training/content card with progress bar |

## Base Card Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| variant | `'default' \| 'elevated' \| 'outlined' \| 'ghost'` | `'default'` | Visual style |
| padding | `'none' \| 'sm' \| 'md' \| 'lg'` | `'md'` | Inner padding |
| radius | `'sm' \| 'md' \| 'lg' \| 'full'` | `'md'` | Border radius |
| fullWidth | `boolean` | `false` | Stretch to container width |
| onClick | `() => void` | — | Click handler (makes card interactive) |
| href | `string` | — | Link target (renders as `<a>`) |
| className | `string` | — | Additional CSS classes |
| children | `ReactNode` | — | Card content |

## Variant-Specific Props

### StatCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| label | `string` | — | Metric label |
| value | `string \| number` | — | Metric value |
| trend | `'up' \| 'down' \| 'neutral'` | — | Trend direction |
| trendValue | `string` | — | Trend percentage/text |
| sparkline | `number[]` | — | Sparkline data points |
| icon | `IconName` | — | Leading icon |

### MetricCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| metrics | `Metric[]` | — | Array of metric objects |
| columns | `2 \| 3 \| 4` | `2` | Grid columns |

### InfoCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| items | `InfoItem[]` | — | Key-value pairs |
| columns | `1 \| 2` | `1` | Label-value columns |

### ProfileCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| name | `string` | — | Display name |
| role | `string` | — | Role/title |
| avatar | `AvatarProps` | — | Avatar configuration |
| status | `'online' \| 'offline' \| 'busy' \| 'away'` | — | Presence status |
| meta | `ProfileMeta[]` | — | Additional metadata |

### FeatureCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| icon | `IconName` | — | Feature icon |
| title | `string` | — | Feature title |
| description | `string` | — | Feature description |
| action | `{ label: string; onClick: () => void }` | — | Call to action |

### PricingCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| plan | `string` | — | Plan name |
| price | `string` | — | Price display |
| period | `string` | `'/month'` | Billing period |
| features | `string[]` | — | Feature list |
| cta | `{ label: string; onClick: () => void; variant?: ButtonVariant }` | — | Call to action |
| highlighted | `boolean` | `false` | Highlight/featured state |
| badge | `string` | — | Optional badge (e.g. "Popular") |

### ProductCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| image | `string` | — | Product image URL |
| title | `string` | — | Product title |
| price | `string` | — | Price display |
| originalPrice | `string` | — | Strikethrough original price |
| rating | `number` | — | Star rating (0–5) |
| reviewCount | `number` | — | Review count |
| badge | `{ label: string; variant: BadgeVariant }` | — | Product badge |
| onAddToCart | `() => void` | — | Add to cart handler |

### OrderCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| orderId | `string` | — | Order identifier |
| status | `OrderStatus` | — | Order status |
| amount | `string` | — | Order total |
| items | `OrderItem[]` | — | Order line items |
| date | `string` | — | Order date |
| timeline | `TimelineStep[]` | — | Order progress |

### SummaryCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| title | `string` | — | Section title |
| sections | `SummarySection[]` | — | Summary sections |
| total | `{ label: string; value: string }` | — | Grand total row |

### StatusCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| status | `'success' \| 'warning' \| 'danger' \| 'info' \| 'neutral'` | — | Status type |
| icon | `IconName` | — | Status icon |
| label | `string` | — | Status label |
| description | `string` | — | Status description |
| timestamp | `string` | — | Timestamp display |
| action | `{ label: string; onClick: () => void }` | — | Optional action |

### NotificationCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| type | `'info' \| 'success' \| 'warning' \| 'error'` | — | Notification type |
| message | `string` | — | Notification message |
| timestamp | `string` | — | Relative time |
| read | `boolean` | `false` | Read state |
| onDismiss | `() => void` | — | Dismiss handler |
| onClick | `() => void` | — | Click to view detail |

### QuickActionCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| icon | `IconName` | — | Action icon |
| label | `string` | — | Action label |
| description | `string` | — | Brief description |
| shortcut | `string` | — | Keyboard shortcut |
| onClick | `() => void` | — | Action handler |

### MediaCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| src | `string` | — | Media source URL |
| type | `'image' \| 'video'` | `'image'` | Media type |
| alt | `string` | — | Alt text |
| overlay | `ReactNode` | — | Overlay content |
| caption | `string` | — | Bottom caption |

### TrainingCard

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| title | `string` | — | Training title |
| duration | `string` | — | Duration (e.g. "2h 30m") |
| progress | `number` | — | Progress percentage (0–100) |
| level | `'beginner' \| 'intermediate' \| 'advanced'` | — | Difficulty |
| thumbnail | `string` | — | Thumbnail URL |
| modules | `number` | — | Total modules |
| completed | `number` | — | Completed modules |

## Usage Example

```tsx
<Card variant="elevated" padding="lg" fullWidth>
  <StatCard
    label="Total Orders"
    value="1,247"
    trend="up"
    trendValue="+12.5%"
    sparkline={[10, 15, 13, 18, 22, 25, 30]}
    icon="shopping-cart"
  />
</Card>
```

## States

| State | Behavior |
|-------|----------|
| Default | Static card with defined variant styling |
| Hover | Elevation increase + subtle transform (`translateY(-2px)`) for interactive cards |
| Loading | Skeleton placeholder matching card dimensions |
| Error | Error state with retry action |
| Empty | Empty state with optional CTA for data-driven cards |

Loading/error/empty states apply to data-fetching card variants (StatCard, MetricCard, OrderCard, SummaryCard). Static cards (Card, FeatureCard, MediaCard) use only default/hover states.

## Responsive Behavior

Cards in a grid use container queries for responsive adjustment:

- `min-width: 300px` — single column, full-width cards
- `min-width: 600px` — 2-column grid
- `min-width: 900px` — 3-column grid
- `min-width: 1200px` — 4-column grid

Cards scale padding and font size based on container width using `@container` queries.

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--card-bg` | `--color-surface` |
| `--card-border` | `--color-border-subtle` |
| `--card-radius` | `--radius-md` (varies by variant) |
| `--card-shadow` | `--elevation-1` / `--elevation-2` |
| `--card-padding` | `--spacing-md` |
| `--card-gap` | `--spacing-md` |

## Composition Patterns

```
Card
├── Card.Header (optional title + action)
├── Card.Body (main content)
│   ├── StatCard / MetricCard / InfoCard
│   ├── ProfileCard / FeatureCard
│   ├── ProductCard / OrderCard / SummaryCard
│   ├── StatusCard / NotificationCard
│   ├── QuickActionCard
│   └── TrainingCard / MediaCard
└── Card.Footer (optional actions)
```
