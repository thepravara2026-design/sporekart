import React, { useCallback } from 'react';

export interface ChipProps {
  children?: React.ReactNode;
  variant?: 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info';
  size?: 'sm' | 'md' | 'lg';
  type?: 'filter' | 'action' | 'selectable' | 'removable' | 'tag';
  icon?: React.ReactNode;
  selected?: boolean;
  disabled?: boolean;
  onSelect?: () => void;
  onRemove?: () => void;
  onClick?: () => void;
  className?: string;
}

const chipVariantMap: Record<string, { bg: string; color: string; border: string }> = {
  default: { bg: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', border: 'var(--color-border-default)' },
  primary: { bg: 'var(--color-bg-primary-weak)', color: 'var(--color-bg-primary-default)', border: 'var(--color-bg-primary-weak)' },
  success: { bg: 'var(--color-success-50)', color: 'var(--color-text-success)', border: 'var(--color-success-50)' },
  warning: { bg: 'var(--color-warning-50)', color: 'var(--color-text-warning)', border: 'var(--color-warning-50)' },
  danger: { bg: 'var(--color-danger-50)', color: 'var(--color-text-danger)', border: 'var(--color-danger-50)' },
  info: { bg: 'var(--color-info-50)', color: 'var(--color-text-info)', border: 'var(--color-info-50)' },
};

const chipSizeMap: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-caption)', height: 24, padding: '0 var(--space-2)' },
  md: { fontSize: 'var(--text-body-sm)', height: 28, padding: '0 var(--space-2)' },
  lg: { fontSize: 'var(--text-body)', height: 32, padding: '0 var(--space-3)' },
};

export const Chip: React.FC<ChipProps> = ({
  children,
  variant = 'default',
  size = 'md',
  type = 'tag',
  icon,
  selected = false,
  disabled = false,
  onSelect,
  onRemove,
  onClick,
  className = '',
}) => {
  const v = chipVariantMap[variant];

  const isInteractive = type === 'filter' || type === 'action' || type === 'selectable';

  const handleClick = useCallback(() => {
    if (disabled) return;
    if (type === 'filter' && onSelect) onSelect();
    else if (type === 'selectable' && onSelect) onSelect();
    else if (type === 'action' && onClick) onClick();
    else if (onClick) onClick();
  }, [disabled, type, onSelect, onClick]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (disabled) return;
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        handleClick();
      }
      if (e.key === 'Escape' && type === 'removable' && onRemove) {
        onRemove();
      }
    },
    [disabled, handleClick, type, onRemove]
  );

  if (type === 'removable') {
    return (
      <span
        className={`sk-chip sk-chip--removable ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          borderRadius: 'var(--radius-tag)',
          border: 'var(--border-width-thin) solid ' + v.border,
          background: v.bg,
          color: v.color,
          fontWeight: 'var(--weight-medium)',
          cursor: disabled ? 'not-allowed' : 'default',
          opacity: disabled ? 'var(--opacity-disabled)' : undefined,
          userSelect: 'none',
          ...chipSizeMap[size],
        }}
      >
        {icon && <span className="sk-chip__icon" style={{ fontSize: size === 'sm' ? 12 : 14 }}>{icon}</span>}
        <span className="sk-chip__label">{children}</span>
        <button
          className="sk-chip__remove"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: size === 'sm' ? 14 : 16,
            height: size === 'sm' ? 14 : 16,
            borderRadius: 'var(--radius-full)',
            border: 'none',
            background: 'transparent',
            color: 'var(--color-text-disabled)',
            cursor: disabled ? 'not-allowed' : 'pointer',
            fontSize: size === 'sm' ? 10 : 12,
            lineHeight: 1,
            padding: 0,
          }}
          onClick={(e) => {
            e.stopPropagation();
            if (!disabled && onRemove) onRemove();
          }}
          onKeyDown={(e) => {
            if (e.key === 'Enter' || e.key === ' ') {
              e.stopPropagation();
              if (!disabled && onRemove) onRemove();
            }
          }}
          aria-label="Remove"
          tabIndex={0}
          disabled={disabled}
        >
          {'\u2715'}
        </button>
      </span>
    );
  }

  if (type === 'selectable') {
    return (
      <span
        className={`sk-chip sk-chip--selectable ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          borderRadius: 'var(--radius-tag)',
          border: 'var(--border-width-thin) solid ' + (selected ? 'var(--color-bg-primary-default)' : v.border),
          background: selected ? 'var(--color-bg-primary-weak)' : v.bg,
          color: selected ? 'var(--color-bg-primary-default)' : v.color,
          fontWeight: 'var(--weight-medium)',
          cursor: disabled ? 'not-allowed' : 'pointer',
          opacity: disabled ? 'var(--opacity-disabled)' : undefined,
          userSelect: 'none',
          transition: 'all var(--duration-fast) var(--easing-standard)',
          ...chipSizeMap[size],
        }}
        role="option"
        aria-selected={selected}
        tabIndex={disabled ? -1 : 0}
        onClick={handleClick}
        onKeyDown={handleKeyDown}
      >
        <span
          style={{
            width: 14,
            height: 14,
            borderRadius: 'var(--radius-xs)',
            border: 'var(--border-width-thin) solid ' + (selected ? 'var(--color-bg-primary-default)' : 'var(--color-border-strong)'),
            background: selected ? 'var(--color-bg-primary-default)' : 'transparent',
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: selected ? '#FFFFFF' : 'transparent',
            fontSize: 10,
            fontWeight: 'var(--weight-bold)',
            flexShrink: 0,
          }}
        >
          {selected ? '\u2713' : ''}
        </span>
        {icon && <span className="sk-chip__icon" style={{ fontSize: size === 'sm' ? 12 : 14 }}>{icon}</span>}
        <span className="sk-chip__label">{children}</span>
      </span>
    );
  }

  if (type === 'filter') {
    return (
      <span
        className={`sk-chip sk-chip--filter ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          borderRadius: 'var(--radius-tag)',
          border: 'var(--border-width-thin) solid ' + (selected ? 'var(--color-bg-primary-default)' : v.border),
          background: selected ? 'var(--color-bg-primary-weak)' : v.bg,
          color: selected ? 'var(--color-bg-primary-default)' : v.color,
          fontWeight: selected ? 'var(--weight-semibold)' : 'var(--weight-medium)',
          cursor: disabled ? 'not-allowed' : 'pointer',
          opacity: disabled ? 'var(--opacity-disabled)' : undefined,
          userSelect: 'none',
          transition: 'all var(--duration-fast) var(--easing-standard)',
          ...chipSizeMap[size],
        }}
        role="option"
        aria-selected={selected}
        tabIndex={disabled ? -1 : 0}
        onClick={handleClick}
        onKeyDown={handleKeyDown}
      >
        {icon && <span className="sk-chip__icon" style={{ fontSize: size === 'sm' ? 12 : 14 }}>{icon}</span>}
        <span className="sk-chip__label">{children}</span>
      </span>
    );
  }

  if (isInteractive) {
    return (
      <span
        className={`sk-chip sk-chip--action ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          borderRadius: 'var(--radius-tag)',
          border: 'var(--border-width-thin) solid ' + v.border,
          background: v.bg,
          color: v.color,
          fontWeight: 'var(--weight-medium)',
          cursor: disabled ? 'not-allowed' : 'pointer',
          opacity: disabled ? 'var(--opacity-disabled)' : undefined,
          userSelect: 'none',
          transition: 'all var(--duration-fast) var(--easing-standard)',
          ...chipSizeMap[size],
        }}
        role="button"
        tabIndex={disabled ? -1 : 0}
        onClick={handleClick}
        onKeyDown={handleKeyDown}
      >
        {icon && <span className="sk-chip__icon" style={{ fontSize: size === 'sm' ? 12 : 14 }}>{icon}</span>}
        <span className="sk-chip__label">{children}</span>
      </span>
    );
  }

  return (
    <span
      className={`sk-chip sk-chip--tag ${className}`}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 'var(--space-inline-xs)',
        borderRadius: 'var(--radius-tag)',
        border: 'var(--border-width-thin) solid ' + v.border,
        background: v.bg,
        color: v.color,
        fontWeight: 'var(--weight-medium)',
        userSelect: 'none',
        ...chipSizeMap[size],
      }}
    >
      {icon && <span className="sk-chip__icon" style={{ fontSize: size === 'sm' ? 12 : 14 }}>{icon}</span>}
      <span className="sk-chip__label">{children}</span>
    </span>
  );
};

Chip.displayName = 'Chip';

export default Chip;
