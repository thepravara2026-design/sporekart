import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_FILTER_OPTIONS, SMART_FILTER_FUTURE } from '../constants';
import type { InventoryFilterState } from '../types';

interface FilterPanelProps {
  state: InventoryFilterState;
  onToggle: (key: keyof InventoryFilterState, value: string) => void;
  onClear: () => void;
  activeCount: number;
}

export const FilterPanel = memo(function FilterPanel({ state, onToggle, onClear, activeCount }: FilterPanelProps) {
  return (
    <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', padding: 16, display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontWeight: 600, color: 'var(--color-text-primary)' }}>
          <Icon name="filter" size={16} /> Filters
          {activeCount > 0 && <span style={{ background: 'var(--color-primary)', color: '#fff', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', padding: '1px 8px' }}>{activeCount}</span>}
        </div>
        <button onClick={onClear} disabled={activeCount === 0} style={{ background: 'none', border: 'none', cursor: activeCount === 0 ? 'not-allowed' : 'pointer', color: 'var(--color-primary)', fontSize: 'var(--text-caption)', opacity: activeCount === 0 ? 0.5 : 1 }}>
          Clear all
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 16 }}>
        {INVENTORY_FILTER_OPTIONS.map((opt) => {
          const selected = (state[opt.id as keyof InventoryFilterState] as string[]) ?? [];
          return (
            <fieldset key={opt.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', padding: 12, margin: 0 }}>
              <legend style={{ fontSize: 'var(--text-caption)', fontWeight: 600, color: 'var(--color-text-secondary)', padding: '0 6px' }}>{opt.label}</legend>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                {opt.options.map((o) => {
                  const checked = selected.includes(o.value);
                  return (
                    <label
                      key={o.value}
                      style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', cursor: opt.placeholder ? 'not-allowed' : 'pointer', opacity: opt.placeholder ? 0.6 : 1 }}
                    >
                      <input
                        type={opt.multi ? 'checkbox' : 'radio'}
                        name={opt.id}
                        checked={checked}
                        disabled={opt.placeholder}
                        onChange={() => !opt.placeholder && onToggle(opt.id as keyof InventoryFilterState, o.value)}
                        style={{ accentColor: 'var(--color-primary)' }}
                      />
                      {o.label}
                      {opt.placeholder && <Icon name="lock" size={11} />}
                    </label>
                  );
                })}
              </div>
            </fieldset>
          );
        })}
      </div>

      <div style={{ borderTop: '1px dashed var(--color-border)', paddingTop: 12 }}>
        <div style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '6px 12px', border: '1px dashed var(--color-border)', borderRadius: 'var(--radius-md)', cursor: 'not-allowed', opacity: 0.7 }}>
          <Icon name="sparkles" size={14} /> {SMART_FILTER_FUTURE.label}
        </div>
      </div>
    </div>
  );
});

