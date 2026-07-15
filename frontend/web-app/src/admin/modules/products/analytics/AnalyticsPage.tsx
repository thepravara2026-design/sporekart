import React from 'react';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { useAnalyticsState } from './state/useAnalyticsState';
import { AnalyticsNav } from './state/AnalyticsNav';
import { AnalyticsToolbar } from './components/AnalyticsToolbar';
import { ExecutiveDashboard } from './components/ExecutiveDashboard';
import { CatalogHealthDashboard } from './components/CatalogHealthDashboard';
import { ProductQuality } from './components/ProductQuality';
import { CategoryAnalyticsView } from './components/CategoryAnalyticsView';
import { BrandAnalyticsView } from './components/BrandAnalyticsView';
import { VariantAnalyticsView } from './components/VariantAnalyticsView';
import { SeoAnalyticsView } from './components/SeoAnalyticsView';
import { MarketplaceAnalyticsView } from './components/MarketplaceAnalyticsView';
import { PublishingAnalyticsView } from './components/PublishingAnalyticsView';
import { KpiCenter } from './components/KpiCenter';
import { InsightsPanel } from './components/InsightsPanel';
import { ReportCenter } from './components/ReportCenter';
import { CURRENT_ANALYTICS_ROLE } from './types';
import './Analytics.css';

function AnalyticsSettings() {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Analytics Settings</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Analytics settings are in Mock Mode. Future settings will include data source configuration, dashboard customization, report scheduling, and BI tool integration.
      </p>
      <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={settRow}><span>Auto-Refresh Interval</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>15 minutes</span></div>
        <div style={settRow}><span>Default Dashboard</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Executive Overview</span></div>
        <div style={settRow}><span>Report Retention</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>90 days</span></div>
        <div style={settRow}><span>Data Export Format</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>PDF, CSV (Future)</span></div>
        <div style={settRow}><span>Active Role (Mock)</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>{CURRENT_ANALYTICS_ROLE}</span></div>
      </div>
    </div>
  );
}

function AnalyticsHelp() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>About the Analytics Workspace</h3>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          The Enterprise Product Analytics, Intelligence & Catalog Insights Platform provides administrators with comprehensive visibility into product health, completeness, quality, SEO, marketplace readiness, and publishing status. Everything operates in Mock Mode.
        </p>
      </div>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Key Sections</h3>
        <ul style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          <li><strong>Executive Overview</strong> — 12 KPI cards, growth chart, recent activity</li>
          <li><strong>Catalog Health</strong> — 9-dimension health score with progress bars</li>
          <li><strong>Category/Brand/Variant Analytics</strong> — Distribution, coverage, and health metrics</li>
          <li><strong>SEO/Marketplace/Publishing</strong> — Channel-specific dashboards</li>
          <li><strong>KPI Center</strong> — 8 enterprise KPIs with score cards</li>
          <li><strong>Insights Panel</strong> — Mock recommendations and action items</li>
        </ul>
      </div>
    </div>
  );
}

export const AnalyticsPage: React.FC = () => {
  const state = useAnalyticsState();

  const renderSection = () => {
    switch (state.section) {
      case 'overview':
        return <ExecutiveDashboard />;
      case 'catalog':
        return <CatalogHealthDashboard health={state.catalogHealth} />;
      case 'products':
        return <ProductQuality metrics={state.qualityMetrics} />;
      case 'categories':
        return <CategoryAnalyticsView categories={state.categoryAnalytics} chartConfig={state.chartCompletion} />;
      case 'brands':
        return <BrandAnalyticsView brands={state.brandAnalytics} />;
      case 'variants':
        return <VariantAnalyticsView variant={state.variantAnalytics} chartVariants={state.chartVariants} chartConfig={state.chartCompletion} />;
      case 'pricing':
        return <KpiCenter kpi={state.productKpi} />;
      case 'seo':
        return <SeoAnalyticsView seo={state.seoAnalytics} />;
      case 'marketplace':
        return <MarketplaceAnalyticsView marketplace={state.marketplaceAnalytics} />;
      case 'publishing':
        return <PublishingAnalyticsView publishing={state.publishingAnalytics} chartConfig={state.chartPublishing} />;
      case 'validation':
      case 'compliance':
        return <CatalogHealthDashboard health={state.catalogHealth} />;
      case 'reports':
        return <ReportCenter reports={state.reports} />;
      case 'insights':
        return <InsightsPanel insights={state.insights} />;
      case 'settings':
        return <AnalyticsSettings />;
      case 'help':
        return <AnalyticsHelp />;
      default:
        return <ExecutiveDashboard />;
    }
  };

  return (
    <PermissionProvider initialRole="manager">
      <FeatureFlagProvider>
        <PermissionGate action="view" resource="analytics">
          <div className="sk-ana-page">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div>
                <h1 style={{ margin: 0, fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)' }}>Product Analytics & Intelligence</h1>
                <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                  Enterprise catalog insights, KPIs, and business intelligence
                </p>
              </div>
            </div>

            <AnalyticsToolbar
              search={state.search}
              onSearchChange={state.setSearch}
              activeFilterCount={state.activeFilterCount}
              onClearFilters={state.clearAllFilters}
            />

            <div className="sk-ana-grid">
              <aside className="sk-ana-sidebar">
                <AnalyticsNav section={state.section} onSectionChange={state.setSection} />
              </aside>
              <main className="sk-ana-content">
              {renderSection()}
            </main>
            </div>

            <footer style={{ marginTop: 'var(--space-section-gap)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', borderTop: '1px solid var(--color-border)', paddingTop: 'var(--space-component-gap)', display: 'flex', justifyContent: 'space-between' }}>
              <span>Mock Mode — no persistence. Role: {CURRENT_ANALYTICS_ROLE}</span>
              <span>Product Analytics · Sprint 24 Part 11</span>
            </footer>
          </div>
        </PermissionGate>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
};

const settRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' };

export default AnalyticsPage;
