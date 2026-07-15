import { memo } from 'react';

interface LineChartProps {
  data: { label: string; value: number }[];
  height?: number;
  color?: string;
}

export const LineChart = memo(function LineChart({ data, height = 240, color = 'var(--color-primary)' }: LineChartProps) {
  if (data.length < 2) return <div style={{ height, display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Not enough data</div>;
  const mx = Math.max(...data.map((d) => d.value), 1);
  const w = 100;
  const h = 100;
  const pts = data.map((d, i) => `${(i / (data.length - 1)) * w},${h - (d.value / mx) * h}`).join(' ');
  return (
    <svg viewBox={`0 0 ${w} ${h}`} style={{ width: '100%', height, maxHeight: h }} preserveAspectRatio="none">
      <polyline points={pts} fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" vectorEffect="non-scaling-stroke" />
    </svg>
  );
});
