# Export

## Overview

Export components and hooks for downloading chart data and visualisations in multiple formats. The `ExportMenu` provides a dropdown UI, while individual hooks enable programmatic export.

---

## Format Support

| Format | Hook | Method | Description |
|--------|------|--------|-------------|
| PNG | `useChartExport` | `exportPNG()` | Raster image of chart container |
| CSV | `useCsvExport` / `useExcelExport` | `exportCSV()` | Comma-separated values |
| Excel | `useExcelExport` | `exportExcel()` | CSV with `.xlsx` extension (foundation) |
| Print | `usePrintExport` / `useChartPrint` | `print()` | Browser print dialog scoped to chart |

---

## ExportMenu

Dropdown button that exposes all export actions.

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `chartRef` | `RefObject<HTMLElement>` | — | Yes | Reference to the chart container |
| `data` | `Record<string, unknown>[]` | — | No | Data for CSV/Excel export |
| `filename` | `string` | `'chart'` | No | Base filename for downloads |
| `formats` | `('png' \| 'csv' \| 'excel' \| 'print')[]` | all | No | Visible format options |
| `onExport` | `(format: string) => void` | — | No | Export callback for analytics |
| `className` | `string` | — | No | Additional CSS classes |

---

## Hooks

### useCsvExport

```ts
useCsvExport(): {
  exportCSV: (data: Record<string, unknown>[], filename?: string) => void;
}
```

Converts data array to CSV string and triggers download. Handles header mapping and string escaping.

### useExcelExport

```ts
useExcelExport(): {
  exportExcel: (data: Record<string, unknown>[], filename?: string) => void;
}
```

Same interface as CSV export. Foundation for future `.xlsx` generation (currently wraps CSV download with `.xlsx` extension).

### usePdfExport

```ts
usePdfExport(chartRef: RefObject<HTMLElement>): {
  exportPDF: (filename?: string) => void;
}
```

Opens the browser print dialog targeting the chart container for "Save as PDF" workflow.

### usePrintExport

```ts
usePrintExport(chartRef: RefObject<HTMLElement>): {
  print: () => void;
}
```

Opens the browser print dialog for the chart container.

---

## Usage Examples

### Export menu on a chart

```tsx
import { ExportMenu, ChartContainer, LineChart } from '@sporekart/ui';

function ChartWithExport() {
  const chartRef = useRef<HTMLDivElement>(null);

  return (
    <div>
      <div ref={chartRef}>
        <ChartContainer title="Quarterly Revenue">
          <LineChart data={revenueData} categories={['revenue']} indexKey="quarter" />
        </ChartContainer>
      </div>
      <ExportMenu
        chartRef={chartRef}
        data={revenueData}
        filename="quarterly-revenue"
        onExport={(fmt) => console.log(`Exported as ${fmt}`)}
      />
    </div>
  );
}
```

### Programmatic CSV export

```tsx
import { useCsvExport } from '@sporekart/ui';

function ExportButton({ data }: { data: Record<string, unknown>[] }) {
  const { exportCSV } = useCsvExport();

  return (
    <button onClick={() => exportCSV(data, 'report-data')}>
      Download CSV
    </button>
  );
}
```

### Print button

```tsx
import { usePrintExport } from '@sporekart/ui';

function PrintChart({ chartRef }: { chartRef: RefObject<HTMLDivElement> }) {
  const { print } = usePrintExport(chartRef);

  return <button onClick={print}>Print Chart</button>;
}
```

### Custom export toolbar

```tsx
function CustomToolbar({ chartRef, data }: ToolbarProps) {
  const { exportPNG, exportCSV } = useChartExport(chartRef);
  const { print } = useChartPrint(chartRef);

  return (
    <div className="export-toolbar">
      <button onClick={() => exportPNG('chart')}>
        <DownloadIcon /> PNG
      </button>
      <button onClick={() => exportCSV(data, 'chart')}>
        <TableIcon /> CSV
      </button>
      <button onClick={print}>
        <PrinterIcon /> Print
      </button>
    </div>
  );
}
```
