import React from 'react';
import type { VariantSortOption } from '../types';

interface VariantToolbarProps {
  search: string;
  onSearchChange: (v: string) => void;
  sort: VariantSortOption;
  onSortChange: (v: VariantSortOption) => void;
  activeFilterCount: number;
  onClearFilters: () => void;
}

const sortOptions: { value: VariantSortOption; label: string }[] = [
  { value: 'updated', label: 'Recently Updated' },
  { value: 'created', label: 'Recently Created' },
  { value: 'name', label: 'Variant Name' },
  { value: 'sku', label: 'SKU' },
  { value: 'weight', label: 'Weight' },
  { value: 'package', label: 'Package' },
  { value: 'status', label: 'Status' },
  { value: 'alphabetical', label: 'Alphabetical' },
];

const toolbarStyle: React.CSSProperties = {
  display: 'flex', alignItems: 'center', gap: 12, padding: 'var(--space-component-gap)',
  borderBottom: '1px solid var(--color-border)', flexWrap: 'wrap',
};

const inputStyle: React.CSSProperties = {
  flex: 1, minWidth: 200, padding: '8px 12px', borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)',
};

const selectStyle: React.CSSProperties = {
  padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)', cursor: 'pointer',
};

export const VariantToolbar: React.FC<VariantToolbarProps> = React.memo(
  ({ search, onSearchChange, sort, onSortChange, activeFilterCount, onClearFilters }) => {
    return (
      <div style={toolbarStyle}>
        <input type="text" placeholder="Search variants, SKU, attributes..." value={search}
          onChange={(e) => onSearchChange(e.target.value)} style={inputStyle} aria-label="Search variants" />
        <select value={sort} onChange={(e) => onSortChange(e.target.value as VariantSortOption)}
          style={selectStyle} aria-label="Sort by">
          {sortOptions.map((opt) => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>
        {activeFilterCount > 0 && (
          <button onClick={onClearFilters} style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)',
            color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer',
          }}>
            Clear Filters <span style={{ background: 'var(--color-accent-blue)', color: '#fff', fontSize: 11, fontWeight: 600, padding: '2px 8px', borderRadius: 10, marginLeft: 4 }}>{activeFilterCount}</span>
          </button>
        )}
      </div>
    );
  }
);
