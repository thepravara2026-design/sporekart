import React from 'react';
import Button from '../../../../../design-system/components/core/Button';
import type { OrgSortOption } from '../types';

interface OrganizationToolbarProps {
  search: string;
  onSearch: (value: string) => void;
  sort: OrgSortOption;
  onSort: (sort: OrgSortOption) => void;
  selectedIds: Set<string>;
  onClearSelection: () => void;
  sectionLabel: string;
  totalResults: number;
}

const inputStyle: React.CSSProperties = {
  flex: 1, minWidth: 200, padding: '8px 12px', fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)',
  background: 'var(--color-bg-background)', border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',
};

const selectStyle: React.CSSProperties = {
  padding: '8px 12px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-primary)', background: 'var(--color-bg-background)',
  border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
};

const SORT_OPTIONS: { value: OrgSortOption; label: string }[] = [
  { value: 'name_asc', label: 'Name A–Z' },
  { value: 'name_desc', label: 'Name Z–A' },
  { value: 'updated', label: 'Recently Updated' },
  { value: 'created', label: 'Recently Created' },
  { value: 'product_count', label: 'Product Count' },
  { value: 'display_order', label: 'Display Order' },
];

export const OrganizationToolbar = React.memo(function OrganizationToolbar({
  search, onSearch, sort, onSort, selectedIds, onClearSelection, sectionLabel, totalResults,
}: OrganizationToolbarProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', flexWrap: 'wrap' }}>
        <input
          type="search"
          placeholder={`Search ${sectionLabel.toLowerCase()}...`}
          value={search}
          onChange={(e) => onSearch(e.target.value)}
          style={inputStyle}
          aria-label={`Search ${sectionLabel}`}
        />
        <select
          value={sort}
          onChange={(e) => onSort(e.target.value as OrgSortOption)}
          style={selectStyle}
          aria-label="Sort"
        >
          {SORT_OPTIONS.map((opt) => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 'var(--space-inline-xs)' }}>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          {totalResults} {sectionLabel.toLowerCase()}
        </span>
        {selectedIds.size > 0 && (
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-primary)', fontWeight: 'var(--weight-semibold)' }}>
              {selectedIds.size} selected
            </span>
            <Button variant="ghost" size="sm" onClick={onClearSelection}>Clear</Button>
          </div>
        )}
      </div>
    </div>
  );
});

export default OrganizationToolbar;
