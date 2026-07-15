import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgCollection } from '../types';
import { COLLECTION_TYPE_LABELS } from '../types';
import { DetailPanel } from './DetailPanel';

interface CollectionExplorerProps {
  collections: OrgCollection[];
}

export const CollectionExplorer = React.memo(function CollectionExplorer({ collections }: CollectionExplorerProps) {
  const [selectedId, setSelectedId] = React.useState<string | null>(null);
  const [typeFilter, setTypeFilter] = React.useState<string | null>(null);
  const selected = selectedId ? collections.find((c) => c.id === selectedId) ?? null : null;

  const filtered = typeFilter ? collections.filter((c) => c.collectionType === typeFilter) : collections;
  const types = React.useMemo(() => Array.from(new Set(collections.map((c) => c.collectionType))), [collections]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', flexWrap: 'wrap' }}>
        <button type="button" onClick={() => setTypeFilter(null)} style={chipStyle(!typeFilter)}>All</button>
        {types.map((t) => (
          <button key={t} type="button" onClick={() => setTypeFilter(t)} style={chipStyle(typeFilter === t)}>
            {COLLECTION_TYPE_LABELS[t] || t}
          </button>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: selected ? '1fr 320px' : '1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {filtered.map((c) => {
            const isActive = selectedId === c.id;
            return (
              <div
                key={c.id}
                style={{
                  borderRadius: 'var(--radius-card)', border: isActive ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
                  overflow: 'hidden', cursor: 'pointer', background: 'var(--color-bg-surface-default)', transition: 'border-color var(--duration-fast) var(--easing-standard)',
                }}
                onClick={() => setSelectedId(c.id)}
                onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setSelectedId(c.id); } }}
                role="button"
                tabIndex={0}
                aria-label={c.name}
              >
                <div style={{ height: 100, background: 'var(--color-bg-surface-raised)', display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative' }}>
                  <Icon name="bookmark" size={32} style={{ color: 'var(--color-text-tertiary)' }} />
                  {c.isFeatured && <span style={{ position: 'absolute', top: 8, right: 8, padding: '2px 6px', borderRadius: 'var(--radius-xs)', background: 'var(--color-accent-orange)', color: '#fff', fontSize: 'var(--text-caption)' }}>Featured</span>}
                </div>
                <div style={{ padding: 12, display: 'flex', flexDirection: 'column', gap: 4 }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{c.name}</span>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '2px 6px', borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)' }}>{COLLECTION_TYPE_LABELS[c.collectionType]}</span>
                  </div>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{c.productCount} products</span>
                  {c.description && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>{c.description}</span>}
                </div>
              </div>
            );
          })}
        </div>
        {selected && (
          <aside style={{ position: 'sticky', top: 'var(--space-component-gap)' }}>
            <DetailPanel title={selected.name} subtitle={COLLECTION_TYPE_LABELS[selected.collectionType]} icon="bookmark">
              <div style={detailRow}><span style={detailLabel}>Status</span><span style={{ textTransform: 'capitalize' }}>{selected.status}</span></div>
              <div style={detailRow}><span style={detailLabel}>Products</span><span>{selected.productCount}</span></div>
              <div style={detailRow}><span style={detailLabel}>Type</span><span>{COLLECTION_TYPE_LABELS[selected.collectionType]}</span></div>
              <div style={detailRow}><span style={detailLabel}>Featured</span><span>{selected.isFeatured ? 'Yes' : 'No'}</span></div>
              <div style={detailRow}><span style={detailLabel}>Display Order</span><span>{selected.displayOrder}</span></div>
              {selected.startDate && <div style={detailRow}><span style={detailLabel}>Period</span><span>{new Date(selected.startDate).toLocaleDateString()} – {selected.endDate ? new Date(selected.endDate).toLocaleDateString() : 'Ongoing'}</span></div>}
              <div style={detailRow}><span style={detailLabel}>Created</span><span>{new Date(selected.createdAt).toLocaleDateString()}</span></div>
              <div style={detailRow}><span style={detailLabel}>By</span><span>{selected.createdBy}</span></div>
              {selected.description && <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 0' }}>{selected.description}</p>}
            </DetailPanel>
          </aside>
        )}
      </div>
    </div>
  );
});

function chipStyle(active: boolean): React.CSSProperties {
  return {
    padding: '4px 12px', borderRadius: 'var(--radius-full)', border: 'none',
    fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)', cursor: 'pointer',
    fontWeight: active ? 'var(--weight-semibold)' : 'var(--weight-normal)',
    background: active ? 'var(--color-primary)' : 'var(--color-bg-surface-raised)',
    color: active ? '#fff' : 'var(--color-text-secondary)',
    transition: 'all var(--duration-fast) var(--easing-standard)',
  };
}

const detailRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border-weak)', fontSize: 'var(--text-body-sm)' };
const detailLabel: React.CSSProperties = { color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' };

export default CollectionExplorer;
