import { memo, useMemo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { WAREHOUSE_FILTER_OPTIONS, WAREHOUSE_TYPES, TEMPERATURE_TYPES, WAREHOUSE_STATUSES } from '../constants';
import { useWarehouseWorkspace } from '../contexts/WarehouseWorkspaceContext';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { useWarehouses, useWarehouseMutations } from '../hooks/useWarehouseData';
import { useWarehouseFilters } from '../hooks/useWarehouseFilters';
import { useWarehouseSearch } from '../hooks/useWarehouseSearch';
import { filterWarehouses, sortWarehouses } from '../utils';
import { SectionHeader } from '../components';
import { Toolbar } from '../components';
import { SearchComponent } from '../components';
import { FilterPanel } from '../components';
import { WarehouseTable } from '../components';
import { PermissionActionBar } from '../components';
import { EmptyState } from '../components';
import { SkeletonTable } from '../components';
import type { WarehouseType, TemperatureType, WarehouseStatus, WarehouseSortKey, SortDir } from '../types';

type NewWarehouse = {
  name: string;
  code: string;
  type: WarehouseType;
  temperatureType: TemperatureType;
  status: WarehouseStatus;
  region: string;
  city: string;
  country: string;
};

const DEFAULT_FIELDS = ['name', 'code', 'location', 'status', 'type', 'manager'];

function NewWarehouseModal({ onClose, onSubmit }: { onClose: () => void; onSubmit: (w: NewWarehouse) => void }) {
  const [form, setForm] = useState<NewWarehouse>({
    name: '', code: '', type: 'distribution', temperatureType: 'ambient', status: 'active', region: '', city: '', country: '',
  });
  const set = <K extends keyof NewWarehouse>(k: K, v: NewWarehouse[K]) => setForm((f) => ({ ...f, [k]: v }));
  const valid = form.name && form.code && form.region && form.country;

  return (
    <div role="dialog" aria-modal="true" aria-label="Create warehouse" style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.45)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 50, padding: 16 }}>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 24, width: 'min(520px, 100%)', display: 'flex', flexDirection: 'column', gap: 12 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>New Warehouse</h2>
          <button onClick={onClose} aria-label="Close" style={iconBtn}><Icon name="x" size={18} /></button>
        </div>
        <Field label="Name"><input style={inputStyle} value={form.name} onChange={(e) => set('name', e.target.value)} /></Field>
        <Field label="Code"><input style={inputStyle} value={form.code} onChange={(e) => set('code', e.target.value.toUpperCase())} placeholder="WH-XXX" /></Field>
        <Field label="Type">
          <select style={inputStyle} value={form.type} onChange={(e) => set('type', e.target.value as WarehouseType)}>
            {WAREHOUSE_TYPES.map((t) => <option key={t.value} value={t.value}>{t.label}</option>)}
          </select>
        </Field>
        <Field label="Temperature">
          <select style={inputStyle} value={form.temperatureType} onChange={(e) => set('temperatureType', e.target.value as TemperatureType)}>
            {TEMPERATURE_TYPES.map((t) => <option key={t.value} value={t.value}>{t.label}</option>)}
          </select>
        </Field>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
          <Field label="Region"><input style={inputStyle} value={form.region} onChange={(e) => set('region', e.target.value)} /></Field>
          <Field label="City"><input style={inputStyle} value={form.city} onChange={(e) => set('city', e.target.value)} /></Field>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
          <Field label="Country"><input style={inputStyle} value={form.country} onChange={(e) => set('country', e.target.value)} /></Field>
          <Field label="Status">
            <select style={inputStyle} value={form.status} onChange={(e) => set('status', e.target.value as WarehouseStatus)}>
              {WAREHOUSE_STATUSES.map((s) => <option key={s.value} value={s.value}>{s.label}</option>)}
            </select>
          </Field>
        </div>
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 8, marginTop: 8 }}>
          <button style={ghostBtn} onClick={onClose}>Cancel</button>
          <button style={{ ...primaryBtn, opacity: valid ? 1 : 0.5 }} disabled={!valid} onClick={() => valid && onSubmit(form)}>Create</button>
        </div>
      </div>
    </div>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <label style={{ display: 'flex', flexDirection: 'column', gap: 4, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
      {label}
      {children}
    </label>
  );
}

export const WarehouseDirectoryPage = memo(function WarehouseDirectoryPage() {
  const { searchQuery, setSearchQuery } = useWarehouseWorkspace();
  const { can } = useWarehousePermissions();
  const warehousesState = useWarehouses();
  const warehouses = warehousesState.data ?? [];
  const filters = useWarehouseFilters(warehouses);
  const [activeFields, setActiveFields] = useState<string[]>(DEFAULT_FIELDS);
  const search = useWarehouseSearch(warehouses, activeFields);
  const mutations = useWarehouseMutations();
  const [selected, setSelected] = useState<Set<string>>(new Set());
  const [modalOpen, setModalOpen] = useState(false);
  const [sortKey, setSortKey] = useState<WarehouseSortKey>('name');
  const [sortDir, setSortDir] = useState<SortDir>('asc');

  const filtered = useMemo(
    () => sortWarehouses(filterWarehouses(search.results, filters.state), sortKey, sortDir),
    [search.results, filters.state, sortKey, sortDir],
  );

  const toggleAll = () => setSelected((prev) => (prev.size === filtered.length ? new Set() : new Set(filtered.map((w) => w.id))));
  const toggleRow = (id: string) => setSelected((prev) => { const n = new Set(prev); if (n.has(id)) n.delete(id); else n.add(id); return n; });

  const handleSort = (key: WarehouseSortKey) => {
    if (key === sortKey) setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));
    else { setSortKey(key); setSortDir('asc'); }
  };

  const handleCreate = (w: NewWarehouse) => {
    mutations.createWarehouse(w);
    setModalOpen(false);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <SectionHeader title="Warehouse Directory" description="Browse, search and manage warehouses." icon="list" sublabel="Warehouses / Directory" />

      <Toolbar
        title="Warehouse Directory"
        actions={selected.size > 0 ? (
          <button style={ghostBtn} onClick={() => setSelected(new Set())}>Clear selection ({selected.size})</button>
        ) : undefined}
      />
      <SearchComponent query={searchQuery} onQueryChange={setSearchQuery} activeFields={activeFields} onToggleField={(f) => setActiveFields((prev) => (prev.includes(f) ? prev.filter((x) => x !== f) : [...prev, f]))} />
      <FilterPanel options={WAREHOUSE_FILTER_OPTIONS} state={filters.state} onToggle={(k, v) => filters.toggle(k as keyof typeof filters.state, v)} onClear={filters.clear} activeCount={filters.activeCount} />

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        {can('create') && (
          <button style={primaryBtn} onClick={() => setModalOpen(true)}><Icon name="plus" size={14} /> New Warehouse</button>
        )}
        <div style={{ display: 'inline-flex', alignItems: 'center', gap: 6, marginLeft: 'auto' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Sort</span>
          <select aria-label="Sort field" value={sortKey} onChange={(e) => setSortKey(e.target.value as WarehouseSortKey)} style={selectStyle}>
            <option value="name">Name</option>
            <option value="code">Code</option>
            <option value="capacity">Capacity</option>
            <option value="updatedAt">Updated</option>
          </select>
          <button style={ghostBtn} onClick={() => setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'))} aria-label="Toggle sort direction">
            <Icon name={sortDir === 'asc' ? 'arrow-up' : 'arrow-down'} size={14} />
          </button>
        </div>
      </div>

      {warehousesState.loading ? <SkeletonTable rows={8} /> : filtered.length === 0 ? (
        <EmptyState stateKey="no_warehouses" onAction={can('create') ? () => setModalOpen(true) : undefined} />
      ) : (
        <WarehouseTable
          data={filtered}
          loading={false}
          selectedIds={Array.from(selected)}
          onToggleSelect={toggleRow}
          onToggleSelectAll={toggleAll}
          onSort={handleSort}
          sortKey={sortKey}
          sortDir={sortDir}
        />
      )}

      {selected.size > 0 && (
        <PermissionActionBar
          permissions={{
            canEdit: can('edit'),
            canDelete: can('archive'),
            canExport: can('reports'),
            canArchive: can('archive'),
          }}
          selectedCount={selected.size}
          onAction={(a) => { if (a === 'archive') { selected.forEach((id) => mutations.archiveWarehouse(id)); setSelected(new Set()); } }}
          onClear={() => setSelected(new Set())}
        />
      )}

      {modalOpen && <NewWarehouseModal onClose={() => setModalOpen(false)} onSubmit={handleCreate} />}
    </div>
  );
});

const inputStyle: React.CSSProperties = {
  padding: '8px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
  background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)',
};
const iconBtn: React.CSSProperties = { border: 'none', background: 'transparent', cursor: 'pointer', color: 'var(--color-text-secondary)' };
const ghostBtn: React.CSSProperties = { padding: '8px 14px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', cursor: 'pointer', fontSize: 'var(--text-body)' };
const primaryBtn: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', border: 'none', background: 'var(--color-primary)', color: 'var(--color-primary-contrast)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 600 };
const selectStyle: React.CSSProperties = { padding: '8px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' };


