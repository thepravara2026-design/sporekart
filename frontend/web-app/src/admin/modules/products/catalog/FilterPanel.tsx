import React, { memo, useCallback } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status';
import { PRODUCT_LIFECYCLE_STATES } from '../lifecycle';
import type { ProductLifecycleState, ProductType } from '../types';
import {
  ALL_BRANDS,
  ALL_CATEGORIES,
  ALL_COLLECTIONS,
  ALL_STATUSES,
  ALL_TYPES,
  TYPE_LABELS,
} from '../mock/catalogMock';
import type { CatalogFilters } from './types';

interface FilterPanelProps {
  filters: CatalogFilters;
  setFilters: React.Dispatch<React.SetStateAction<CatalogFilters>>;
  activeFilterCount: number;
  clearAllFilters: () => void;
}

function lifecycleLabelOf(state: ProductLifecycleState): string {
  return PRODUCT_LIFECYCLE_STATES.find((s) => s.state === state)?.label ?? state;
}

const chipBase: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 4,
  padding: '4px 10px',
  borderRadius: 'var(--radius-badge)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  cursor: 'pointer',
  fontFamily: 'var(--font-family-sans)',
};

const chipActive: React.CSSProperties = {
  ...chipBase,
  background: 'var(--color-primary-alpha)',
  borderColor: 'var(--color-primary)',
  color: 'var(--color-primary)',
  fontWeight: 'var(--weight-semibold)',
};

const groupTitle: React.CSSProperties = {
  margin: '0 0 var(--space-stack-xs)',
  fontSize: 'var(--text-caption)',
  textTransform: 'uppercase',
  letterSpacing: '0.06em',
  color: 'var(--color-text-tertiary)',
  fontWeight: 'var(--weight-semibold)',
};

const inputStyle: React.CSSProperties = {
  width: '100%',
  padding: '6px 8px',
  border: '1px solid var(--color-border)',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-surface)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
  fontFamily: 'var(--font-family-sans)',

};

const Chip = memo(function Chip({ label, active, onClick }: { label: string; active: boolean; onClick: () => void }) {
  return (
    <button type="button" style={active ? chipActive : chipBase} onClick={onClick} aria-pressed={active}>
      {label}
    </button>
  );
});

interface ActiveChip {
  key: string;
  label: string;
  onRemove: () => void;
}

export const FilterPanel: React.FC<FilterPanelProps> = memo(function FilterPanel({
  filters,
  setFilters,
  activeFilterCount,
  clearAllFilters,
}) {
  const toggleInArray = useCallback(
    <T,>(field: keyof CatalogFilters, value: T) => {
      setFilters((prev) => {
        const arr = prev[field] as unknown as T[];
        const exists = arr.includes(value);
        const next = exists ? arr.filter((v) => v !== value) : [...arr, value];
        return { ...prev, [field]: next };
      });
    },
    [setFilters],
  );

  const setField = useCallback(
    <K extends keyof CatalogFilters>(field: K, value: CatalogFilters[K]) => {
      setFilters((prev) => ({ ...prev, [field]: value }));
    },
    [setFilters],
  );

  const toggleBool = useCallback(
    (field: keyof CatalogFilters) => {
      setFilters((prev) => ({ ...prev, [field]: prev[field] === true ? null : true }));
    },
    [setFilters],
  );

  const activeChips: ActiveChip[] = [];
  filters.categories.forEach((c) =>
    activeChips.push({ key: `cat-${c}`, label: c, onRemove: () => toggleInArray('categories', c) }),
  );
  filters.brands.forEach((b) =>
    activeChips.push({ key: `brand-${b}`, label: b, onRemove: () => toggleInArray('brands', b) }),
  );
  filters.collections.forEach((c) =>
    activeChips.push({ key: `col-${c}`, label: c, onRemove: () => toggleInArray('collections', c) }),
  );
  filters.statuses.forEach((s) =>
    activeChips.push({ key: `st-${s}`, label: lifecycleLabelOf(s), onRemove: () => toggleInArray('statuses', s) }),
  );
  filters.types.forEach((t) =>
    activeChips.push({ key: `ty-${t}`, label: TYPE_LABELS[t] ?? t, onRemove: () => toggleInArray('types', t) }),
  );
  if (filters.priceMin != null || filters.priceMax != null) {
    activeChips.push({
      key: 'price',
      label: `Price ${filters.priceMin ?? 0}–${filters.priceMax ?? '∞'}`,
      onRemove: () => setFilters((prev) => ({ ...prev, priceMin: null, priceMax: null })),
    });
  }
  if (filters.hasImages != null) {
    activeChips.push({ key: 'img', label: filters.hasImages ? 'Has images' : 'No images', onRemove: () => setField('hasImages', null) });
  }
  if (filters.featured != null) {
    activeChips.push({ key: 'feat', label: 'Featured', onRemove: () => setField('featured', null) });
  }
  if (filters.draft) activeChips.push({ key: 'draft', label: 'Draft', onRemove: () => setField('draft', null) });
  if (filters.published) activeChips.push({ key: 'pub', label: 'Published', onRemove: () => setField('published', null) });
  if (filters.archived) activeChips.push({ key: 'arc', label: 'Archived', onRemove: () => setField('archived', null) });

  return (
    <Card variant="outlined" padding="md" as="section" aria-label="Product filters">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
          <Icon name="filter" size={16} />
          <span style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>Filters</span>
          {activeFilterCount > 0 && <StatusBadge status={String(activeFilterCount)} variant="info" />}
        </div>
        {activeFilterCount > 0 && (
          <Button variant="ghost" size="sm" onClick={clearAllFilters} leftIcon={<Icon name="x" size={13} />}>
            Clear All
          </Button>
        )}
      </div>

      {activeChips.length > 0 && (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginBottom: 'var(--space-component-gap)' }}>
          {activeChips.map((chip) => (
            <button
              key={chip.key}
              type="button"
              onClick={chip.onRemove}
              style={{ ...chipActive, cursor: 'pointer' }}
              aria-label={`Remove filter ${chip.label}`}
            >
              {chip.label}
              <Icon name="x" size={12} />
            </button>
          ))}
        </div>
      )}

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
        <div>
          <h4 style={groupTitle}>Category</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            {ALL_CATEGORIES.map((c) => (
              <Chip key={c} label={c} active={filters.categories.includes(c)} onClick={() => toggleInArray('categories', c)} />
            ))}
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Brand</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            {ALL_BRANDS.map((b) => (
              <Chip key={b} label={b} active={filters.brands.includes(b)} onClick={() => toggleInArray('brands', b)} />
            ))}
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Collection</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            {ALL_COLLECTIONS.map((c) => (
              <Chip key={c} label={c} active={filters.collections.includes(c)} onClick={() => toggleInArray('collections', c)} />
            ))}
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Status</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            {ALL_STATUSES.map((s) => (
              <Chip key={s} label={lifecycleLabelOf(s)} active={filters.statuses.includes(s)} onClick={() => toggleInArray<ProductLifecycleState>('statuses', s)} />
            ))}
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Type</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            {ALL_TYPES.map((t) => (
              <Chip key={String(t)} label={TYPE_LABELS[t] ?? String(t)} active={filters.types.includes(t)} onClick={() => toggleInArray<ProductType>('types', t)} />
            ))}
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Price Range</h4>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
            <input
              type="number"
              placeholder="Min"
              value={filters.priceMin ?? ''}
              onChange={(e) => setField('priceMin', e.target.value === '' ? null : Number(e.target.value))}
              style={inputStyle}
              aria-label="Minimum price"
            />
            <span style={{ color: 'var(--color-text-tertiary)' }}>–</span>
            <input
              type="number"
              placeholder="Max"
              value={filters.priceMax ?? ''}
              onChange={(e) => setField('priceMax', e.target.value === '' ? null : Number(e.target.value))}
              style={inputStyle}
              aria-label="Maximum price"
            />
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Created Date</h4>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
            <input type="date" value={filters.createdFrom ?? ''} onChange={(e) => setField('createdFrom', e.target.value || null)} style={inputStyle} aria-label="Created from" />
            <input type="date" value={filters.createdTo ?? ''} onChange={(e) => setField('createdTo', e.target.value || null)} style={inputStyle} aria-label="Created to" />
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Updated Date</h4>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
            <input type="date" value={filters.updatedFrom ?? ''} onChange={(e) => setField('updatedFrom', e.target.value || null)} style={inputStyle} aria-label="Updated from" />
            <input type="date" value={filters.updatedTo ?? ''} onChange={(e) => setField('updatedTo', e.target.value || null)} style={inputStyle} aria-label="Updated to" />
          </div>
        </div>

        <div>
          <h4 style={groupTitle}>Attributes</h4>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
            <Chip label="Has Images" active={filters.hasImages === true} onClick={() => setField('hasImages', filters.hasImages === true ? null : true)} />
            <Chip label="Featured" active={filters.featured === true} onClick={() => setField('featured', filters.featured === true ? null : true)} />
            <Chip label="Draft" active={filters.draft === true} onClick={() => toggleBool('draft')} />
            <Chip label="Published" active={filters.published === true} onClick={() => toggleBool('published')} />
            <Chip label="Archived" active={filters.archived === true} onClick={() => toggleBool('archived')} />
          </div>
        </div>
      </div>
    </Card>
  );
});

export default FilterPanel;
