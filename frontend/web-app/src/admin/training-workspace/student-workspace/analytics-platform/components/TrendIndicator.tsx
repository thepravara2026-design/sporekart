import { memo } from 'react';

interface TrendIndicatorProps {
  value: number;
  label: string;
  inverse?: boolean;
}

export const TrendIndicator = memo(function TrendIndicator({ value, label, inverse }: TrendIndicatorProps) {
  const isGood = inverse ? value <= 50 : value >= 50;
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm)' }}>
      <span style={{ color: isGood ? '#16a34a' : '#dc2626', fontWeight: 'var(--weight-bold)' }}>{value}%</span>
      <span style={{ color: 'var(--color-text-tertiary)' }}>{label}</span>
    </div>
  );
});
