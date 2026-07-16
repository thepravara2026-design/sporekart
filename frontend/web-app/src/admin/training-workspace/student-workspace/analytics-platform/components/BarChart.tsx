import { memo } from 'react';
import type { ChartDataPoint } from '../types';

interface BarChartProps {
  data: ChartDataPoint[];
  height?: number;
  color?: string;
  showValues?: boolean;
}

export const BarChart = memo(function BarChart({ data, height = 120, color = '#2563eb', showValues = true }: BarChartProps) {
  const max = Math.max(...data.map((d) => d.value), 1);
  return (
    <div style={{ display: 'flex', gap: 8, alignItems: 'flex-end', height, position: 'relative' }}>
      {data.map((d) => (
        <div key={d.label} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4, height: '100%', justifyContent: 'flex-end' }}>
          <div style={{ width: '100%', maxWidth: 40, background: d.color || color, borderRadius: '4px 4px 0 0', height: `${(d.value / max) * (height - 24)}px`, minHeight: 4, transition: 'height 0.3s' }} />
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap', textAlign: 'center' }}>{d.label}</span>
          {showValues && <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{d.value}</span>}
        </div>
      ))}
    </div>
  );
});
