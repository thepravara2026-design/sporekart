import { memo, useMemo } from 'react';
import { inventoryMockService } from '../services/inventoryMockService';
import { useInventoryMockData } from '../hooks/useInventoryMockData';
import { INVENTORY_MOCK_METRICS, INVENTORY_HEALTH, INVENTORY_STATUS_COUNTS } from '../constants';
import { SectionHeader } from '../components/SectionHeader';
import { StatisticsGrid } from '../components/StatisticsGrid';
import { WorkspaceBanner } from '../components/WorkspaceBanner';
import { RecentActivityCard } from '../components/RecentActivityCard';
import { SkeletonDashboard } from '../components/InventorySkeleton';
import { EmptyState } from '../components/EmptyState';
import { SummaryCard } from '../components/SummaryCard';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';

export const InventoryDashboard = memo(function InventoryDashboard() {
  const metricsState = useInventoryMockData(() => inventoryMockService.fetchDashboardMetrics());
  const activityState = useInventoryMockData(() => inventoryMockService.fetchRecentActivities());
  const { can } = useInventoryPermissions();

  const metrics = useMemo(() => metricsState.data?.metrics ?? INVENTORY_MOCK_METRICS, [metricsState.data]);
  const health = useMemo(() => metricsState.data?.health ?? INVENTORY_HEALTH, [metricsState.data]);

  if (metricsState.loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        <SkeletonDashboard />
      </div>
    );
  }

  if (metricsState.error) {
    return <EmptyState stateKey="mock_data_missing" onAction={() => metricsState.reload()} />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <SectionHeader
        title="Inventory Dashboard"
        description="Executive overview of inventory health, stock status, and recent activity."
        icon="bar-chart"
        sublabel="Inventory / Dashboard"
      />
      <WorkspaceBanner variant="success" title="Inventory synced" message="Stock levels reconciled with warehouse systems." />

      <StatisticsGrid metrics={metrics} health={health} statusCounts={INVENTORY_STATUS_COUNTS} />

      {can('reports') && (
        <SummaryCard
          title="Analytics & Reporting"
          description="Advanced analytics are available for roles with reporting access."
          icon="trending-up"
          actions={[{ id: 'view-report', label: 'View Reports', icon: 'file-text' }]}
        >
          Detailed trend analysis, turnover reports, and ABC classification will render here in a future sprint. This placeholder demonstrates the gated analytics region.
        </SummaryCard>
      )}

      <RecentActivityCard activities={activityState.data ?? []} loading={activityState.loading} />
    </div>
  );
});
