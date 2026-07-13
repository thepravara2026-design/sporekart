function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { ChartContainer } from '../../components/charts/standard/ChartContainer';
import { LineChart } from '../../components/charts/standard/LineChart';
import { AreaChart } from '../../components/charts/standard/AreaChart';
import { BarChart } from '../../components/charts/standard/BarChart';
import { PieChart } from '../../components/charts/standard/PieChart';
import { RadialProgress } from '../../components/charts/standard/RadialProgress';
import { CircularKPI } from '../../components/charts/standard/CircularKPI';
import { Gauge } from '../../components/charts/standard/Gauge';
import { ScatterChart } from '../../components/charts/standard/ScatterChart';
import { BubbleChart } from '../../components/charts/standard/BubbleChart';
import { CalendarHeatmap } from '../../components/charts/standard/CalendarHeatmap';
import { GridHeatmap } from '../../components/charts/standard/GridHeatmap';

const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
const lineData = months.map(m => ({ label: m, values: [Math.random() * 80 + 20, Math.random() * 60 + 10] }));
const lineSeries = [{ name: 'Revenue' }, { name: 'Costs' }];

const areaData = months.slice(0, 6).map(m => ({ label: m, values: [Math.random() * 100 + 50, Math.random() * 80 + 20] }));
const areaSeries = [{ name: 'Products' }, { name: 'Services' }];

const barData = months.slice(0, 6).map(m => ({ label: m, values: [Math.random() * 200 + 50, Math.random() * 150 + 30] }));
const barSeries = [{ name: 'Actual' }, { name: 'Forecast' }];

const pieData = [
  { label: 'Direct', value: 35 }, { label: 'Organic', value: 28 },
  { label: 'Referral', value: 18 }, { label: 'Social', value: 12 }, { label: 'Other', value: 7 },
];

const scatterData = Array.from({ length: 20 }, (_, i) => ({
  x: Math.random() * 100, y: Math.random() * 100, label: `P${i + 1}`,
  color: `var(--color-data-viz-${(i % 8) + 1})`,
}));

const bubbleData = Array.from({ length: 12 }, (_, i) => ({
  x: Math.random() * 100, y: Math.random() * 100, radius: Math.random() * 15 + 5,
  label: `B${i + 1}`, color: `var(--color-data-viz-${(i % 8) + 1})`,
}));

const heatmapData = Array.from({ length: 90 }, () => {
  const d = new Date();
  d.setDate(d.getDate() - Math.floor(Math.random() * 90));
  return { date: d.toISOString().slice(0, 10), value: Math.floor(Math.random() * 20) };
});

const gridRows = ['Product A', 'Product B', 'Product C', 'Product D'];
const gridCols = ['Q1', 'Q2', 'Q3', 'Q4'];
const gridData = gridRows.flatMap(r =>
  gridCols.map(c => ({ row: r, column: c, value: Math.floor(Math.random() * 100) }))
);

export default function ChartsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Charts</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All chart types, variants, and states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Line Charts</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Single Line" subtitle="Straight variant">
            <LineChart data={lineData.map(d => ({ label: d.label, values: [d.values[0]] }))} series={[{ name: 'Revenue' }]} variant="straight" width={500} height={250} showPoints />
          </ChartContainer>
          <ChartContainer title="Multi Line" subtitle="Smooth variant">
            <LineChart data={lineData} series={lineSeries} variant="smooth" width={500} height={250} showPoints showArea />
          </ChartContainer>
          <ChartContainer title="Stepped & Area Overlay" subtitle="Stepped variant with area fill">
            <LineChart data={lineData.map(d => ({ label: d.label, values: [d.values[0]] }))} series={[{ name: 'Revenue' }]} variant="stepped" showArea width={500} height={250} />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Area Charts</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Single Area" subtitle="Gradient fill">
            <AreaChart data={areaData.map(d => ({ label: d.label, values: [d.values[0]] }))} series={[{ name: 'Products' }]} variant="single" width={500} height={250} />
          </ChartContainer>
          <ChartContainer title="Stacked Area" subtitle="Stacked variant">
            <AreaChart data={areaData} series={areaSeries} variant="stacked" width={500} height={250} />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Bar Charts</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Vertical & Grouped">
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
              <BarChart data={barData.map(d => ({ label: d.label, values: [d.values[0]] }))} series={[{ name: 'Actual' }]} variant="vertical" width={240} height={200} />
              <BarChart data={barData} series={barSeries} variant="grouped" width={240} height={200} />
            </div>
          </ChartContainer>
          <ChartContainer title="Stacked & Comparison">
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
              <BarChart data={barData} series={barSeries} variant="stacked" width={240} height={200} />
              <BarChart data={barData.map(d => ({ label: d.label, values: [d.values[0], d.values[1]] }))} series={barSeries} variant="comparison" width={240} height={200} />
            </div>
          </ChartContainer>
          <ChartContainer title="Horizontal Bars">
            <BarChart data={barData.slice(0, 4).map(d => ({ label: d.label, values: [d.values[0]] }))} series={[{ name: 'Actual' }]} variant="horizontal" width={500} height={250} />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Pie / Donut / Semi-circle</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Pie Chart">
            <PieChart data={pieData} variant="pie" width={350} height={350} showLegend showTooltip />
          </ChartContainer>
          <ChartContainer title="Donut">
            <PieChart data={pieData} variant="donut" width={350} height={350} showLegend />
          </ChartContainer>
          <ChartContainer title="Semi-circle">
            <PieChart data={pieData} variant="semi-circle" width={350} height={280} showLegend />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Radial / KPI / Gauge</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '16px' }}>
          <StateCard label="RadialProgress">
            <RadialProgress value={75} label="Completion" />
            <RadialProgress value={45} color="var(--color-data-viz-3)" size={100} />
            <RadialProgress value={90} color="var(--color-success-500)" size={80} showValue={false} />
          </StateCard>
          <StateCard label="CircularKPI">
            <CircularKPI value="82.5K" label="Users" progress={82} trend="up" trendValue="+12%" />
            <CircularKPI value="2.1K" label="Orders" progress={45} trend="down" trendValue="-8%" color="var(--color-data-viz-3)" />
            <CircularKPI value="98%" label="Uptime" progress={98} color="var(--color-success-500)" />
          </StateCard>
          <StateCard label="Gauge">
            <Gauge value={72} label="Performance" size={180} />
            <Gauge value={35} label="Risk" size={160} min={0} max={100} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Scatter & Bubble</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Scatter Chart">
            <ScatterChart data={scatterData} width={500} height={300} showGrid />
          </ChartContainer>
          <ChartContainer title="Bubble Chart">
            <BubbleChart data={bubbleData} width={500} height={300} showGrid />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Heatmaps</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Calendar Heatmap" height={180}>
            <CalendarHeatmap data={heatmapData} />
          </ChartContainer>
          <ChartContainer title="Grid Heatmap" height={260}>
            <GridHeatmap data={gridData} rows={gridRows} columns={gridCols} />
          </ChartContainer>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Chart States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <ChartContainer title="Loading State" loading>
            <div />
          </ChartContainer>
          <ChartContainer title="Empty State" empty>
            <div />
          </ChartContainer>
          <ChartContainer title="Error State" error errorMessage="Failed to load chart data">
            <div />
          </ChartContainer>
        </div>
      </section>
    </div>
  );
}
