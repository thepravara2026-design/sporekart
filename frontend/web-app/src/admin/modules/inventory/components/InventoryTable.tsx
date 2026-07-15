import { memo, type ReactNode, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { stockStatusVariant, stockStatusLabel, formatNumber } from '../utils';
import type { InventoryItem } from '../types';
import { SkeletonTable } from './InventorySkeleton';

interface InventoryTableProps {
  data: InventoryItem[];
  loading?: boolean;
  onRowClick?: (item: InventoryItem) => void;
  emptyKey?: 'no_inventory' | 'no_results';
  onEmptyAction?: () => void;
}

export const InventoryTable = memo(function InventoryTable({ data, loading, onRowClick, emptyKey, onEmptyAction }: InventoryTableProps) {
  if (loading) return <SkeletonTable rows={6} />;

  if (data.length === 0) {
    return (
      <div role="status" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 12, padding: '48px 24px', border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', minHeight: 240 }}>
        <Icon name={emptyKey === 'no_results' ? 'search' : 'package'} size={28} />
        <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>
          {emptyKey === 'no_results' ? 'No inventory matches your search and filters.' : 'No inventory items found.'}
        </p>
        {onEmptyAction && <button onClick={onEmptyAction} style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontWeight: 600 }}>Clear Filters</button>}
      </div>
    );
  }

  return (
    <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="Inventory items">
        <thead>
          <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
            <Th>Inventory ID</Th>
            <Th>Name</Th>
            <Th>SKU</Th>
            <Th>Warehouse</Th>
            <Th>Stock Status</Th>
            <Th>Level</Th>
            <Th>Location</Th>
            <Th>Updated</Th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr
              key={item.id}
              onClick={onRowClick ? () => onRowClick(item) : undefined}
              tabIndex={onRowClick ? 0 : undefined}
              onKeyDown={onRowClick ? (e) => { if (e.key === 'Enter') onRowClick(item); } : undefined}
              style={{ borderTop: '1px solid var(--color-border)', cursor: onRowClick ? 'pointer' : 'default' }}
            >
              <Td>{item.inventoryId}</Td>
              <Td>{item.name}</Td>
              <Td>{item.sku}</Td>
              <Td>{item.warehouse}</Td>
              <Td><StatusBadge status={stockStatusLabel(item.stockStatus)} variant={stockStatusVariant(item.stockStatus)} /></Td>
              <Td>{formatNumber(item.stockLevel)}</Td>
              <Td>{item.location}</Td>
              <Td style={{ color: 'var(--color-text-tertiary)' }}>{item.updatedAt}</Td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});

function Th({ children }: { children: ReactNode }) {
  return <th scope="col" style={{ padding: '12px 16px', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em', whiteSpace: 'nowrap' }}>{children}</th>;
}

function Td({ children, style }: { children: ReactNode; style?: CSSProperties }) {
  return <td style={{ padding: '12px 16px', color: 'var(--color-text-primary)', verticalAlign: 'middle', ...style }}>{children}</td>;
}

