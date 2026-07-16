import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export interface SectionTab {
  key: string;
  label: string;
  icon?: string;
  badge?: number;
}

export interface SectionTabsProps {
  tabs: SectionTab[];
  activeKey: string;
  onChange: (key: string) => void;
  ariaLabel?: string;
}

const SectionTabs = memo(function SectionTabs({ tabs, activeKey, onChange, ariaLabel = 'Sections' }: SectionTabsProps) {
  return (
    <div
      role="tablist"
      aria-label={ariaLabel}
      style={{
        display: 'flex',
        gap: 'var(--space-1)',
        overflowX: 'auto',
        borderBottom: '1px solid var(--color-border-default)',
        paddingBottom: 0,
      }}
    >
      {tabs.map((tab) => {
        const active = tab.key === activeKey;
        return (
          <button
            key={tab.key}
            type="button"
            role="tab"
            aria-selected={active}
            aria-controls={`panel-${tab.key}`}
            id={`tab-${tab.key}`}
            onClick={() => onChange(tab.key)}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 'var(--space-2)',
              padding: 'var(--space-2) var(--space-3)',
              border: 'none',
              borderBottom: `2px solid ${active ? 'var(--color-primary)' : 'transparent'}`,
              background: 'transparent',
              color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
              fontWeight: active ? 600 : 500,
              cursor: 'pointer',
              whiteSpace: 'nowrap',
            }}
          >
            {tab.icon && <Icon name={tab.icon} size={16} />}
            {tab.label}
            {typeof tab.badge === 'number' && tab.badge > 0 && (
              <span
                style={{
                  minWidth: 18,
                  height: 18,
                  padding: '0 5px',
                  borderRadius: 9,
                  background: active ? 'var(--color-primary)' : 'var(--color-bg-surface-muted)',
                  color: active ? '#fff' : 'var(--color-text-secondary)',
                  fontSize: 'var(--text-caption)',
                  fontWeight: 600,
                  display: 'inline-flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                {tab.badge > 99 ? '99+' : tab.badge}
              </span>
            )}
          </button>
        );
      })}
    </div>
  );
});

export default SectionTabs;
