import type { AnalyticsActivityEvent } from '../types';

export const MOCK_ANALYTICS_ACTIVITY: AnalyticsActivityEvent[] = [
  { id: 'aa-001', type: 'report_generated', message: 'Catalog Health Report generated', user: 'System', timestamp: '2026-07-14T10:00:00Z', icon: 'file-text' },
  { id: 'aa-002', type: 'dashboard_updated', message: 'Executive Dashboard refreshed with latest data', user: 'System', timestamp: '2026-07-13T12:00:00Z', icon: 'refresh' },
  { id: 'aa-003', type: 'insight_generated', message: 'New insight: 3 products missing SEO metadata', user: 'Analytics Engine', timestamp: '2026-07-12T08:00:00Z', icon: 'lightbulb' },
  { id: 'aa-004', type: 'report_scheduled', message: 'Weekly compliance report scheduled', user: 'Compliance Manager', timestamp: '2026-07-11T14:00:00Z', icon: 'calendar' },
  { id: 'aa-005', type: 'export', message: 'SEO Performance Report exported as PDF', user: 'SEO Manager', timestamp: '2026-07-10T16:00:00Z', icon: 'download' },
  { id: 'aa-006', type: 'report_generated', message: 'Executive Insights Report generated', user: 'System', timestamp: '2026-07-09T09:00:00Z', icon: 'file-text' },
  { id: 'aa-007', type: 'insight_generated', message: 'Marketplace readiness gap analysis updated', user: 'Analytics Engine', timestamp: '2026-07-08T11:00:00Z', icon: 'shopping-cart' },
  { id: 'aa-008', type: 'dashboard_updated', message: 'KPI Center recalculated with latest validation data', user: 'System', timestamp: '2026-07-07T15:00:00Z', icon: 'refresh' },
  { id: 'aa-009', type: 'report_scheduled', message: 'Monthly executive report scheduled for board review', user: 'Administrator', timestamp: '2026-07-06T10:00:00Z', icon: 'calendar' },
  { id: 'aa-010', type: 'export', message: 'Category Performance Report exported', user: 'Business Analyst', timestamp: '2026-07-05T13:00:00Z', icon: 'download' },
];
