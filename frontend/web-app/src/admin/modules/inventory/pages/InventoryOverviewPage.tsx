import { memo, useMemo } from 'react';
import { useInventoryWorkspace } from '../contexts/InventoryWorkspaceContext';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';
import { useInventoryDashboard, useInventoryItems, useInventoryActivities } from '../hooks/useInventoryMockData';
import { INVENTORY_QUICK_ACTIONS } from '../constants';
import { SectionHeader } from '../components/SectionHeader';
import { StatisticsGrid } from '../components/StatisticsGrid';
import { QuickActionCard } from '../components/QuickActionCard';
import { InventoryTable } from '../components/InventoryTable';
import { RecentActivityCard } from '../components/RecentActivityCard';
import { SkeletonMetricCards } from '../components/InventorySkeleton';

const QUICK_ACTION_SECTION: Record<string, string> = {
  new_item: 'items',
  receive: 'receiving',
  transfer: 'transfers',
  adjust: 'adjustments',
  new_warehouse: 'warehouses',
  validate: 'validation',
};

export const InventoryOverviewPage = memo(function InventoryOverviewPage() {
  const { setActiveSection } = useInventoryWorkspace();
  const { can } = useInventoryPermissions();
  const metricsState = useInventoryDashboard();
  const itemsState = useInventoryItems();
  const activityState = useInventoryActivities();

  const quickActions = useMemo(
    () => INVENTORY_QUICK_ACTIONS.filter((a) => !a.permission || can(a.permission)),
    [can],
  );

  const recent = useMemo(() => (itemsState.data ?? []).slice(0, 8), [itemsState.data]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <SectionHeader
        title="Inventory Overview"
        description="A concise snapshot of your inventory across all warehouses."
        icon="grid"
        sublabel="Inventory"
      />

      {metricsState.loading ? (
        <SkeletonMetricCards count={6} />
      ) : (
        <StatisticsGrid metrics={(metricsState.data?.metrics ?? []).slice(0, 6)} />
      )}

      <section aria-label="Quick actions">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Quick Actions</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: 12 }}>
          {quickActions.map((a) => (
            <QuickActionCard
              key={a.id}
              action={a}
              onClick={() => setActiveSection(QUICK_ACTION_SECTION[a.id] ?? 'overview')}
            />
          ))}
        </div>
      </section>

      <section aria-label="Sample inventory">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Sample Inventory</h3>
        <InventoryTable data={recent} loading={itemsState.loading} emptyKey="no_inventory" />
      </section>

      <RecentActivityCard activities={activityState.data ?? []} loading={activityState.loading} />
    </div>
  );
});
