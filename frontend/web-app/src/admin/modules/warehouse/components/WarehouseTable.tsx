import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { warehouseStatusVariant, warehouseStatusLabel, warehouseTypeLabel, utilization, formatNumber } from '../utils';
import type { Warehouse, WarehouseSortKey, SortDir } from '../types';
import { SkeletonTable } from '../../inventory/components';

interface WarehouseTableProps {
  data: Warehouse[];
  loading?: boolean;
  selectedIds?: string[];
  onToggleSelect?: (id: string) => void;
  onToggleSelectAll?: () => void;
  sortKey?: WarehouseSortKey;
  sortDir?: SortDir;
  onSort?: (key: WarehouseSortKey) => void;
  onRowClick?: (w: Warehouse) => void;
}

const Th: React.FC<{ children?: React.ReactNode; sortKey?: WarehouseSortKey; active?: boolean; dir?: SortDir; onSort?: (k: WarehouseSortKey) => void; style?: CSSProperties }> = ({
  children, sortKey, active, dir, onSort, style,
}) => (
  <th
    scope="col"
    onClick={sortKey && onSort ? () => onSort(sortKey) : undefined}
    aria-sort={sortKey && active ? (dir === 'asc' ? 'ascending' : 'descending') : undefined}
    role={sortKey ? 'columnheader' : undefined}
    tabIndex={sortKey ? 0 : undefined}
    onKeyDown={sortKey && onSort ? (e) => e.key === 'Enter' && onSort(sortKey) : undefined}
    style={{ padding: '12px 16px', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em', whiteSpace: 'nowrap', cursor: sortKey ? 'pointer' : 'default', userSelect: 'none', ...style }}
  >
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
      {children}
      {sortKey && active && <Icon name={dir === 'asc' ? 'arrow-up' : 'arrow-down'} size={12} />}
    </span>
  </th>
);

export const WarehouseTable = memo(function WarehouseTable({
  data, loading, selectedIds = [], onToggleSelect, onToggleSelectAll, sortKey, sortDir, onSort, onRowClick,
}: WarehouseTableProps) {
  if (loading) return <SkeletonTable rows={6} />;

  return (
    <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="Warehouses">
        <thead>
          <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
            <th scope="col" style={{ padding: '12px 16px', width: 40 }}>
              <input
                type="checkbox"
                aria-label="Select all"
                checked={data.length > 0 && selectedIds.length === data.length}
                onChange={() => onToggleSelectAll?.()}
                style={{ accentColor: 'var(--color-primary)' }}
              />
            </th>
            <Th sortKey="code" active={sortKey === 'code'} dir={sortDir} onSort={onSort}>Code</Th>
            <Th sortKey="name" active={sortKey === 'name'} dir={sortDir} onSort={onSort}>Name</Th>
            <Th>Type</Th>
            <Th>Location</Th>
            <Th sortKey="capacity" active={sortKey === 'capacity'} dir={sortDir} onSort={onSort}>Capacity</Th>
            <Th>Status</Th>
            <Th sortKey="updatedAt" active={sortKey === 'updatedAt'} dir={sortDir} onSort={onSort}>Updated</Th>
          </tr>
        </thead>
        <tbody>
          {data.map((w) => {
            const u = utilization(w);
            const selected = selectedIds.includes(w.id);
            return (
              <tr
                key={w.id}
                onClick={onRowClick ? () => onRowClick(w) : undefined}
                tabIndex={onRowClick ? 0 : undefined}
                role={onRowClick ? 'button' : undefined}
                onKeyDown={onRowClick ? (e) => { if (e.key === 'Enter') onRowClick(w); } : undefined}
                style={{ borderTop: '1px solid var(--color-border)', cursor: onRowClick ? 'pointer' : 'default', background: selected ? 'var(--color-primary-alpha)' : 'transparent' }}
              >
                <td style={{ padding: '12px 16px' }} onClick={(e) => e.stopPropagation()}>
                  <input
                    type="checkbox"
                    aria-label={`Select ${w.name}`}
                    checked={selected}
                    onChange={() => onToggleSelect?.(w.id)}
                    style={{ accentColor: 'var(--color-primary)' }}
                  />
                </td>
                <td style={{ padding: '12px 16px', color: 'var(--color-text-primary)', fontWeight: 500, whiteSpace: 'nowrap' }}>{w.code}</td>
                <td style={{ padding: '12px 16px', color: 'var(--color-text-primary)' }}>{w.name}</td>
                <td style={{ padding: '12px 16px', color: 'var(--color-text-secondary)' }}>{warehouseTypeLabel(w.type)}</td>
                <td style={{ padding: '12px 16px', color: 'var(--color-text-secondary)' }}>{w.location}</td>
                <td style={{ padding: '12px 16px' }}>
                  <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{formatNumber(w.used)} / {formatNumber(w.capacity)}</div>
                  <div style={{ marginTop: 4, height: 6, width: 120, background: 'var(--color-surface-hover)', borderRadius: 4, overflow: 'hidden' }}>
                    <div style={{ height: '100%', width: `${u}%`, background: u > 80 ? 'var(--color-danger)' : u > 50 ? 'var(--color-warning)' : 'var(--color-success)' }} />
                  </div>
                </td>
                <td style={{ padding: '12px 16px' }}><StatusBadge status={warehouseStatusLabel(w.status)} variant={warehouseStatusVariant(w.status)} /></td>
                <td style={{ padding: '12px 16px', color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap' }}>{w.updatedAt}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
});


