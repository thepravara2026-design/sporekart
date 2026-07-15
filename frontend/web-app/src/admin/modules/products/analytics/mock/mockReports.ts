import type { AnalyticsReport } from '../types';

export const MOCK_ANALYTICS_REPORTS: AnalyticsReport[] = [
  { id: 'ar-1', title: 'Catalog Health Report', type: 'catalog', icon: '📊', generatedAt: '2026-07-14T10:00:00Z', generatedBy: 'System', description: 'Complete overview of catalog health, completion, and quality metrics across all products.' },
  { id: 'ar-2', title: 'Category Performance Report', type: 'category', icon: '📂', generatedAt: '2026-07-13T14:00:00Z', generatedBy: 'System', description: 'Category-level analytics showing product distribution, completion rates, and health scores.' },
  { id: 'ar-3', title: 'Brand Coverage Report', type: 'brand', icon: '🏷️', generatedAt: '2026-07-12T11:00:00Z', generatedBy: 'Business Analyst', description: 'Brand analytics with coverage metrics, active/inactive status, and product distribution.' },
  { id: 'ar-4', title: 'Variant Intelligence Report', type: 'variant', icon: '🧩', generatedAt: '2026-07-11T09:00:00Z', generatedBy: 'System', description: 'Variant analytics including distribution, attribute usage, and SKU coverage metrics.' },
  { id: 'ar-5', title: 'SEO Performance Report', type: 'seo', icon: '🔍', generatedAt: '2026-07-10T15:00:00Z', generatedBy: 'SEO Manager', description: 'SEO analytics with meta coverage, schema coverage, and AI search readiness scores.' },
  { id: 'ar-6', title: 'Compliance Summary Report', type: 'compliance', icon: '🛡️', generatedAt: '2026-07-09T12:00:00Z', generatedBy: 'Compliance Manager', description: 'Compliance analytics across all products with risk assessment and certification status.' },
  { id: 'ar-7', title: 'Marketplace Readiness Report', type: 'marketplace', icon: '🛒', generatedAt: '2026-07-08T10:00:00Z', generatedBy: 'Marketing Manager', description: 'Marketplace readiness analytics for all 6 sales channels with gap analysis.' },
  { id: 'ar-8', title: 'Publishing Pipeline Report', type: 'publishing', icon: '📤', generatedAt: '2026-07-07T14:00:00Z', generatedBy: 'System', description: 'Publishing pipeline analytics showing status distribution and bottlenecks.' },
  { id: 'ar-9', title: 'Executive Insights Report', type: 'executive', icon: '👔', generatedAt: '2026-07-14T12:00:00Z', generatedBy: 'System', description: 'Executive summary of all analytics KPIs, trends, and recommendations for leadership.' },
];
