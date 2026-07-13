import React, { useCallback, useEffect, useRef, useState } from 'react';
import { SidebarNav } from './SidebarNav';
import { SidebarGroup } from './SidebarGroup';
import { SidebarToggle } from './SidebarToggle';
import type { SidebarItemData } from './SidebarItem';

export type { SidebarItemData };

export interface SidebarProps {
  items: SidebarItemData[];
  variant?: 'primary' | 'secondary' | 'mini';
  collapsed?: boolean;
  onCollapse?: (collapsed: boolean) => void;
  onNavigate?: (item: SidebarItemData) => void;
  activeId?: string;
  pinnedIds?: string[];
  onTogglePin?: (id: string) => void;
  header?: React.ReactNode;
  footer?: React.ReactNode;
  responsive?: boolean;
  open?: boolean;
  onClose?: () => void;
  className?: string;
  'aria-label'?: string;
}

const sidebarBase: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  height: '100%',
  background: 'var(--color-bg-surface-default)',
  borderRight: '1px solid var(--color-border-default)',
  transition: 'width var(--duration-normal) var(--easing-standard), transform var(--duration-normal) var(--easing-standard)',
  overflow: 'hidden',
  position: 'relative',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  padding: 'var(--space-stack-md) var(--space-page-x)',
  flexShrink: 0,
};

const scrollArea: React.CSSProperties = {
  flex: 1,
  overflowY: 'auto',
  overflowX: 'hidden',
};

const footerStyle: React.CSSProperties = {
  flexShrink: 0,
  padding: 'var(--space-stack-sm) var(--space-page-x)',
  borderTop: '1px solid var(--color-border-default)',
};

const overlayStyle: React.CSSProperties = {
  position: 'fixed',
  inset: 0,
  background: 'rgba(0, 0, 0, 0.4)',
  zIndex: 'var(--z-sidebar)',
};

const variantBackground: Record<string, string> = {
  primary: 'var(--color-bg-surface-default)',
  secondary: 'var(--color-bg-background)',
  mini: 'var(--color-bg-surface-default)',
};

export const Sidebar: React.FC<SidebarProps> = ({
  items,
  variant = 'primary',
  collapsed = false,
  onCollapse,
  onNavigate,
  activeId,
  pinnedIds = [],
  onTogglePin,
  header,
  footer,
  responsive = false,
  open = false,
  onClose,
  className = '',
  'aria-label': ariaLabel = 'Sidebar navigation',
}) => {
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const sidebarRef = useRef<HTMLDivElement>(null);

  const flatItemsRef = useRef<SidebarItemData[]>([]);
  flatItemsRef.current = flattenItems(items);

  const isMini = variant === 'mini' || collapsed;
  const width = isMini ? 'var(--layout-sidebar-icon-width)' : 'var(--layout-sidebar-width)';

  const sidebarStyle: React.CSSProperties = {
    ...sidebarBase,
    width: responsive && open ? width : width,
    background: variantBackground[variant] || variantBackground.primary,
    zIndex: 'var(--z-sidebar)',
  };

  const isOverlay = responsive && open;

  useEffect(() => {
    if (!responsive) return;
    const handleResize = () => {
      if (window.innerWidth >= 768 && onClose) {
        onClose();
      }
    };
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, [responsive, onClose]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    const flat = flatItemsRef.current;
    if (flat.length === 0) return;

    let idx = focusedIndex;
    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        idx = Math.min(idx + 1, flat.length - 1);
        setFocusedIndex(idx);
        break;
      case 'ArrowUp':
        e.preventDefault();
        idx = Math.max(idx - 1, 0);
        setFocusedIndex(idx);
        break;
      case 'Enter':
      case ' ':
        e.preventDefault();
        if (idx >= 0 && idx < flat.length) {
          onNavigate?.(flat[idx]);
        }
        break;
      case 'Home':
        e.preventDefault();
        setFocusedIndex(0);
        break;
      case 'End':
        e.preventDefault();
        setFocusedIndex(flat.length - 1);
        break;
    }
  }, [focusedIndex, onNavigate]);

  const handleToggleCollapse = useCallback(() => {
    onCollapse?.(!collapsed);
  }, [collapsed, onCollapse]);

  const handleOverlayClick = useCallback(() => {
    onClose?.();
  }, [onClose]);

  const sidebarElement = (
    <div
      ref={sidebarRef}
      role="navigation"
      aria-label={ariaLabel}
      className={className}
      style={isOverlay ? {
        ...sidebarStyle,
        position: 'fixed',
        left: 0,
        top: 0,
        bottom: 0,
        transform: open ? 'translateX(0)' : 'translateX(-100%)',
      } : sidebarStyle}
      onKeyDown={handleKeyDown}
    >
      {header && (
        <div style={headerStyle}>
          <div style={{ flex: 1, minWidth: 0 }}>{header}</div>
          {onCollapse && !responsive && (
            <SidebarToggle collapsed={collapsed} onClick={handleToggleCollapse} />
          )}
          {responsive && onClose && (
            <SidebarToggle collapsed={false} onClick={onClose} />
          )}
        </div>
      )}
      <div style={scrollArea}>
        <SidebarNav
          items={items}
          variant={isMini ? 'mini' : 'primary'}
          activeId={activeId}
          collapsed={collapsed}
          onNavigate={onNavigate}
          onTogglePin={onTogglePin}
          pinnedIds={pinnedIds}
        />
      </div>
      {footer && <div style={footerStyle}>{footer}</div>}
    </div>
  );

  if (isOverlay) {
    return (
      <>
        <div style={overlayStyle} onClick={handleOverlayClick} aria-hidden="true" />
        {sidebarElement}
      </>
    );
  }

  return sidebarElement;
};

function flattenItems(items: SidebarItemData[]): SidebarItemData[] {
  const result: SidebarItemData[] = [];
  items.forEach((item) => {
    result.push(item);
    if (item.children) {
      result.push(...flattenItems(item.children));
    }
  });
  return result;
}

Sidebar.displayName = 'Sidebar';
SidebarGroup.displayName = 'SidebarGroup';

export default Sidebar;
