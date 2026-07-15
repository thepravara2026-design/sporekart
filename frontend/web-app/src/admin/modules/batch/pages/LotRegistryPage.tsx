import { memo, useState, useMemo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { filterLots, paginate } from '../utils';
import { BATCH_EMPTY_STATES } from '../constants';

export const LotRegistryPage = memo(function LotRegistryPage() {
  const { lots } = useBatchData();
  const [query, setQuery] = useState('');
  const [page, setPage] = useState(1);
  const pageSize = 15;
  const filtered = useMemo(() => filterLots(lots, query), [lots, query]);
  const { items, totalPages } = paginate(filtered, page, pageSize);
  const emptyState = filtered.length === 0 && query ? BATCH_EMPTY_STATES.noLots : filtered.length === 0 ? BATCH_EMPTY_STATES.noResults : null;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Lot Registry" description="All lots associated with batches." />
      <input value={query} onChange={(e) => { setQuery(e.target.value); setPage(1); }} placeholder="Search by lot code, batch code, product, warehouse..." style={{ padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)', width: '100%', maxWidth: 480 }} />
      {emptyState ? (
        <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <p style={{ fontWeight: 600, margin: '0 0 4px', color: 'var(--color-text-primary)' }}>{emptyState.title}</p>
          <p style={{ margin: '0 0 12px', fontSize: 'var(--text-body)' }}>{emptyState.message}</p>
        </div>
      ) : (
      <div style={{ overflowX: 'auto', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Lot Code</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Batch Code</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Product</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Warehouse</th>
              <th style={{ padding: 12, textAlign: 'right', fontWeight: 600, color: 'var(--color-text-primary)' }}>Quantity</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-primary)' }}>Created</th>
            </tr>
          </thead>
          <tbody>
            {items.map((lot) => (
              <tr key={lot.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500 }}>{lot.lotCode}</td>
                <td style={{ padding: 12 }}>{lot.batchCode}</td>
                <td style={{ padding: 12 }}>{lot.product}</td>
                <td style={{ padding: 12 }}>{lot.warehouse}</td>
                <td style={{ padding: 12, textAlign: 'right' }}>{lot.quantity.toLocaleString()}</td>
                <td style={{ padding: 12, whiteSpace: 'nowrap' }}>{new Date(lot.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 16px', borderTop: '1px solid var(--color-border)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          <span>Showing {(page - 1) * pageSize + 1}–{Math.min(page * pageSize, filtered.length)} of {filtered.length}</span>
          <div style={{ display: 'flex', gap: 4 }}>
            <button disabled={page <= 1} onClick={() => setPage((p) => p - 1)} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: page <= 1 ? 'default' : 'pointer', opacity: page <= 1 ? 0.5 : 1 }}>Prev</button>
            <span style={{ padding: '4px 10px' }}>Page {page} of {totalPages}</span>
            <button disabled={page >= totalPages} onClick={() => setPage((p) => p + 1)} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: page >= totalPages ? 'default' : 'pointer', opacity: page >= totalPages ? 0.5 : 1 }}>Next</button>
          </div>
        </div>
      </div>
      )}
    </div>
  );
});


