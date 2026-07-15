import { memo } from 'react';
import { StatisticsGrid, SectionHeader, SummaryCard, QuickActionCard, RecentActivityCard, WorkspaceBanner } from '../../inventory/components';
import { INVENTORY_ITEM_MOCK_METRICS, INVENTORY_ITEM_HEALTH, INVENTORY_ITEM_STATUS_COUNTS, INVENTORY_ITEM_RECENT_ACTIVITIES, INVENTORY_ITEM_QUICK_ACTIONS } from '../constants';
import type { InventoryItemMetric, HealthMetric, StatusCount, RecentActivity } from '../types';

export const InventoryItemDashboard = memo(function InventoryItemDashboard() {
  const metrics: InventoryItemMetric[] = INVENTORY_ITEM_MOCK_METRICS;
  const health: HealthMetric[] = INVENTORY_ITEM_HEALTH;
  const statusCounts: StatusCount[] = INVENTORY_ITEM_STATUS_COUNTS;
  const recent: RecentActivity[] = INVENTORY_ITEM_RECENT_ACTIVITIES;
  const quick = INVENTORY_ITEM_QUICK_ACTIONS;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <WorkspaceBanner
        variant="info"
        title="Inventory Item Foundation"
        message="Inventory Item foundation is operational. Every Product, Variant and SKU is now tracked as an Inventory Item. Future transactions (stock, warehouse, procurement, sales) will reference these records."
      />

      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <SectionHeader title="Executive Metrics" description="Key inventory item performance indicators" />
        <StatisticsGrid metrics={metrics} health={health} statusCounts={statusCounts} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <SectionHeader title="Health Scores" description="Data quality and completeness" />
          {health.map((h) => (
            <SummaryCard key={h.id} title={h.label}>
              <span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{h.score}%</span>
            </SummaryCard>
          ))}
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <SectionHeader title="Status Breakdown" description="Items by lifecycle status" />
          {statusCounts.map((s) => (
            <SummaryCard key={s.id} title={s.label}>
              <span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{s.count.toLocaleString()}</span>
            </SummaryCard>
          ))}
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <SectionHeader title="Quick Actions" description="Common operations" />
          {quick.slice(0, 4).map((q) => (
            <QuickActionCard key={q.id} action={q as import('../../inventory/types').QuickAction} />
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <SectionHeader title="Recent Activity" description="Latest changes to inventory items" />
        <RecentActivityCard activities={recent} />
      </div>
    </div>
  );
});
