import { memo } from 'react';
import type { ChartDataPoint } from '../types';

interface LineChartProps {
  data: ChartDataPoint[];
  height?: number;
  color?: string;
  showArea?: boolean;
}

export const LineChart = memo(function LineChart({ data, height = 120, color = '#2563eb', showArea = true }: LineChartProps) {
  const max = Math.max(...data.map((d) => d.value), 1);
  const w = 100;
  const h = height - 24;
  const points = data.map((d, i) => `${(i / Math.max(data.length - 1, 1)) * w},${h - (d.value / max) * h}`).join(' ');
  const areaPoints = `0,${h} ${points} ${w},${h}`;

  return (
    <div style={{ position: 'relative', height }}>
      <svg viewBox={`0 0 ${w} ${h}`} style={{ width: '100%', height: h, overflow: 'visible' }}>
        {showArea && <polygon points={areaPoints} fill={`${color}22`} />}
        <polyline points={points} fill="none" stroke={color} strokeWidth={2} strokeLinejoin="round" strokeLinecap="round" />
        {data.map((d, i) => (
          <circle key={i} cx={`${(i / Math.max(data.length - 1, 1)) * w}`} cy={`${h - (d.value / max) * h}`} r={3} fill={color} />
        ))}
      </svg>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 4 }}>
        {data.map((d) => (
          <span key={d.label} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{d.label}</span>
        ))}
      </div>
    </div>
  );
});
