import React from 'react';

export interface BadgeProps {
  children?: React.ReactNode;
  variant?: 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'neutral';
  size?: 'sm' | 'md' | 'lg';
  type?: 'status' | 'count' | 'notification' | 'verification' | 'progress';
  icon?: React.ReactNode;
  count?: number;
  maxCount?: number;
  dot?: boolean;
  pulse?: boolean;
  className?: string;
}

const variantMap: Record<string, { bg: string; color: string }> = {
  default: { bg: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)' },
  primary: { bg: 'var(--color-bg-primary-default)', color: 'var(--color-text-on-primary)' },
  success: { bg: 'var(--color-success)', color: '#FFFFFF' },
  warning: { bg: 'var(--color-warning)', color: 'var(--color-neutral-900)' },
  danger: { bg: 'var(--color-danger)', color: '#FFFFFF' },
  info: { bg: 'var(--color-info)', color: '#FFFFFF' },
  neutral: { bg: 'var(--color-neutral-200)', color: 'var(--color-text-secondary)' },
};

const badgeSizeMap: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-caption)', padding: '1px 6px', height: 18, gap: 2 },
  md: { fontSize: 'var(--text-body-sm)', padding: '2px 8px', height: 22, gap: 4 },
  lg: { fontSize: 'var(--text-body)', padding: '3px 10px', height: 26, gap: 4 },
};

const dotSizeMap: Record<string, number> = { sm: 6, md: 8, lg: 10 };

const iconSizeMap: Record<string, number> = { sm: 10, md: 12, lg: 14 };

export const Badge: React.FC<BadgeProps> = ({
  children,
  variant = 'default',
  size = 'md',
  type = 'status',
  icon,
  count,
  maxCount = 99,
  dot = false,
  pulse = false,
  className = '',
}) => {
  const v = variantMap[variant];
  const pulseAnim = pulse ? 'sk-badge-pulse 2s ease-in-out infinite' : undefined;

  if (type === 'status' && dot) {
    return (
      <span
        className={`sk-badge sk-badge--dot ${className}`}
        style={{
          display: 'inline-block',
          width: dotSizeMap[size],
          height: dotSizeMap[size],
          borderRadius: 'var(--radius-full)',
          background: v.bg,
          animation: pulseAnim,
        }}
        role="status"
      >
        <style>{`@keyframes sk-badge-pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }`}</style>
      </span>
    );
  }

  if (type === 'notification') {
    return (
      <span
        className={`sk-badge sk-badge--notification ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          minWidth: dotSizeMap[size],
          height: dotSizeMap[size],
          borderRadius: 'var(--radius-full)',
          background: v.bg,
          color: v.color,
          fontSize: 'var(--text-caption)',
          fontWeight: 'var(--weight-bold)',
          lineHeight: 1,
          padding: '0 4px',
          animation: pulseAnim,
        }}
        aria-label={count != null ? `${count} notifications` : 'notification'}
      >
        {count != null && count > 0 && (
          <span>{count > maxCount ? `${maxCount}+` : count}</span>
        )}
        {!count && <span style={{ width: 6, height: 6, borderRadius: '50%', background: 'currentColor' }} />}
        <style>{`@keyframes sk-badge-pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }`}</style>
      </span>
    );
  }

  if (type === 'count') {
    const dim = badgeSizeMap[size].height as number;
    return (
      <span
        className={`sk-badge sk-badge--count ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          minWidth: dim,
          height: dim,
          borderRadius: 'var(--radius-full)',
          background: v.bg,
          color: v.color,
          fontSize: 'var(--text-caption)',
          fontWeight: 'var(--weight-bold)',
          lineHeight: 1,
          padding: '0 6px',
        }}
      >
        {count != null ? (count > maxCount ? `${maxCount}+` : count) : children}
      </span>
    );
  }

  if (type === 'verification') {
    const iconDim = iconSizeMap[size];
    return (
      <span
        className={`sk-badge sk-badge--verification ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: iconDim,
          height: iconDim,
          borderRadius: 'var(--radius-full)',
          background: v.bg,
          color: v.color,
          fontSize: iconDim * 0.6,
          fontWeight: 'var(--weight-bold)',
        }}
        aria-label="Verified"
      >
        {icon || '\u2713'}
      </span>
    );
  }

  if (type === 'progress') {
    const barWidth = size === 'sm' ? 40 : size === 'md' ? 56 : 72;
    return (
      <span
        className={`sk-badge sk-badge--progress ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 4,
          fontSize: 'var(--text-caption)',
          color: v.color,
          fontWeight: 'var(--weight-medium)',
        }}
      >
        <span
          style={{
            width: barWidth,
            height: 4,
            borderRadius: 'var(--radius-full)',
            background: 'var(--color-bg-skeleton-base)',
            overflow: 'hidden',
            position: 'relative',
          }}
        >
          <span
            style={{
              position: 'absolute',
              inset: 0,
              background: v.bg,
              borderRadius: 'var(--radius-full)',
              animation: 'sk-badge-indeterminate 1.5s ease-in-out infinite',
              width: '40%',
            }}
          />
        </span>
        {children}
        <style>{`@keyframes sk-badge-indeterminate { 0% { left: -40%; } 100% { left: 100%; } }`}</style>
      </span>
    );
  }

  return (
    <span
      className={`sk-badge ${className}`}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 'var(--radius-badge)',
        fontWeight: 'var(--weight-medium)',
        lineHeight: 1,
        whiteSpace: 'nowrap',
        userSelect: 'none',
        border: variant === 'default' ? 'var(--border-width-thin) solid var(--color-border-default)' : 'var(--border-width-none) solid transparent',
        ...badgeSizeMap[size],
        background: v.bg,
        color: v.color,
      }}
    >
      {icon && (
        <span className="sk-badge__icon" style={{ fontSize: iconSizeMap[size] }}>
          {icon}
        </span>
      )}
      {children}
    </span>
  );
};

Badge.displayName = 'Badge';

export default Badge;
