# Chart Foundation

## Overview

The chart foundation provides base components and hooks shared by all chart types. Every chart in the library composes these primitives for consistent behavior, theming, and responsiveness.

---

## Components

### ChartContainer

Responsive chart wrapper with loading, empty, error, and skeleton states.

#### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `children` | `ReactNode` | — | Yes | Chart content |
| `title` | `string` | — | No | Accessible chart title (renders as `aria-label`) |
| `description` | `string` | — | No | Accessible chart description |
| `loading` | `boolean` | `false` | No | Shows skeleton when true |
| `empty` | `boolean` | `false` | No | Shows empty state |
| `error` | `string \| null` | `null` | No | Shows error state with message |
| `emptyMessage` | `string` | `"No data available"` | No | Custom empty state message |
| `errorMessage` | `string` | `"Failed to load chart"` | No | Custom error message |
| `height` | `string \| number` | `400` | No | Chart height in px or CSS string |
| `className` | `string` | — | No | Additional CSS classes |
| `onRetry` | `() => void` | — | No | Retry callback for error state |

---

### ChartTooltip

Reusable tooltip that appears on chart hover.

#### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `active` | `boolean` | — | Yes | Whether tooltip is visible |
| `payload` | `Array<{name, value, color}>` | — | Yes | Data payload from chart |
| `label` | `string` | — | No | Tooltip label |
| `formatter` | `(value: number) => string` | — | No | Value formatting function |
| `labelFormatter` | `(label: string) => string` | — | No | Label formatting function |

---

### ChartLegend

Renders a legend for chart series.

#### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `payload` | `Array<{value, color, type}>` | — | Yes | Legend items |
| `vertical` | `boolean` | `false` | No | Vertical layout |
| `onClick` | `(item) => void` | — | No | Click handler for legend items |
| `formatter` | `(value: string) => string` | — | No | Label formatter |

---

### ChartAxis

Configurable axis with labels and grid lines.

#### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `type` | `'x' \| 'y'` | — | Yes | Axis orientation |
| `dataKey` | `string` | — | Yes | Key in the data object |
| `tickFormatter` | `(value) => string` | — | No | Tick label formatter |
| `label` | `string` | — | No | Axis label |
| `hide` | `boolean` | `false` | No | Hide the axis |
| `gridLines` | `boolean` | `true` | No | Show grid lines |
| `tickCount` | `number` | — | No | Number of ticks |

---

### ChartSkeleton

Loading placeholder for charts.

#### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `variant` | `'bar' \| 'line' \| 'pie' \| 'area'` | `'bar'` | No | Skeleton shape |
| `height` | `string \| number` | `400` | No | Skeleton height |

---

## Hooks

### useChartResize

```ts
useChartResize(options?: { debounceMs?: number }): {
  containerRef: RefObject<HTMLDivElement>;
  width: number;
  height: number;
}
```

Responds to container resizing via `ResizeObserver`. Returns the container ref and current dimensions.

### useChartTheme

```ts
useChartTheme(): {
  colors: string[];
  backgroundColor: string;
  textColor: string;
  gridColor: string;
  fontFamily: string;
}
```

Returns the current theme tokens for chart rendering. Light/dark aware.

### useChartExport

```ts
useChartExport(containerRef: RefObject<HTMLElement>): {
  exportPNG: (filename?: string) => void;
  exportCSV: (data: Record<string, unknown>[], filename?: string) => void;
}
```

Captures the chart container as PNG (via `html-to-image`) or exports data as CSV.

### useChartPrint

```ts
useChartPrint(containerRef: RefObject<HTMLElement>): {
  print: () => void;
}
```

Opens the print dialog scoped to the chart container.

### useChartFullscreen

```ts
useChartFullscreen(containerRef: RefObject<HTMLElement>): {
  isFullscreen: boolean;
  toggle: () => void;
  enter: () => void;
  exit: () => void;
}
```

Manages fullscreen state for the chart container.

---

## Usage Examples

### Basic chart with container

```tsx
import { ChartContainer, ChartTooltip, ChartLegend, ChartAxis, useChartResize, useChartTheme } from '@sporekart/ui';

function MyChart() {
  const { containerRef, width, height } = useChartResize();
  const { colors } = useChartTheme();
  const data = [{ month: 'Jan', sales: 400 }, { month: 'Feb', sales: 300 }];

  return (
    <ChartContainer title="Monthly Sales" height={400}>
      <svg ref={containerRef} width={width} height={height}>
        {/* chart implementation */}
      </svg>
    </ChartContainer>
  );
}
```

### Loading and error states

```tsx
<ChartContainer loading={isLoading} error={errorMessage} onRetry={handleRetry}>
  <LineChart data={data} />
</ChartContainer>
```

### Export and print

```tsx
function ChartWithExport() {
  const chartRef = useRef<HTMLDivElement>(null);
  const { exportPNG, exportCSV } = useChartExport(chartRef);
  const { print } = useChartPrint(chartRef);
  const { isFullscreen, toggle } = useChartFullscreen(chartRef);

  return (
    <div ref={chartRef}>
      <ChartContainer title="Revenue">
        <LineChart data={revenueData} />
      </ChartContainer>
      <button onClick={() => exportPNG('revenue')}>Export PNG</button>
      <button onClick={print}>Print</button>
      <button onClick={toggle}>{isFullscreen ? 'Exit' : 'Fullscreen'}</button>
    </div>
  );
}
```

### Custom tooltip

```tsx
function CustomTooltip({ active, payload, label }: TooltipProps) {
  if (!active || !payload?.length) return null;
  return (
    <ChartTooltip
      active={active}
      payload={payload}
      label={label}
      formatter={(v) => `$${v.toLocaleString()}`}
    />
  );
}
```

---

## Best Practices

- Always pass a `title` prop to `ChartContainer` for accessibility.
- Use `useChartResize` with a debounce of 200-300ms for performance.
- Combine `useChartExport` with `useChartPrint` for a complete export toolbar.
- Keep tooltip formatting consistent across all charts on the same page.
- Use `ChartSkeleton` variants that match the actual chart shape for a smooth loading experience.
- Avoid nesting `ChartContainer` inside scrollable parents that may interfere with `ResizeObserver`.
