import React, { memo } from 'react';

interface PriorityConfig {
  label: string;
  variant: 'danger' | 'warning' | 'info' | 'neutral';
}

const PRIORITY_MAP: Record<string, PriorityConfig> = {
  critical: { label: 'Critical', variant: 'danger' },
  high: { label: 'High', variant: 'warning' },
  medium: { label: 'Medium', variant: 'info' },
  low: { label: 'Low', variant: 'neutral' },
};

export interface PriorityBadgeProps {
  priority: string;
  size?: 'sm' | 'md';
  className?: string;
  style?: React.CSSProperties;
}

export const PriorityBadge: React.FC<PriorityBadgeProps> = memo(({
  priority,
  size = 'sm',
  className,
  style,
}) => {
  const config = PRIORITY_MAP[priority.toLowerCase()] || { label: priority, variant: 'neutral' as const };

  const variantMap: Record<string, { bg: string; color: string }> = {
    danger: { bg: 'var(--color-bg-danger-weak)', color: 'var(--color-text-danger)' },
    warning: { bg: 'var(--color-bg-warning-weak)', color: 'var(--color-text-warning)' },
    info: { bg: 'var(--color-bg-info-weak)', color: 'var(--color-text-info)' },
    neutral: { bg: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)' },
  };

  const v = variantMap[config.variant];
  const sizes: Record<string, React.CSSProperties> = {
    sm: { fontSize: 'var(--text-caption)', padding: '2px 8px', height: 20 },
    md: { fontSize: 'var(--text-caption)', padding: '3px 10px', height: 24 },
  };

  return (
    <span
      className={className}
      style={{
        display: 'inline-flex', alignItems: 'center', borderRadius: 'var(--radius-badge)',
        fontWeight: 'var(--weight-medium)', lineHeight: 1, whiteSpace: 'nowrap',
        ...sizes[size], background: v.bg, color: v.color, ...style,
      }}
    >
      {config.label}
    </span>
  );
}) as React.FC<PriorityBadgeProps>;

PriorityBadge.displayName = 'PriorityBadge';
export default PriorityBadge;
