import { memo, useCallback, useState } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import {
  CATALOG_CATEGORY_OPTIONS,
  CATALOG_DELIVERY_OPTIONS,
  CATALOG_DURATION_BANDS,
  CATALOG_LANGUAGE_OPTIONS,
  CATALOG_LEVEL_OPTIONS,
  CATALOG_AVAILABILITY_OPTIONS,
  CATALOG_PRICE_BANDS,
  CATALOG_SKILL_OPTIONS,
  CATALOG_SORT_OPTIONS,
  CATALOG_STATUS_OPTIONS,
  CATALOG_TRAINING_TYPE_OPTIONS,
  type CatalogFilters,
  type CatalogSortField,
  type CatalogViewMode,
} from '../data/catalogOptions';

export interface CatalogToolbarProps {
  filters: CatalogFilters;
  onFilterChange: (key: keyof CatalogFilters, value: string) => void;
  onClearFilters: () => void;
  activeFilterCount: number;
  sort: CatalogSortField;
  onSortChange: (field: CatalogSortField) => void;
  viewMode: CatalogViewMode;
  onViewModeChange: (mode: CatalogViewMode) => void;
  totalItems: number;
}

const VIEW_OPTIONS: { mode: CatalogViewMode; icon: string; label: string }[] = [
  { mode: 'grid', icon: 'grid', label: 'Grid view' },
  { mode: 'list', icon: 'list', label: 'List view' },
  { mode: 'compact', icon: 'menu', label: 'Compact view' },
  { mode: 'featured', icon: 'star', label: 'Featured view' },
  { mode: 'carousel', icon: 'play-circle', label: 'Carousel view' },
];

function FilterSelect({
  label,
  value,
  options,
  onChange,
  ariaLabel,
}: {
  label: string;
  value: string;
  options: { value: string; label: string }[];
  onChange: (value: string) => void;
  ariaLabel: string;
}) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{label}</label>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        aria-label={ariaLabel}
        style={{
          padding: '6px 8px',
          fontSize: 'var(--text-body-sm)',
          borderRadius: 'var(--radius-input)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-background)',
          color: 'var(--color-text-primary)',
          minWidth: 150,
        }}
      >
        {options.map((o) => (
          <option key={o.value} value={o.value}>{o.label}</option>
        ))}
      </select>
    </div>
  );
}

export const CatalogToolbar = memo(function CatalogToolbar({
  filters,
  onFilterChange,
  onClearFilters,
  activeFilterCount,
  sort,
  onSortChange,
  viewMode,
  onViewModeChange,
  totalItems,
}: CatalogToolbarProps) {
  const [searchFocused, setSearchFocused] = useState(false);
  const [filterOpen, setFilterOpen] = useState(false);

  const handleSearch = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => onFilterChange('search', e.target.value),
    [onFilterChange],
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)', flexWrap: 'wrap' }}>
        <div style={{ flex: 1, minWidth: 220, maxWidth: 460, position: 'relative', display: 'flex', alignItems: 'center' }}>
          <span style={{ position: 'absolute', left: 10, color: 'var(--color-text-tertiary)', pointerEvents: 'none' }}>
            <Icon name="search" size={16} color="currentColor" />
          </span>
          <input
            type="search"
            value={filters.search}
            onChange={handleSearch}
            onFocus={() => setSearchFocused(true)}
            onBlur={() => setSearchFocused(false)}
            placeholder="Search courses, skills, trainers, keywords…"
            aria-label="Search the course catalog"
            role="searchbox"
            style={{
              width: '100%',
              padding: '10px 12px 10px 34px',
              fontSize: 'var(--text-body-sm)',
              background: 'var(--color-bg-background)',
              border: `1px solid ${searchFocused ? 'var(--color-border-focus)' : 'var(--color-border-default)'}`,
              borderRadius: 'var(--radius-input)',
              color: 'var(--color-text-primary)',
              outline: 'none',
            }}
          />
        </div>

        <button
          type="button"
          onClick={() => setFilterOpen((v) => !v)}
          aria-expanded={filterOpen}
          aria-label="Toggle course filters"
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 6,
            padding: '8px 12px', borderRadius: 'var(--radius-input)',
            background: activeFilterCount > 0 ? 'var(--color-bg-accent-subtle, #eef2ff)' : 'var(--color-bg-background)',
            border: '1px solid var(--color-border-default)',
            cursor: 'pointer',
            color: activeFilterCount > 0 ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-text-secondary)',
            fontSize: 'var(--text-body-sm)',
            fontWeight: 600,
          }}
        >
          <Icon name="filter" size={16} color="currentColor" />
          Filters
          {activeFilterCount > 0 && (
            <span style={{ minWidth: 18, height: 18, borderRadius: 'var(--radius-full)', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontSize: 11, display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>{activeFilterCount}</span>
          )}
        </button>

        <div style={{ display: 'flex', gap: 2, background: 'var(--color-bg-background)', borderRadius: 'var(--radius-input)', border: '1px solid var(--color-border-default)', padding: 2 }}>
          {VIEW_OPTIONS.map((opt) => (
            <button
              key={opt.mode}
              type="button"
              onClick={() => onViewModeChange(opt.mode)}
              aria-label={opt.label}
              aria-pressed={viewMode === opt.mode}
              style={{
                display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                width: 34, height: 32, borderRadius: 'var(--radius-xs)',
                background: viewMode === opt.mode ? 'var(--color-bg-surface-default)' : 'transparent',
                border: 'none', cursor: 'pointer',
                color: viewMode === opt.mode ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-text-tertiary)',
              }}
            >
              <Icon name={opt.icon} size={16} color="currentColor" />
            </button>
          ))}
        </div>

        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', whiteSpace: 'nowrap' }}>
          {totalItems} course{totalItems !== 1 ? 's' : ''}
        </span>
      </div>

      {filterOpen && (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-inline-md)', padding: 'var(--space-4)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', alignItems: 'flex-end' }}>
          <FilterSelect label="Category" value={filters.category} options={CATALOG_CATEGORY_OPTIONS} ariaLabel="Filter by category" onChange={(v) => onFilterChange('category', v)} />
          <FilterSelect label="Level / Type" value={filters.level} options={CATALOG_LEVEL_OPTIONS} ariaLabel="Filter by level" onChange={(v) => onFilterChange('level', v)} />
          <FilterSelect label="Delivery Mode" value={filters.deliveryMode} options={CATALOG_DELIVERY_OPTIONS} ariaLabel="Filter by delivery mode" onChange={(v) => onFilterChange('deliveryMode', v)} />
          <FilterSelect label="Language" value={filters.language} options={CATALOG_LANGUAGE_OPTIONS} ariaLabel="Filter by language" onChange={(v) => onFilterChange('language', v)} />
          <FilterSelect label="Training Type" value={filters.trainingType} options={CATALOG_TRAINING_TYPE_OPTIONS} ariaLabel="Filter by training type" onChange={(v) => onFilterChange('trainingType', v)} />
          <FilterSelect label="Skill Level" value={filters.skillLevel} options={CATALOG_SKILL_OPTIONS} ariaLabel="Filter by skill level" onChange={(v) => onFilterChange('skillLevel', v)} />
          <FilterSelect label="Price" value={filters.priceBand} options={CATALOG_PRICE_BANDS} ariaLabel="Filter by price" onChange={(v) => onFilterChange('priceBand', v)} />
          <FilterSelect label="Duration" value={filters.durationBand} options={CATALOG_DURATION_BANDS} ariaLabel="Filter by duration" onChange={(v) => onFilterChange('durationBand', v)} />
          <FilterSelect label="Availability" value={filters.availability} options={CATALOG_AVAILABILITY_OPTIONS} ariaLabel="Filter by availability" onChange={(v) => onFilterChange('availability', v)} />
          <FilterSelect label="Status" value={filters.status} options={CATALOG_STATUS_OPTIONS} ariaLabel="Filter by status" onChange={(v) => onFilterChange('status', v)} />
          {activeFilterCount > 0 && (
            <button
              type="button"
              onClick={onClearFilters}
              aria-label="Clear all filters"
              style={{ display: 'inline-flex', alignItems: 'center', gap: 4, padding: '6px 10px', borderRadius: 'var(--radius-input)', background: 'transparent', border: '1px solid var(--color-border-default)', cursor: 'pointer', color: 'var(--color-danger)', fontSize: 'var(--text-body-sm)' }}
            >
              <Icon name="x" size={14} color="currentColor" /> Clear
            </button>
          )}
        </div>
      )}

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 'var(--space-inline-sm)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Sort by</span>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {CATALOG_SORT_OPTIONS.map((o) => (
              <button
                key={o.value}
                type="button"
                onClick={() => onSortChange(o.value)}
                aria-pressed={sort === o.value}
                aria-label={`Sort by ${o.label}`}
                style={{
                  padding: '4px 10px', borderRadius: 'var(--radius-pill, 999px)',
                  background: sort === o.value ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-bg-background)',
                  border: `1px solid ${sort === o.value ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-border-default)'}`,
                  cursor: 'pointer',
                  color: sort === o.value ? '#fff' : 'var(--color-text-secondary)',
                  fontSize: 'var(--text-body-sm)',
                  fontWeight: 600,
                }}
              >
                {o.label}
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
});

export default CatalogToolbar;
