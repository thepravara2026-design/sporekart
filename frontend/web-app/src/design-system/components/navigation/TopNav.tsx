import React, { useCallback, useState, useRef, useEffect } from 'react';
import { NavItem, type TopNavItem } from './NavItem';

export type { TopNavItem };

export interface TopNavProps {
  items: TopNavItem[];
  activeId?: string;
  onNavigate?: (item: TopNavItem) => void;
  variant?: 'primary' | 'secondary' | 'underline';
  className?: string;
  'aria-label'?: string;
}

const navStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  flexWrap: 'wrap',
  padding: 'var(--space-stack-xs) 0',
  listStyle: 'none',
  margin: 0,
};

const dropdownContainer: React.CSSProperties = {
  position: 'absolute',
  top: '100%',
  left: 0,
  minWidth: 200,
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-dropdown)',
  boxShadow: 'var(--shadow-lg)',
  zIndex: 'var(--z-dropdown)',
  padding: 'var(--space-stack-xs) 0',
  animation: 'topnav-fadeIn var(--duration-fast) var(--easing-standard)',
};

const dropdownItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-sm)',
  width: '100%',
  padding: 'var(--space-stack-xs) var(--space-page-x)',
  border: 'none',
  background: 'transparent',
  color: 'var(--color-text-primary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-normal)',
  cursor: 'pointer',
  textAlign: 'left',
  transition: 'background var(--duration-fast) var(--easing-standard)',
};

export const TopNav: React.FC<TopNavProps> = ({
  items,
  activeId,
  onNavigate,
  variant = 'primary',
  className = '',
  'aria-label': ariaLabel = 'Top navigation',
}) => {
  const [openDropdownId, setOpenDropdownId] = useState<string | null>(null);
  const navRef = useRef<HTMLDivElement>(null);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const handleItemClick = useCallback((item: TopNavItem) => {
    if (item.children && item.children.length > 0) {
      setOpenDropdownId((prev) => (prev === item.id ? null : item.id));
    } else {
      setOpenDropdownId(null);
    }
    onNavigate?.(item);
  }, [onNavigate]);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(e.target as Node) &&
        navRef.current &&
        !navRef.current.contains(e.target as Node)
      ) {
        setOpenDropdownId(null);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    switch (e.key) {
      case 'Escape':
        setOpenDropdownId(null);
        break;
    }
  }, []);

  return (
    <nav
      ref={navRef}
      role="navigation"
      aria-label={ariaLabel}
      className={className}
      style={navStyle}
      onKeyDown={handleKeyDown}
    >
      {items.map((item) => {
        const isActive = activeId === item.id || item.active === true;
        const isOpen = openDropdownId === item.id;
        const hasChildren = item.children && item.children.length > 0;

        return (
          <div
            key={item.id}
            style={{ position: 'relative' }}
            onMouseEnter={() => {
              if (hasChildren) setOpenDropdownId(item.id);
            }}
            onMouseLeave={() => {
              if (hasChildren) setOpenDropdownId(null);
            }}
          >
            <NavItem
              item={item}
              active={isActive}
              onNavigate={handleItemClick}
              variant={variant}
            />
            {hasChildren && isOpen && (
              <div ref={dropdownRef} style={dropdownContainer} role="menu">
                {item.children!.map((child) => (
                  <button
                    key={child.id}
                    type="button"
                    role="menuitem"
                    tabIndex={0}
                    style={dropdownItemStyle}
                    onClick={() => {
                      onNavigate?.(child);
                      setOpenDropdownId(null);
                    }}
                    onMouseEnter={(e) => {
                      (e.currentTarget as HTMLElement).style.background = 'var(--color-bg-background)';
                    }}
                    onMouseLeave={(e) => {
                      (e.currentTarget as HTMLElement).style.background = 'transparent';
                    }}
                  >
                    {child.icon && <span style={{ flexShrink: 0, fontSize: 16 }}>{child.icon}</span>}
                    <span>{child.label}</span>
                  </button>
                ))}
              </div>
            )}
          </div>
        );
      })}
      <style>{`@keyframes topnav-fadeIn { from { opacity: 0; transform: translateY(-4px); } to { opacity: 1; transform: translateY(0); } }`}</style>
    </nav>
  );
};

TopNav.displayName = 'TopNav';

export default TopNav;
