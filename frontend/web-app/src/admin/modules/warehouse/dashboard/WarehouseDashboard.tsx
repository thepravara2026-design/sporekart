import { memo, useMemo } from 'react';
import { useWarehouseDashboard, useWarehouseActivities } from '../hooks/useWarehouseData';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { WAREHOUSE_MOCK_METRICS, WAREHOUSE_HEALTH, WAREHOUSE_STATUS_COUNTS } from '../constants';
import { SectionHeader } from '../components';
import { StatisticsGrid } from '../components';
import { WorkspaceBanner } from '../components';
import { RecentActivityCard } from '../components';
import { SkeletonDashboard } from '../components';
import { EmptyState } from '../components';
import { SummaryCard } from '../components';

export const WarehouseDashboard = memo(function WarehouseDashboard() {
  const metricsState = useWarehouseDashboard();
  const activityState = useWarehouseActivities();
  const { can } = useWarehousePermissions();

  const metrics = useMemo(() => metricsState.data?.metrics ?? WAREHOUSE_MOCK_METRICS, [metricsState.data]);
  const health = useMemo(() => metricsState.data?.health ?? WAREHOUSE_HEALTH, [metricsState.data]);

  if (metricsState.loading) return <SkeletonDashboard />;
  if (metricsState.error) return <EmptyState stateKey="mock_data_missing" onAction={() => metricsState.reload()} />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <SectionHeader title="Warehouse Dashboard" description="Executive overview of warehouse health, capacity and recent activity." icon="bar-chart" sublabel="Warehouses / Dashboard" />
      <WorkspaceBanner variant="success" title="All systems operational" message="Warehouse data synced with inventory." />

      <StatisticsGrid metrics={metrics} health={health} statusCounts={WAREHOUSE_STATUS_COUNTS} />

      {can('reports') && (
        <SummaryCard
          title="Analytics & Reporting"
          description="Advanced warehouse analytics are available for roles with reporting access."
          icon="trending-up"
          actions={[{ id: 'view-report', label: 'View Reports', icon: 'file-text' }]}
        >
          Turnover, slotting and ABC classification will render here in a future sprint. This placeholder demonstrates the gated analytics region.
        </SummaryCard>
      )}

      <RecentActivityCard activities={activityState.data ?? []} loading={activityState.loading} />
    </div>
  );
});


