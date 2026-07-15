import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { SkeletonTable } from '../../inventory/components';
import type { StockRecord, StockSortKey, SortDir } from '../types';
import { getStatusVariant } from '../utils';

interface Props {
  data: StockRecord[];
  loading?: boolean;
  sortKey?: StockSortKey;
  sortDir?: SortDir;
  onSort?: (key: StockSortKey) => void;
  onRowClick?: (record: StockRecord) => void;
  selectedIds?: Set<string>;
  onSelect?: (id: string, checked: boolean) => void;
  onSelectAll?: (checked: boolean) => void;
  emptyKey?: 'no_stock' | 'no_results';
  onEmptyAction?: () => void;
  showWarehouse?: boolean;
}

export const StockTable = memo(function StockTable({ data, loading, sortKey, sortDir, onSort, onRowClick, selectedIds, onSelect, onSelectAll, emptyKey, onEmptyAction, showWarehouse = true }: Props) {
  if (loading) return <SkeletonTable rows={8} />;

  if (data.length === 0) {
    return (
      <div role="status" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 12, padding: '48px 24px', border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', minHeight: 240 }}>
        <Icon name={emptyKey === 'no_results' ? 'search' : 'database'} size={28} />
        <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>{emptyKey === 'no_results' ? 'No stock records match your search and filters.' : 'No stock records found.'}</p>
        {onEmptyAction && <button onClick={onEmptyAction} style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontWeight: 600 }}>Clear Filters</button>}
      </div>
    );
  }

  const allSelected = selectedIds && selectedIds.size === data.length;

  return (
    <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="Stock records">
        <thead>
          <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
            {onSelect && <Th><input type="checkbox" checked={!!allSelected} onChange={(e) => onSelectAll?.(e.target.checked)} aria-label="Select all" style={{ accentColor: 'var(--color-primary)' }} /></Th>}
            <SortTh label="Stock ID" keyField="code" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Item" keyField="inventoryItemName" current={sortKey} dir={sortDir} onSort={onSort} />
            <Th>SKU</Th>
            {showWarehouse && <SortTh label="Warehouse" keyField="warehouseName" current={sortKey} dir={sortDir} onSort={onSort} />}
            <Th>Available</Th>
            <Th>Reserved</Th>
            <Th>Incoming</Th>
            <SortTh label="Health" keyField="health" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Status" keyField="status" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Updated" keyField="updatedAt" current={sortKey} dir={sortDir} onSort={onSort} />
          </tr>
        </thead>
        <tbody>
          {data.map((rec) => (
            <tr key={rec.id} onClick={onRowClick ? () => onRowClick(rec) : undefined} tabIndex={onRowClick ? 0 : undefined} onKeyDown={onRowClick ? (e) => { if (e.key === 'Enter') onRowClick(rec); } : undefined} style={{ borderTop: '1px solid var(--color-border)', cursor: onRowClick ? 'pointer' : 'default' }}>
              {onSelect && <Td><input type="checkbox" checked={selectedIds?.has(rec.id) ?? false} onChange={(e) => onSelect(rec.id, e.target.checked)} aria-label={`Select ${rec.code}`} style={{ accentColor: 'var(--color-primary)' }} /></Td>}
              <Td><span style={{ fontFamily: 'monospace', fontSize: 'var(--text-caption)' }}>{rec.code}</span></Td>
              <Td><span style={{ fontWeight: 500 }}>{rec.inventoryItemName}</span></Td>
              <Td style={{ fontSize: 'var(--text-caption)', fontFamily: 'monospace' }}>{rec.sku}</Td>
              {showWarehouse && <Td style={{ color: 'var(--color-text-secondary)' }}>{rec.warehouseName}</Td>}
              <Td><NumberBadge value={rec.quantities.available ?? 0} /></Td>
              <Td><NumberBadge value={rec.quantities.reserved ?? 0} /></Td>
              <Td><NumberBadge value={rec.quantities.incoming ?? 0} /></Td>
              <Td><HealthBadge health={rec.health} /></Td>
              <Td><StatusBadge status={rec.status} variant={getStatusVariant(rec.status)} /></Td>
              <Td style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{rec.updatedAt}</Td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});

function NumberBadge({ value }: { value: number }) {
  const color = value === 0 ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)';
  return <span style={{ fontWeight: 600, color }}>{value.toLocaleString()}</span>;
}

function HealthBadge({ health }: { health: string }) {
  const variants: Record<string, CSSProperties> = {
    healthy: { background: 'var(--color-success-alpha)', color: 'var(--color-success)' },
    low: { background: 'var(--color-warning-alpha)', color: 'var(--color-warning)' },
    critical: { background: 'var(--color-danger-alpha)', color: 'var(--color-danger)' },
    out_of_stock: { background: 'var(--color-danger-alpha)', color: 'var(--color-danger)' },
    overstock: { background: 'var(--color-info-alpha)', color: 'var(--color-info)' },
    needs_inspection: { background: 'var(--color-warning-alpha)', color: 'var(--color-warning)' },
    near_expiry: { background: 'var(--color-warning-alpha)', color: 'var(--color-warning)' },
    damaged: { background: 'var(--color-danger-alpha)', color: 'var(--color-danger)' },
  };
  return <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...(variants[health] ?? variants.healthy) }}>{health.replace(/_/g, ' ')}</span>;
}

function Th({ children, style, ...rest }: { children: React.ReactNode; style?: CSSProperties; [key: string]: unknown }) {
  return <th scope="col" style={{ padding: '12px 16px', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em', whiteSpace: 'nowrap', ...style }} {...rest}>{children}</th>;
}

function SortTh({ label, keyField, current, dir, onSort }: { label: string; keyField: StockSortKey; current?: StockSortKey; dir?: SortDir; onSort?: (key: StockSortKey) => void }) {
  const active = current === keyField;
  return (
    <Th aria-sort={active ? (dir === 'asc' ? 'ascending' : 'descending') : undefined}>
      {onSort ? (
        <button onClick={() => onSort(keyField)} onKeyDown={(e) => e.key === 'Enter' && onSort(keyField)} tabIndex={0}
          style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 0, display: 'inline-flex', alignItems: 'center', gap: 4, fontWeight: 600, color: active ? 'var(--color-primary)' : 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
          {label}
          {active && <Icon name={dir === 'asc' ? 'chevron-up' : 'chevron-down'} size={12} />}
        </button>
      ) : label}
    </Th>
  );
}

function Td({ children, style }: { children: React.ReactNode; style?: CSSProperties }) {
  return <td style={{ padding: '12px 16px', color: 'var(--color-text-primary)', verticalAlign: 'middle', ...style }}>{children}</td>;
}
