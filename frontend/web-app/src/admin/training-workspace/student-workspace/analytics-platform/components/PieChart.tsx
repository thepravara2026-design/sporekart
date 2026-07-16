import { memo } from 'react';
import type { ChartDataPoint } from '../types';

interface PieChartProps {
  data: ChartDataPoint[];
  size?: number;
  innerRadius?: number;
}

const COLORS = ['#2563eb', '#16a34a', '#ca8a04', '#dc2626', '#8b5cf6', '#0891b2', '#f59e0b', '#ec4899'];

export const PieChart = memo(function PieChart({ data, size = 140, innerRadius = 30 }: PieChartProps) {
  const total = data.reduce((s, d) => s + d.value, 0) || 1;
  const cx = size / 2;
  const cy = size / 2;
  const r = Math.min(cx, cy) - 4;
  let cumulative = 0;

  const slices = data.map((d, i) => {
    const startAngle = (cumulative / total) * 360;
    cumulative += d.value;
    const endAngle = (cumulative / total) * 360;
    const startRad = ((startAngle - 90) * Math.PI) / 180;
    const endRad = ((endAngle - 90) * Math.PI) / 180;
    const x1 = cx + r * Math.cos(startRad);
    const y1 = cy + r * Math.sin(startRad);
    const x2 = cx + r * Math.cos(endRad);
    const y2 = cy + r * Math.sin(endRad);
    const largeArc = endAngle - startAngle > 180 ? 1 : 0;
    const pathData = [`M ${cx} ${cy}`, `L ${x1} ${y1}`, `A ${r} ${r} 0 ${largeArc} 1 ${x2} ${y2}`, 'Z'].join(' ');
    return <path key={i} d={pathData} fill={d.color || COLORS[i % COLORS.length]} />;
  });

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
      <svg width={size} height={size} style={{ flexShrink: 0 }}>
        {slices}
        {innerRadius > 0 && <circle cx={cx} cy={cy} r={innerRadius} fill="var(--color-bg-surface-default)" />}
      </svg>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
        {data.map((d, i) => (
          <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-caption)' }}>
            <span style={{ width: 10, height: 10, borderRadius: 2, background: d.color || COLORS[i % COLORS.length], flexShrink: 0 }} />
            <span style={{ color: 'var(--color-text-secondary)' }}>{d.label}</span>
            <span style={{ fontWeight: 'var(--weight-medium)', marginLeft: 'auto' }}>{Math.round((d.value / total) * 100)}%</span>
          </div>
        ))}
      </div>
    </div>
  );
});
