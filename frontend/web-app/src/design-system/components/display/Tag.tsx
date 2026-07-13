import React from 'react';

export interface TagProps {
  children?: React.ReactNode;
  variant?: 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'neutral';
  size?: 'sm' | 'md' | 'lg';
  type?: 'category' | 'status' | 'label';
  icon?: React.ReactNode;
  removable?: boolean;
  onRemove?: () => void;
  disabled?: boolean;
  className?: string;
}

const tagVariantMap: Record<string, { bg: string; color: string }> = {
  default: { bg: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)' },
  primary: { bg: 'var(--color-bg-primary-weak)', color: 'var(--color-bg-primary-default)' },
  success: { bg: 'var(--color-success-50)', color: 'var(--color-text-success)' },
  warning: { bg: 'var(--color-warning-50)', color: 'var(--color-text-warning)' },
  danger: { bg: 'var(--color-danger-50)', color: 'var(--color-text-danger)' },
  info: { bg: 'var(--color-info-50)', color: 'var(--color-text-info)' },
  neutral: { bg: 'var(--color-neutral-100)', color: 'var(--color-text-secondary)' },
};

const tagSizeMap: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-caption)', padding: '1px var(--space-inline-xs)', height: 20 },
  md: { fontSize: 'var(--text-body-sm)', padding: '2px var(--space-inline-sm)', height: 24 },
  lg: { fontSize: 'var(--text-body)', padding: '3px var(--space-inline-sm)', height: 28 },
};

export const Tag: React.FC<TagProps> = ({
  children,
  variant = 'default',
  size = 'md',
  type = 'label',
  icon,
  removable = false,
  onRemove,
  disabled = false,
  className = '',
}) => {
  const v = tagVariantMap[variant];

  const dotColor = type === 'status'
    ? v.color
    : undefined;

  return (
    <span
      className={`sk-tag sk-tag--${type} ${className}`}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 'var(--space-inline-xs)',
        borderRadius: 'var(--radius-tag)',
        background: v.bg,
        color: v.color,
        fontWeight: type === 'category' ? 'var(--weight-semibold)' : 'var(--weight-medium)',
        whiteSpace: 'nowrap',
        userSelect: 'none',
        opacity: disabled ? 'var(--opacity-disabled)' : undefined,
        pointerEvents: disabled ? 'none' : undefined,
        border: variant === 'default'
          ? 'var(--border-width-thin) solid var(--color-border-default)'
          : 'var(--border-width-none) solid transparent',
        ...tagSizeMap[size],
      }}
    >
      {type === 'status' && (
        <span
          className="sk-tag__dot"
          style={{
            width: size === 'sm' ? 6 : 8,
            height: size === 'sm' ? 6 : 8,
            borderRadius: 'var(--radius-full)',
            background: dotColor,
            flexShrink: 0,
          }}
        />
      )}
      {icon && (
        <span className="sk-tag__icon" style={{ fontSize: size === 'sm' ? 10 : 12, flexShrink: 0 }}>
          {icon}
        </span>
      )}
      <span className="sk-tag__label">{children}</span>
      {removable && (
        <button
          className="sk-tag__remove"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: size === 'sm' ? 12 : 14,
            height: size === 'sm' ? 12 : 14,
            borderRadius: 'var(--radius-full)',
            border: 'none',
            background: 'transparent',
            color: 'inherit',
            cursor: 'pointer',
            fontSize: size === 'sm' ? 8 : 10,
            lineHeight: 1,
            padding: 0,
            opacity: 0.6,
            flexShrink: 0,
          }}
          onClick={(e) => {
            e.stopPropagation();
            if (!disabled && onRemove) onRemove();
          }}
          aria-label="Remove tag"
        >
          {'\u2715'}
        </button>
      )}
    </span>
  );
};

Tag.displayName = 'Tag';

export default Tag;
