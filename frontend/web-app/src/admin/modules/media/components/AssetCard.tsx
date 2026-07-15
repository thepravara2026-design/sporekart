import React from 'react';
import Icon from '../../../../design-system/icons/Icon';
import type { Asset, AssetType } from '../types';

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function formatDate(iso: string): string {
  const d = new Date(iso);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}

function truncate(str: string, max: number): string {
  return str.length > max ? str.slice(0, max) + '...' : str;
}

const TYPE_COLORS: Record<AssetType, string> = {
  image: 'var(--color-accent-blue)',
  video: 'var(--color-accent-purple)',
  document: 'var(--color-accent-orange)',
  '3d_model': 'var(--color-accent-green)',
  audio: 'var(--color-accent-pink)',
};

const TYPE_ICONS: Record<AssetType, string> = {
  image: 'image',
  video: 'video',
  document: 'file-text',
  '3d_model': 'box',
  audio: 'music',
};

interface AssetCardProps {
  asset: Asset;
  isSelected: boolean;
  onSelect: (id: string) => void;
  onPreview: (id: string) => void;
}

export const AssetCard = React.memo(function AssetCard({ asset, isSelected, onSelect, onPreview }: AssetCardProps) {
  const cardStyle: React.CSSProperties = {
    position: 'relative', borderRadius: 'var(--radius-card)',
    border: isSelected ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
    overflow: 'hidden', cursor: 'pointer', background: 'var(--color-bg-surface-default)',
    transition: 'border-color var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
    outline: 'none',
  };

  const handleClick = () => onPreview(asset.id);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      onPreview(asset.id);
    }
  };

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.stopPropagation();
    onSelect(asset.id);
  };

  const typeColor = TYPE_COLORS[asset.type];

  return (
    <div
      className={`sk-asset-card ${isSelected ? 'sk-asset-card--selected' : ''}`}
      style={cardStyle}
      onClick={handleClick}
      onKeyDown={handleKeyDown}
      tabIndex={0}
      role="button"
      aria-label={`Preview ${asset.name}`}
    >
      <div
        style={{
          position: 'absolute', top: 8, left: 8, zIndex: 2,
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <input
          type="checkbox"
          checked={isSelected}
          onChange={handleCheckboxChange}
          aria-label={`Select ${asset.name}`}
          style={{ width: 18, height: 18, cursor: 'pointer', accentColor: 'var(--color-primary)' }}
        />
      </div>

      <div
        style={{
          position: 'relative', width: '100%', aspectRatio: '1', overflow: 'hidden',
          background: 'var(--color-bg-surface-raised)',
        }}
      >
        {asset.thumbnailUrl ? (
          <img
            src={asset.thumbnailUrl}
            alt={asset.alt || asset.name}
            style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
            loading="lazy"
          />
        ) : (
          <div
            style={{
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              width: '100%', height: '100%', color: typeColor,
            }}
          >
            <Icon name={TYPE_ICONS[asset.type]} size={48} />
          </div>
        )}
        <div
          style={{
            position: 'absolute', bottom: 0, left: 0, right: 0,
            padding: '4px 6px', fontSize: 'var(--text-caption)',
            color: '#fff', background: 'rgba(0,0,0,0.55)',
            display: 'flex', alignItems: 'center', gap: 4,
            fontFamily: 'var(--font-family-sans)',
          }}
        >
          <Icon name={TYPE_ICONS[asset.type]} size={12} />
          <span style={{ textTransform: 'uppercase' }}>{asset.extension}</span>
          <span style={{ marginLeft: 'auto' }}>{formatFileSize(asset.fileSize)}</span>
        </div>
      </div>

      <div style={{ padding: '8px 10px 10px', display: 'flex', flexDirection: 'column', gap: 4 }}>
        <span
          style={{
            fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)', lineHeight: 'var(--leading-tight)',
            overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap',
          }}
          title={asset.name}
        >
          {truncate(asset.name, 30)}
        </span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {formatDate(asset.createdAt)}
        </span>
        <span
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 4, alignSelf: 'flex-start',
            padding: '2px 6px', borderRadius: 'var(--radius-xs)', fontSize: 'var(--text-caption)',
            background: typeColor, color: '#fff', fontFamily: 'var(--font-family-sans)',
            textTransform: 'capitalize',
          }}
        >
          {asset.type}
        </span>
      </div>
    </div>
  );
});

export default AssetCard;
