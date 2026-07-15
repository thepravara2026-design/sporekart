import React, { memo } from 'react';

export interface ProgressChartProps {
  value: number;
  max?: number;
  label: string;
  color?: string;
  size?: 'sm' | 'md' | 'lg';
  showLabel?: boolean;
}

const sizeConfig: Record<string, { barHeight: number; fontSize: string; circleSize: number; strokeWidth: number; labelSize: string; valueSize: string }> = {
  sm: { barHeight: 6, fontSize: 'var(--text-body-xs)', circleSize: 80, strokeWidth: 6, labelSize: 'var(--text-caption)', valueSize: 'var(--text-body-sm)' },
  md: { barHeight: 10, fontSize: 'var(--text-body-sm)', circleSize: 120, strokeWidth: 8, labelSize: 'var(--text-body-xs)', valueSize: 'var(--text-body-md)' },
  lg: { barHeight: 14, fontSize: 'var(--text-body-md)', circleSize: 160, strokeWidth: 12, labelSize: 'var(--text-body-sm)', valueSize: 'var(--text-body-lg)' },
};

function getThresholdColor(val: number, max: number) {
  const pct = max > 0 ? (val / max) * 100 : 0;
  if (pct >= 80) return 'var(--color-success)';
  if (pct >= 50) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export const ProgressChart: React.FC<ProgressChartProps> = memo(({ value, max = 100, label, color, size = 'md', showLabel = true }) => {
  const pct = Math.min(Math.max(max > 0 ? (value / max) * 100 : 0, 0), 100);
  const cfg = sizeConfig[size];
  const barColor = color || getThresholdColor(value, max);

  const polarToCartesian = (cx: number, cy: number, r: number, angleDeg: number) => {
    const rad = (angleDeg - 90) * Math.PI / 180;
    return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
  };

  const describeArc = (cx: number, cy: number, r: number, startDeg: number, endDeg: number) => {
    const start = polarToCartesian(cx, cy, r, endDeg);
    const end = polarToCartesian(cx, cy, r, startDeg);
    const large = endDeg - startDeg > 180 ? 1 : 0;
    return `M ${start.x} ${start.y} A ${r} ${r} 0 ${large} 0 ${end.x} ${end.y}`;
  };

  const s = cfg.circleSize;
  const cx = s / 2;
  const cy = s / 2;
  const r = (s - cfg.strokeWidth) / 2;
  const bgArc = describeArc(cx, cy, r, 0, 359.999);
  const fgArc = describeArc(cx, cy, r, 0, (pct / 100) * 360);

  return (
    <div role="img" aria-label={`${label}: ${Math.round(pct)}%`} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, textAlign: 'center', width: '100%', overflow: 'hidden' }}>
      <svg width="100%" height={s} viewBox={`0 0 ${s} ${s}`} style={{ display: 'block', margin: '0 auto' }}>
        <path d={bgArc} fill="none" stroke="var(--color-border)" strokeWidth={cfg.strokeWidth} strokeLinecap="round" />
        <path d={fgArc} fill="none" stroke={barColor} strokeWidth={cfg.strokeWidth} strokeLinecap="round" />
        {showLabel && (
          <text x={cx} y={cy - 6} textAnchor="middle" dominantBaseline="central" fill="var(--color-text-primary)" fontSize={cfg.valueSize} fontWeight="var(--weight-bold)">
            {Math.round(pct)}%
          </text>
        )}
        {showLabel && (
          <text x={cx} y={cy + 14} textAnchor="middle" dominantBaseline="central" fill="var(--color-text-secondary)" fontSize={cfg.labelSize}>
            {label}
          </text>
        )}
      </svg>
    </div>
  );
});

ProgressChart.displayName = 'ProgressChart';
export default ProgressChart;
