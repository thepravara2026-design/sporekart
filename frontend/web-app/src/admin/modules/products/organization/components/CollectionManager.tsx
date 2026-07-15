import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgCollection } from '../types';
import { DetailPanel } from './DetailPanel';
import { COLLECTION_TYPE_LABELS } from '../types';

interface CollectionManagerProps {
  collections: OrgCollection[];
}

export const CollectionManager = React.memo(function CollectionManager({ collections }: CollectionManagerProps) {
  const [selectedId, setSelectedId] = React.useState<string | null>(null);
  const selected = selectedId ? collections.find((c) => c.id === selectedId) ?? null : null;

  const gridStyle: React.CSSProperties = {
    display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 'var(--space-component-gap)',
  };

  const cardStyle = (c: OrgCollection): React.CSSProperties => ({
    borderRadius: 'var(--radius-card)', border: selectedId === c.id ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
    overflow: 'hidden', cursor: 'pointer', background: 'var(--color-bg-surface-default)',
    transition: 'border-color var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
  });

  return (
    <div style={{ display: 'grid', gridTemplateColumns: selected ? '1fr 320px' : '1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
      <div style={gridStyle}>
        {collections.map((c) => (
          <div key={c.id} style={cardStyle(c)} onClick={() => setSelectedId(c.id)} onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setSelectedId(c.id); } }} role="button" tabIndex={0} aria-label={c.name}>
            <div style={{ height: 100, background: 'var(--color-bg-surface-raised)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Icon name="bookmark" size={32} style={{ color: 'var(--color-text-tertiary)' }} />
            </div>
            <div style={{ padding: 12, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{c.name}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '2px 6px', borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)' }}>
                  {COLLECTION_TYPE_LABELS[c.collectionType] || c.collectionType}
                </span>
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{c.productCount} products</span>
              <div style={{ display: 'flex', gap: 4, marginTop: 4 }}>
                <span style={{ fontSize: 'var(--text-caption)', color: c.status === 'active' ? 'var(--color-success)' : 'var(--color-text-tertiary)', textTransform: 'capitalize' }}>{c.status}</span>
                {c.isFeatured && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-accent-orange)' }}>Featured</span>}
              </div>
            </div>
          </div>
        ))}
      </div>
      {selected && (
        <aside style={{ position: 'sticky', top: 'var(--space-component-gap)' }}>
          <CollectionDetailPanel collection={selected} />
        </aside>
      )}
    </div>
  );
});

function CollectionDetailPanel({ collection }: { collection: OrgCollection }) {
  const rowStyle: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border-weak)', fontSize: 'var(--text-body-sm)' };
  const labelStyle: React.CSSProperties = { color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' };

  return (
    <DetailPanel title={collection.name} subtitle={COLLECTION_TYPE_LABELS[collection.collectionType]} icon="bookmark">
      <div style={rowStyle}><span style={labelStyle}>Status</span><span style={{ textTransform: 'capitalize' }}>{collection.status}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Products</span><span>{collection.productCount}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Featured</span><span>{collection.isFeatured ? 'Yes' : 'No'}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Display Order</span><span>{collection.displayOrder}</span></div>
      {collection.startDate && <div style={rowStyle}><span style={labelStyle}>Start Date</span><span>{new Date(collection.startDate).toLocaleDateString()}</span></div>}
      {collection.endDate && <div style={rowStyle}><span style={labelStyle}>End Date</span><span>{new Date(collection.endDate).toLocaleDateString()}</span></div>}
      <div style={rowStyle}><span style={labelStyle}>Created</span><span>{new Date(collection.createdAt).toLocaleDateString()}</span></div>
      <div style={rowStyle}><span style={labelStyle}>By</span><span>{collection.createdBy}</span></div>
      {collection.description && (
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 0', lineHeight: 'var(--leading-relaxed)' }}>
          {collection.description}
        </p>
      )}
    </DetailPanel>
  );
}

export default CollectionManager;
