import { memo, useMemo } from 'react';
import { BarChart } from '../../../../design-system/components/charts/standard/BarChart';
import { PieChart } from '../../../../design-system/components/charts/standard/PieChart';
import { useAnalyticsState } from '../state/useAnalyticsState';
import AnalyticsToolbar from '../components/AnalyticsToolbar';
import WidgetGrid from '../components/WidgetGrid';
import WidgetCard from '../components/WidgetCard';
import KpiCard from '../components/KpiCard';
import ResponsiveChart from '../components/ResponsiveChart';
import AnalyticsTable from '../components/AnalyticsTable';
import { formatNumber, resourceUsage } from '../data/analyticsMockData';

const ResourceAnalyticsPage = memo(function ResourceAnalyticsPage() {
  const { filters, setFilter, resetFilters, activeFilterCount } = useAnalyticsState();

  const resources = useMemo(() => resourceUsage(), []);

  const byType = useMemo(() => {
    const map = new Map<string, number>();
    resources.forEach((r) => map.set(r.type, (map.get(r.type) ?? 0) + r.used));
    return Array.from(map.entries()).map(([label, value]) => ({ label, value }));
  }, [resources]);

  const topResources = useMemo(() => [...resources].sort((a, b) => b.used - a.used).slice(0, 8), [resources]);
  const totalUsed = useMemo(() => resources.reduce((s, r) => s + r.used, 0), [resources]);
  const unusedCount = useMemo(() => resources.filter((r) => r.unused).length, [resources]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <AnalyticsToolbar
        filters={filters}
        onFilterChange={setFilter}
        onClearFilters={resetFilters}
        activeFilterCount={activeFilterCount}
        showCourseFilters={false}
      />

      <WidgetGrid minColWidth={220}>
        <KpiCard title="Resources" value={formatNumber(resources.length)} subtitle="Total assets" />
        <KpiCard title="Total Views" value={formatNumber(totalUsed)} subtitle="Cumulative usage" trend="up" trendValue="+7%" />
        <KpiCard title="Unused" value={formatNumber(unusedCount)} subtitle="Low engagement" trend="down" trendValue="-2" />
        <KpiCard title="Avg / Resource" value={formatNumber(Math.round(totalUsed / Math.max(1, resources.length)))} subtitle="Mean views" />
      </WidgetGrid>

      <WidgetGrid>
        <WidgetCard title="Usage by Type">
          <ResponsiveChart height={280} ariaLabel="Resource usage by type donut chart">
            {({ width, height }) => <PieChart data={byType} variant="donut" showLegend width={width} height={height} />}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Top Resources" subtitle="By views">
          <ResponsiveChart height={280} ariaLabel="Top resources by views bar chart">
            {({ width, height }) => (
              <BarChart
                data={topResources.map((r) => ({ label: r.name, values: [r.used] }))}
                series={[{ name: 'Views' }]}
                variant="horizontal"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Resource Engagement Detail" span={2}>
          <AnalyticsTable
            caption="Resource usage and engagement status"
            rows={resources}
            rowKey={(r) => r.name}
            columns={[
              { key: 'name', header: 'Resource', render: (r) => r.name },
              { key: 'type', header: 'Type', render: (r) => r.type },
              { key: 'used', header: 'Views', align: 'right', render: (r) => formatNumber(r.used) },
              { key: 'status', header: 'Status', render: (r) => (r.unused ? 'Low engagement' : 'Active') },
            ]}
          />
        </WidgetCard>
      </WidgetGrid>
    </div>
  );
});

export default ResourceAnalyticsPage;
