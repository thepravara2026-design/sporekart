import React, { memo, useMemo, useRef, useState } from 'react';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status';
import { SearchBar } from '../../../components/search/SearchBar';
import { SavedViews } from '../../../components/saved-views/SavedViews';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { FeatureGate } from '../../../feature-flags/FeatureGate';
import { CATALOG_PRODUCTS } from '../mock/catalogMock';
import { PRODUCT_SORT_OPTIONS } from './catalogColumns';
import type { BulkActionKind } from './bulkActions';
import type { CatalogFilters, CatalogViewMode, SavedCatalogView, SortOption } from './types';
import { EMPTY_FILTERS } from './types';

interface PresetDef {
  id: string;
  label: string;
  filters: Partial<CatalogFilters>;
  sort: SortOption;
  viewMode?: CatalogViewMode;
}

const PRESETS: PresetDef[] = [
  { id: 'default', label: 'Default', filters: {}, sort: 'newest' },
  { id: 'mine', label: 'My Products', filters: { featured: true }, sort: 'updated' },
  { id: 'published', label: 'Published', filters: { published: true }, sort: 'updated' },
  { id: 'drafts', label: 'Drafts', filters: { draft: true }, sort: 'newest' },
  { id: 'recent', label: 'Recently Updated', filters: {}, sort: 'updated' },
  { id: 'oos', label: 'Out of Stock', filters: {}, sort: 'status' },
  { id: 'featured', label: 'Featured', filters: { featured: true }, sort: 'newest', viewMode: 'card' },
];

const VIEW_MODES: { mode: CatalogViewMode; label: string; icon: string }[] = [
  { mode: 'table', label: 'Table', icon: 'list' },
  { mode: 'grid', label: 'Grid', icon: 'grid' },
  { mode: 'compact', label: 'Compact', icon: 'layout' },
  { mode: 'card', label: 'Card', icon: 'box' },
];

interface CatalogToolbarProps {
  viewMode: CatalogViewMode;
  setViewMode: (mode: CatalogViewMode) => void;
  search: string;
  setSearch: (value: string) => void;
  addRecentSearch: (q: string) => void;
  recentSearches: string[];
  sort: SortOption;
  setSort: (sort: SortOption) => void;
  setFilters: React.Dispatch<React.SetStateAction<CatalogFilters>>;
  savedViews: SavedCatalogView[];
  saveView: (name: string) => void;
  loadView: (id: string) => void;
  deleteView: (id: string) => void;
  selectedCount: number;
  clearSelection: () => void;
  onBulkAction: (kind: BulkActionKind) => void;
  focusSearch?: boolean;
}

const chipBase: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 5,
  padding: '6px 10px',
  borderRadius: 'var(--radius-sm)',
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

const selectStyle: React.CSSProperties = {
  padding: '7px 8px',
  border: '1px solid var(--color-border)',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-surface)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-caption)',
  fontFamily: 'var(--font-family-sans)',
  cursor: 'pointer',

};

export const CatalogToolbar: React.FC<CatalogToolbarProps> = memo(function CatalogToolbar({
  viewMode,
  setViewMode,
  search,
  setSearch,
  addRecentSearch,
  recentSearches,
  sort,
  setSort,
  setFilters,
  savedViews,
  saveView,
  loadView,
  deleteView,
  selectedCount,
  clearSelection,
  onBulkAction,
  focusSearch,
}) {
  const [showSuggest, setShowSuggest] = useState(false);
  const blurTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  const suggestions = useMemo(() => {
    if (!search.trim()) return [];
    const needle = search.toLowerCase();
    const seen = new Set<string>();
    const result: string[] = [];
    for (const p of CATALOG_PRODUCTS) {
      if (p.name.toLowerCase().includes(needle) && !seen.has(p.name)) {
        seen.add(p.name);
        result.push(p.name);
        if (result.length >= 6) break;
      }
    }
    return result;
  }, [search]);

  const applyPreset = (preset: PresetDef) => {
    setFilters({ ...EMPTY_FILTERS, ...preset.filters });
    setSort(preset.sort);
    if (preset.viewMode) setViewMode(preset.viewMode);
  };

  const applySuggestion = (value: string) => {
    setSearch(value);
    addRecentSearch(value);
    setShowSuggest(false);
  };

  const savedViewsForComponent = savedViews.map((v) => ({
    id: v.id,
    name: v.name,
    queryState: v.queryState,
    pinned: v.pinned,
    recent: v.recent,
    default: v.default,
  }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)', marginBottom: 'var(--space-section-gap)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', gap: 4 }} role="group" aria-label="View mode">
          {VIEW_MODES.map((v) => (
            <button
              key={v.mode}
              type="button"
              style={viewMode === v.mode ? chipActive : chipBase}
              onClick={() => setViewMode(v.mode)}
              aria-pressed={viewMode === v.mode}
            >
              <Icon name={v.icon} size={14} />
              {v.label}
            </button>
          ))}
        </div>

        <div style={{ position: 'relative', flex: 1, minWidth: 220, maxWidth: 420 }}>
          <SearchBar
            value={search}
            onChange={(v) => {
              setSearch(v);
              if (v.trim()) addRecentSearch(v);
            }}
            placeholder="Search products, SKU, tags..."
            autoFocus={focusSearch}
            onFocus={() => {
              if (blurTimer.current) clearTimeout(blurTimer.current);
              setShowSuggest(true);
            }}
            onBlur={() => {
              blurTimer.current = setTimeout(() => setShowSuggest(false), 150);
            }}
          />
          {showSuggest && (recentSearches.length > 0 || suggestions.length > 0) && (
            <div
              style={{
                position: 'absolute',
                top: '100%',
                left: 0,
                right: 0,
                marginTop: 4,
                zIndex: 40,
                background: 'var(--color-surface)',
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                boxShadow: 'var(--shadow-3)',
                padding: 6,
                maxHeight: 300,
                overflowY: 'auto',
              }}
            >
              {recentSearches.length > 0 && (
                <>
                  <div style={{ padding: '4px 8px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Recent</div>
                  {recentSearches.map((r) => (
                    <button
                      key={`recent-${r}`}
                      type="button"
                      onMouseDown={(e) => { e.preventDefault(); applySuggestion(r); }}
                      style={{ display: 'flex', alignItems: 'center', gap: 6, width: '100%', textAlign: 'left', padding: '6px 8px', background: 'transparent', border: 'none', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', borderRadius: 'var(--radius-sm)' }}
                    >
                      <Icon name="clock" size={13} />
                      {r}
                    </button>
                  ))}
                </>
              )}
              {suggestions.length > 0 && (
                <>
                  <div style={{ padding: '4px 8px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Suggestions</div>
                  {suggestions.map((s) => (
                    <button
                      key={`sug-${s}`}
                      type="button"
                      onMouseDown={(e) => { e.preventDefault(); applySuggestion(s); }}
                      style={{ display: 'flex', alignItems: 'center', gap: 6, width: '100%', textAlign: 'left', padding: '6px 8px', background: 'transparent', border: 'none', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', borderRadius: 'var(--radius-sm)' }}
                    >
                      <Icon name="search" size={13} />
                      {s}
                    </button>
                  ))}
                </>
              )}
            </div>
          )}
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', marginLeft: 'auto' }}>
          <select style={selectStyle} value={sort} onChange={(e) => setSort(e.target.value as SortOption)} aria-label="Sort products">
            {PRODUCT_SORT_OPTIONS.map((opt) => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </select>
          <SavedViews
            views={savedViewsForComponent}
            onSave={saveView}
            onLoad={(view) => loadView(view.id)}
            onDelete={deleteView}
          />
        </div>
      </div>

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
        {PRESETS.map((preset) => (
          <button key={preset.id} type="button" style={chipBase} onClick={() => applyPreset(preset)}>
            <Icon name="bookmark" size={12} />
            {preset.label}
          </button>
        ))}
      </div>

      {selectedCount > 0 && (
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-xs)',
            flexWrap: 'wrap',
            padding: '10px 12px',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-primary-alpha)',
            border: '1px solid var(--color-primary)',
          }}
        >
          <StatusBadge status={`${selectedCount} selected`} variant="info" />
          <div style={{ flex: 1 }} />
          <PermissionGate action="bulk_actions" resource="products" fallback={<span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>No bulk permissions</span>}>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="archive" size={14} />} onClick={() => onBulkAction('archive')}>Archive</Button>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="trash" size={14} />} onClick={() => onBulkAction('delete')}>Delete</Button>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="upload" size={14} />} onClick={() => onBulkAction('publish')}>Publish</Button>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="download" size={14} />} onClick={() => onBulkAction('unpublish')}>Unpublish</Button>
            <FeatureGate flag="bulk-export">
              <Button variant="secondary" size="sm" leftIcon={<Icon name="download" size={14} />} onClick={() => onBulkAction('export')}>Export</Button>
            </FeatureGate>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="tag" size={14} />} onClick={() => onBulkAction('assign_category')}>Assign Category</Button>
            <Button variant="secondary" size="sm" leftIcon={<Icon name="shield" size={14} />} onClick={() => onBulkAction('assign_brand')}>Assign Brand</Button>
          </PermissionGate>
          <Button variant="ghost" size="sm" leftIcon={<Icon name="x" size={14} />} onClick={clearSelection}>Clear</Button>
        </div>
      )}
    </div>
  );
});

export default CatalogToolbar;
