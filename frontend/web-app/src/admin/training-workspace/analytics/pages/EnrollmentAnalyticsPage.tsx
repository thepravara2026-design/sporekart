import { memo, useMemo } from 'react';
import { LineChart } from '../../../../design-system/components/charts/standard/LineChart';
import { BarChart } from '../../../../design-system/components/charts/standard/BarChart';
import { CircularKPI } from '../../../../design-system/components/charts/standard/CircularKPI';
import { useAnalyticsState } from '../state/useAnalyticsState';
import AnalyticsToolbar from '../components/AnalyticsToolbar';
import WidgetGrid from '../components/WidgetGrid';
import WidgetCard from '../components/WidgetCard';
import KpiCard from '../components/KpiCard';
import ResponsiveChart from '../components/ResponsiveChart';
import {
  buildDailyEnrollments,
  buildEnrollmentTrend,
  capacityUtilization,
  enrollmentFunnel,
  formatNumber,
} from '../data/analyticsMockData';

const EnrollmentAnalyticsPage = memo(function EnrollmentAnalyticsPage() {
  const { filters, setFilter, resetFilters, activeFilterCount, filteredCourses, kpis } = useAnalyticsState();

  const daily = useMemo(() => buildDailyEnrollments(30), []);
  const trend = useMemo(() => buildEnrollmentTrend(12, 5), []);
  const funnel = useMemo(() => enrollmentFunnel(), []);
  const capacity = useMemo(() => capacityUtilization(filteredCourses), [filteredCourses]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <AnalyticsToolbar
        filters={filters}
        onFilterChange={setFilter}
        onClearFilters={resetFilters}
        activeFilterCount={activeFilterCount}
        showTrainerFilter
      />

      <WidgetGrid minColWidth={220}>
        <KpiCard title="Requests" value={formatNumber(kpis.enrollmentRequests)} subtitle="All enrollment requests" trend="up" trendValue="+9%" />
        <KpiCard title="Approved" value={formatNumber(kpis.approvedEnrollments)} subtitle="Active learners" trend="up" trendValue="+8%" />
        <KpiCard title="Pending" value={formatNumber(kpis.pendingEnrollments)} subtitle="Awaiting review" trend="flat" trendValue="0%" />
        <KpiCard title="Completed" value={formatNumber(kpis.completedTrainings)} subtitle="Finished trainings" trend="up" trendValue="+4%" />
      </WidgetGrid>

      <WidgetGrid>
        <WidgetCard title="Daily Enrollments" subtitle="Last 30 days">
          <ResponsiveChart height={280} ariaLabel="Daily enrollments line chart">
            {({ width, height }) => (
              <LineChart
                data={daily.map((d) => ({ label: d.label, values: [d.value] }))}
                series={[{ name: 'Enrollments' }]}
                variant="smooth"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Monthly Trend" subtitle="Enrollment vs completion">
          <ResponsiveChart height={280} ariaLabel="Monthly enrollment versus completion line chart">
            {({ width, height }) => (
              <LineChart
                data={trend.map((p) => ({ label: p.label, values: [p.enrollments, p.completions] }))}
                series={[{ name: 'Enrollments' }, { name: 'Completions' }]}
                variant="smooth"
                showArea
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Enrollment Funnel">
          <ResponsiveChart height={280} ariaLabel="Enrollment funnel bar chart">
            {({ width, height }) => (
              <BarChart
                data={funnel.map((f) => ({ label: f.label, values: [f.value] }))}
                series={[{ name: 'Learners' }]}
                variant="vertical"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Capacity Utilization" subtitle="By course code">
          <ResponsiveChart height={280} ariaLabel="Capacity utilization bar chart">
            {({ width, height }) => (
              <BarChart
                data={capacity.map((c) => ({ label: c.label, values: [c.value] }))}
                series={[{ name: 'Utilization %' }]}
                variant="horizontal"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Approval Rate" subtitle="Mock KPI">
          <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-2)' }}>
            <CircularKPI value="71%" label="Approved" progress={71} trend="up" trendValue="+1.4%" />
          </div>
        </WidgetCard>
        <WidgetCard title="Completion Rate" subtitle="Mock KPI">
          <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-2)' }}>
            <CircularKPI value="62%" label="Completed" progress={62} trend="up" trendValue="+2.1%" />
          </div>
        </WidgetCard>
      </WidgetGrid>
    </div>
  );
});

export default EnrollmentAnalyticsPage;
