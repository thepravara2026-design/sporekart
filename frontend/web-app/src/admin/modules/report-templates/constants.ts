import type { ReportTemplate } from './types';

export const DEFAULT_TEMPLATES: ReportTemplate[] = [
  { id: 'TPL-001', name: 'Executive Summary', description: 'Standard executive report template covering key business metrics and KPIs.', category: 'Executive', type: 'Executive', sectionsCount: 5, active: true, usageCount: 124, createdAt: '2026-01-15T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'TPL-002', name: 'Revenue Report', description: 'Detailed revenue breakdown by channel, segment, and product line.', category: 'Revenue', type: 'Business', sectionsCount: 5, active: true, usageCount: 98, createdAt: '2026-01-20T00:00:00Z', updatedAt: '2026-06-10T00:00:00Z' },
  { id: 'TPL-003', name: 'Sales Performance', description: 'Sales pipeline, conversion metrics, and channel performance analysis.', category: 'Sales', type: 'Performance', sectionsCount: 4, active: true, usageCount: 87, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'TPL-004', name: 'Order Fulfillment', description: 'Order processing, shipping, and fulfillment efficiency metrics.', category: 'Orders', type: 'Operational', sectionsCount: 4, active: true, usageCount: 62, createdAt: '2026-02-10T00:00:00Z', updatedAt: '2026-05-20T00:00:00Z' },
  { id: 'TPL-005', name: 'Inventory Health', description: 'Stock levels, turnover, and reorder recommendations across warehouses.', category: 'Inventory', type: 'Health', sectionsCount: 4, active: true, usageCount: 55, createdAt: '2026-02-15T00:00:00Z', updatedAt: '2026-06-20T00:00:00Z' },
  { id: 'TPL-006', name: 'Customer Growth', description: 'Customer acquisition, retention, and lifetime value analysis.', category: 'Customers', type: 'Business', sectionsCount: 4, active: true, usageCount: 73, createdAt: '2026-03-01T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'TPL-007', name: 'Platform Health', description: 'System performance, uptime, and infrastructure metrics.', category: 'Platform Health', type: 'Health', sectionsCount: 4, active: false, usageCount: 18, createdAt: '2026-03-10T00:00:00Z', updatedAt: '2026-05-01T00:00:00Z' },
  { id: 'TPL-008', name: 'Compliance Audit', description: 'Regulatory compliance status, audit findings, and remediation tracking.', category: 'Compliance', type: 'Audit', sectionsCount: 4, active: false, usageCount: 9, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-05-15T00:00:00Z' },
];

export const CATEGORY_COLORS: Record<string, string> = {
  Executive: '#2f6f4f', Revenue: '#1d9bf0', Sales: '#7c3aed', Orders: '#d97706',
  Inventory: '#ef4444', Customers: '#06b6d4', 'Platform Health': '#e11d48',
  Compliance: '#a855f7',
};
