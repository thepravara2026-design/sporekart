import { memo } from 'react';

interface ChartProps {
  data: { label: string; value: number; color: string }[];
  size?: number;
  holeSize?: number;
}

const COLORS = ['var(--color-primary)', 'var(--color-secondary)', 'var(--color-tertiary)', 'var(--color-warning)', 'var(--color-danger)', 'var(--color-success)', 'var(--color-info)', 'var(--color-neutral)'];

export const DonutChart = memo(function DonutChart({ data, size = 160, holeSize = 22 }: ChartProps) {
  const total = data.reduce((s, d) => s + d.value, 0) || 1;
  let offset = 0;
  const r = 40;
  const circ = 2 * Math.PI * r;
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 20 }}>
      <svg width={size} height={size} viewBox="0 0 100 100">
        <circle cx="50" cy="50" r={r} fill="none" stroke="var(--color-border)" strokeWidth="12" />
        {data.map((d, i) => {
          const pct = d.value / total;
          const seg = pct * circ;
          const segColor = d.color || COLORS[i % COLORS.length];
          const el = (
            <circle key={`seg-${i}`} cx="50" cy="50" r={r} fill="none" stroke={segColor} strokeWidth="12"
              strokeDasharray={`${Math.max(seg, 0.5)} ${circ}`}
              strokeDashoffset={-offset}
              transform="rotate(-90 50 50)"
              strokeLinecap="butt"
              vectorEffect="non-scaling-stroke" />
          );
          offset += seg;
          return el;
        })}
        {holeSize > 0 && <circle cx="50" cy="50" r={holeSize} fill="var(--color-surface)" />}
      </svg>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
        {data.map((d, i) => (
          <div key={d.label} style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <span style={{ width: 10, height: 10, borderRadius: 2, background: d.color || COLORS[i % COLORS.length], flexShrink: 0 }} />
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{d.label} ({(d.value / total * 100).toFixed(1)}%)</span>
          </div>
        ))}
      </div>
    </div>
  );
});
