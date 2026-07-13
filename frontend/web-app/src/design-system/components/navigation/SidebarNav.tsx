import React from 'react';
import { SidebarItem, type SidebarItemData } from './SidebarItem';

export interface SidebarNavProps {
  items: SidebarItemData[];
  variant?: 'primary' | 'mini';
  activeId?: string;
  collapsed?: boolean;
  onNavigate?: (item: SidebarItemData) => void;
  onTogglePin?: (id: string) => void;
  pinnedIds?: string[];
  level?: number;
}

const chevronStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 20,
  height: 20,
  flexShrink: 0,
  transition: 'transform var(--duration-fast) var(--easing-standard)',
  color: 'var(--color-text-secondary)',
};

export const SidebarNav: React.FC<SidebarNavProps> = ({
  items,
  variant = 'primary',
  activeId,
  collapsed = false,
  onNavigate,
  onTogglePin,
  pinnedIds = [],
  level = 0,
}) => {
  const [expandedIds, setExpandedIds] = React.useState<Set<string>>(() => {
    const ids = new Set<string>();
    items.forEach((item) => {
      if (item.children && item.children.length > 0) {
        if (item.active || item.children.some((c) => c.active || c.id === activeId)) {
          ids.add(item.id);
        }
      }
    });
    return ids;
  });

  const toggleExpand = (id: string) => {
    setExpandedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  const handleNavigate = (item: SidebarItemData) => {
    if (item.children && item.children.length > 0) {
      toggleExpand(item.id);
    }
    onNavigate?.(item);
  };

  const paddingLeft = level === 0 ? 0 : 16;

  return (
    <div role="menu" style={{ paddingLeft }} aria-orientation="vertical">
      {items.map((item) => {
        const isActive = activeId === item.id || item.active === true;
        const isExpanded = expandedIds.has(item.id);
        const hasChildren = item.children && item.children.length > 0;

        return (
          <div key={item.id}>
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <div style={{ flex: 1, minWidth: 0 }}>
                <SidebarItem
                  item={item}
                  variant={variant}
                  active={isActive}
                  collapsed={collapsed}
                  onNavigate={handleNavigate}
                  onTogglePin={onTogglePin}
                  level={level}
                />
              </div>
              {hasChildren && variant !== 'mini' && !collapsed && (
                <button
                  type="button"
                  onClick={(e) => { e.stopPropagation(); toggleExpand(item.id); }}
                  style={{
                    ...chevronStyle,
                    transform: isExpanded ? 'rotate(90deg)' : 'rotate(0deg)',
                    background: 'transparent',
                    border: 'none',
                    cursor: 'pointer',
                    padding: 0,
                    marginRight: 'var(--space-page-x)',
                  }}
                  aria-label={isExpanded ? `Collapse ${item.label}` : `Expand ${item.label}`}
                  aria-expanded={isExpanded}
                  tabIndex={-1}
                >
                  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4 2L8 6L4 10" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                </button>
              )}
            </div>
            {hasChildren && isExpanded && (
              <SidebarNav
                items={item.children!}
                variant={variant}
                activeId={activeId}
                collapsed={collapsed}
                onNavigate={onNavigate}
                onTogglePin={onTogglePin}
                pinnedIds={pinnedIds}
                level={level + 1}
              />
            )}
          </div>
        );
      })}
    </div>
  );
};

SidebarNav.displayName = 'SidebarNav';

export default SidebarNav;
