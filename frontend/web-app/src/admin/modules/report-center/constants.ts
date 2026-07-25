import type { Report, ReportTemplate, ReportSchedule, ReportExport, BusinessIntelligenceReport, ReportSummary } from './types';
import { ReportType, ReportStatus, ReportCategory, ExportFormat, ScheduleFrequency } from './types';

export const DEFAULT_REPORTS: Report[] = [
  {
    id: 'RPT-001',
    title: 'Monthly Executive Summary',
    description: 'Comprehensive executive overview of platform performance, revenue, and key growth metrics for the current month.',
    type: ReportType.EXECUTIVE,
    category: ReportCategory.EXECUTIVE,
    status: ReportStatus.GENERATED,
    owner: 'Alice Johnson',
    summary: 'Revenue grew 18.3% MoM with strong performance across marketplace and direct sales channels.',
    businessHealth: 'Strong',
    recommendations: [
      'Increase inventory capacity for top 10 SKUs',
      'Launch promotional campaign for high-margin products',
    ],
    risks: ['Supply chain disruption in region A', 'Currency fluctuation impacting margins'],
    kpis: { revenue: '$842K', orders: '3,471', customers: '12,938' },
    supportingMetrics: { aov: '$52.40', conversionRate: '3.8%', churnRate: '2.1%' },
    templateId: 'TPL-001',
    traceId: 'trace-exec-mar-001',
    executionTimeMs: 3200,
    generatedAt: '2026-07-25T10:30:00Z',
    createdAt: '2026-07-25T10:25:00Z',
  },
  {
    id: 'RPT-002',
    title: 'Revenue Performance Report',
    description: 'Detailed revenue breakdown by channel, product category, and customer segment with trend analysis.',
    type: ReportType.BUSINESS,
    category: ReportCategory.REVENUE,
    status: ReportStatus.GENERATED,
    owner: 'Bob Smith',
    summary: 'Marketplace revenue increased 24% YoY. Direct sales channel shows 12% growth.',
    businessHealth: 'Healthy',
    recommendations: [
      'Expand marketplace seller base in tier-2 cities',
      'Optimize pricing for bulk orders',
    ],
    risks: ['Increased competition in premium segment', 'Seasonal demand softening expected'],
    kpis: { marketplaceRevenue: '$520K', directRevenue: '$322K', mrr: '$842K' },
    supportingMetrics: { yoyGrowth: '24%', momGrowth: '18.3%', avgDealSize: '$12,400' },
    templateId: 'TPL-002',
    generatedAt: '2026-07-24T08:00:00Z',
    createdAt: '2026-07-24T07:45:00Z',
  },
  {
    id: 'RPT-003',
    title: 'Sales Channel Analytics',
    description: 'Multi-channel sales performance analysis including marketplace, direct, and distributor channels.',
    type: ReportType.PERFORMANCE,
    category: ReportCategory.SALES,
    status: ReportStatus.DRAFT,
    owner: 'Carol Davis',
    summary: 'Q3 sales pipeline shows 42% increase in qualified leads across all channels.',
    businessHealth: 'Positive',
    recommendations: [
      'Allocate additional resources to top-performing channels',
      'Implement cross-selling strategy for marketplace',
    ],
    risks: ['Channel partner attrition', 'Logistics cost escalation'],
    kpis: { totalSales: '$1.2M', leadsCount: '1,847', conversionRate: '6.2%' },
    supportingMetrics: { qualifiedLeads: '776', avgDealCycle: '18d', winRate: '34%' },
    templateId: 'TPL-003',
    generatedAt: '2026-07-23T14:00:00Z',
    createdAt: '2026-07-23T13:30:00Z',
  },
  {
    id: 'RPT-004',
    title: 'Order Fulfillment Analysis',
    description: 'End-to-end order fulfillment metrics including processing time, shipping performance, and return rates.',
    type: ReportType.OPERATIONAL,
    category: ReportCategory.ORDERS,
    status: ReportStatus.GENERATED,
    owner: 'David Wilson',
    summary: 'On-time delivery rate at 96.2%. Average fulfillment time reduced by 8 hours.',
    businessHealth: 'Strong',
    recommendations: [
      'Upgrade sorting facility in Mumbai hub',
      'Negotiate better SLAs with regional carriers',
    ],
    risks: ['Peak season capacity constraints', 'Last-mile delivery issues in remote areas'],
    kpis: { totalOrders: '3,471', onTimeRate: '96.2%', returnRate: '2.3%' },
    supportingMetrics: { avgFulfillmentTime: '6.2h', shippedOnTime: '3,340', returns: '80' },
    templateId: 'TPL-004',
    generatedAt: '2026-07-22T09:00:00Z',
    createdAt: '2026-07-22T08:30:00Z',
  },
  {
    id: 'RPT-005',
    title: 'Inventory Health Dashboard',
    description: 'Current inventory status across all warehouses with stock level alerts and reorder recommendations.',
    type: ReportType.HEALTH,
    category: ReportCategory.INVENTORY,
    status: ReportStatus.SCHEDULED,
    owner: 'Eve Martin',
    summary: 'Inventory turnover improved to 4.8x. 37 SKUs at low stock levels requiring attention.',
    businessHealth: 'Fair',
    recommendations: [
      'Expedite replenishment for 12 critical SKUs',
      'Reallocate slow-moving stock to discount channels',
    ],
    risks: ['Potential stockouts for high-demand items', 'Overstock in 3 categories'],
    kpis: { totalItems: '8,942', lowStock: '37', turnoverRate: '4.8x' },
    supportingMetrics: { stockValue: '$2.4M', slowMoving: '124', deadStock: '18' },
    templateId: 'TPL-005',
    generatedAt: '2026-07-21T06:00:00Z',
    createdAt: '2026-07-21T05:30:00Z',
  },
  {
    id: 'RPT-006',
    title: 'Customer Growth & Retention',
    description: 'Customer acquisition, engagement, and retention metrics with cohort analysis.',
    type: ReportType.BUSINESS,
    category: ReportCategory.CUSTOMERS,
    status: ReportStatus.DRAFT,
    owner: 'Frank Brown',
    summary: 'New customer acquisition up 11.2% MoM. Retention rate stable at 84%.',
    businessHealth: 'Healthy',
    recommendations: [
      'Launch referral program for existing customers',
      'Improve onboarding flow to reduce time-to-first-purchase',
    ],
    risks: ['Customer concentration risk - top 10% contribute 40% revenue', 'Churn uptick in 60-90 day cohort'],
    kpis: { totalCustomers: '12,938', newCustomers: '1,208', retentionRate: '84%' },
    supportingMetrics: { repeatPurchaseRate: '62%', avgLifetimeValue: '$340', nps: '72' },
    templateId: 'TPL-006',
    generatedAt: '2026-07-20T11:00:00Z',
    createdAt: '2026-07-20T10:30:00Z',
  },
];

export const DEFAULT_TEMPLATES: ReportTemplate[] = [
  { id: 'TPL-001', name: 'Executive Summary', description: 'Standard executive report template covering key business metrics and KPIs.', category: ReportCategory.EXECUTIVE, type: ReportType.EXECUTIVE, sections: ['Overview', 'Revenue', 'KPIs', 'Risks', 'Recommendations'], active: true, createdAt: '2026-01-15T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'TPL-002', name: 'Revenue Report', description: 'Detailed revenue breakdown by channel, segment, and product line.', category: ReportCategory.REVENUE, type: ReportType.BUSINESS, sections: ['Revenue Overview', 'Channel Breakdown', 'Segment Analysis', 'Trends', 'Forecast'], active: true, createdAt: '2026-01-20T00:00:00Z', updatedAt: '2026-06-10T00:00:00Z' },
  { id: 'TPL-003', name: 'Sales Performance', description: 'Sales pipeline, conversion metrics, and channel performance analysis.', category: ReportCategory.SALES, type: ReportType.PERFORMANCE, sections: ['Pipeline Overview', 'Conversion Funnel', 'Channel Comparison', 'Forecast'], active: true, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'TPL-004', name: 'Order Fulfillment', description: 'Order processing, shipping, and fulfillment efficiency metrics.', category: ReportCategory.ORDERS, type: ReportType.OPERATIONAL, sections: ['Order Summary', 'Fulfillment Times', 'Shipping Performance', 'Returns Analysis'], active: true, createdAt: '2026-02-10T00:00:00Z', updatedAt: '2026-05-20T00:00:00Z' },
  { id: 'TPL-005', name: 'Inventory Health', description: 'Stock levels, turnover, and reorder recommendations across warehouses.', category: ReportCategory.INVENTORY, type: ReportType.HEALTH, sections: ['Stock Overview', 'Low Stock Alerts', 'Turnover Analysis', 'Replenishment Plan'], active: true, createdAt: '2026-02-15T00:00:00Z', updatedAt: '2026-06-20T00:00:00Z' },
  { id: 'TPL-006', name: 'Customer Growth', description: 'Customer acquisition, retention, and lifetime value analysis.', category: ReportCategory.CUSTOMERS, type: ReportType.BUSINESS, sections: ['Acquisition Funnel', 'Retention Metrics', 'Cohort Analysis', 'LTV Projection'], active: true, createdAt: '2026-03-01T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'TPL-007', name: 'Platform Health', description: 'System performance, uptime, and infrastructure metrics.', category: ReportCategory.PLATFORM_HEALTH, type: ReportType.HEALTH, sections: ['Uptime Overview', 'Performance Metrics', 'Incident Log', 'Capacity Planning'], active: false, createdAt: '2026-03-10T00:00:00Z', updatedAt: '2026-05-01T00:00:00Z' },
  { id: 'TPL-008', name: 'Compliance Audit', description: 'Regulatory compliance status, audit findings, and remediation tracking.', category: ReportCategory.COMPLIANCE, type: ReportType.AUDIT, sections: ['Compliance Overview', 'Audit Findings', 'Remediation Plan', 'Risk Assessment'], active: false, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-05-15T00:00:00Z' },
];

export const DEFAULT_SCHEDULES: ReportSchedule[] = [
  { id: 'SCH-001', reportId: 'RPT-001', frequency: ScheduleFrequency.MONTHLY, recipients: ['exec@sporekart.com', 'board@sporekart.com'], format: ExportFormat.PDF, nextRunAt: '2026-08-01T08:00:00Z', active: true, createdAt: '2026-01-15T00:00:00Z' },
  { id: 'SCH-002', reportId: 'RPT-002', frequency: ScheduleFrequency.WEEKLY, recipients: ['finance@sporekart.com'], format: ExportFormat.EXCEL, nextRunAt: '2026-07-28T07:00:00Z', active: true, createdAt: '2026-02-01T00:00:00Z' },
  { id: 'SCH-003', reportId: 'RPT-003', frequency: ScheduleFrequency.DAILY, recipients: ['sales-ops@sporekart.com'], format: ExportFormat.CSV, nextRunAt: '2026-07-26T06:00:00Z', active: true, createdAt: '2026-03-01T00:00:00Z' },
  { id: 'SCH-004', reportId: 'RPT-004', frequency: ScheduleFrequency.WEEKLY, recipients: ['ops@sporekart.com', 'logistics@sporekart.com'], format: ExportFormat.PDF, nextRunAt: '2026-07-28T09:00:00Z', active: true, createdAt: '2026-02-15T00:00:00Z' },
];

export const DEFAULT_EXPORTS: ReportExport[] = [
  { id: 'EXP-001', reportId: 'RPT-001', format: ExportFormat.PDF, exportedBy: 'Alice Johnson', fileSize: '2.4 MB', exportedAt: '2026-07-25T10:35:00Z' },
  { id: 'EXP-002', reportId: 'RPT-002', format: ExportFormat.EXCEL, exportedBy: 'Bob Smith', fileSize: '1.8 MB', exportedAt: '2026-07-24T08:15:00Z' },
  { id: 'EXP-003', reportId: 'RPT-001', format: ExportFormat.PDF, exportedBy: 'Alice Johnson', fileSize: '2.4 MB', exportedAt: '2026-06-25T10:30:00Z' },
  { id: 'EXP-004', reportId: 'RPT-004', format: ExportFormat.CSV, exportedBy: 'David Wilson', fileSize: '856 KB', exportedAt: '2026-07-22T09:20:00Z' },
];

export const DEFAULT_BI_REPORTS: BusinessIntelligenceReport[] = [
  { id: 'BI-001', title: 'Business Health Index', description: 'Aggregated health score across all business units.', metrics: { overallScore: 87, revenueHealth: 92, opsHealth: 84, customerHealth: 78 }, insights: ['Revenue growth sustained for 6 consecutive months', 'Operational efficiency improved by 12% QoQ'], createdAt: '2026-07-25T00:00:00Z' },
  { id: 'BI-002', title: 'Market Trend Analysis', description: 'Market trends, competitive landscape, and growth opportunities.', metrics: { marketShare: '18.4%', tam: '$4.2B', sam: '$890M', som: '$168M' }, insights: ['Market expanding at 14% CAGR', 'Growing demand in organic segment'], createdAt: '2026-07-20T00:00:00Z' },
  { id: 'BI-003', title: 'AI Platform Intelligence', description: 'AI-driven insights from platform data analysis.', metrics: { predictionsGenerated: '1,204', accuracyRate: '93.7%', anomaliesDetected: '47', recommendations: '312' }, insights: ['Demand forecasting accuracy improved to 94%', 'Anomaly detection prevented 3 potential stockouts'], createdAt: '2026-07-22T00:00:00Z' },
];

export const REPORT_TYPE_LABELS: Record<string, string> = {
  daily: 'Daily', weekly: 'Weekly', monthly: 'Monthly', quarterly: 'Quarterly', yearly: 'Yearly',
  custom: 'Custom', executive: 'Executive', operational: 'Operational', business: 'Business',
  performance: 'Performance', health: 'Health', audit: 'Audit',
};

export const REPORT_CATEGORY_LABELS: Record<string, string> = {
  executive: 'Executive', revenue: 'Revenue', sales: 'Sales', orders: 'Orders', inventory: 'Inventory',
  customers: 'Customers', products: 'Products', marketplace: 'Marketplace', training: 'Training',
  vendors: 'Vendors', growers: 'Growers', ai_platform: 'AI Platform', automation: 'Automation',
  platform_health: 'Platform Health', risk: 'Risk', business_health: 'Business Health', compliance: 'Compliance',
};

export const REPORT_STATUS_LABELS: Record<string, string> = {
  draft: 'Draft', generated: 'Generated', scheduled: 'Scheduled', exported: 'Exported', failed: 'Failed',
};

export const REPORT_TYPE_COLORS: Record<string, string> = {
  daily: '#1d9bf0', weekly: '#7c3aed', monthly: '#2f6f4f', quarterly: '#d97706', yearly: '#ef4444',
  custom: '#6b7280', executive: '#2f6f4f', operational: '#1d9bf0', business: '#7c3aed',
  performance: '#d97706', health: '#ef4444', audit: '#6b7280',
};

export const REPORT_CATEGORY_COLORS: Record<string, string> = {
  executive: '#2f6f4f', revenue: '#1d9bf0', sales: '#7c3aed', orders: '#d97706', inventory: '#ef4444',
  customers: '#06b6d4', products: '#f59e0b', marketplace: '#8b5cf6', training: '#ec4899',
  vendors: '#14b8a6', growers: '#84cc16', ai_platform: '#6366f1', automation: '#22c55e',
  platform_health: '#e11d48', risk: '#f97316', business_health: '#0ea5e9', compliance: '#a855f7',
};

export const REPORT_STATUS_COLORS: Record<string, string> = {
  draft: '#6b7280', generated: '#2f6f4f', scheduled: '#1d9bf0', exported: '#7c3aed', failed: '#ef4444',
};

export const REPORT_SUMMARY_MOCK: ReportSummary = {
  totalReports: 1284,
  byType: { daily: 420, weekly: 310, monthly: 180, quarterly: 45, yearly: 12, executive: 84, operational: 96, business: 72, performance: 38, health: 18, audit: 9 },
  byCategory: { executive: 84, revenue: 120, sales: 145, orders: 98, inventory: 76, customers: 112, products: 95, marketplace: 42, training: 28, vendors: 18, growers: 14, ai_platform: 35, automation: 22, platform_health: 30, risk: 16, business_health: 20, compliance: 8 },
  byStatus: { draft: 214, generated: 836, scheduled: 76, exported: 142, failed: 16 },
};
