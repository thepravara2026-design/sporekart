import { memo, useMemo } from 'react';
import { WAREHOUSE_QUICK_ACTIONS } from '../constants';
import { useWarehouseWorkspace } from '../contexts/WarehouseWorkspaceContext';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { useWarehouseDashboard, useWarehouses, useWarehouseActivities } from '../hooks/useWarehouseData';
import { SectionHeader } from '../components';
import { StatisticsGrid } from '../components';
import { QuickActionCard } from '../components';
import { WarehouseTable } from '../components';
import { RecentActivityCard } from '../components';
import { WorkspaceBanner } from '../components';
import { SkeletonMetricCards } from '../components';

const QUICK_ACTION_SECTION: Record<string, string> = {
  new_warehouse: 'warehouses',
  new_zone: 'zones',
  receive: 'operations',
  transfer: 'virtual_warehouses',
  adjust: 'capacity',
  validate: 'audit',
};

export const WarehouseWorkspace = memo(function WarehouseWorkspace() {
  const { setActiveSection } = useWarehouseWorkspace();
  const { can } = useWarehousePermissions();
  const metricsState = useWarehouseDashboard();
  const warehousesState = useWarehouses();
  const activityState = useWarehouseActivities();

  const visibleActions = useMemo(
    () => WAREHOUSE_QUICK_ACTIONS.filter((a) => !a.permission || can(a.permission)),
    [can],
  );
  const topWarehouses = useMemo(() => (warehousesState.data ?? []).slice(0, 6), [warehousesState.data]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <SectionHeader title="Warehouse Workspace" description="Manage warehouses, storage hierarchy, zones and operations." icon="grid" sublabel="Warehouses / Overview" />
      <WorkspaceBanner variant="info" title="Warehouse workspace hub" message="Sections are extensible placeholders for upcoming sprints." />

      {metricsState.loading ? <SkeletonMetricCards count={6} /> : (
        <StatisticsGrid metrics={(metricsState.data?.metrics ?? []).slice(0, 6)} />
      )}

      <section aria-label="Quick actions">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Quick Actions</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: 12 }}>
          {visibleActions.map((a) => (
            <QuickActionCard key={a.id} action={a} onClick={() => setActiveSection(QUICK_ACTION_SECTION[a.id] ?? 'overview')} />
          ))}
        </div>
      </section>

      <section aria-label="Top warehouses">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Top Warehouses</h3>
                <WarehouseTable data={topWarehouses} loading={warehousesState.loading} onRowClick={() => setActiveSection('profile')} />
      </section>

      <RecentActivityCard activities={activityState.data ?? []} loading={activityState.loading} />
    </div>
  );
});


