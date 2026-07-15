import React from 'react';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import type { Asset } from '../types';
import { getCollectionById } from '../mock/mockCollections';

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(iso: string): string {
  const d = new Date(iso);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
}

interface AssetPreviewProps {
  asset: Asset;
  onClose: () => void;
}

export const AssetPreview = React.memo(function AssetPreview({ asset, onClose }: AssetPreviewProps) {
  const overlayStyle: React.CSSProperties = {
    position: 'fixed', inset: 0, zIndex: 1000,
    background: 'rgba(0,0,0,0.6)', display: 'flex', alignItems: 'center',
    justifyContent: 'center', padding: 'var(--space-4)',
  };

  const modalStyle: React.CSSProperties = {
    display: 'flex', flexDirection: 'column',
    background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-modal)',
    maxWidth: 900, width: '100%', maxHeight: '90vh',
    boxShadow: 'var(--shadow-3)', overflow: 'hidden',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex', alignItems: 'center', justifyContent: 'space-between',
    padding: 'var(--space-3) var(--space-4)',
    borderBottom: '1px solid var(--color-border-default)',
  };

  const closeBtnStyle: React.CSSProperties = {
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    width: 36, height: 36, border: 'none', background: 'none',
    cursor: 'pointer', borderRadius: 'var(--radius-xs)',
    color: 'var(--color-text-secondary)', fontSize: 20,
  };

  const rowStyle: React.CSSProperties = {
    display: 'flex', justifyContent: 'space-between', padding: '6px 0',
    borderBottom: '1px solid var(--color-border-weak)',
    fontSize: 'var(--text-body-sm)',
  };

  const labelStyle: React.CSSProperties = {
    color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)', minWidth: 130,
  };

  const valueStyle: React.CSSProperties = {
    color: 'var(--color-text-primary)', textAlign: 'right' as const, flex: 1,
  };

  const collectionNames = asset.collectionIds
    .map((cid) => getCollectionById(cid)?.name)
    .filter(Boolean)
    .join(', ');

  const metaRows = [
    { label: 'Type', value: asset.type },
    { label: 'File name', value: asset.fileName },
    { label: 'File size', value: formatFileSize(asset.fileSize) },
    { label: 'Dimensions', value: asset.width && asset.height ? `${asset.width} × ${asset.height} px` : '—' },
    { label: 'MIME type', value: asset.mimeType },
    { label: 'Created', value: formatDate(asset.createdAt) },
    { label: 'Updated', value: formatDate(asset.updatedAt) },
    { label: 'Created by', value: asset.createdBy },
    { label: 'Version', value: `v${asset.version}` },
    { label: 'Collections', value: collectionNames || '—' },
  ];

  return (
    <div style={overlayStyle} onClick={onClose} role="dialog" aria-modal="true" aria-label={`Preview: ${asset.name}`}>
      <div style={modalStyle} onClick={(e) => e.stopPropagation()}>
        <div style={headerStyle}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
            <Icon name={asset.type === 'image' ? 'image' : asset.type === 'video' ? 'video' : 'file-text'} size={20} />
            <span style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{asset.name}</span>
          </div>
          <button type="button" style={closeBtnStyle} onClick={onClose} aria-label="Close preview">
            <Icon name="x" size={20} />
          </button>
        </div>

        <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
          <div
            style={{
              flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center',
              padding: 'var(--space-4)', background: 'var(--color-bg-background)',
              minHeight: 300,
            }}
          >
            {asset.thumbnailUrl ? (
              <img
                src={asset.thumbnailUrl}
                alt={asset.alt || asset.name}
                style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain', borderRadius: 'var(--radius-sm)' }}
              />
            ) : (
              <div style={{ textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
                <Icon name="file-text" size={64} />
                <p style={{ margin: '8px 0 0', fontSize: 'var(--text-body-sm)' }}>Preview not available</p>
              </div>
            )}
          </div>

          <div
            style={{
              width: 280, flexShrink: 0, padding: 'var(--space-3) var(--space-4)',
              borderLeft: '1px solid var(--color-border-default)',
              display: 'flex', flexDirection: 'column', gap: 2, overflowY: 'auto',
            }}
          >
            <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>Details</h4>
            {metaRows.map((row) => (
              <div key={row.label} style={rowStyle}>
                <span style={labelStyle}>{row.label}</span>
                <span style={valueStyle}>{row.value}</span>
              </div>
            ))}

            {asset.tags.length > 0 && (
              <div style={{ marginTop: 'var(--space-stack-sm)' }}>
                <span style={{ ...labelStyle, display: 'block', marginBottom: 4 }}>Tags</span>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
                  {asset.tags.map((tag) => (
                    <span
                      key={tag}
                      style={{
                        padding: '2px 8px', borderRadius: 'var(--radius-full)',
                        background: 'var(--color-bg-surface-raised)',
                        fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
                      }}
                    >
                      {tag}
                    </span>
                  ))}
                </div>
              </div>
            )}

            <div style={{ marginTop: 'auto', paddingTop: 'var(--space-stack-sm)' }}>
              <Button variant="primary" size="sm" leftIcon={<Icon name="download" size={14} />} style={{ width: '100%' }} disabled title="Mock Mode — download disabled">
                Download
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
});

export default AssetPreview;
