import React from 'react';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import type { SortOption, ViewMode } from '../types';
import { getCollectionById } from '../mock/mockCollections';

interface MediaToolbarProps {
  search: string;
  onSearch: (value: string) => void;
  sort: SortOption;
  onSort: (sort: SortOption) => void;
  viewMode: ViewMode;
  onViewMode: (mode: ViewMode) => void;
  selectedCount: number;
  activeCollectionId: string | null;
  sidebarOpen: boolean;
  onToggleSidebar: () => void;
  onUpload: () => void;
  onClearSelection: () => void;
  totalResults: number;
}

const SORT_OPTIONS: { value: SortOption; label: string }[] = [
  { value: 'newest', label: 'Newest' },
  { value: 'oldest', label: 'Oldest' },
  { value: 'name_asc', label: 'Name A–Z' },
  { value: 'name_desc', label: 'Name Z–A' },
  { value: 'size_asc', label: 'Smallest' },
  { value: 'size_desc', label: 'Largest' },
  { value: 'type', label: 'Type' },
];

export const MediaToolbar = React.memo(function MediaToolbar({
  search, onSearch, sort, onSort, viewMode, onViewMode,
  selectedCount, activeCollectionId, sidebarOpen, onToggleSidebar,
  onUpload, onClearSelection, totalResults,
}: MediaToolbarProps) {
  const rowStyle: React.CSSProperties = {
    display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', flexWrap: 'wrap',
  };
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
  const iconBtnStyle: React.CSSProperties = {
    display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
    padding: 8, border: '1px solid var(--color-border-default)',
    borderRadius: 'var(--radius-input)', background: 'var(--color-bg-surface-default)',
    cursor: 'pointer', color: viewMode === 'grid' ? 'var(--color-primary)' : 'var(--color-text-secondary)',
  };
  const iconBtnStyleList: React.CSSProperties = {
    ...iconBtnStyle, color: viewMode === 'list' ? 'var(--color-primary)' : 'var(--color-text-secondary)',
  };

  const collectionName = activeCollectionId ? getCollectionById(activeCollectionId)?.name ?? 'Unknown' : 'All Assets';

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
      <div style={rowStyle}>
        <button
          type="button"
          onClick={onToggleSidebar}
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 12px',
            border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)',
            background: sidebarOpen ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)',
            cursor: 'pointer', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)',
            color: sidebarOpen ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          }}
          aria-label={sidebarOpen ? 'Hide sidebar' : 'Show sidebar'}
          aria-pressed={sidebarOpen}
        >
          <Icon name="sidebar" size={16} />
        </button>

        <input
          type="search"
          placeholder="Search assets by name, tags, description..."
          value={search}
          onChange={(e) => onSearch(e.target.value)}
          style={inputStyle}
          aria-label="Search media assets"
        />

        <select
          value={sort}
          onChange={(e) => onSort(e.target.value as SortOption)}
          style={selectStyle}
          aria-label="Sort assets"
        >
          {SORT_OPTIONS.map((opt) => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>

        <button
          type="button"
          onClick={() => onViewMode('grid')}
          style={iconBtnStyle}
          aria-label="Grid view"
          aria-pressed={viewMode === 'grid'}
        >
          <Icon name="grid" size={16} />
        </button>
        <button
          type="button"
          onClick={() => onViewMode('list')}
          style={iconBtnStyleList}
          aria-label="List view"
          aria-pressed={viewMode === 'list'}
        >
          <Icon name="list" size={16} />
        </button>

        <Button variant="primary" size="sm" leftIcon={<Icon name="upload" size={14} />} onClick={onUpload}>
          Upload
        </Button>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 'var(--space-inline-xs)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
            {collectionName}
          </span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            · {totalResults} asset{totalResults === 1 ? '' : 's'}
          </span>
          {selectedCount > 0 && (
            <>
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-primary)', fontWeight: 'var(--weight-semibold)' }}>
                · {selectedCount} selected
              </span>
              <Button variant="ghost" size="sm" onClick={onClearSelection}>
                Clear
              </Button>
            </>
          )}
        </div>
      </div>
    </div>
  );
});

export default MediaToolbar;
