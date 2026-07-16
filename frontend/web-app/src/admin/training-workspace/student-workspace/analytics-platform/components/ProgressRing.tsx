import { memo } from 'react';

interface ProgressRingProps {
  value: number;
  size?: number;
  strokeWidth?: number;
  color?: string;
  label?: string;
}

export const ProgressRing = memo(function ProgressRing({ value, size = 80, strokeWidth = 6, color = '#2563eb', label }: ProgressRingProps) {
  const r = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * r;
  const offset = circumference - (Math.min(value, 100) / 100) * circumference;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
      <svg width={size} height={size}>
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="var(--color-bg-skeleton-base)" strokeWidth={strokeWidth} />
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke={color} strokeWidth={strokeWidth} strokeDasharray={circumference} strokeDashoffset={offset} strokeLinecap="round" transform={`rotate(-90 ${size / 2} ${size / 2})`} style={{ transition: 'stroke-dashoffset 0.5s' }} />
        <text x="50%" y="50%" textAnchor="middle" dominantBaseline="central" fontSize={size * 0.25} fontWeight="bold" fill="var(--color-text-primary)">{Math.round(value)}%</text>
      </svg>
      {label && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textAlign: 'center' }}>{label}</span>}
    </div>
  );
});
