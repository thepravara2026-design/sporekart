import React, { memo } from 'react';

const ORDER_STATUS_MAP: Record<string, { label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' }> = {
  confirmed: { label: 'Confirmed', variant: 'info' },
  processing: { label: 'Processing', variant: 'info' },
  shipped: { label: 'Shipped', variant: 'warning' },
  delivered: { label: 'Delivered', variant: 'success' },
  cancelled: { label: 'Cancelled', variant: 'danger' },
  returned: { label: 'Returned', variant: 'neutral' },
  refunded: { label: 'Refunded', variant: 'neutral' },
  pending: { label: 'Pending', variant: 'warning' },
};

export interface OrderBadgeProps {
  status: string;
  size?: 'sm' | 'md';
  className?: string;
  style?: React.CSSProperties;
}

const variantMap: Record<string, { bg: string; color: string }> = {
  success: { bg: 'var(--color-bg-success-weak)', color: 'var(--color-text-success)' },
  warning: { bg: 'var(--color-bg-warning-weak)', color: 'var(--color-text-warning)' },
  danger: { bg: 'var(--color-bg-danger-weak)', color: 'var(--color-text-danger)' },
  info: { bg: 'var(--color-bg-info-weak)', color: 'var(--color-text-info)' },
  neutral: { bg: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)' },
};

export const OrderBadge: React.FC<OrderBadgeProps> = memo(({ status, size = 'sm', className, style }) => {
  const config = ORDER_STATUS_MAP[status.toLowerCase()] || { label: status, variant: 'neutral' as const };
  const v = variantMap[config.variant];
  const sizes: Record<string, React.CSSProperties> = {
    sm: { fontSize: 'var(--text-caption)', padding: '2px 8px', height: 20 },
    md: { fontSize: 'var(--text-caption)', padding: '3px 10px', height: 24 },
  };
  return (
    <span className={className} style={{ display: 'inline-flex', alignItems: 'center', borderRadius: 'var(--radius-badge)', fontWeight: 'var(--weight-medium)', lineHeight: 1, whiteSpace: 'nowrap', ...sizes[size], background: v.bg, color: v.color, ...style }}>
      {config.label}
    </span>
  );
}) as React.FC<OrderBadgeProps>;

OrderBadge.displayName = 'OrderBadge';
export default OrderBadge;
