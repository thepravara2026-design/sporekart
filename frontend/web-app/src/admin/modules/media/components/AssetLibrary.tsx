import React from 'react';
import { AssetCard } from './AssetCard';
import { MediaEmptyState } from './MediaEmptyState';
import { MediaLoading } from './MediaLoading';
import type { Asset, ViewMode } from '../types';

interface AssetLibraryProps {
  assets: Asset[];
  loading: boolean;
  viewMode: ViewMode;
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onPreview: (id: string) => void;
  search: string;
  activeFilterCount: number;
  onClearSearch: () => void;
  onClearFilters: () => void;
}

export const AssetLibrary = React.memo(function AssetLibrary({
  assets, loading, viewMode, selectedIds, onToggleSelect, onPreview,
  search, activeFilterCount, onClearSearch, onClearFilters,
}: AssetLibraryProps) {
  if (loading) {
    return <MediaLoading viewMode={viewMode} />;
  }

  if (assets.length === 0) {
    if (search.trim()) {
      return <MediaEmptyState.NoSearchResults onClear={onClearSearch} />;
    }
    if (activeFilterCount > 0) {
      return <MediaEmptyState.NoFilterResults onClear={onClearFilters} />;
    }
    return <MediaEmptyState.EmptyLibrary />;
  }

  if (viewMode === 'list') {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        {assets.map((asset) => (
          <AssetListItem
            key={asset.id}
            asset={asset}
            isSelected={selectedIds.has(asset.id)}
            onSelect={onToggleSelect}
            onPreview={onPreview}
          />
        ))}
      </div>
    );
  }

  return (
    <div
      style={{
        display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}
    >
      {assets.map((asset) => (
        <AssetCard
          key={asset.id}
          asset={asset}
          isSelected={selectedIds.has(asset.id)}
          onSelect={onToggleSelect}
          onPreview={onPreview}
        />
      ))}
    </div>
  );
});

interface AssetListItemProps {
  asset: Asset;
  isSelected: boolean;
  onSelect: (id: string) => void;
  onPreview: (id: string) => void;
}

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(iso: string): string {
  const d = new Date(iso);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

const AssetListItem = React.memo(function AssetListItem({ asset, isSelected, onSelect, onPreview }: AssetListItemProps) {
  return (
    <div
      style={{
        display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
        padding: '8px 12px', borderRadius: 'var(--radius-sm)',
        border: '1px solid var(--color-border-default)',
        background: isSelected ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)',
        cursor: 'pointer', transition: 'background var(--duration-fast) var(--easing-standard)',
      }}
      onClick={() => onPreview(asset.id)}
      role="button"
      tabIndex={0}
      aria-label={`Preview ${asset.name}`}
    >
      <input
        type="checkbox"
        checked={isSelected}
        onChange={() => onSelect(asset.id)}
        onClick={(e) => e.stopPropagation()}
        aria-label={`Select ${asset.name}`}
        style={{ accentColor: 'var(--color-primary)' }}
      />
      <div
        style={{
          width: 44, height: 44, borderRadius: 'var(--radius-xs)', overflow: 'hidden',
          background: 'var(--color-bg-surface-raised)', flexShrink: 0,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
        }}
      >
        {asset.thumbnailUrl ? (
          <img src={asset.thumbnailUrl} alt={asset.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        ) : (
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{asset.extension}</span>
        )}
      </div>
      <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
        <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{asset.name}</span>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          <span>{formatFileSize(asset.fileSize)}</span>
          <span>·</span>
          <span>{asset.type}</span>
          <span>·</span>
          <span>{formatDate(asset.createdAt)}</span>
        </div>
      </div>
    </div>
  );
});

export default AssetLibrary;
