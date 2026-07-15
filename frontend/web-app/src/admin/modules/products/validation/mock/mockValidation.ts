import type { ProductValidationScore } from '../types';

export const MOCK_VALIDATION_SCORES: ProductValidationScore[] = [
  { productId: 'prod-001', productName: 'Fresh Organic Tomatoes', overall: 88, completeness: 92, seo: 88, compliance: 85, marketplace: 70, accessibility: 90, aiReadiness: 72, publishing: 95, riskLevel: 'low', certificationStatus: 'gold', validatedAt: '2026-07-14T10:00:00Z' },
  { productId: 'prod-003', productName: 'Premium Basmati Rice 5kg', overall: 92, completeness: 95, seo: 92, compliance: 90, marketplace: 81, accessibility: 85, aiReadiness: 78, publishing: 95, riskLevel: 'low', certificationStatus: 'enterprise', validatedAt: '2026-07-13T14:00:00Z' },
  { productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', overall: 78, completeness: 82, seo: 85, compliance: 75, marketplace: 68, accessibility: 70, aiReadiness: 80, publishing: 85, riskLevel: 'medium', certificationStatus: 'silver', validatedAt: '2026-07-12T11:00:00Z' },
  { productId: 'prod-009', productName: 'Hydroponic Starter Kit', overall: 62, completeness: 65, seo: 72, compliance: 60, marketplace: 48, accessibility: 55, aiReadiness: 65, publishing: 50, riskLevel: 'high', certificationStatus: 'bronze', validatedAt: '2026-07-11T09:00:00Z' },
  { productId: 'prod-015', productName: 'Button Mushroom', overall: 74, completeness: 78, seo: 78, compliance: 70, marketplace: 64, accessibility: 80, aiReadiness: 70, publishing: 65, riskLevel: 'medium', certificationStatus: 'silver', validatedAt: '2026-07-10T14:00:00Z' },
  { productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk', overall: 45, completeness: 40, seo: 55, compliance: 50, marketplace: 36, accessibility: 45, aiReadiness: 45, publishing: 30, riskLevel: 'high', certificationStatus: 'none', validatedAt: '2026-07-09T10:00:00Z' },
  { productId: 'prod-007', productName: 'Organic Compost 25kg', overall: 82, completeness: 85, seo: 82, compliance: 80, marketplace: 78, accessibility: 85, aiReadiness: 68, publishing: 90, riskLevel: 'low', certificationStatus: 'gold', validatedAt: '2026-07-08T15:00:00Z' },
  { productId: 'prod-002', productName: 'Fresh Oyster Mushrooms 250g', overall: 76, completeness: 80, seo: 75, compliance: 72, marketplace: 65, accessibility: 78, aiReadiness: 70, publishing: 80, riskLevel: 'medium', certificationStatus: 'silver', validatedAt: '2026-07-07T12:00:00Z' },
  { productId: 'prod-004', productName: 'Dried Shiitake Mushrooms 100g', overall: 70, completeness: 72, seo: 72, compliance: 68, marketplace: 60, accessibility: 72, aiReadiness: 65, publishing: 75, riskLevel: 'medium', certificationStatus: 'bronze', validatedAt: '2026-07-06T10:00:00Z' },
  { productId: 'prod-008', productName: 'Advanced Shiitake Log Kit', overall: 80, completeness: 82, seo: 80, compliance: 78, marketplace: 72, accessibility: 82, aiReadiness: 76, publishing: 85, riskLevel: 'low', certificationStatus: 'gold', validatedAt: '2026-07-05T14:00:00Z' },
  { productId: 'prod-010', productName: 'Home Cultivation Masterclass', overall: 85, completeness: 88, seo: 82, compliance: 85, marketplace: 78, accessibility: 90, aiReadiness: 82, publishing: 88, riskLevel: 'low', certificationStatus: 'enterprise', validatedAt: '2026-07-04T09:00:00Z' },
  { productId: 'prod-011', productName: 'Fresh King Oyster Mushrooms 250g', overall: 72, completeness: 75, seo: 70, compliance: 68, marketplace: 62, accessibility: 75, aiReadiness: 68, publishing: 78, riskLevel: 'medium', certificationStatus: 'bronze', validatedAt: '2026-07-03T11:00:00Z' },
];

export const MOCK_PRODUCT_HEALTH: ProductValidationScore = {
  productId: 'summary', productName: 'All Products', overall: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.overall, 0) / MOCK_VALIDATION_SCORES.length),
  completeness: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.completeness, 0) / MOCK_VALIDATION_SCORES.length),
  seo: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.seo, 0) / MOCK_VALIDATION_SCORES.length),
  compliance: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.compliance, 0) / MOCK_VALIDATION_SCORES.length),
  marketplace: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.marketplace, 0) / MOCK_VALIDATION_SCORES.length),
  accessibility: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.accessibility, 0) / MOCK_VALIDATION_SCORES.length),
  aiReadiness: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.aiReadiness, 0) / MOCK_VALIDATION_SCORES.length),
  publishing: Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.publishing, 0) / MOCK_VALIDATION_SCORES.length),
  riskLevel: 'low', certificationStatus: 'gold', validatedAt: '2026-07-14T12:00:00Z',
};
