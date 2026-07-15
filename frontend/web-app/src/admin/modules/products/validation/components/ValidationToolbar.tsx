import React, { useCallback } from 'react';
import type { ValidationSortOption } from '../types';
const SORT_OPTIONS: ValidationSortOption[] = ['score', 'risk', 'compliance', 'marketplace', 'certification', 'product', 'updated', 'alphabetical'];

interface ValidationToolbarProps {
  search: string;
  onSearchChange: (v: string) => void;
  sort: ValidationSortOption;
  onSortChange: (v: ValidationSortOption) => void;
  activeFilterCount: number;
  onClearFilters: () => void;
}

const sortOptions = [
  { value: 'score', label: 'Score' },
  { value: 'risk', label: 'Risk' },
  { value: 'compliance', label: 'Compliance' },
  { value: 'marketplace', label: 'Marketplace' },
  { value: 'certification', label: 'Certification' },
  { value: 'product', label: 'Product Name' },
  { value: 'updated', label: 'Recently Updated' },
];

export const ValidationToolbar = React.memo(function ValidationToolbar({ search, onSearchChange, sort, onSortChange, activeFilterCount, onClearFilters }: ValidationToolbarProps) {
  const handleSearch = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    onSearchChange(e.target.value);
  }, [onSearchChange]);

  const handleSort = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
    const val = e.target.value as ValidationSortOption;
    if (SORT_OPTIONS.includes(val)) onSortChange(val);
  }, [onSortChange]);

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12, padding: 'var(--space-component-gap)', flexWrap: 'wrap' }} role="toolbar" aria-label="Validation toolbar">
      <div style={{ flex: 1, minWidth: 200, position: 'relative' }}>
        <span style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', pointerEvents: 'none' }} aria-hidden="true">
          🔍
        </span>
        <input
          type="search"
          value={search}
          onChange={handleSearch}
          placeholder="Search products..."
          aria-label="Search products"
          style={{
            width: '100%', padding: '8px 12px 8px 32px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)',
            background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)',
            boxSizing: 'border-box',
          }}
        />
      </div>

      <select
        value={sort}
        onChange={handleSort}
        aria-label="Sort by"
        style={{
          padding: '8px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)',
          background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)',
          cursor: 'pointer',
        }}
      >
        {sortOptions.map((opt) => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>

      {activeFilterCount > 0 && (
        <button
          onClick={onClearFilters}
          aria-label={`Clear ${activeFilterCount} active filter${activeFilterCount !== 1 ? 's' : ''}`}
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)',
            border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)',
            color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer',
          }}
        >
          <span style={{ fontWeight: 700, color: 'var(--color-accent-blue)' }}>{activeFilterCount}</span>
          Clear filters
          <span style={{ fontSize: 14 }} aria-hidden="true">✕</span>
        </button>
      )}
    </div>
  );
});
