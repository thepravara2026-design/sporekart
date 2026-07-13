# Accessibility — Data Visualization

## Overview

All chart and visualization components comply with WCAG 2.2 AA. This document details the specific patterns used across the library.

---

## SVG Charts

### Roles and Labels

Every chart SVG includes:

```html
<svg role="img" aria-label="Quarterly Revenue by Region" aria-description="Bar chart showing Q1 through Q4 revenue for North America, Europe, and Asia">
```

- `role="img"` identifies the SVG as an image
- `aria-label` comes from the `title` prop
- `aria-description` comes from the `description` prop
- Data marks (bars, lines, slices) use `role="graphics-symbol"`

### Accessible Data Tables

Every chart renders a hidden `<table>` as a sibling to the SVG:

```html
<table class="sr-only">
  <caption>Quarterly Revenue</caption>
  <thead>
    <tr>
      <th>Quarter</th>
      <th>North America</th>
      <th>Europe</th>
      <th>Asia</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Q1</td>
      <td>$400K</td>
      <td>$350K</td>
      <td>$280K</td>
    </tr>
  </tbody>
</table>
```

- `class="sr-only"` visually hides the table while keeping it available to screen readers
- The table is dynamically generated from the same `data` prop passed to the chart
- Charts with no data render an empty state message instead of an empty table

---

## Color-Independent Indicators

Color is never the sole differentiator between data series. The following mechanisms ensure identification without color perception:

| Mechanism | Applied To | Description |
|-----------|------------|-------------|
| **Pattern fills** | Bar charts, area charts, pie slices | Hatching, dots, diagonal lines via SVG `<pattern>` |
| **Stroke styles** | Line charts | Dashed, dotted, solid lines per series |
| **Marker shapes** | Line charts (dots) | Circle, square, diamond, triangle |
| **Direct labels** | Pie charts, bar charts | Value labels directly on data marks |
| **Legend** | All chart types | Text labels with multiple identifying attributes |

### Pattern Fill Example

```tsx
<BarChart
  data={data}
  categories={['actual', 'target']}
  variant="grouped"
  patterns // enables pattern fills
/>
```

---

## Keyboard Navigation

Interactive chart elements support full keyboard operability.

| Component | Interaction | Keys |
|-----------|-------------|------|
| CalendarMonth | Navigate days, weeks, months | Arrow keys, Page Up/Down, Home/End, Enter/Space |
| CalendarWeek | Navigate time slots | Arrow keys, Tab, Enter/Space |
| CalendarDateRange | Select range | Tab between fields, Arrow keys for dates, Enter to confirm |
| PieChart (interactive) | Select slices | Tab to slice, Enter/Space to select |
| ExportMenu | Open menu, select format | Enter to open, Arrow keys to navigate, Enter to select, Escape to close |
| FilterChips | Remove filters | Tab to chip, Backspace/Delete to remove |

All interactive elements have visible `:focus-visible` outlines using the design system's focus token.

---

## Reduced Motion

- Chart enter animations respect `prefers-reduced-motion: reduce` and skip all animation
- Tooltip fade/scale transitions are disabled when reduced motion is preferred
- Progress indicators (RadialProgress, TargetProgress) animate via CSS transitions that respect the media query
- No continuous or blinking animations are used

### Implementation

```css
@media (prefers-reduced-motion: reduce) {
  .chart-animate {
    animation: none !important;
    transition: none !important;
  }
}
```

---

## High Contrast

- Charts respect `prefers-contrast: high` by increasing stroke widths and fill opacities
- Pattern fills automatically activate in high contrast mode even when `patterns` prop is not set
- Text labels use `currentColor` — they inherit the user's forced colors palette
- SVG shapes use `stroke="currentColor"` where appropriate
- The `forced-colors: active` media query is used to disable decorative fills that rely on color alone

---

## Data Table Alternative

For complex charts where SVG accessibility is insufficient, consumers can render an external data table:

```tsx
<ChartContainer title="Sales by Region" description="Data table available below the chart">
  <BarChart data={data} categories={['sales']} indexKey="region" />
</ChartContainer>
<table aria-label="Sales by Region data table">
  <caption>Raw data for the chart above</caption>
  <thead>
    <tr><th>Region</th><th>Sales</th></tr>
  </thead>
  <tbody>
    {data.map(row => (
      <tr key={row.region}>
        <td>{row.region}</td>
        <td>{row.sales}</td>
      </tr>
    ))}
  </tbody>
</table>
```

---

## Non-Text Data Accessibility

Pie charts, donuts, and radial components rely heavily on color. Additional measures:

- Pie slices include `<title>` elements with the slice name, value, and percentage
- RadialProgress and CircularKPI use `role="progressbar"` with `aria-valuenow`, `aria-valuemin`, `aria-valuemax`
- Gauge includes a text description of the value and range
- Donut centre labels are rendered as visible `<text>` elements (not background images)
