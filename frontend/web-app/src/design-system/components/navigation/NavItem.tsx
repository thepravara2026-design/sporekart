import React, { useCallback } from 'react';

export interface TopNavItem {
  id: string;
  label: string;
  href?: string;
  active?: boolean;
  disabled?: boolean;
  icon?: React.ReactNode;
  children?: TopNavItem[];
}

export interface NavItemProps {
  item: TopNavItem;
  active?: boolean;
  onNavigate?: (item: TopNavItem) => void;
  variant?: 'primary' | 'secondary' | 'underline';
}

const btnBase: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  padding: 'var(--space-stack-xs) var(--space-inline-sm)',
  border: 'none',
  borderRadius: 'var(--radius-sm)',
  background: 'transparent',
  color: 'var(--color-text-secondary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-normal)',
  lineHeight: 'var(--leading-normal)',
  cursor: 'pointer',
  whiteSpace: 'nowrap',
  textDecoration: 'none',
  transition: 'color var(--duration-fast) var(--easing-standard), background var(--duration-fast) var(--easing-standard)',
  outline: 'none',
  position: 'relative',
};

const underlineActive: React.CSSProperties = {
  color: 'var(--color-primary)',
};

const underlineIndicator: React.CSSProperties = {
  position: 'absolute',
  bottom: 0,
  left: 'var(--space-inline-sm)',
  right: 'var(--space-inline-sm)',
  height: 2,
  background: 'var(--color-primary)',
  borderRadius: 'var(--radius-full)',
};

export const NavItem: React.FC<NavItemProps> = ({
  item,
  active = false,
  onNavigate,
  variant = 'primary',
}) => {
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

  const activeStyle: React.CSSProperties = (() => {
    if (!active) return {};
    switch (variant) {
      case 'underline':
        return underlineActive;
      case 'primary':
        return { color: 'var(--color-primary)', fontWeight: 'var(--weight-medium)' };
      case 'secondary':
        return { color: 'var(--color-primary)', background: 'var(--color-bg-primary-weak)' };
      default:
        return { color: 'var(--color-primary)', fontWeight: 'var(--weight-medium)' };
    }
  })();

  const disabledStyle: React.CSSProperties = item.disabled
    ? { opacity: 'var(--opacity-disabled)', cursor: 'not-allowed', pointerEvents: 'none' }
    : {};

  return (
    <button
      type="button"
      role="menuitem"
      tabIndex={item.disabled ? -1 : 0}
      aria-disabled={item.disabled}
      aria-current={active ? 'page' : undefined}
      style={{ ...btnBase, ...activeStyle, ...disabledStyle }}
      onClick={handleClick}
      onKeyDown={handleKeyDown}
    >
      {item.icon && <span style={{ flexShrink: 0, fontSize: 16, display: 'inline-flex' }}>{item.icon}</span>}
      <span>{item.label}</span>
      {variant === 'underline' && active && <span style={underlineIndicator} />}
    </button>
  );
};

NavItem.displayName = 'NavItem';

export default NavItem;
