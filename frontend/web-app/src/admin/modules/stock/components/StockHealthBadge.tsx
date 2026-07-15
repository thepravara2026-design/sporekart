import { memo, type CSSProperties } from 'react';
import type { StockHealth } from '../types';
import { getHealthVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';

const variantStyle = (v: string): CSSProperties => ({
  background: `var(--color-${v}-alpha, var(--color-surface-hover))`,
  color: VARIANT_COLORS[v] ?? VARIANT_COLORS.default,
});

interface Props { health: StockHealth; }

export const StockHealthBadge = memo(function StockHealthBadge({ health }: Props) {
  const variant = getHealthVariant(health);
  const colors = variantStyle(variant);
  return <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...colors }}>{health.replace(/_/g, ' ')}</span>;
});
