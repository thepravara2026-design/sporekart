import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { AnalyticsPage } from '../AnalyticsPage';
import { KpiCenter } from '../components/KpiCenter';
import { InsightsPanel } from '../components/InsightsPanel';
import { ReportCenter } from '../components/ReportCenter';
import { MOCK_PRODUCT_KPI } from '../mock/mockAnalytics';
import { MOCK_INSIGHTS } from '../mock/mockInsights';
import { MOCK_ANALYTICS_REPORTS } from '../mock/mockReports';


const FullPreview: React.FC = () => <AnalyticsPage />;

const KpiPreview: React.FC = () => <KpiCenter kpi={MOCK_PRODUCT_KPI} />;

const InsightsPreview: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)' }}>
    <InsightsPanel insights={MOCK_INSIGHTS} />
  </div>
);

const ReportsPreview: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)' }}>
    <ReportCenter reports={MOCK_ANALYTICS_REPORTS} />
  </div>
);

export const AnalyticsPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<FullPreview />} />
      <Route path="kpi" element={<KpiPreview />} />
      <Route path="insights" element={<InsightsPreview />} />
      <Route path="reports" element={<ReportsPreview />} />
      <Route path="info" element={<AnalyticsInfo />} />
    </Routes>
  );
};

function AnalyticsInfo() {
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Analytics & Intelligence — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <ArchCard title="Mock Mode">
          This module operates entirely in mock mode. 12 KPI cards, catalog health data, 8 quality metrics, 6 categories, 8 brands, variant/SEO/marketplace/publishing analytics, 6 chart configs, 10 insights, 9 reports, and 10 activity events are statically defined.
        </ArchCard>
        <ArchCard title="Analytics Architecture">
          16-section workspace covering executive overview, catalog health, product quality, category/brand/variant analytics, pricing/SEO/marketplace/publishing/validation/compliance, KPI center, insights, reports, activity, settings, and help.
        </ArchCard>
        <ArchCard title="Chart System (Pure SVG)">
          8 reusable chart components — LineChart, BarChart, AreaChart, PieChart, DonutChart, ProgressChart, ScoreCard, HeatMap — all built using pure SVG. No external charting library. All use CSS var tokens for theming.
        </ArchCard>
        <ArchCard title="Integration Points">
          Future: Real-time analytics via WebSocket, BI tool integration (Power BI/Tableau), automated report scheduling, data export (PDF/CSV), AI-powered insights generation, custom dashboard builder.
        </ArchCard>
        <ArchCard title="Preview Routes">
          <code>/preview/products/analytics/workspace</code> — Full workspace<br />
          <code>/preview/products/analytics/kpi</code> — KPI Center<br />
          <code>/preview/products/analytics/insights</code> — Insights Panel<br />
          <code>/preview/products/analytics/reports</code> — Report Center<br />
          <code>/preview/products/analytics/info</code> — Architecture notes
        </ArchCard>
      </div>
    </div>
  );
}

function ArchCard({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{children}</div>
    </div>
  );
}

export default AnalyticsPreviewApp;
