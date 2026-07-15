import { useState, useCallback, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { SidebarItem } from './SidebarItem';
import type { NavItem, SidebarMode } from '../types';

interface EnterpriseSidebarProps {
  items: NavItem[];
  mode: SidebarMode;
  onModeChange?: (mode: SidebarMode) => void;
}

export const EnterpriseSidebar = memo(function EnterpriseSidebar({
  items,
  mode,
  onModeChange,
}: EnterpriseSidebarProps) {
  const [collapsedGroups, setCollapsedGroups] = useState<Set<string>>(new Set());
  const [searchQuery, setSearchQuery] = useState('');

  const toggleGroup = useCallback((id: string) => {
    setCollapsedGroups((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id); else next.add(id);
      return next;
    });
  }, []);

  const filteredItems = searchQuery
    ? filterItems(items, searchQuery)
    : items;

  const isMini = mode === 'mini';
  const isFloating = mode === 'floating';
  const isCollapsed = mode === 'collapsed';

  if (isMini) {
    return (
      <nav
        aria-label="Sidebar navigation"
        style={{
          width: 56,
          background: 'var(--color-surface)',
          borderRight: '1px solid var(--color-border)',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          padding: '8px 0',
          gap: 2,
          overflowY: 'auto',
        }}
      >
        {items.map((item) => (
          <SidebarItem key={item.id} item={item} mini onToggle={toggleGroup} collapsed={collapsedGroups.has(item.id)} />
        ))}
      </nav>
    );
  }

  return (
    <nav
      aria-label="Sidebar navigation"
      style={{
        width: isFloating ? 280 : isCollapsed ? 0 : 'var(--sidebar-width, 280px)',
        overflow: isCollapsed ? 'hidden' : 'visible',
        background: 'var(--color-surface)',
        borderRight: isCollapsed ? 'none' : '1px solid var(--color-border)',
        display: 'flex',
        flexDirection: 'column',
        transition: 'width 0.2s, border-color 0.2s',
        ...(isFloating ? { position: 'absolute', left: 0, top: 0, bottom: 0, zIndex: 100, boxShadow: 'var(--elevation-lg)' } : {}),
      }}
    >
      {/* Search */}
      {!isCollapsed && (
        <div style={{ padding: '12px 12px 8px' }}>
          <div style={{ position: 'relative' }}>
            <Icon name="search" size={14} style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-tertiary)' }} />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search navigation..."
              aria-label="Search navigation"
              style={{
                width: '100%',
                padding: '6px 10px 6px 32px',
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                background: 'var(--color-surface-hover)',
                color: 'var(--color-text-primary)',
                fontSize: 'var(--text-caption)',
                outline: 'none',
              }}
            />
          </div>
        </div>
      )}

      {/* Navigation Items */}
      <div style={{ flex: 1, overflowY: 'auto', padding: '4px 8px' }}>
        {filteredItems.map((item) => (
          <SidebarItem
            key={item.id}
            item={item}
            onToggle={toggleGroup}
            collapsed={collapsedGroups.has(item.id)}
          />
        ))}
      </div>

      {/* Mode switcher */}
      {!isCollapsed && (
        <div style={{ borderTop: '1px solid var(--color-border)', padding: '8px', display: 'flex', justifyContent: 'center', gap: 4 }}>
          {(['expanded', 'mini', 'collapsed'] as SidebarMode[]).map((m) => (
            <button
              key={m}
              onClick={() => onModeChange?.(m)}
              aria-label={`${m} sidebar`}
              style={{
                width: 28, height: 28,
                borderRadius: 'var(--radius-sm)',
                border: 'none',
                background: mode === m ? 'var(--color-primary-alpha)' : 'transparent',
                color: mode === m ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <Icon name={m === 'expanded' ? 'sidebar' : m === 'mini' ? 'minimize' : 'chevrons-left'} size={14} />
            </button>
          ))}
        </div>
      )}
    </nav>
  );
});

function filterItems(items: NavItem[], query: string): NavItem[] {
  const lower = query.toLowerCase();
  return items
    .map((item) => {
      if (item.label.toLowerCase().includes(lower)) return item;
      if (item.children) {
        const filtered = item.children.filter((c) => c.label.toLowerCase().includes(lower));
        if (filtered.length > 0) return { ...item, children: filtered };
      }
      return null;
    })
    .filter(Boolean) as NavItem[];
}
