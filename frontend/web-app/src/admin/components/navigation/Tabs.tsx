import React, { useCallback } from 'react';

export interface Tab {
  id: string;
  label: string;
  icon?: React.ReactNode;
  disabled?: boolean;
  badge?: string | number;
}

export interface TabsProps {
  tabs: Tab[];
  activeId?: string;
  onChange?: (id: string) => void;
  variant?: 'underline' | 'pills' | 'buttons';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  style?: React.CSSProperties;
}

const tabListStyle: React.CSSProperties = {
  display: 'flex',
  gap: 0,
  listStyle: 'none',
  margin: 0,
  padding: 0,
  borderBottom: '1px solid var(--color-border-default)',
};

const sizeMap: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-caption)', padding: '6px 12px', gap: 4 },
  md: { fontSize: 'var(--text-body)', padding: '8px 16px', gap: 6 },
  lg: { fontSize: 'var(--text-body-lg)', padding: '10px 20px', gap: 8 },
};

const badgeStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  minWidth: 18,
  height: 18,
  padding: '0 5px',
  borderRadius: 'var(--radius-full)',
  background: 'var(--color-bg-primary-default)',
  color: 'var(--color-text-on-primary)',
  fontSize: 11,
  fontWeight: 'var(--weight-bold)',
  lineHeight: 1,
};

export const Tabs: React.FC<TabsProps> = ({
  tabs,
  activeId,
  onChange,
  variant = 'underline',
  size = 'md',
  className = '',
  style,
}) => {
  const handleClick = useCallback((tab: Tab) => {
    if (tab.disabled) return;
    onChange?.(tab.id);
  }, [onChange]);

  const sizes = sizeMap[size];

  return (
    <div className={className} style={{ ...style }} role="tablist" aria-label="Tabs">
      <div style={tabListStyle}>
        {tabs.map((tab) => {
          const isActive = tab.id === activeId;
          let activeStyle: React.CSSProperties = {};
          if (variant === 'underline') {
            activeStyle = isActive
              ? { color: 'var(--color-primary)', borderBottom: '2px solid var(--color-primary)', marginBottom: -1 }
              : { color: 'var(--color-text-secondary)', borderBottom: '2px solid transparent', marginBottom: -1 };
          } else if (variant === 'pills') {
            activeStyle = isActive
              ? { background: 'var(--color-bg-primary-weak)', color: 'var(--color-primary)', borderRadius: 'var(--radius-md)' }
              : { color: 'var(--color-text-secondary)', borderRadius: 'var(--radius-md)' };
          } else if (variant === 'buttons') {
            activeStyle = isActive
              ? { background: 'var(--color-primary)', color: 'var(--color-text-on-primary)', borderRadius: 'var(--radius-sm)' }
              : { color: 'var(--color-text-secondary)', borderRadius: 'var(--radius-sm)' };
          }

          return (
            <button
              key={tab.id}
              role="tab"
              aria-selected={isActive}
              aria-disabled={tab.disabled}
              tabIndex={isActive ? 0 : -1}
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: sizes.gap,
                ...sizes,
                border: 'none',
                background: 'transparent',
                cursor: tab.disabled ? 'not-allowed' : 'pointer',
                opacity: tab.disabled ? 'var(--opacity-disabled)' : undefined,
                fontFamily: 'var(--font-family-sans)',
                fontWeight: isActive ? 'var(--weight-medium)' : 'var(--weight-normal)',
                whiteSpace: 'nowrap',
                transition: 'color var(--duration-fast) var(--easing-standard), background var(--duration-fast) var(--easing-standard), border-color var(--duration-fast) var(--easing-standard)',
                ...activeStyle,
              }}
              onClick={() => handleClick(tab)}
            >
              {tab.icon && <span style={{ flexShrink: 0, display: 'inline-flex' }}>{tab.icon}</span>}
              <span>{tab.label}</span>
              {tab.badge != null && <span style={badgeStyle}>{tab.badge}</span>}
            </button>
          );
        })}
      </div>
    </div>
  );
};

Tabs.displayName = 'Tabs';
export default Tabs;
