import { memo, useState, useCallback } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { SectionHeader } from '../../inventory/components';
import { InventoryItemRegistryTable } from '../components/InventoryItemRegistryTable';
import { useInventoryItemData } from '../hooks/useInventoryItemData';
import { useInventoryItemFilters } from '../hooks/useInventoryItemFilters';
import type { InventoryItemRecord } from '../types';

export const InventoryItemsRegistryPage = memo(function InventoryItemsRegistryPage() {
  const { filters, setFilter, clearFilters, hasActiveFilters } = useInventoryItemFilters();
  const { items, totalCount, totalPages, page, sortKey, sortDir, toggleSort, searchQuery, setSearchQuery, goToPage } = useInventoryItemData(filters);
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());
  const [previewItem, setPreviewItem] = useState<InventoryItemRecord | null>(null);

  const handleSelect = useCallback((id: string, checked: boolean) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (checked) next.add(id); else next.delete(id);
      return next;
    });
  }, []);

  const handleSelectAll = useCallback((checked: boolean) => {
    if (checked) {
      setSelectedIds(new Set(items.map((i) => i.id)));
    } else {
      setSelectedIds(new Set());
    }
  }, [items]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Inventory Items Registry" description="Search, filter, sort and manage all inventory items derived from products, variants and SKUs." />

      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <div style={{ display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap' }}>
          <div style={{ flex: 1, minWidth: 200, position: 'relative' }}>
            <Icon name="search" size={14} />
            <input
              type="search"
              placeholder="Search by name, code, SKU, product..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              aria-label="Search inventory items"
              style={{ width: '100%', padding: '8px 12px 8px 32px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}
            />
          </div>
          {hasActiveFilters && (
            <button onClick={clearFilters} style={filterBtn}>
              <Icon name="x" size={14} /> Clear Filters ({Object.entries(filters).filter(([, v]) => Array.isArray(v) && v.length > 0).length})
            </button>
          )}
        </div>

        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
          <FilterSelect label="Status" value={filters.status} onChange={(v) => setFilter('status', v)} options={['active', 'inactive', 'draft', 'archived', 'verified', 'pending']} />
          <FilterSelect label="Classification" value={filters.classification} onChange={(v) => setFilter('classification', v)} options={['raw_material', 'work_in_progress', 'finished_good', 'consumable', 'asset', 'packaging', 'supplies']} />
          <FilterSelect label="Lifecycle" value={filters.lifecycle} onChange={(v) => setFilter('lifecycle', v)} options={['draft', 'pending_approval', 'approved', 'active', 'frozen', 'suspended', 'discontinued', 'archived']} />
          <FilterSelect label="Category" value={filters.category} onChange={(v) => setFilter('category', v)} options={['spawn', 'compost', 'supplements', 'tools', 'packaging', 'consumables']} />
          <FilterSelect label="Brand" value={filters.brand} onChange={(v) => setFilter('brand', v)} options={['SporeKart', 'AgriGrow', 'GreenLine', 'Mycelium Pro']} />
          <FilterSelect label="Grade" value={filters.grade} onChange={(v) => setFilter('grade', v)} options={['a_plus', 'a', 'b', 'c', 'unclassified']} />
        </div>
      </div>

      {selectedIds.size > 0 && (
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '10px 16px', background: 'var(--color-primary-alpha)', borderRadius: 'var(--radius-md)', flexWrap: 'wrap' }}>
          <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{selectedIds.size} selected</span>
          <button style={bulkBtn}><Icon name="bookmark" size={14} /> Classify</button>
          <button style={bulkBtn}><Icon name="activity" size={14} /> Update Lifecycle</button>
          <button style={bulkBtn}><Icon name="file-text" size={14} /> Export</button>
          <button style={bulkBtn} onClick={() => setSelectedIds(new Set())}><Icon name="x" size={14} /> Clear</button>
        </div>
      )}

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 8 }}>
        <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{totalCount} items · Page {page} of {totalPages}</span>
        <div style={{ display: 'flex', gap: 4 }}>
          <button disabled={page <= 1} onClick={() => goToPage(page - 1)} style={pageBtn}><Icon name="chevron-left" size={14} /></button>
          <button disabled={page >= totalPages} onClick={() => goToPage(page + 1)} style={pageBtn}><Icon name="chevron-right" size={14} /></button>
        </div>
      </div>

      <InventoryItemRegistryTable
        data={items}
        sortKey={sortKey}
        sortDir={sortDir}
        onSort={toggleSort}
        onRowClick={(item) => setPreviewItem(item)}
        selectedIds={selectedIds}
        onSelect={handleSelect}
        onSelectAll={handleSelectAll}
        emptyKey={searchQuery || hasActiveFilters ? 'no_results' : 'no_items'}
        onEmptyAction={clearFilters}
      />

      {previewItem && (
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 24, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 16 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>{previewItem.name}</h3>
            <button onClick={() => setPreviewItem(null)} style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 4, color: 'var(--color-text-tertiary)' }}><Icon name="x" size={18} /></button>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 12, fontSize: 'var(--text-body)' }}>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Code:</span> {previewItem.code}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Product:</span> {previewItem.productName}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Variant:</span> {previewItem.variantName}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>SKU:</span> {previewItem.sku}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Category:</span> {previewItem.category}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Brand:</span> {previewItem.brand}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Classification:</span> {previewItem.classification.type.replace(/_/g, ' ')} / {previewItem.classification.grade.replace(/_/g, ' ')}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Lifecycle:</span> {previewItem.lifecycle.replace(/_/g, ' ')}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Status:</span> {previewItem.status}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Unit:</span> {previewItem.unit}</div>
          </div>
        </div>
      )}
    </div>
  );
});

function FilterSelect({ label, value, onChange, options }: { label: string; value: string[]; onChange: (v: string[]) => void; options: string[] }) {
  const active = value.length > 0;
  return (
    <select
      aria-label={label}
      value={value[0] ?? ''}
      onChange={(e) => onChange(e.target.value ? [e.target.value] : [])}
      style={{ padding: '6px 10px', borderRadius: 'var(--radius-md)', border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border)'}`, background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-caption)' }}
    >
      <option value="">{label}</option>
      {options.map((o) => <option key={o} value={o}>{o.replace(/_/g, ' ')}</option>)}
    </select>
  );
}

const bulkBtn: React.CSSProperties = {
  display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer',
  color: 'var(--color-text-primary)', fontSize: 'var(--text-body)',
};

const pageBtn: React.CSSProperties = {
  display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 32, height: 32,
  borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)',
  cursor: 'pointer', color: 'var(--color-text-primary)',
};

const filterBtn: React.CSSProperties = {
  display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-danger)', background: 'var(--color-surface)', cursor: 'pointer',
  color: 'var(--color-danger)', fontSize: 'var(--text-caption)',
};
