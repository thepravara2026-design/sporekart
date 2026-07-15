import React from 'react';

interface AnalyticsToolbarProps {
  search: string;
  onSearchChange: (v: string) => void;
  activeFilterCount: number;
  onClearFilters: () => void;
}

export const AnalyticsToolbar: React.FC<AnalyticsToolbarProps> = React.memo(({ search, onSearchChange, activeFilterCount, onClearFilters }) => (
  <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)', padding: 'var(--space-component-gap)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }} role="toolbar" aria-label="Analytics toolbar">
    <div style={{ position: 'relative', flex: 1 }}>
      <span style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-tertiary)', fontSize: 14, pointerEvents: 'none' }} aria-hidden="true">🔍</span>
      <input
        type="text"
        value={search}
        onChange={(e) => onSearchChange(e.target.value)}
        placeholder="Search analytics..."
        aria-label="Search analytics"
        style={{ width: '100%', padding: '8px 12px 8px 36px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', boxSizing: 'border-box' }}
      />
    </div>
    {activeFilterCount > 0 && (
      <button onClick={onClearFilters} aria-label={`Clear ${activeFilterCount} active filter${activeFilterCount > 1 ? 's' : ''}`}
        style={{ display: 'flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer', whiteSpace: 'nowrap' }}>
        <span aria-hidden="true">✕</span> Clear ({activeFilterCount})
      </button>
    )}
  </div>
));

export default AnalyticsToolbar;
