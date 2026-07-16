import { memo, useMemo } from 'react';
import { LineChart } from '../../../../design-system/components/charts/standard/LineChart';
import { BarChart } from '../../../../design-system/components/charts/standard/BarChart';
import { PieChart } from '../../../../design-system/components/charts/standard/PieChart';
import { useAnalyticsState } from '../state/useAnalyticsState';
import AnalyticsToolbar from '../components/AnalyticsToolbar';
import WidgetGrid from '../components/WidgetGrid';
import WidgetCard from '../components/WidgetCard';
import KpiCard from '../components/KpiCard';
import ResponsiveChart from '../components/ResponsiveChart';
import AnalyticsTable from '../components/AnalyticsTable';
import {
  buildEnrollmentTrend,
  categoryDistribution,
  coursePopularity,
  difficultyDistribution,
  formatNumber,
  languageDistribution,
} from '../data/analyticsMockData';

const CourseAnalyticsPage = memo(function CourseAnalyticsPage() {
  const { filters, setFilter, resetFilters, activeFilterCount, filteredCourses, kpis } = useAnalyticsState();

  const catDist = useMemo(() => categoryDistribution(filteredCourses), [filteredCourses]);
  const diffDist = useMemo(() => difficultyDistribution(filteredCourses), [filteredCourses]);
  const langDist = useMemo(() => languageDistribution(filteredCourses), [filteredCourses]);
  const popularity = useMemo(() => coursePopularity(filteredCourses), [filteredCourses]);
  const trend = useMemo(() => buildEnrollmentTrend(12, 11), []);

  const avgRating = useMemo(() => {
    if (filteredCourses.length === 0) return 0;
    return filteredCourses.reduce((s, c) => s + c.rating, 0) / filteredCourses.length;
  }, [filteredCourses]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <AnalyticsToolbar
        filters={filters}
        onFilterChange={setFilter}
        onClearFilters={resetFilters}
        activeFilterCount={activeFilterCount}
      />

      <WidgetGrid minColWidth={220}>
        <KpiCard title="Courses" value={formatNumber(filteredCourses.length)} subtitle="Matching filters" />
        <KpiCard title="Published" value={formatNumber(kpis.publishedCourses)} subtitle="Live courses" />
        <KpiCard title="Avg Rating" value={avgRating.toFixed(2)} subtitle="Across matches" trend="up" trendValue="+0.1" />
        <KpiCard title="Modules" value={formatNumber(filteredCourses.reduce((s, c) => s + c.moduleCount, 0))} subtitle="Total" />
      </WidgetGrid>

      <WidgetGrid>
        <WidgetCard title="Courses by Category">
          <ResponsiveChart height={280} ariaLabel="Courses by category donut chart">
            {({ width, height }) => <PieChart data={catDist} variant="donut" showLegend width={width} height={height} />}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Difficulty Levels">
          <ResponsiveChart height={280} ariaLabel="Difficulty level distribution pie chart">
            {({ width, height }) => <PieChart data={diffDist} variant="pie" showLegend width={width} height={height} />}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Languages">
          <ResponsiveChart height={280} ariaLabel="Language distribution bar chart">
            {({ width, height }) => (
              <BarChart
                data={langDist.map((l) => ({ label: l.label, values: [l.value] }))}
                series={[{ name: 'Courses' }]}
                variant="vertical"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Enrollment Trend">
          <ResponsiveChart height={280} ariaLabel="Course enrollment trend line chart">
            {({ width, height }) => (
              <LineChart
                data={trend.map((p) => ({ label: p.label, values: [p.enrollments] }))}
                series={[{ name: 'Enrollments' }]}
                variant="smooth"
                showArea
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Course Popularity Ranking" span={2}>
          <AnalyticsTable
            caption="Courses ranked by enrollment count"
            rows={popularity}
            rowKey={(r) => r.slug}
            columns={[
              { key: 'rank', header: '#', align: 'right', render: () => '' },
              { key: 'name', header: 'Course', render: (r) => r.label },
              { key: 'enroll', header: 'Enrollments', align: 'right', render: (r) => formatNumber(r.value) },
            ]}
          />
        </WidgetCard>
      </WidgetGrid>
    </div>
  );
});

export default CourseAnalyticsPage;
