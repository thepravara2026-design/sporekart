import React from 'react';
import Icon from '../../../../design-system/icons/Icon';
import type { AssetCollection } from '../types';

interface CollectionManagerProps {
  collections: AssetCollection[];
  activeCollectionId: string | null;
  onSelectCollection: (id: string | null) => void;
}

export const CollectionManager = React.memo(function CollectionManager({
  collections, activeCollectionId, onSelectCollection,
}: CollectionManagerProps) {
  const allItem = collections.find((c) => c.id === 'col-all');

  const itemStyle = (isActive: boolean): React.CSSProperties => ({
    display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
    padding: '8px 12px', borderRadius: 'var(--radius-sm)',
    border: 'none', cursor: 'pointer', textAlign: 'left',
    fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)',
    background: isActive ? 'var(--color-primary-alpha)' : 'transparent',
    color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
    fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-normal)',
    width: '100%', transition: 'background var(--duration-fast) var(--easing-standard)',
  });

  return (
    <nav aria-label="Asset collections" style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <div style={{ padding: '0 12px 8px', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)' }}>
        Collections
      </div>

      {allItem && (
        <button
          type="button"
          style={itemStyle(activeCollectionId === null || activeCollectionId === 'col-all')}
          onClick={() => onSelectCollection(null)}
          aria-current={activeCollectionId === null || activeCollectionId === 'col-all' ? 'page' : undefined}
        >
          <Icon name="folder" size={16} />
          <span style={{ flex: 1 }}>{allItem.name}</span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{allItem.assetCount}</span>
        </button>
      )}

      {collections
        .filter((c) => c.id !== 'col-all')
        .map((collection) => {
          const isActive = activeCollectionId === collection.id;
          return (
            <button
              key={collection.id}
              type="button"
              style={itemStyle(isActive)}
              onClick={() => onSelectCollection(collection.id)}
              aria-current={isActive ? 'page' : undefined}
              title={collection.description}
            >
              <Icon name={collection.isSystem ? 'lock' : 'folder'} size={16} />
              <span style={{ flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                {collection.name}
              </span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{collection.assetCount}</span>
            </button>
          );
        })}
    </nav>
  );
});

export default CollectionManager;
