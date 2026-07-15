import { memo } from 'react';

interface BarChartProps {
  data: { label: string; value: number; color?: string }[];
  height?: number;
  maxValue?: number;
}

export const BarChart = memo(function BarChart({ data, height = 240, maxValue }: BarChartProps) {
  const mx = maxValue ?? Math.max(...data.map((d) => d.value), 1);
  return (
    <div style={{ display: 'flex', alignItems: 'flex-end', gap: 8, height, width: '100%' }}>
      {data.map((d) => {
        const pct = (d.value / mx) * 100;
        return (
          <div key={d.label} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4, height: '100%', justifyContent: 'flex-end' }}>
            <div style={{ width: '100%', height: `${pct}%`, background: d.color ?? 'var(--color-primary)', borderRadius: 'var(--radius-sm) var(--radius-sm) 0 0', minHeight: 2, transition: 'height 0.3s' }} />
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textAlign: 'center', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '100%' }}>{d.label}</span>
          </div>
        );
      })}
    </div>
  );
});
