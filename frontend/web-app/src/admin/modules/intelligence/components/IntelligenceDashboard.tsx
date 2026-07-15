import { memo, type ElementType } from 'react';
import { useIntelligenceWorkspace } from '../contexts/IntelligenceWorkspaceContext';
import * as P from '../pages';

const PAGE_MAP: Record<string, ElementType> = {
  overview: P.OverviewPage,
  kpis: P.ExecutiveKpisPage,
  health: P.InventoryHealthPage,
  warehouse: P.WarehouseHealthPage,
  stock: P.StockHealthPage,
  batch: P.BatchHealthPage,
  movement: P.MovementAnalyticsPage,
  receiving: P.ReceivingAnalyticsPage,
  forecast: P.ForecastingPage,
  insights: P.InsightsPage,
  reports: P.ReportsPage,
  alerts: P.AlertsPage,
  settings: P.SettingsPage,
  help: P.HelpPage,
};

export const IntelligenceDashboard = memo(function IntelligenceDashboard() {
  const { activeSection } = useIntelligenceWorkspace();
  const Page = PAGE_MAP[activeSection] as ElementType | undefined;
  if (!Page) return <div style={{ padding: 20, color: 'var(--color-text-tertiary)' }}>Section not found: {activeSection}</div>;
  return <Page />;
});
