import { memo, useState } from 'react';
import { SectionHeader, StatisticsGrid, SummaryCard } from '../../inventory/components';
import { StockHealthBadge } from '../components/StockHealthBadge';
import { STOCK_HEALTH_METRICS, STOCK_MOCK_METRICS, STOCK_HEALTH_LEVELS } from '../constants';
import { getStockRecords } from '../services/stockMockService';
import { LOW_STOCK_THRESHOLDS } from '../constants';

export const StockHealthPage = memo(function StockHealthPage() {
  const [records] = useState(() => getStockRecords());

  const healthCounts = STOCK_HEALTH_LEVELS.map((level) => ({
    ...level,
    count: records.filter((r) => r.health === level.value).length,
  }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Stock Health Dashboard" description="Comprehensive health assessment across all stock records. Health is determined by available quantities relative to configured thresholds." />

      <StatisticsGrid metrics={STOCK_MOCK_METRICS.slice(0, 6)} health={STOCK_HEALTH_METRICS} />

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Health Distribution</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 }}>
          {healthCounts.map((h) => (
            <SummaryCard key={h.value} title={h.label}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{h.count}</span>
                <StockHealthBadge health={h.value} />
              </div>
            </SummaryCard>
          ))}
        </div>
      </div>

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Threshold Configuration</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 }}>
          <SummaryCard title="Low Stock Threshold"><span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{LOW_STOCK_THRESHOLDS.lowThreshold}</span></SummaryCard>
          <SummaryCard title="Critical Threshold"><span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{LOW_STOCK_THRESHOLDS.criticalThreshold}</span></SummaryCard>
          <SummaryCard title="Overstock Threshold"><span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{LOW_STOCK_THRESHOLDS.overstockThreshold}</span></SummaryCard>
          <SummaryCard title="Safety Stock"><span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>Placeholder</span></SummaryCard>
          <SummaryCard title="Min Stock"><span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>Placeholder</span></SummaryCard>
          <SummaryCard title="Max Stock"><span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>Placeholder</span></SummaryCard>
        </div>
      </div>
    </div>
  );
});
