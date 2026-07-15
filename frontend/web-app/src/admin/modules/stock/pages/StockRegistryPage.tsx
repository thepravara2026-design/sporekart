import { memo, useState, useCallback } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { SectionHeader } from '../../inventory/components';
import { StockTable } from '../components/StockTable';
import { useStockData } from '../hooks/useStockData';
import { useStockFilters } from '../hooks/useStockFilters';
import type { StockRecord, StockFilterState } from '../types';

interface Props { defaultFilter?: Partial<StockFilterState>; title?: string; description?: string; }

export const StockRegistryPage = memo(function StockRegistryPage({ defaultFilter, title = 'Stock Registry', description = 'Search, filter, sort and manage all stock records. Every inventory item has a stock record with multiple states.' }: Props) {
  const { filters, setFilter, clearFilters, hasActiveFilters } = useStockFilters(defaultFilter);
  const { records, totalCount, totalPages, page, sortKey, sortDir, toggleSort, searchQuery, setSearchQuery, goToPage } = useStockData(filters);
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());
  const [previewRecord, setPreviewRecord] = useState<StockRecord | null>(null);

  const handleSelect = useCallback((id: string, checked: boolean) => { setSelectedIds((prev) => { const n = new Set(prev); if (checked) n.add(id); else n.delete(id); return n; }); }, []);
  const handleSelectAll = useCallback((checked: boolean) => { setSelectedIds(checked ? new Set(records.map((r) => r.id)) : new Set()); }, [records]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title={title} description={description} />

      <div style={{ display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap' }}>
        <div style={{ flex: 1, minWidth: 200, position: 'relative' }}>
          <Icon name="search" size={14} />
          <input type="search" placeholder="Search by item, SKU, warehouse..." value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)} aria-label="Search stock records"
            style={{ width: '100%', padding: '8px 12px 8px 32px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }} />
        </div>
        {hasActiveFilters && <button onClick={clearFilters} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-danger)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-danger)', fontSize: 'var(--text-caption)' }}><Icon name="x" size={14} /> Clear</button>}
      </div>

      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        <FilterSelect label="Warehouse" value={filters.warehouse} onChange={(v) => setFilter('warehouse', v)} options={['wh-1', 'wh-2', 'wh-3', 'wh-4']} optLabels={['Mumbai', 'Delhi', 'Pune', 'Bengaluru']} />
        <FilterSelect label="Health" value={filters.health} onChange={(v) => setFilter('health', v)} options={['healthy', 'low', 'critical', 'out_of_stock', 'overstock', 'damaged']} />
        <FilterSelect label="Availability" value={filters.availability} onChange={(v) => setFilter('availability', v)} options={['available', 'limited', 'unavailable', 'pre_order', 'backorder']} />
        <FilterSelect label="Status" value={filters.status} onChange={(v) => setFilter('status', v)} options={['active', 'inactive', 'archived']} />
      </div>

      {selectedIds.size > 0 && (
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '10px 16px', background: 'var(--color-primary-alpha)', borderRadius: 'var(--radius-md)', flexWrap: 'wrap' }}>
          <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{selectedIds.size} selected</span>
          <BulkBtn><Icon name="edit" size={14} /> Update Status</BulkBtn>
          <BulkBtn><Icon name="check-circle" size={14} /> Validate</BulkBtn>
          <BulkBtn><Icon name="archive" size={14} /> Archive</BulkBtn>
          <BulkBtn onClick={() => setSelectedIds(new Set())}><Icon name="x" size={14} /> Clear</BulkBtn>
        </div>
      )}

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 8 }}>
        <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{totalCount} records · Page {page} of {totalPages}</span>
        <div style={{ display: 'flex', gap: 4 }}>
          <button disabled={page <= 1} onClick={() => goToPage(page - 1)} style={pageBtn}><Icon name="chevron-left" size={14} /></button>
          <button disabled={page >= totalPages} onClick={() => goToPage(page + 1)} style={pageBtn}><Icon name="chevron-right" size={14} /></button>
        </div>
      </div>

      <StockTable data={records} sortKey={sortKey} sortDir={sortDir} onSort={toggleSort} onRowClick={(r) => setPreviewRecord(r)} selectedIds={selectedIds} onSelect={handleSelect} onSelectAll={handleSelectAll} emptyKey={searchQuery || hasActiveFilters ? 'no_results' : 'no_stock'} onEmptyAction={clearFilters} />

      {previewRecord && (
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 24, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>{previewRecord.inventoryItemName}</h3>
            <button onClick={() => setPreviewRecord(null)} style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 4, color: 'var(--color-text-tertiary)' }}><Icon name="x" size={18} /></button>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 12, fontSize: 'var(--text-body)' }}>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Stock ID:</span> {previewRecord.code}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>SKU:</span> {previewRecord.sku}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Warehouse:</span> {previewRecord.warehouseName}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Available:</span> {(previewRecord.quantities.available ?? 0).toLocaleString()}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Reserved:</span> {(previewRecord.quantities.reserved ?? 0).toLocaleString()}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Incoming:</span> {(previewRecord.quantities.incoming ?? 0).toLocaleString()}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Health:</span> {previewRecord.health}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Availability:</span> {previewRecord.availability}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Health Score:</span> {previewRecord.healthScore}%</div>
          </div>
        </div>
      )}
    </div>
  );
});

function FilterSelect({ label, value, onChange, options, optLabels }: { label: string; value: string[]; onChange: (v: string[]) => void; options: string[]; optLabels?: string[] }) {
  const active = value.length > 0;
  return (
    <select aria-label={label} value={value[0] ?? ''} onChange={(e) => onChange(e.target.value ? [e.target.value] : [])}
      style={{ padding: '6px 10px', borderRadius: 'var(--radius-md)', border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border)'}`, background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-caption)' }}>
      <option value="">{label}</option>
      {options.map((o, i) => <option key={o} value={o}>{optLabels?.[i] ?? o.replace(/_/g, ' ')}</option>)}
    </select>
  );
}

function BulkBtn({ children, onClick }: { children: React.ReactNode; onClick?: () => void }) {
  return <button onClick={onClick} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>{children}</button>;
}

const pageBtn: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 32, height: 32, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)' };



