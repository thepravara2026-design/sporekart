import { memo, type CSSProperties } from 'react';
import type { StockState } from '../types';
import { getStateVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';

const variantStyle = (v: string): CSSProperties => ({
  background: `var(--color-${v}-alpha, var(--color-surface-hover))`,
  color: VARIANT_COLORS[v] ?? VARIANT_COLORS.default,
});

interface Props { state: StockState; }

export const StockStateBadge = memo(function StockStateBadge({ state }: Props) {
  const variant = getStateVariant(state);
  const colors = variantStyle(variant);
  return <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...colors }}>{state.replace(/_/g, ' ')}</span>;
});
