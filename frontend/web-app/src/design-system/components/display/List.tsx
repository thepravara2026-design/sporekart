import React from 'react';

export interface ListItemData {
  id: string | number;
  label?: string;
  description?: string;
  icon?: React.ReactNode;
  media?: React.ReactNode;
  meta?: string;
  disabled?: boolean;
  onClick?: () => void;
}

export interface ListProps {
  items?: ListItemData[];
  variant?: 'simple' | 'icon' | 'description' | 'media' | 'interactive';
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  loadingCount?: number;
  empty?: boolean;
  emptyMessage?: string;
  error?: string;
  onRetry?: () => void;
  selectable?: boolean;
  selectedId?: string | number;
  onSelect?: (id: string | number) => void;
  divided?: boolean;
  className?: string;
}

const paddingMap: Record<string, string> = {
  sm: 'var(--space-sm) var(--space-md)',
  md: 'var(--space-md) var(--space-lg)',
  lg: 'var(--space-lg) var(--space-xl)',
};

const fontMap: Record<string, string> = {
  sm: 'var(--text-sm)',
  md: 'var(--text-base)',
  lg: 'var(--text-lg)',
};

const descFontMap: Record<string, string> = {
  sm: 'var(--text-xs)',
  md: 'var(--text-sm)',
  lg: 'var(--text-base)',
};

export const List: React.FC<ListProps> = ({
  items = [],
  variant = 'simple',
  size = 'md',
  loading = false,
  loadingCount = 3,
  empty = false,
  emptyMessage = 'No items',
  error,
  onRetry,
  selectable = false,
  selectedId,
  onSelect,
  divided = false,
  className = '',
}) => {
  const itemPadding = paddingMap[size];
  const itemFont = fontMap[size];
  const descFont = descFontMap[size];

  const listStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 0,
    fontFamily: 'var(--font-family-sans)',
  };

  const baseItemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: variant === 'description' || variant === 'media' ? 'flex-start' : 'center',
    gap: 'var(--space-md)',
    padding: itemPadding,
    fontSize: itemFont,
    color: 'var(--color-text-primary)',
    borderBottom: divided ? '1px solid var(--color-border-default)' : undefined,
    transition: `background-color var(--duration-fast) var(--easing-standard)`,
    cursor: variant === 'interactive' || (variant !== 'simple' && variant !== 'description' && variant !== 'media') ? 'pointer' : undefined,
  };

  const selectedStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-primary-default)',
    color: 'var(--color-text-on-primary)',
  };

  const disabledItemStyle: React.CSSProperties = {
    opacity: 'var(--opacity-disabled)',
    cursor: 'not-allowed',
    pointerEvents: 'none',
  };

  const labelStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-medium)',
  };

  const descStyle: React.CSSProperties = {
    fontSize: descFont,
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-xs)',
  };

  const iconStyle: React.CSSProperties = {
    flexShrink: 0,
    fontSize: 'var(--icon-md)',
    color: 'var(--color-text-secondary)',
  };

  const mediaStyle: React.CSSProperties = {
    flexShrink: 0,
    width: 48,
    height: 48,
    borderRadius: 'var(--radius-sm)',
    overflow: 'hidden',
  };

  const metaStyle: React.CSSProperties = {
    marginLeft: 'auto',
    fontSize: descFont,
    color: 'var(--color-text-secondary)',
    flexShrink: 0,
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const skeletonStyle: React.CSSProperties = {
    height: 12,
    borderRadius: 'var(--radius-sm)',
    backgroundColor: 'var(--color-neutral-200)',
    animation: 'sk-pulse 1.5s ease-in-out infinite',
  };

  const skeletonItemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-md)',
    padding: itemPadding,
    borderBottom: divided ? '1px solid var(--color-border-default)' : undefined,
  };

  const skeletonCircleStyle: React.CSSProperties = {
    width: 32,
    height: 32,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-neutral-200)',
    animation: 'sk-pulse 1.5s ease-in-out infinite',
    flexShrink: 0,
  };

  const emptyContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'var(--space-xl)',
    color: 'var(--color-text-secondary)',
    fontSize: itemFont,
  };

  const errorContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'var(--space-xl)',
    color: 'var(--color-danger)',
    fontSize: itemFont,
    gap: 'var(--space-md)',
  };

  const retryStyle: React.CSSProperties = {
    padding: 'var(--space-sm) var(--space-md)',
    fontSize: 'var(--text-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-on-primary)',
    backgroundColor: 'var(--color-danger)',
    border: 'none',
    borderRadius: 'var(--radius-sm)',
    cursor: 'pointer',
  };

  const listRole = selectable ? 'listbox' : undefined;

  const handleClick = (item: ListItemData) => {
    if (item.disabled) return;
    if (selectable && onSelect) {
      onSelect(item.id);
    }
    item.onClick?.();
  };

  const renderSkeleton = () => {
    const count = loadingCount > 0 ? loadingCount : 3;
    return (
      <ul className={`sk-list sk-list--loading ${className}`.trim()} style={listStyle} role={listRole}>
        {Array.from({ length: count }).map((_, i) => (
          <li key={i} style={skeletonItemStyle}>
            {(variant === 'icon' || variant === 'media') && <span style={skeletonCircleStyle} />}
            <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-sm)' }}>
              <span style={{ ...skeletonStyle, width: '60%' }} />
              {(variant === 'description' || variant === 'media') && (
                <span style={{ ...skeletonStyle, width: '40%', height: 10 }} />
              )}
            </div>
            {variant === 'media' && <span style={{ ...skeletonCircleStyle, width: 48, height: 48, borderRadius: 'var(--radius-sm)' }} />}
          </li>
        ))}
        <style>{`@keyframes sk-pulse { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`}</style>
      </ul>
    );
  };

  if (loading) return renderSkeleton();

  if (error) {
    return (
      <ul className={`sk-list sk-list--error ${className}`.trim()} style={listStyle} role="alert">
        <li style={errorContainerStyle}>
          <span>{error}</span>
          {onRetry && (
            <button
              style={retryStyle}
              onClick={onRetry}
              type="button"
            >
              Retry
            </button>
          )}
        </li>
      </ul>
    );
  }

  if (empty || items.length === 0) {
    return (
      <ul className={`sk-list sk-list--empty ${className}`.trim()} style={listStyle}>
        <li style={emptyContainerStyle}>
          {emptyMessage}
        </li>
      </ul>
    );
  }

  return (
    <ul className={`sk-list ${className}`.trim()} style={listStyle} role={listRole}>
      {items.map((item) => {
        const isSelected = selectable && selectedId === item.id;
        const mergedItemStyle: React.CSSProperties = {
          ...baseItemStyle,
          ...(isSelected ? selectedStyle : {}),
          ...(item.disabled ? disabledItemStyle : {}),
          backgroundColor: isSelected ? selectedStyle.backgroundColor : (variant === 'interactive' ? undefined : undefined),
        };

        return (
          <li
            key={item.id}
            style={mergedItemStyle}
            onClick={() => handleClick(item)}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                handleClick(item);
              }
            }}
            tabIndex={variant === 'interactive' || selectable ? 0 : undefined}
            role={selectable ? 'option' : undefined}
            aria-selected={selectable ? isSelected : undefined}
            aria-disabled={item.disabled}
            className={`sk-list__item ${isSelected ? 'sk-list__item--selected' : ''} ${item.disabled ? 'sk-list__item--disabled' : ''}`.trim()}
          >
            {variant === 'icon' && item.icon && (
              <span style={iconStyle} aria-hidden="true">{item.icon}</span>
            )}
            {variant === 'media' && item.media && (
              <span style={mediaStyle} aria-hidden="true">{item.media}</span>
            )}
            <div style={contentStyle}>
              <div style={labelStyle}>{item.label}</div>
              {(variant === 'description' || variant === 'media') && item.description && (
                <div style={descStyle}>{item.description}</div>
              )}
            </div>
            {item.meta && <span style={metaStyle}>{item.meta}</span>}
          </li>
        );
      })}
    </ul>
  );
};

List.displayName = 'List';
export default List;
