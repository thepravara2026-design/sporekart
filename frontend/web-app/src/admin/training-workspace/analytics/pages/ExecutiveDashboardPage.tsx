import { memo, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { LineChart } from '../../../../design-system/components/charts/standard/LineChart';
import { BarChart } from '../../../../design-system/components/charts/standard/BarChart';
import { PieChart } from '../../../../design-system/components/charts/standard/PieChart';
import { CircularKPI } from '../../../../design-system/components/charts/standard/CircularKPI';
import { Icon } from '../../../../design-system/icons/Icon';
import { useAnalyticsState } from '../state/useAnalyticsState';
import AnalyticsToolbar from '../components/AnalyticsToolbar';
import WidgetGrid from '../components/WidgetGrid';
import WidgetCard from '../components/WidgetCard';
import KpiCard from '../components/KpiCard';
import ResponsiveChart from '../components/ResponsiveChart';
import {
  buildEnrollmentTrend,
  categoryDistribution,
  coursePopularity,
  deliveryDistribution,
  enrollmentFunnel,
  formatCurrency,
  formatNumber,
} from '../data/analyticsMockData';

const ExecutiveDashboardPage = memo(function ExecutiveDashboardPage() {
  const navigate = useNavigate();
  const { filters, setFilter, resetFilters, activeFilterCount, filteredCourses, kpis } = useAnalyticsState();

  const trend = useMemo(() => buildEnrollmentTrend(12), []);
  const catDist = useMemo(() => categoryDistribution(filteredCourses), [filteredCourses]);
  const deliveryDist = useMemo(() => deliveryDistribution(filteredCourses), [filteredCourses]);
  const popularity = useMemo(() => coursePopularity(filteredCourses).slice(0, 6), [filteredCourses]);
  const funnel = useMemo(() => enrollmentFunnel(), []);

  const trendData = trend.map((p) => ({ label: p.label, values: [p.enrollments, p.completions] }));
  const revenueData = trend.map((p) => ({ label: p.label, values: [p.revenue] }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <AnalyticsToolbar
        filters={filters}
        onFilterChange={setFilter}
        onClearFilters={resetFilters}
        activeFilterCount={activeFilterCount}
      />

      <section aria-label="Key performance indicators">
        <WidgetGrid minColWidth={220}>
          <KpiCard
            title="Total Courses"
            value={formatNumber(kpis.totalCourses)}
            subtitle={`${kpis.publishedCourses} published`}
            icon={<Icon name="book" size={18} />}
            trend="up"
            trendValue="+4.2%"
            onClick={() => navigate('/admin/training/lms-analytics/course')}
          />
          <KpiCard
            title="Active Enrollments"
            value={formatNumber(kpis.approvedEnrollments)}
            subtitle={`${formatNumber(kpis.pendingEnrollments)} pending`}
            icon={<Icon name="user-check" size={18} />}
            trend="up"
            trendValue="+8.1%"
            onClick={() => navigate('/admin/training/lms-analytics/enrollment')}
          />
          <KpiCard
            title="Revenue (mock)"
            value={formatCurrency(kpis.revenuePlaceholder)}
            subtitle="Placeholder metric"
            icon={<Icon name="tag" size={18} />}
            trend="up"
            trendValue="+5.6%"
          />
          <KpiCard
            title="Training Capacity"
            value={formatNumber(kpis.trainingCapacity)}
            subtitle={`${kpis.upcomingTrainings} upcoming`}
            icon={<Icon name="sliders" size={18} />}
            trend="flat"
            trendValue="0.0%"
          />
          <KpiCard
            title="Certificates (mock)"
            value={formatNumber(kpis.certificatesPlaceholder)}
            subtitle="Completed trainings"
            icon={<Icon name="star" size={18} />}
            trend="up"
            trendValue="+3.3%"
          />
          <KpiCard
            title="Learning Hours (mock)"
            value={formatNumber(kpis.learningHoursPlaceholder)}
            subtitle="Cumulative"
            icon={<Icon name="file" size={18} />}
            trend="up"
            trendValue="+6.7%"
          />
        </WidgetGrid>
      </section>

      <WidgetGrid>
        <WidgetCard title="Enrollment vs Completion" subtitle="Last 12 months">
          <ResponsiveChart height={280} ariaLabel="Enrollment versus completion trend line chart">
            {({ width, height }) => (
              <LineChart
                data={trendData}
                series={[{ name: 'Enrollments' }, { name: 'Completions' }]}
                variant="smooth"
                showArea
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>

        <WidgetCard title="Revenue Trend (mock)" subtitle="Last 12 months">
          <ResponsiveChart height={280} ariaLabel="Revenue trend line chart">
            {({ width, height }) => (
              <LineChart
                data={revenueData}
                series={[{ name: 'Revenue' }]}
                variant="smooth"
                showArea
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>

        <WidgetCard title="Courses by Category" subtitle="Distribution">
          <ResponsiveChart height={280} ariaLabel="Courses by category donut chart">
            {({ width, height }) => (
              <PieChart data={catDist} variant="donut" showLegend width={width} height={height} />
            )}
          </ResponsiveChart>
        </WidgetCard>

        <WidgetCard title="Delivery Modes" subtitle="Distribution">
          <ResponsiveChart height={280} ariaLabel="Delivery mode distribution pie chart">
            {({ width, height }) => (
              <PieChart data={deliveryDist} variant="pie" showLegend width={width} height={height} />
            )}
          </ResponsiveChart>
        </WidgetCard>

        <WidgetCard title="Top Courses" subtitle="By enrollment">
          <ResponsiveChart height={280} ariaLabel="Top courses by enrollment bar chart">
            {({ width, height }) => (
              <BarChart
                data={popularity.map((p) => ({ label: p.label, values: [p.value] }))}
                series={[{ name: 'Enrollments' }]}
                variant="horizontal"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>

        <WidgetCard title="Enrollment Funnel" subtitle="Visit to completion">
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

        <WidgetCard title="Completion Rate" subtitle="Mock KPI">
          <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-2)' }}>
            <CircularKPI value="62%" label="Completion" progress={62} trend="up" trendValue="+2.1%" />
          </div>
        </WidgetCard>

        <WidgetCard title="Capacity Utilization" subtitle="Mock KPI">
          <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-2)' }}>
            <CircularKPI value="74%" label="Utilization" progress={74} trend="flat" trendValue="0.0%" />
          </div>
        </WidgetCard>
      </WidgetGrid>
    </div>
  );
});

export default ExecutiveDashboardPage;
