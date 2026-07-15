import { memo } from 'react';
import type { BatchLifecycleState } from '../types';
import { getLifecycleVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';

const STYLES: Record<string, { bg: string; text: string }> = {
  success: { bg: 'var(--color-success-alpha)', text: VARIANT_COLORS.success },
  warning: { bg: 'var(--color-warning-alpha)', text: VARIANT_COLORS.warning },
  danger: { bg: 'var(--color-danger-alpha)', text: VARIANT_COLORS.danger },
  info: { bg: 'var(--color-info-alpha)', text: VARIANT_COLORS.info },
  neutral: { bg: 'var(--color-surface-hover)', text: VARIANT_COLORS.neutral },
  default: { bg: 'var(--color-surface-hover)', text: VARIANT_COLORS.default },
};

export const BatchStatusBadge = memo(function BatchStatusBadge({ status, label }: { status: BatchLifecycleState; label?: string }) {
  const variant = getLifecycleVariant(status);
  const colors = STYLES[variant] ?? STYLES.default;

  return (
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '2px 10px', borderRadius: 'var(--radius-badge, 12px)', background: colors.bg, color: colors.text, fontSize: 'var(--text-caption, 12px)', fontWeight: 500, whiteSpace: 'nowrap' }}>
      <span style={{ width: 6, height: 6, borderRadius: '50%', background: colors.text, flexShrink: 0 }} />
      {label ?? status.replace(/_/g, ' ')}
    </span>
  );
});
