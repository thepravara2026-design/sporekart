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
import {
  categoryDistribution,
  curriculumStats,
  difficultyDistribution,
  formatNumber,
} from '../data/analyticsMockData';

const CurriculumAnalyticsPage = memo(function CurriculumAnalyticsPage() {
  const { filters, setFilter, resetFilters, activeFilterCount, filteredCourses } = useAnalyticsState();

  const stats = useMemo(() => curriculumStats(filteredCourses), [filteredCourses]);
  const catDist = useMemo(() => categoryDistribution(filteredCourses), [filteredCourses]);
  const diffDist = useMemo(() => difficultyDistribution(filteredCourses), [filteredCourses]);

  const coverage = useMemo(
    () =>
      filteredCourses.slice(0, 10).map((c) => ({
        code: c.code,
        name: c.name,
        modules: c.moduleCount,
        lessons: c.moduleCount * 6,
        hours: c.durationHours,
      })),
    [filteredCourses],
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <AnalyticsToolbar
        filters={filters}
        onFilterChange={setFilter}
        onClearFilters={resetFilters}
        activeFilterCount={activeFilterCount}
      />

      <WidgetGrid minColWidth={200}>
        {stats.map((s) => (
          <KpiCard key={s.label} title={s.label} value={formatNumber(s.value)} subtitle="Curriculum total" />
        ))}
      </WidgetGrid>

      <WidgetGrid>
        <WidgetCard title="Curriculum Volume" subtitle="By type">
          <ResponsiveChart height={280} ariaLabel="Curriculum volume bar chart">
            {({ width, height }) => (
              <BarChart
                data={stats.map((s) => ({ label: s.label, values: [s.value] }))}
                series={[{ name: 'Count' }]}
                variant="horizontal"
                width={width}
                height={height}
              />
            )}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Coverage by Category">
          <ResponsiveChart height={280} ariaLabel="Curriculum coverage by category donut chart">
            {({ width, height }) => <PieChart data={catDist} variant="donut" showLegend width={width} height={height} />}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Coverage by Difficulty">
          <ResponsiveChart height={280} ariaLabel="Curriculum coverage by difficulty pie chart">
            {({ width, height }) => <PieChart data={diffDist} variant="pie" showLegend width={width} height={height} />}
          </ResponsiveChart>
        </WidgetCard>
        <WidgetCard title="Curriculum Coverage Detail" span={2}>
          <AnalyticsTable
            caption="Modules, lessons and hours per course"
            rows={coverage}
            rowKey={(r) => r.code}
            columns={[
              { key: 'code', header: 'Code', render: (r) => r.code },
              { key: 'name', header: 'Course', render: (r) => r.name },
              { key: 'modules', header: 'Modules', align: 'right', render: (r) => formatNumber(r.modules) },
              { key: 'lessons', header: 'Lessons', align: 'right', render: (r) => formatNumber(r.lessons) },
              { key: 'hours', header: 'Hours', align: 'right', render: (r) => formatNumber(r.hours) },
            ]}
          />
        </WidgetCard>
      </WidgetGrid>
    </div>
  );
});

export default CurriculumAnalyticsPage;
