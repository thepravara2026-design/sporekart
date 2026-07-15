import React from 'react';
import type { PricingSortOption } from '../types';

interface PricingToolbarProps {
  search: string;
  onSearchChange: (value: string) => void;
  sort: PricingSortOption;
  onSortChange: (value: PricingSortOption) => void;
  activeFilterCount: number;
  onClearFilters: () => void;
}

const sortOptions: { value: PricingSortOption; label: string }[] = [
  { value: 'updated', label: 'Recently Updated' },
  { value: 'mrp', label: 'MRP' },
  { value: 'selling_price', label: 'Selling Price' },
  { value: 'wholesale_price', label: 'Wholesale' },
  { value: 'discount', label: 'Discount' },
  { value: 'gst', label: 'GST' },
  { value: 'category', label: 'Category' },
  { value: 'brand', label: 'Brand' },
  { value: 'alphabetical', label: 'Alphabetical' },
];

const toolbarStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 12,
  padding: 'var(--space-component-gap)',
  borderBottom: '1px solid var(--color-border)',
  flexWrap: 'wrap',
};

const inputStyle: React.CSSProperties = {
  flex: 1,
  minWidth: 200,
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
};

const selectStyle: React.CSSProperties = {
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
  cursor: 'pointer',
};

const badgeStyle: React.CSSProperties = {
  background: 'var(--color-accent-blue)',
  color: '#fff',
  fontSize: 11,
  fontWeight: 600,
  padding: '2px 8px',
  borderRadius: 10,
};

export const PricingToolbar: React.FC<PricingToolbarProps> = React.memo(
  ({ search, onSearchChange, sort, onSortChange, activeFilterCount, onClearFilters }) => {
    return (
      <div style={toolbarStyle}>
        <input
          type="text"
          placeholder="Search products, SKU, category, brand..."
          value={search}
          onChange={(e) => onSearchChange(e.target.value)}
          style={inputStyle}
          aria-label="Search pricing"
        />
        <select
          value={sort}
          onChange={(e) => onSortChange(e.target.value as PricingSortOption)}
          style={selectStyle}
          aria-label="Sort by"
        >
          {sortOptions.map((opt) => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>
        {activeFilterCount > 0 && (
          <button
            onClick={onClearFilters}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 6,
              padding: '6px 12px',
              borderRadius: 'var(--radius-sm)',
              border: '1px solid var(--color-border)',
              background: 'var(--color-bg-surface-raised)',
              color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
              cursor: 'pointer',
            }}
            aria-label={`Clear ${activeFilterCount} active filters`}
          >
            Clear Filters
            <span style={badgeStyle}>{activeFilterCount}</span>
          </button>
        )}
      </div>
    );
  }
);
