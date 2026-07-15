import type { InsightRecommendation } from '../types';

export const MOCK_INSIGHTS: InsightRecommendation[] = [
  { id: 'ins-1', type: 'seo', severity: 'critical', message: 'Products missing SEO metadata', productCount: 3, action: 'Open SEO Manager' },
  { id: 'ins-2', type: 'images', severity: 'warning', message: 'Products with fewer than 3 images', productCount: 2, action: 'Open Media Manager' },
  { id: 'ins-3', type: 'category', severity: 'info', message: 'Products missing category assignment', productCount: 0, action: 'Open Categories' },
  { id: 'ins-4', type: 'publishing', severity: 'warning', message: 'Products blocked from publishing', productCount: 2, action: 'Open Publishing' },
  { id: 'ins-5', type: 'marketplace', severity: 'warning', message: 'Products not ready for any marketplace', productCount: 3, action: 'Open Marketplace' },
  { id: 'ins-6', type: 'quality', severity: 'critical', message: 'Products with critical quality issues', productCount: 1, action: 'Open Validation' },
  { id: 'ins-7', type: 'duplicate', severity: 'info', message: 'Potential duplicate products detected', productCount: 0, action: 'Review Duplicates' },
  { id: 'ins-8', type: 'seo', severity: 'info', message: 'Products without structured data markup', productCount: 4, action: 'Add Structured Data' },
  { id: 'ins-9', type: 'marketplace', severity: 'warning', message: 'Export readiness documentation missing', productCount: 4, action: 'Prepare Export Docs' },
  { id: 'ins-10', type: 'quality', severity: 'info', message: 'Product descriptions below recommended length', productCount: 3, action: 'Improve Descriptions' },
];
