import { memo } from 'react';
import type { ChartDataPoint } from '../types';

interface AreaChartProps {
  data: ChartDataPoint[];
  height?: number;
  color?: string;
  showGrid?: boolean;
}

export const AreaChart = memo(function AreaChart({ data, height = 120, color = '#2563eb', showGrid = true }: AreaChartProps) {
  const max = Math.max(...data.map((d) => d.value), 1);
  const w = 100;
  const h = height - 20;
  const pts = data.map((d, i) => `${(i / Math.max(data.length - 1, 1)) * w},${h - (d.value / max) * h}`).join(' ');
  const area = `0,${h} ${pts} ${w},${h}`;

  return (
    <div style={{ position: 'relative', height }}>
      {showGrid && (
        <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', paddingBottom: 20, opacity: 0.3 }}>
          {[0.25, 0.5, 0.75].map((_, i) => <div key={i} style={{ borderTop: `1px dashed var(--color-border-default)` }} />)}
        </div>
      )}
      <svg viewBox={`0 0 ${w} ${h}`} style={{ width: '100%', height: h }}>
        <polygon points={area} fill={`${color}18`} />
        <polyline points={pts} fill="none" stroke={color} strokeWidth={2} strokeLinejoin="round" strokeLinecap="round" />
      </svg>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 4 }}>
        {data.filter((_, i) => i % Math.ceil(data.length / 6) === 0 || i === data.length - 1).map((d) => (
          <span key={d.label} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{d.label}</span>
        ))}
      </div>
    </div>
  );
});
