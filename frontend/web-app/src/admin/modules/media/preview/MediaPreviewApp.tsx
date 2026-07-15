import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { MediaPage } from '../MediaPage';
import { ImageGallery } from '../components/ImageGallery';
import { AssetPreview } from '../components/AssetPreview';
import { MOCK_ASSETS } from '../mock/mockAssets';

const GalleryPreview: React.FC = () => {
  const images = MOCK_ASSETS.filter((a) => a.type === 'image').slice(0, 8);
  const [previewId, setPreviewId] = React.useState<string | null>(null);
  const previewAsset = previewId ? MOCK_ASSETS.find((a) => a.id === previewId) ?? null : null;

  return (
    <div style={{ padding: 'var(--space-component-gap)' }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Image Gallery
      </h2>
      <ImageGallery assets={images} onPreview={setPreviewId} />
      {previewAsset && (
        <AssetPreview asset={previewAsset} onClose={() => setPreviewId(null)} />
      )}
    </div>
  );
};

const LibraryPreview: React.FC = () => {
  return (
    <PermissionProvider initialRole="administrator">
      <FeatureFlagProvider>
        <MediaPage />
      </FeatureFlagProvider>
    </PermissionProvider>
  );
};

const InfoPreview: React.FC = () => {
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Digital Asset Library — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Mock Mode</h3>
          <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
            This module operates entirely in mock mode. No backend, cloud storage, or database is connected.
            All 40 assets are statically defined. The upload dialog simulates a 1.5s processing delay.
            The architecture supports future integration with Supabase Storage, Cloudinary, AWS S3, Azure Blob Storage, and CDN without redesign.
          </p>
        </div>
        <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Key Features</h3>
          <ul style={{ margin: 0, paddingLeft: 16, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
            <li>Asset Library with grid/list views</li>
            <li>Collection-based organization (7 collections + system)</li>
            <li>Advanced multi-criteria filtering (type, status, tags, date, size, dimensions)</li>
            <li>Sorting by recency, name, size, type</li>
            <li>Asset preview with metadata panel</li>
            <li>Mock upload with drag-and-drop support</li>
            <li>Bulk operations (archive, delete)</li>
            <li>Product Image Gallery with navigation and thumbnails</li>
            <li>Permission-gated actions</li>
            <li>Session-persisted view preferences</li>
          </ul>
        </div>
        <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Future Integration Points</h3>
          <ul style={{ margin: 0, paddingLeft: 16, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
            <li><strong>Storage:</strong> Replace mock URLs with signed S3/Blob URLs</li>
            <li><strong>Upload:</strong> Replace setTimeout with multipart upload to storage service</li>
            <li><strong>CDN:</strong> Add image transformation params (width, quality, format)</li>
            <li><strong>Search:</strong> Integrate with Elasticsearch or Algolia for full-text search</li>
            <li><strong>Auth:</strong> Replace CURRENT_MEDIA_ROLE with real role from identity service</li>
            <li><strong>Persistence:</strong> Replace sessionStorage with API calls to media service</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export const MediaPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="library" replace />} />
      <Route path="library" element={<LibraryPreview />} />
      <Route path="gallery" element={<GalleryPreview />} />
      <Route path="info" element={<InfoPreview />} />
    </Routes>
  );
};

export default MediaPreviewApp;
