import React from 'react';
import type { SeoSortOption } from '../types';

const sortOptions: { value: SeoSortOption; label: string }[] = [
  { value: 'updated', label: 'Recently Updated' }, { value: 'score', label: 'SEO Score' },
  { value: 'marketplace', label: 'Marketplace Score' }, { value: 'product', label: 'Product Name' },
  { value: 'slug', label: 'Slug' }, { value: 'title', label: 'Meta Title' },
  { value: 'status', label: 'Status' }, { value: 'alphabetical', label: 'Alphabetical' },
];
const tb: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 12, padding: 'var(--space-component-gap)', borderBottom: '1px solid var(--color-border)', flexWrap: 'wrap' };
const inp: React.CSSProperties = { flex: 1, minWidth: 200, padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)' };
const sel: React.CSSProperties = { padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer' };

export const SeoToolbar: React.FC<{ search: string; onSearchChange: (v: string) => void; sort: SeoSortOption; onSortChange: (v: SeoSortOption) => void; activeFilterCount: number; onClearFilters: () => void }> = React.memo(
  ({ search, onSearchChange, sort, onSortChange, activeFilterCount, onClearFilters }) => (
    <div style={tb}>
      <input type="text" placeholder="Search products, slug, keywords..." value={search} onChange={(e) => onSearchChange(e.target.value)} style={inp} aria-label="Search SEO" />
      <select value={sort} onChange={(e) => onSortChange(e.target.value as SeoSortOption)} style={sel} aria-label="Sort by">
        {sortOptions.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
      </select>
      {activeFilterCount > 0 && (
        <button onClick={onClearFilters} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer' }}>
          Clear <span style={{ background: 'var(--color-accent-blue)', color: '#fff', fontSize: 11, fontWeight: 600, padding: '2px 8px', borderRadius: 10, marginLeft: 4 }}>{activeFilterCount}</span>
        </button>
      )}
    </div>
  )
);
