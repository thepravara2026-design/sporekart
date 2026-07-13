function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { MetricTile } from '../../components/charts/kpi/MetricTile';
import { TrendIndicator } from '../../components/charts/kpi/TrendIndicator';
import { GrowthIndicator } from '../../components/charts/kpi/GrowthIndicator';
import { PercentageChange } from '../../components/charts/kpi/PercentageChange';
import { ComparisonMetric } from '../../components/charts/kpi/ComparisonMetric';
import { TargetProgress } from '../../components/charts/kpi/TargetProgress';
import { SummaryBlock } from '../../components/charts/kpi/SummaryBlock';
import { StatisticGrid } from '../../components/charts/kpi/StatisticGrid';
import { NumberFormatter } from '../../components/charts/kpi/NumberFormatter';
import { CurrencyFormatter } from '../../components/charts/kpi/CurrencyFormatter';
import { PercentageFormatter } from '../../components/charts/kpi/PercentageFormatter';

export default function KPIsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>KPIs & Metrics</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Metric tiles, indicators, comparisons, and formatters</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>MetricTile</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '16px' }}>
          <MetricTile title="Total Revenue" value="$284,500" subtitle="Last 30 days" trend="up" trendValue="+12.5%" size="lg" />
          <MetricTile title="Active Users" value="12,847" subtitle="Daily average" trend="up" trendValue="+8.3%" size="md" color="var(--color-data-viz-3)" />
          <MetricTile title="Bounce Rate" value="32.1%" subtitle="Current month" trend="down" trendValue="-2.4%" size="sm" color="var(--color-danger-500)" />
          <MetricTile title="Avg. Session" value="4m 32s" subtitle="Per visit" trend="flat" size="md" color="var(--color-data-viz-4)" />
          <MetricTile title="Loading Example" value="-" loading />
          <MetricTile title="With Click" value="Click Me" subtitle="onClick demo" onClick={() => alert('Metric clicked!')} size="sm" />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Trend / Growth / Change Indicators</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="TrendIndicator">
            <TrendIndicator direction="up" value="+15.2%" />
            <TrendIndicator direction="down" value="-8.7%" size="sm" />
            <TrendIndicator direction="flat" value="0.0%" />
          </StateCard>
          <StateCard label="GrowthIndicator">
            <GrowthIndicator value={12.5} suffix="%" size="lg" />
            <GrowthIndicator value={-8.3} suffix="%" size="md" />
            <GrowthIndicator value={0} suffix="%" size="sm" showIcon={false} />
          </StateCard>
          <StateCard label="PercentageChange">
            <PercentageChange current={1240} previous={1100} precision={1} size="md" showLabel />
            <PercentageChange current={850} previous={920} size="sm" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>ComparisonMetric</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '16px' }}>
          <ComparisonMetric
            label="Revenue Comparison"
            current={{ label: 'This Year', value: '$2.4M' }}
            previous={{ label: 'Last Year', value: '$1.8M' }}
            change={33.3}
          />
          <ComparisonMetric
            label="Customer Growth"
            current={{ label: 'Q2 2026', value: '8,240' }}
            previous={{ label: 'Q1 2026', value: '7,100' }}
          />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>TargetProgress</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Bar variants">
            <TargetProgress current={750} target={1000} label="Revenue Target" size="lg" />
            <TargetProgress current={340} target={500} label="User Signups" size="md" color="var(--color-data-viz-3)" />
            <TargetProgress current={120} target={600} label="Course Completions" size="sm" />
          </StateCard>
          <StateCard label="Circle variants">
            <TargetProgress current={820} target={1000} variant="circle" size="lg" label="Revenue" />
            <TargetProgress current={45} target={200} variant="circle" size="md" color="var(--color-danger-500)" label="Goals" />
            <TargetProgress current={160} target={200} variant="circle" size="sm" label="Tasks" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>SummaryBlock & StatisticGrid</h2>
        <SummaryBlock
          title="This Month Overview"
          metrics={[
            { label: 'Revenue', value: '$84.2K', trend: 'up', trendValue: '+12%' },
            { label: 'Orders', value: '1,847', trend: 'up', trendValue: '+8%' },
            { label: 'Conversion', value: '3.2%', trend: 'down', trendValue: '-0.4%', color: 'var(--color-danger-500)' },
          ]}
          columns={3}
        />
        <div style={{ marginTop: '8px' }}>
          <StatisticGrid
            columns={4}
            items={[
              { title: 'Page Views', value: '1.2M', subtitle: '+18% vs last month', trend: 'up', trendValue: '18%' },
              { title: 'Avg. Time', value: '3m 12s', subtitle: 'Per session', trend: 'flat' },
              { title: 'Bounce Rate', value: '28.4%', subtitle: '-2.1% improvement', trend: 'down', trendValue: '2.1%' },
              { title: 'CTR', value: '4.7%', subtitle: 'Click-through rate', trend: 'up', trendValue: '+0.8%' },
            ]}
          />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Formatters</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="NumberFormatter">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
              <NumberFormatter value={1234567} />
              <NumberFormatter value={12345} />
              <NumberFormatter value={123} />
            </div>
          </StateCard>
          <StateCard label="CurrencyFormatter">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
              <CurrencyFormatter value={284500} currency="USD" />
              <CurrencyFormatter value={12500} currency="EUR" />
              <CurrencyFormatter value={999} currency="GBP" />
            </div>
          </StateCard>
          <StateCard label="PercentageFormatter">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
              <PercentageFormatter value={0.752} />
              <PercentageFormatter value={0.083} />
              <PercentageFormatter value={1.0} />
            </div>
          </StateCard>
        </div>
      </section>
    </div>
  );
}
