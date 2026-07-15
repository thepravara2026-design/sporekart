import React, { memo } from 'react';

export interface StatusBadgeProps {
  status: string;
  variant?: 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral';
  size?: 'sm' | 'md' | 'lg';
  pulse?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const variantMap: Record<string, { bg: string; color: string; dot: string }> = {
  default: { bg: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)', dot: 'var(--color-text-secondary)' },
  success: { bg: 'var(--color-bg-success-weak)', color: 'var(--color-text-success)', dot: 'var(--color-success)' },
  warning: { bg: 'var(--color-bg-warning-weak)', color: 'var(--color-text-warning)', dot: 'var(--color-warning)' },
  danger: { bg: 'var(--color-bg-danger-weak)', color: 'var(--color-text-danger)', dot: 'var(--color-danger)' },
  info: { bg: 'var(--color-bg-info-weak)', color: 'var(--color-text-info)', dot: 'var(--color-info)' },
  neutral: { bg: 'var(--color-bg-surface-default)', color: 'var(--color-text-secondary)', dot: 'var(--color-text-disabled)' },
};

const sizeMap: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-caption)', padding: '2px 8px', gap: 4, height: 20 },
  md: { fontSize: 'var(--text-caption)', padding: '3px 10px', gap: 5, height: 24 },
  lg: { fontSize: 'var(--text-body-sm)', padding: '4px 12px', gap: 6, height: 28 },
};

const dotSizeMap: Record<string, number> = { sm: 6, md: 7, lg: 8 };

export const StatusBadge: React.FC<StatusBadgeProps> = memo(({
  status,
  variant = 'default',
  size = 'sm',
  pulse = false,
  className = '',
  style,
}) => {
  const v = variantMap[variant];
  return (
    <span
      className={className}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: sizeMap[size].gap,
        borderRadius: 'var(--radius-badge)',
        fontWeight: 'var(--weight-medium)',
        lineHeight: 1,
        whiteSpace: 'nowrap',
        userSelect: 'none',
        ...sizeMap[size],
        background: v.bg,
        color: v.color,
        ...style,
      }}
    >
      <span
        style={{
          width: dotSizeMap[size],
          height: dotSizeMap[size],
          borderRadius: 'var(--radius-full)',
          background: v.dot,
          flexShrink: 0,
          animation: pulse ? 'sk-status-pulse 2s ease-in-out infinite' : undefined,
        }}
      />
      <span>{status}</span>
      <style>{`@keyframes sk-status-pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.4; } }`}</style>
    </span>
  );
}) as React.FC<StatusBadgeProps>;

StatusBadge.displayName = 'StatusBadge';
export default StatusBadge;
