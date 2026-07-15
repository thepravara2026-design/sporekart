import { memo, type CSSProperties } from 'react';
import type { AvailabilityLevel } from '../types';
import { getAvailabilityVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';

const variantStyle = (v: string): CSSProperties => ({
  background: `var(--color-${v}-alpha, var(--color-surface-hover))`,
  color: VARIANT_COLORS[v] ?? VARIANT_COLORS.default,
});

interface Props { level: AvailabilityLevel; }

export const AvailabilityBadge = memo(function AvailabilityBadge({ level }: Props) {
  const variant = getAvailabilityVariant(level);
  const colors = variantStyle(variant);
  return <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...colors }}>{level.replace(/_/g, ' ')}</span>;
});
