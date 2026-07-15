import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { SkeletonTable } from '../../inventory/components';
import type { InventoryItemRecord, InventoryItemSortKey, SortDir } from '../types';
import { getStatusVariant, getLifecycleVariant } from '../utils';

interface Props {
  data: InventoryItemRecord[];
  loading?: boolean;
  sortKey?: InventoryItemSortKey;
  sortDir?: SortDir;
  onSort?: (key: InventoryItemSortKey) => void;
  onRowClick?: (item: InventoryItemRecord) => void;
  selectedIds?: Set<string>;
  onSelect?: (id: string, checked: boolean) => void;
  onSelectAll?: (checked: boolean) => void;
  emptyKey?: 'no_items' | 'no_results';
  onEmptyAction?: () => void;
}

export const InventoryItemRegistryTable = memo(function InventoryItemRegistryTable({
  data, loading, sortKey, sortDir, onSort, onRowClick, selectedIds, onSelect, onSelectAll, emptyKey, onEmptyAction,
}: Props) {
  if (loading) return <SkeletonTable rows={8} />;

  if (data.length === 0) {
    return (
      <div role="status" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 12, padding: '48px 24px', border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', minHeight: 240 }}>
        <Icon name={emptyKey === 'no_results' ? 'search' : 'package'} size={28} />
        <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>
          {emptyKey === 'no_results' ? 'No inventory items match your search and filters.' : 'No inventory items found.'}
        </p>
        {onEmptyAction && <button onClick={onEmptyAction} style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontWeight: 600 }}>Clear Filters</button>}
      </div>
    );
  }

  const allSelected = selectedIds && selectedIds.size === data.length;

  return (
    <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="Inventory items registry">
        <thead>
          <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
            {onSelect && (
              <Th>
                <input type="checkbox" checked={!!allSelected} onChange={(e) => onSelectAll?.(e.target.checked)} aria-label="Select all" style={{ accentColor: 'var(--color-primary)' }} />
              </Th>
            )}
            <SortTh label="Code" sortable keyField="code" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Name" sortable keyField="name" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Product" sortable keyField="productName" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="SKU" sortable keyField="sku" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Category" sortable keyField="category" current={sortKey} dir={sortDir} onSort={onSort} />
            <Th>Classification</Th>
            <SortTh label="Lifecycle" sortable keyField="lifecycle" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Status" sortable keyField="status" current={sortKey} dir={sortDir} onSort={onSort} />
            <SortTh label="Updated" sortable keyField="updatedAt" current={sortKey} dir={sortDir} onSort={onSort} />
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
              {onSelect && (
                <Td>
                  <input type="checkbox" checked={selectedIds?.has(item.id) ?? false} onChange={(e) => onSelect(item.id, e.target.checked)} aria-label={`Select ${item.code}`} style={{ accentColor: 'var(--color-primary)' }} />
                </Td>
              )}
              <Td><span style={{ fontFamily: 'monospace', fontSize: 'var(--text-caption)' }}>{item.code}</span></Td>
              <Td><span style={{ fontWeight: 500 }}>{item.name}</span></Td>
              <Td style={{ color: 'var(--color-text-secondary)' }}>{item.productName}</Td>
              <Td style={{ fontFamily: 'monospace', fontSize: 'var(--text-caption)' }}>{item.sku}</Td>
              <Td><span style={{ background: 'var(--color-surface-hover)', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)' }}>{item.category}</span></Td>
              <Td><span style={{ fontSize: 'var(--text-caption)' }}>{item.classification.type.replace(/_/g, ' ')}</span></Td>
              <Td><LifecycleBadge stage={item.lifecycle} /></Td>
              <Td><StatusBadge status={item.status.replace(/_/g, ' ')} variant={getStatusVariant(item.status)} /></Td>
              <Td style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{item.updatedAt}</Td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});

function LifecycleBadge({ stage }: { stage: string }) {
  const v = getLifecycleVariant(stage);
  const colors: Record<string, CSSProperties> = {
    success: { background: 'var(--color-success-alpha)', color: 'var(--color-success)' },
    warning: { background: 'var(--color-warning-alpha)', color: 'var(--color-warning)' },
    danger: { background: 'var(--color-danger-alpha)', color: 'var(--color-danger)' },
    info: { background: 'var(--color-info-alpha)', color: 'var(--color-info)' },
    neutral: { background: 'var(--color-surface-hover)', color: 'var(--color-text-tertiary)' },
    default: { background: 'var(--color-surface-hover)', color: 'var(--color-text-secondary)' },
  };
  return (
    <span style={{ display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', fontWeight: 500, ...colors[v] }}>
      {stage.replace(/_/g, ' ')}
    </span>
  );
}

function Th({ children, style, ...rest }: { children: React.ReactNode; style?: CSSProperties; [key: string]: unknown }) {
  return <th scope="col" style={{ padding: '12px 16px', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em', whiteSpace: 'nowrap', ...style }} {...rest}>{children}</th>;
}

function SortTh({ label, sortable, keyField, current, dir, onSort }: { label: string; sortable: boolean; keyField: InventoryItemSortKey; current?: InventoryItemSortKey; dir?: SortDir; onSort?: (key: InventoryItemSortKey) => void }) {
  const active = current === keyField;
  return (
    <Th aria-sort={active ? (dir === 'asc' ? 'ascending' : 'descending') : undefined}>
      {sortable && onSort ? (
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
