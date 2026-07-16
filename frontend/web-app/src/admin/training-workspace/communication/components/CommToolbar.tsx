import { memo } from 'react';
import type { ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { SelectOption } from '../data/communicationOptions';

export interface FilterSelectConfig {
  id: string;
  label: string;
  value: string;
  options: SelectOption[];
  onChange: (value: string) => void;
}

export interface CommToolbarProps {
  search: string;
  searchPlaceholder?: string;
  onSearchChange: (value: string) => void;
  selects?: FilterSelectConfig[];
  activeFilterCount?: number;
  onReset?: () => void;
  actions?: ReactNode;
}

const selectStyle: React.CSSProperties = {
  padding: '6px 10px',
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
  minWidth: 140,
};

const CommToolbar = memo(function CommToolbar({
  search,
  searchPlaceholder = 'Search…',
  onSearchChange,
  selects = [],
  activeFilterCount = 0,
  onReset,
  actions,
}: CommToolbarProps) {
  return (
    <div
      role="search"
      style={{
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        gap: 'var(--space-3)',
        padding: 'var(--space-3)',
        background: 'var(--color-bg-surface-raised)',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-lg)',
      }}
    >
      <div style={{ position: 'relative', flex: '1 1 220px', minWidth: 200 }}>
        <span style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-muted)', pointerEvents: 'none' }}>
          <Icon name="search" size={16} />
        </span>
        <input
          type="search"
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
          placeholder={searchPlaceholder}
          aria-label={searchPlaceholder}
          style={{
            width: '100%',
            padding: '8px 10px 8px 32px',
            borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body-sm)',
          }}
        />
      </div>

      {selects.map((cfg) => (
        <label key={cfg.id} style={{ display: 'inline-flex', flexDirection: 'column', gap: 2 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>{cfg.label}</span>
          <select
            value={cfg.value}
            onChange={(e) => cfg.onChange(e.target.value)}
            aria-label={cfg.label}
            style={selectStyle}
          >
            <option value="">All</option>
            {cfg.options.map((opt) => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </select>
        </label>
      ))}

      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', marginLeft: 'auto' }}>
        {activeFilterCount > 0 && onReset && (
          <button
            type="button"
            onClick={onReset}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 'var(--space-1)',
              padding: '6px 10px',
              borderRadius: 'var(--radius-sm)',
              border: '1px solid var(--color-border-default)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
              cursor: 'pointer',
            }}
          >
            <Icon name="x" size={14} />
            Clear ({activeFilterCount})
          </button>
        )}
        {actions}
      </div>
    </div>
  );
});

export default CommToolbar;
