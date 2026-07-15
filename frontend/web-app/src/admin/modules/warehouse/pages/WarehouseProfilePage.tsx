import { memo, useMemo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { useWarehouseWorkspace } from '../contexts/WarehouseWorkspaceContext';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { useWarehouses, useWarehouseZones, useWarehouseStorageNodes } from '../hooks/useWarehouseData';
import { WAREHOUSE_TYPES, TEMPERATURE_TYPES, WAREHOUSE_STATUSES } from '../constants';
import { SectionHeader } from '../components';
import { SummaryCard } from '../components';
import { MetricCard } from '../components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { StorageHierarchy } from '../components';
import { EmptyState } from '../components';
import { SkeletonTable } from '../components';

export const WarehouseProfilePage = memo(function WarehouseProfilePage() {
  const { setActiveSection } = useWarehouseWorkspace();
  const { can } = useWarehousePermissions();
  const warehousesState = useWarehouses();
  const zonesState = useWarehouseZones();
  const storageState = useWarehouseStorageNodes();
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const warehouses = warehousesState.data ?? [];
  const warehouse = useMemo(() => warehouses.find((w) => w.id === selectedId) ?? warehouses[0], [warehouses, selectedId]);

  if (warehousesState.loading) return <SkeletonTable rows={4} />;
  if (!warehouse) return <EmptyState stateKey="no_warehouses" onAction={can('create') ? () => setActiveSection('warehouses') : undefined} />;

  const zoneCount = (zonesState.data ?? []).filter((z) => z.warehouseId === warehouse.id).length;
  const nodeCount = (storageState.data ?? []).length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <SectionHeader title={warehouse.name} description={`${warehouse.code} · ${warehouse.location}`} icon="home" sublabel="Warehouses / Profile" />

      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center' }}>
        <select aria-label="Select warehouse" value={warehouse.id} onChange={(e) => setSelectedId(e.target.value)} style={{ padding: '8px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
          {warehouses.map((w) => <option key={w.id} value={w.id}>{w.name}</option>)}
        </select>
        <StatusBadge status={warehouse.status} variant={warehouse.status === 'active' ? 'success' : warehouse.status === 'maintenance' ? 'warning' : 'neutral'} />
        <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{WAREHOUSE_TYPES.find((t) => t.value === warehouse.type)?.label}</span>
        <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{TEMPERATURE_TYPES.find((t) => t.value === warehouse.temperatureType)?.label}</span>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: 12 }}>
        <MetricCard metric={{ id: 'util', label: 'Utilization', value: `${warehouse.utilization}%`, trend: 'neutral', icon: 'activity' }} />
        <MetricCard metric={{ id: 'zones', label: 'Zones', value: String(zoneCount), trend: 'neutral', icon: 'grid' }} />
        <MetricCard metric={{ id: 'nodes', label: 'Storage Nodes', value: String(nodeCount), trend: 'neutral', icon: 'package' }} />
        <MetricCard metric={{ id: 'status', label: 'Status', value: WAREHOUSE_STATUSES.find((s) => s.value === warehouse.status)?.label ?? warehouse.status, trend: 'neutral', icon: 'check-circle' }} />
      </div>

      <SummaryCard title="Storage Hierarchy" description={`Physical hierarchy for ${warehouse.name}`} icon="git-branch">
        <StorageHierarchy nodes={storageState.data ?? []} loading={storageState.loading} />
      </SummaryCard>

      {can('edit') && (
        <div style={{ display: 'flex', gap: 8 }}>
          <button style={{ ...primaryBtn }} onClick={() => setActiveSection('settings')}><Icon name="edit" size={14} /> Edit Warehouse</button>
        </div>
      )}
    </div>
  );
});

const primaryBtn: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', border: 'none', background: 'var(--color-primary)', color: 'var(--color-primary-contrast)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 600 };


