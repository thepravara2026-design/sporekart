import { memo, useCallback, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { ViewMode, CourseFilters } from '../state/courseState';

interface CourseExplorerToolbarProps {
  viewMode: ViewMode;
  onViewModeChange: (mode: ViewMode) => void;
  filters: CourseFilters;
  onFilterChange: (key: keyof CourseFilters, value: string) => void;
  onClearFilters: () => void;
  activeFilterCount: number;
  totalItems: number;
  pinnedOnly: boolean;
  onPinnedOnlyChange: (v: boolean) => void;
  favoritesOnly: boolean;
  onFavoritesOnlyChange: (v: boolean) => void;
  sortBy: string;
  sortOrder: string;
  onSort: (field: 'updatedAt' | 'name' | 'createdAt' | 'rating') => void;
  onClearSelection: () => void;
  selectionCount: number;
}

const VIEW_OPTIONS: { mode: ViewMode; icon: string; label: string }[] = [
  { mode: 'grid', icon: 'grid', label: 'Grid view' },
  { mode: 'list', icon: 'list', label: 'List view' },
  { mode: 'table', icon: 'menu', label: 'Table view' },
];

const STATUS_OPTIONS = [
  { value: '', label: 'All Statuses' },
  { value: 'draft', label: 'Draft' },
  { value: 'pending-review', label: 'Pending Review' },
  { value: 'approved', label: 'Approved' },
  { value: 'published', label: 'Published' },
  { value: 'scheduled', label: 'Scheduled' },
  { value: 'archived', label: 'Archived' },
  { value: 'retired', label: 'Retired' },
];

const CATEGORY_OPTIONS = [
  { value: '', label: 'All Categories' },
  { value: 'mushroom-cultivation', label: 'Mushroom Cultivation' },
  { value: 'spawn-production', label: 'Spawn Production' },
  { value: 'commercial-farming', label: 'Commercial Farming' },
  { value: 'value-added-products', label: 'Value Added Products' },
  { value: 'business-training', label: 'Business Training' },
  { value: 'corporate-training', label: 'Corporate Training' },
  { value: 'institutional-programs', label: 'Institutional Programs' },
  { value: 'franchise-programs', label: 'Franchise Programs' },
];

const LEVEL_OPTIONS = [
  { value: '', label: 'All Levels' },
  { value: 'beginner', label: 'Beginner' },
  { value: 'intermediate', label: 'Intermediate' },
  { value: 'advanced', label: 'Advanced' },
  { value: 'workshop', label: 'Workshop' },
  { value: 'masterclass', label: 'Masterclass' },
  { value: 'certification', label: 'Certification' },
];

const DELIVERY_OPTIONS = [
  { value: '', label: 'All Modes' },
  { value: 'offline', label: 'Offline' },
  { value: 'online', label: 'Online' },
  { value: 'hybrid', label: 'Hybrid' },
  { value: 'recorded', label: 'Recorded' },
  { value: 'live', label: 'Live' },
];

export const CourseExplorerToolbar = memo(function CourseExplorerToolbar({
  viewMode, onViewModeChange, filters, onFilterChange, onClearFilters,
  activeFilterCount, totalItems, pinnedOnly, onPinnedOnlyChange,
  favoritesOnly, onFavoritesOnlyChange, sortBy, sortOrder, onSort,
  onClearSelection, selectionCount,
}: CourseExplorerToolbarProps) {
  const [searchFocused, setSearchFocused] = useState(false);
  const [filterOpen, setFilterOpen] = useState(false);

  const handleSearch = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      onFilterChange('search', e.target.value);
    },
    [onFilterChange]
  );

  return (
    <div style={{
      display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)',
    }}>
      <div style={{
        display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
        flexWrap: 'wrap',
      }}>
        <div style={{
          flex: 1, minWidth: 200, maxWidth: 400, position: 'relative',
          display: 'flex', alignItems: 'center',
        }}>
          <span style={{
            position: 'absolute', left: 8, color: 'var(--color-text-tertiary)',
            pointerEvents: 'none',
          }}>
            <Icon name="search" size={14} color="currentColor" />
          </span>
          <input
            type="search"
            value={filters.search}
            onChange={handleSearch}
            onFocus={() => setSearchFocused(true)}
            onBlur={() => setSearchFocused(false)}
            placeholder="Search courses by name, code or description..."
            aria-label="Search courses"
            style={{
              width: '100%', padding: '6px 12px 6px 30px',
              fontSize: 'var(--text-body-sm)', lineHeight: '20px',
              background: 'var(--color-bg-background)',
              border: `1px solid ${searchFocused ? 'var(--color-border-focus)' : 'var(--color-border-default)'}`,
              borderRadius: 'var(--radius-input)',
              color: 'var(--color-text-primary)',
              outline: 'none',
              transition: 'border-color var(--duration-fast) var(--easing-standard)',
            }}
          />
        </div>

        <div style={{
          position: 'relative', display: 'flex', alignItems: 'center',
        }}>
          <button
            type="button"
            onClick={() => setFilterOpen(!filterOpen)}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              padding: '6px 10px', borderRadius: 'var(--radius-input)',
              background: activeFilterCount > 0 ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
              cursor: 'pointer', color: activeFilterCount > 0 ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontSize: 'var(--text-caption)',
            }}
            aria-label="Toggle filters"
            aria-expanded={filterOpen}
          >
            <Icon name="filter" size={14} color="currentColor" />
            <span>Filters</span>
            {activeFilterCount > 0 && (
              <span style={{
                width: 16, height: 16, borderRadius: 'var(--radius-full)',
                background: 'var(--color-primary)',
                color: 'var(--color-text-on-primary)',
                fontSize: 10, display: 'flex', alignItems: 'center', justifyContent: 'center',
              }}>
                {activeFilterCount}
              </span>
            )}
          </button>
        </div>

        <div style={{ display: 'flex', gap: 2, background: 'var(--color-bg-background)', borderRadius: 'var(--radius-input)', border: '1px solid var(--color-border-default)', padding: 2 }}>
          {VIEW_OPTIONS.map((opt) => (
            <button
              key={opt.mode}
              type="button"
              onClick={() => onViewModeChange(opt.mode)}
              style={{
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                width: 28, height: 28, borderRadius: 'var(--radius-xs)',
                background: viewMode === opt.mode ? 'var(--color-bg-surface-default)' : 'transparent',
                border: 'none', cursor: 'pointer',
                color: viewMode === opt.mode ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
              }}
              aria-label={opt.label}
              aria-pressed={viewMode === opt.mode}
            >
              <Icon name={opt.icon} size={14} color="currentColor" />
            </button>
          ))}
        </div>

        <span style={{
          fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)',
          whiteSpace: 'nowrap',
        }}>
          {totalItems} course{totalItems !== 1 ? 's' : ''}
        </span>
      </div>

      {filterOpen && (
        <div style={{
          display: 'flex', flexWrap: 'wrap', gap: 'var(--space-inline-sm)',
          padding: 'var(--space-3)',
          background: 'var(--color-bg-surface-default)',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-sm)',
          alignItems: 'flex-end',
        }}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Status</label>
            <select
              value={filters.status}
              onChange={(e) => onFilterChange('status', e.target.value)}
              style={{
                padding: '4px 8px', fontSize: 'var(--text-body-sm)',
                borderRadius: 'var(--radius-input)',
                border: '1px solid var(--color-border-default)',
                background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
              }}
              aria-label="Filter by status"
            >
              {STATUS_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Category</label>
            <select
              value={filters.category}
              onChange={(e) => onFilterChange('category', e.target.value)}
              style={{
                padding: '4px 8px', fontSize: 'var(--text-body-sm)',
                borderRadius: 'var(--radius-input)',
                border: '1px solid var(--color-border-default)',
                background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
              }}
              aria-label="Filter by category"
            >
              {CATEGORY_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Level</label>
            <select
              value={filters.level}
              onChange={(e) => onFilterChange('level', e.target.value)}
              style={{
                padding: '4px 8px', fontSize: 'var(--text-body-sm)',
                borderRadius: 'var(--radius-input)',
                border: '1px solid var(--color-border-default)',
                background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
              }}
              aria-label="Filter by level"
            >
              {LEVEL_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Delivery</label>
            <select
              value={filters.deliveryMode}
              onChange={(e) => onFilterChange('deliveryMode', e.target.value)}
              style={{
                padding: '4px 8px', fontSize: 'var(--text-body-sm)',
                borderRadius: 'var(--radius-input)',
                border: '1px solid var(--color-border-default)',
                background: 'var(--color-bg-background)', color: 'var(--color-text-primary)',
              }}
              aria-label="Filter by delivery mode"
            >
              {DELIVERY_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', gap: 4, alignItems: 'flex-end', paddingBottom: 2 }}>
            <button
              type="button"
              onClick={() => onPinnedOnlyChange(!pinnedOnly)}
              style={{
                display: 'flex', alignItems: 'center', gap: 4,
                padding: '4px 8px', borderRadius: 'var(--radius-input)',
                background: pinnedOnly ? 'var(--color-bg-primary-subtle)' : 'transparent',
                border: `1px solid ${pinnedOnly ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
                cursor: 'pointer', color: pinnedOnly ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
              aria-pressed={pinnedOnly}
              aria-label="Show pinned only"
            >
              <Icon name="star" size={12} color="currentColor" />
              Pinned
            </button>
            <button
              type="button"
              onClick={() => onFavoritesOnlyChange(!favoritesOnly)}
              style={{
                display: 'flex', alignItems: 'center', gap: 4,
                padding: '4px 8px', borderRadius: 'var(--radius-input)',
                background: favoritesOnly ? 'var(--color-bg-primary-subtle)' : 'transparent',
                border: `1px solid ${favoritesOnly ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
                cursor: 'pointer', color: favoritesOnly ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
              aria-pressed={favoritesOnly}
              aria-label="Show favorites only"
            >
              <Icon name="heart" size={12} color="currentColor" />
              Favorites
            </button>
          </div>
          {activeFilterCount > 0 && (
            <button
              type="button"
              onClick={onClearFilters}
              style={{
                display: 'flex', alignItems: 'center', gap: 4,
                padding: '4px 8px', borderRadius: 'var(--radius-input)',
                background: 'transparent', border: 'none',
                cursor: 'pointer', color: 'var(--color-danger)',
                fontSize: 'var(--text-caption)',
              }}
              aria-label="Clear all filters"
            >
              <Icon name="x" size={12} color="currentColor" />
              Clear
            </button>
          )}
        </div>
      )}

      <div style={{
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        flexWrap: 'wrap', gap: 'var(--space-inline-sm)',
      }}>
        <div style={{
          display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
        }}>
          {selectionCount > 0 && (
            <span style={{
              fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
            }}>
              {selectionCount} selected
            </span>
          )}
          <div style={{ display: 'flex', gap: 2, fontSize: 'var(--text-caption)' }}>
            <button
              type="button"
              onClick={() => onSort('name')}
              style={{
                padding: '2px 6px', borderRadius: 'var(--radius-xs)',
                background: sortBy === 'name' ? 'var(--color-bg-primary-subtle)' : 'transparent',
                border: 'none', cursor: 'pointer',
                color: sortBy === 'name' ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
              }}
              aria-label="Sort by name"
            >
              Name {sortBy === 'name' && (sortOrder === 'asc' ? '\u2191' : '\u2193')}
            </button>
            <button
              type="button"
              onClick={() => onSort('updatedAt')}
              style={{
                padding: '2px 6px', borderRadius: 'var(--radius-xs)',
                background: sortBy === 'updatedAt' ? 'var(--color-bg-primary-subtle)' : 'transparent',
                border: 'none', cursor: 'pointer',
                color: sortBy === 'updatedAt' ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
              }}
              aria-label="Sort by last updated"
            >
              Updated {sortBy === 'updatedAt' && (sortOrder === 'asc' ? '\u2191' : '\u2193')}
            </button>
            <button
              type="button"
              onClick={() => onSort('rating')}
              style={{
                padding: '2px 6px', borderRadius: 'var(--radius-xs)',
                background: sortBy === 'rating' ? 'var(--color-bg-primary-subtle)' : 'transparent',
                border: 'none', cursor: 'pointer',
                color: sortBy === 'rating' ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
              }}
              aria-label="Sort by rating"
            >
              Rating {sortBy === 'rating' && (sortOrder === 'asc' ? '\u2191' : '\u2193')}
            </button>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 'var(--space-inline-xs)' }}>
          {selectionCount > 0 && (
            <button
              type="button"
              onClick={onClearSelection}
              style={{
                padding: '2px 8px', borderRadius: 'var(--radius-input)',
                background: 'transparent', border: '1px solid var(--color-border-default)',
                cursor: 'pointer', color: 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
            >
              Clear selection
            </button>
          )}
        </div>
      </div>
    </div>
  );
});
