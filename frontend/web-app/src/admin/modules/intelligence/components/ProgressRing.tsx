import { memo } from 'react';

interface ProgressRingProps {
  value: number; max?: number; size?: number; strokeWidth?: number; color?: string;
}

export const ProgressRing = memo(function ProgressRing({ value, max = 100, size = 80, strokeWidth = 8, color = 'var(--color-primary)' }: ProgressRingProps) {
  const r = (size - strokeWidth) / 2;
  const circ = 2 * Math.PI * r;
  const pct = Math.min(value / max, 1);
  return (
    <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
      <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="var(--color-border)" strokeWidth={strokeWidth} />
      <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke={color} strokeWidth={strokeWidth}
        strokeDasharray={`${pct * circ} ${circ}`} transform={`rotate(-90 ${size / 2} ${size / 2})`} strokeLinecap="round" />
      <text x={size / 2} y={size / 2} textAnchor="middle" dominantBaseline="central" fontSize={size * 0.2} fontWeight={700} fill="var(--color-text-primary)">
        {Math.round(pct * 100)}%
      </text>
    </svg>
  );
});
