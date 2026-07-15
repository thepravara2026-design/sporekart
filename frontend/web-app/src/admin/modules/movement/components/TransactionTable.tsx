import { memo, useState } from 'react';
import type { TransactionRecord } from '../types';
import { sortTransactions, paginate, getStatusVariant, getMovementTypeLabel } from '../utils';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { SkeletonTable } from '../../inventory/components';

const SortIcon = memo(function SortIcon({ active, dir }: { active: boolean; dir: string }) {
  return <span style={{ marginLeft: 4, opacity: active ? 1 : 0.3 }}>{dir === 'asc' ? '\u25B2' : '\u25BC'}</span>;
});

interface TransactionTableProps {
  transactions: TransactionRecord[];
  loading?: boolean;
  onSelect?: (id: string) => void;
}

export const TransactionTable = memo(function TransactionTable({ transactions, loading, onSelect }: TransactionTableProps) {
  const [sortKey, setSortKey] = useState<keyof TransactionRecord>('createdAt');
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('desc');
  const [page, setPage] = useState(1);
  const [selected, setSelected] = useState<Set<string>>(new Set());
  const pageSize = 10;

  const handleSort = (key: keyof TransactionRecord) => {
    if (key === sortKey) setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));
    else { setSortKey(key); setSortDir('asc'); }
  };

  const sorted = sortTransactions(transactions, sortKey, sortDir);
  const { items: pageItems, totalPages } = paginate(sorted, page, pageSize);
  const hasSelection = selected.size > 0;

  const toggleSelect = (id: string) => {
    setSelected((prev) => { const n = new Set(prev); if (n.has(id)) n.delete(id); else n.add(id); return n; });
  };
  const toggleAll = () => {
    if (selected.size === pageItems.length) setSelected(new Set());
    else setSelected(new Set(pageItems.map((b) => b.id)));
  };

  if (loading) return <SkeletonTable rows={10} />;
  if (transactions.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No transactions found.</div>;

  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
      {hasSelection && (
        <div style={{ display: 'flex', gap: 8, padding: '10px 16px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-primary-alpha)', alignItems: 'center', flexWrap: 'wrap' }}>
          <span style={{ fontSize: 'var(--text-caption)', fontWeight: 600, color: 'var(--color-primary)' }}>{selected.size} selected</span>
          <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Update Status</button>
          <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Archive</button>
          <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Validate</button>
          <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Export</button>
          <button onClick={() => setSelected(new Set())} style={{ padding: '4px 12px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'transparent', cursor: 'pointer', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 'auto' }}>Clear</button>
        </div>
      )}
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} role="grid" aria-label="Transaction registry table">
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', width: 40 }}><input type="checkbox" checked={selected.size === pageItems.length && pageItems.length > 0} onChange={toggleAll} aria-label="Select all" /></th>
              {[{ key: 'referenceNumber', label: 'Reference' }, { key: 'movementType', label: 'Type' }, { key: 'product', label: 'Product' }, { key: 'warehouse', label: 'Warehouse' }, { key: 'status', label: 'Status' }, { key: 'quantity', label: 'Qty' }, { key: 'createdAt', label: 'Created' }].map((col) => (
                <th key={col.key} onClick={() => handleSort(col.key as keyof TransactionRecord)}
                  aria-sort={sortKey === col.key ? (sortDir === 'asc' ? 'ascending' : 'descending') : undefined}
                  role="columnheader" tabIndex={0} onKeyDown={(e) => e.key === 'Enter' && handleSort(col.key as keyof TransactionRecord)}
                  style={{ padding: 12, textAlign: 'left', cursor: 'pointer', userSelect: 'none', fontWeight: 600, color: 'var(--color-text-primary)', whiteSpace: 'nowrap' }}>
                  {col.label}<SortIcon active={sortKey === col.key} dir={sortDir} />
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {pageItems.map((txn) => (
              <tr key={txn.id} onClick={() => onSelect?.(txn.id)} tabIndex={onSelect ? 0 : undefined} role={onSelect ? 'button' : undefined} onKeyDown={onSelect ? (e) => { if (e.key === 'Enter') onSelect(txn.id); } : undefined}
                style={{ borderBottom: '1px solid var(--color-border)', cursor: onSelect ? 'pointer' : 'default', background: selected.has(txn.id) ? 'var(--color-primary-alpha)' : 'transparent' }}>
                <td style={{ padding: 12 }} onClick={(e) => e.stopPropagation()}>
                  <input type="checkbox" checked={selected.has(txn.id)} onChange={() => toggleSelect(txn.id)} aria-label={`Select ${txn.referenceNumber}`} />
                </td>
                <td style={{ padding: 12, fontWeight: 500 }}>{txn.referenceNumber}</td>
                <td style={{ padding: 12 }}><StatusBadge status={getMovementTypeLabel(txn.movementType)} variant="info" /></td>
                <td style={{ padding: 12 }}>{txn.product}</td>
                <td style={{ padding: 12 }}>{txn.warehouse}</td>
                <td style={{ padding: 12 }}><StatusBadge status={txn.status.replace(/_/g, ' ')} variant={getStatusVariant(txn.status)} /></td>
                <td style={{ padding: 12, textAlign: 'right' }}>{txn.quantity.toLocaleString()}</td>
                <td style={{ padding: 12, whiteSpace: 'nowrap' }}>{new Date(txn.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 16px', borderTop: '1px solid var(--color-border)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{(page - 1) * pageSize + 1}–{Math.min(page * pageSize, sorted.length)} of {sorted.length}</span>
        <div style={{ display: 'flex', gap: 4 }}>
          <button disabled={page <= 1} onClick={() => setPage((p) => p - 1)} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: page <= 1 ? 'default' : 'pointer', opacity: page <= 1 ? 0.5 : 1 }}>Prev</button>
          {Array.from({ length: Math.min(totalPages, 7) }, (_, i) => i + 1).map((p) => (
            <button key={p} onClick={() => setPage(p)} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: p === page ? 'var(--color-primary)' : 'var(--color-surface)', color: p === page ? '#fff' : 'var(--color-text-primary)', cursor: 'pointer', fontWeight: p === page ? 600 : 400 }}>{p}</button>
          ))}
          <button disabled={page >= totalPages} onClick={() => setPage((p) => p + 1)} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: page >= totalPages ? 'default' : 'pointer', opacity: page >= totalPages ? 0.5 : 1 }}>Next</button>
        </div>
      </div>
    </div>
  );
});
