import React, { useCallback } from 'react';

export interface SidebarItemData {
  id: string;
  label: string;
  icon?: React.ReactNode;
  href?: string;
  active?: boolean;
  disabled?: boolean;
  badge?: string | number;
  children?: SidebarItemData[];
  pinned?: boolean;
}

export interface SidebarItemProps {
  item: SidebarItemData;
  variant?: 'primary' | 'mini';
  active?: boolean;
  collapsed?: boolean;
  onNavigate?: (item: SidebarItemData) => void;
  onTogglePin?: (id: string) => void;
  level?: number;
}

const containerBase: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  width: '100%',
  gap: 'var(--space-inline-sm)',
  padding: 'var(--space-stack-xs) var(--space-page-x)',
  border: 'none',
  borderRadius: 'var(--radius-sm)',
  background: 'transparent',
  color: 'var(--color-text-primary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-normal)',
  lineHeight: 'var(--leading-normal)',
  cursor: 'pointer',
  transition: 'background var(--duration-fast) var(--easing-standard), color var(--duration-fast) var(--easing-standard)',
  textDecoration: 'none',
  position: 'relative',
  outline: 'none',
};

const labelStyle: React.CSSProperties = {
  flex: 1,
  textAlign: 'left',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
  whiteSpace: 'nowrap',
};

const badgeStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  minWidth: 18,
  height: 18,
  padding: '0 4px',
  borderRadius: 'var(--radius-full)',
  background: 'var(--color-bg-primary-default)',
  color: 'var(--color-text-on-primary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-bold)',
  lineHeight: 1,
  flexShrink: 0,
};

const pinBtnStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 20,
  height: 20,
  border: 'none',
  background: 'transparent',
  cursor: 'pointer',
  color: 'var(--color-text-secondary)',
  borderRadius: 'var(--radius-sm)',
  padding: 0,
  flexShrink: 0,
  opacity: 0,
  transition: 'opacity var(--duration-fast) var(--easing-standard)',
};

const pinBtnVisible: React.CSSProperties = {
  ...pinBtnStyle,
  opacity: 1,
};

export const SidebarItem: React.FC<SidebarItemProps> = ({
  item,
  variant = 'primary',
  active = false,
  collapsed = false,
  onNavigate,
  onTogglePin,
  level = 0,
}) => {
  const [hovered, setHovered] = React.useState(false);

  const handleClick = useCallback(() => {
    if (item.disabled) return;
    onNavigate?.(item);
  }, [item, onNavigate]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (item.disabled) return;
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      onNavigate?.(item);
    }
  }, [item, onNavigate]);

  const handlePinClick = useCallback((e: React.MouseEvent) => {
    e.stopPropagation();
    onTogglePin?.(item.id);
  }, [item.id, onTogglePin]);

  const showLabel = variant !== 'mini' || (variant === 'mini' && (!collapsed || hovered));

  const containerActive: React.CSSProperties = active
    ? { background: 'var(--color-bg-primary-weak)', color: 'var(--color-primary)', fontWeight: 'var(--weight-medium)' }
    : {};

  const containerHover: React.CSSProperties = !active && hovered && !item.disabled
    ? { background: 'var(--color-bg-background)' }
    : {};

  const disabledStyle: React.CSSProperties = item.disabled
    ? { opacity: 'var(--opacity-disabled)', cursor: 'not-allowed' }
    : {};

  const containerStyle: React.CSSProperties = {
    ...containerBase,
    ...(active ? containerActive : {}),
    ...containerHover,
    ...disabledStyle,
    paddingLeft: `calc(var(--space-page-x) + ${level * 16}px)`,
  };

  return (
    <div
      role="menuitem"
      tabIndex={item.disabled ? -1 : 0}
      aria-disabled={item.disabled}
      aria-current={active ? 'page' : undefined}
      style={containerStyle}
      onClick={handleClick}
      onKeyDown={handleKeyDown}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
      onFocus={() => setHovered(true)}
      onBlur={() => setHovered(false)}
    >
      {(variant !== 'mini' || !collapsed || hovered) && item.icon && (
        <span style={{ flexShrink: 0, fontSize: 18, display: 'inline-flex', alignItems: 'center' }}>
          {item.icon}
        </span>
      )}
      {showLabel && <span style={labelStyle}>{item.label}</span>}
      {showLabel && item.badge != null && (
        <span style={badgeStyle}>{item.badge}</span>
      )}
      {onTogglePin && (
        <button
          type="button"
          style={hovered ? pinBtnVisible : pinBtnStyle}
          onClick={handlePinClick}
          aria-label={item.pinned ? `Unpin ${item.label}` : `Pin ${item.label}`}
          tabIndex={-1}
        >
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d={item.pinned
                ? 'M9.5 1.5L5.5 5.5L2 6L8 12L8.5 8.5L12.5 4.5L9.5 1.5Z M1 13L5.5 8.5'
                : 'M9.5 1.5L5.5 5.5L2 6L8 12L8.5 8.5L12.5 4.5L9.5 1.5Z'}
              stroke="currentColor"
              strokeWidth="1.2"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>
      )}
      {collapsed && variant === 'mini' && hovered && (
        <div
          style={{
            position: 'fixed',
            left: 'var(--layout-sidebar-icon-width)',
            marginLeft: 4,
            padding: 'var(--space-stack-xs) var(--space-page-x)',
            background: 'var(--color-bg-surface-default)',
            borderRadius: 'var(--radius-dropdown)',
            boxShadow: 'var(--shadow-lg)',
            zIndex: 'var(--z-dropdown)',
            whiteSpace: 'nowrap',
            fontSize: 'var(--text-body)',
            color: 'var(--color-text-primary)',
            fontWeight: 'var(--weight-medium)',
            pointerEvents: 'none',
          }}
        >
          {item.label}
        </div>
      )}
    </div>
  );
};

SidebarItem.displayName = 'SidebarItem';

export default SidebarItem;
