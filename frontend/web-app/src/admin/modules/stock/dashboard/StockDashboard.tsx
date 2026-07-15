import { memo } from 'react';
import { StatisticsGrid, SectionHeader, QuickActionCard, RecentActivityCard, WorkspaceBanner } from '../../inventory/components';
import { STOCK_MOCK_METRICS, STOCK_HEALTH_METRICS, STOCK_STATUS_COUNTS, STOCK_RECENT_ACTIVITIES, STOCK_QUICK_ACTIONS } from '../constants';
import { StockSummaryCards } from '../components/StockSummaryCards';
import { getStockRecords } from '../services/stockMockService';

export const StockDashboard = memo(function StockDashboard() {
  const records = getStockRecords();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <WorkspaceBanner variant="success" title="Stock Engine Operational" message="All inventory items now maintain comprehensive stock states. The Stock Management Engine is ready for warehouse, order, procurement and manufacturing operations." />

      <SectionHeader title="Executive Metrics" description="Key stock performance indicators" />
      <StatisticsGrid metrics={STOCK_MOCK_METRICS} health={STOCK_HEALTH_METRICS} statusCounts={STOCK_STATUS_COUNTS} />

      <SectionHeader title="Stock Distribution" description="Current stock quantities by state" />
      <StockSummaryCards records={records} />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div>
          <SectionHeader title="Quick Actions" description="Common stock operations" />
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {STOCK_QUICK_ACTIONS.slice(0, 4).map((q) => (
              <QuickActionCard key={q.id} action={q as import('../../inventory/types').QuickAction} />
            ))}
          </div>
        </div>
        <div>
          <SectionHeader title="Recent Activity" description="Latest stock events" />
          <RecentActivityCard activities={STOCK_RECENT_ACTIVITIES} />
        </div>
      </div>
    </div>
  );
});
