import React, { useState, useCallback } from 'react';
import { PermissionGate } from '../../permissions/PermissionGate';
import { useMediaLibrary } from './state/useMediaLibrary';
import { AssetLibrary } from './components/AssetLibrary';
import { MediaToolbar } from './components/MediaToolbar';
import { MediaFilterPanel } from './components/MediaFilterPanel';
import { CollectionManager } from './components/CollectionManager';
import { AssetPreview } from './components/AssetPreview';
import { MediaUploader } from './components/MediaUploader';
import { BulkActionDialogs, type BulkActionKind } from './components/BulkActions';
import { CURRENT_MEDIA_ROLE, canMedia } from './permissions';
import './Media.css';

export const MediaPage: React.FC = () => {
  const state = useMediaLibrary();
  const [bulkAction, setBulkAction] = useState<BulkActionKind>(null);
  const [showFilters, setShowFilters] = useState(true);

  const handleBulkConfirm = useCallback(() => {
    setBulkAction(null);
    state.clearSelection();
  }, [state]);

  const handleBulkCancel = useCallback(() => setBulkAction(null), []);

  const canUpload = canMedia(CURRENT_MEDIA_ROLE, 'upload');

  const bulkActionsList = state.selectedIds.size > 0 ? (
    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        {state.selectedIds.size} selected
      </span>
      <button type="button" onClick={() => setBulkAction('archive')} style={bulkBtnStyle} aria-label="Archive selected">
        Archive
      </button>
      <button type="button" onClick={() => setBulkAction('delete')} style={bulkBtnStyle} aria-label="Delete selected">
        Delete
      </button>
      <button type="button" onClick={state.clearSelection} style={bulkBtnStyle}>
        Clear
      </button>
    </div>
  ) : null;

  const pageContent = (
    <div className="sk-media-page">
      <div style={{ marginBottom: 'var(--space-component-gap)' }}>
        <MediaToolbar
          search={state.search}
          onSearch={state.setSearch}
          sort={state.sort}
          onSort={state.setSort}
          viewMode={state.viewMode}
          onViewMode={state.setViewMode}
          selectedCount={state.selectedIds.size}
          activeCollectionId={state.activeCollectionId}
          sidebarOpen={state.sidebarOpen}
          onToggleSidebar={() => state.setSidebarOpen(!state.sidebarOpen)}
          onUpload={state.openUpload}
          onClearSelection={state.clearSelection}
          totalResults={state.filteredAssets.length}
        />
        {bulkActionsList}
      </div>

      <div className="sk-media-grid">
        <aside
          className="sk-media-sidebar"
          style={{
            display: state.sidebarOpen ? 'flex' : 'none',
            flexDirection: 'column',
            borderRadius: 'var(--radius-md)',
            border: '1px solid var(--color-border)',
            background: 'var(--color-bg-surface-default)',
          }}
        >
          <CollectionManager
            collections={state.collections}
            activeCollectionId={state.activeCollectionId}
            onSelectCollection={state.setActiveCollectionId}
          />
          <div style={{ borderTop: '1px solid var(--color-border)', margin: '0' }} />
          <button
            type="button"
            onClick={() => setShowFilters((v) => !v)}
            style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
              padding: '8px 12px', border: 'none', background: 'none',
              cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
              fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)',
              fontWeight: 'var(--weight-semibold)', width: '100%',
            }}
          >
            Filters {state.activeFilterCount > 0 && `(${state.activeFilterCount})`}
            <span style={{ marginLeft: 'auto', fontSize: 'var(--text-caption)' }}>
              {showFilters ? '▲' : '▼'}
            </span>
          </button>
          {showFilters && (
            <MediaFilterPanel
              filters={state.filters}
              onFiltersChange={state.setFilters}
              allTags={state.allTags}
              activeFilterCount={state.activeFilterCount}
              onClearAll={state.clearAllFilters}
            />
          )}
        </aside>

        <main className="sk-media-content" style={{ minWidth: 0 }}>
          <AssetLibrary
            assets={state.filteredAssets}
            loading={state.loading}
            viewMode={state.viewMode}
            selectedIds={state.selectedIds}
            onToggleSelect={state.toggleSelect}
            onPreview={state.openPreview}
            search={state.search}
            activeFilterCount={state.activeFilterCount}
            onClearSearch={() => state.setSearch('')}
            onClearFilters={state.clearAllFilters}
          />
        </main>
      </div>

      {state.previewAsset && (
        <AssetPreview asset={state.previewAsset} onClose={state.closePreview} />
      )}

      {canUpload && (
        <MediaUploader open={state.uploadOpen} onClose={state.closeUpload} />
      )}

      <BulkActionDialogs
        active={bulkAction}
        count={state.selectedIds.size}
        onConfirm={handleBulkConfirm}
        onCancel={handleBulkCancel}
      />

      <footer
        style={{
          marginTop: 'var(--space-section-gap)',
          fontSize: 'var(--text-caption)',
          color: 'var(--color-text-tertiary)',
          borderTop: '1px solid var(--color-border)',
          paddingTop: 'var(--space-component-gap)',
          display: 'flex',
          justifyContent: 'space-between',
        }}
      >
        <span>Mock Mode — no persistence. Role: {CURRENT_MEDIA_ROLE}</span>
        <span>{state.filteredAssets.length} assets loaded</span>
      </footer>
    </div>
  );

  return (
    <PermissionGate action="view" resource="media" fallback={<MediaAccessDenied />}>
      {pageContent}
    </PermissionGate>
  );
};

function MediaAccessDenied() {
  return (
    <div
      style={{
        display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
        gap: 'var(--space-stack-md)', padding: 'var(--space-12) var(--space-4)', textAlign: 'center',
      }}
    >
      <h2 style={{ margin: 0, fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)' }}>Access Denied</h2>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
        You do not have permission to view the Media Library.
      </p>
    </div>
  );
}

const bulkBtnStyle: React.CSSProperties = {
  padding: '4px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)', cursor: 'pointer',
  fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

export default MediaPage;
