import { memo } from 'react';
import { TIER_LABELS, TIER_VARIANTS } from '../types';

export const TierBadge = memo(function TierBadge({ tier }: { tier: string }) {
  const v = TIER_VARIANTS[tier] || { bg: '#f3f4f6', color: '#6b7280' };
  const label = TIER_LABELS[tier as keyof typeof TIER_LABELS] || tier;
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: v.bg, color: v.color,
    }}>
      {label}
    </span>
  );
});
