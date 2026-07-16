import { memo } from 'react';
import type { ChartDataPoint } from '../types';

interface ComparisonChartProps {
  current: ChartDataPoint[];
  previous: ChartDataPoint[];
  height?: number;
}

export const ComparisonChart = memo(function ComparisonChart({ current, previous, height = 140 }: ComparisonChartProps) {
  const all = [...current, ...previous];
  const max = Math.max(...all.map((d) => d.value), 1);
  const barW = `${100 / Math.max(current.length, 1) * 0.35}%`;

  return (
    <div style={{ display: 'flex', gap: 8, alignItems: 'flex-end', height, position: 'relative' }}>
      {current.map((d, i) => {
        const prev = previous[i];
        return (
          <div key={d.label} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
            {prev && <div style={{ width: barW, maxWidth: 16, background: '#94a3b8', borderRadius: '3px 3px 0 0', height: `${(prev.value / max) * (height - 28)}px`, minHeight: 2 }} />}
            <div style={{ width: barW, maxWidth: 16, background: '#2563eb', borderRadius: '3px 3px 0 0', height: `${(d.value / max) * (height - 28)}px`, minHeight: 2 }} />
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{d.label}</span>
          </div>
        );
      })}
    </div>
  );
});
