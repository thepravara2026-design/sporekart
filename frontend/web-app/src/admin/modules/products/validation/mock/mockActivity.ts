import type { ValidationActivityEvent } from '../types';

export const MOCK_VALIDATION_ACTIVITY: ValidationActivityEvent[] = [
  { id: 'va-001', type: 'validation_run', message: 'Full validation run completed for all products', productName: 'All Products', productId: 'all', user: 'QA Engine', timestamp: '2026-07-14T12:00:00Z', icon: 'check-circle' },
  { id: 'va-002', type: 'certification_issued', message: 'Gold certification issued', productName: 'Organic Compost 25kg', productId: 'prod-007', user: 'QA Team', timestamp: '2026-07-10T10:00:00Z', icon: 'award' },
  { id: 'va-003', type: 'compliance_check', message: 'Compliance check completed with 85% score', productName: 'Fresh Organic Tomatoes', productId: 'prod-001', user: 'Compliance Manager', timestamp: '2026-07-09T14:00:00Z', icon: 'shield' },
  { id: 'va-004', type: 'approval_granted', message: 'Marketplace approval granted for all channels', productName: 'Premium Basmati Rice 5kg', productId: 'prod-003', user: 'Marketing Manager', timestamp: '2026-07-08T11:00:00Z', icon: 'thumbs-up' },
  { id: 'va-005', type: 'rejection', message: 'Publishing blocked due to compliance gaps', productName: 'Shiitake Spawn Bags 10pk', productId: 'prod-006', user: 'QA Engine', timestamp: '2026-07-07T16:00:00Z', icon: 'x-circle' },
  { id: 'va-006', type: 'report_generated', message: 'Executive health summary report generated', productName: 'All Products', productId: 'all', user: 'System', timestamp: '2026-07-06T12:00:00Z', icon: 'file-text' },
  { id: 'va-007', type: 'bulk_validated', message: 'Bulk validation completed for 5 products', productName: 'Multiple', productId: 'bulk-001', user: 'QA Engineer', timestamp: '2026-07-05T10:00:00Z', icon: 'layers' },
  { id: 'va-008', type: 'validation_run', message: 'SEO re-validation after meta update', productName: 'Oyster Mushroom Grow Kit', productId: 'prod-005', user: 'SEO Manager', timestamp: '2026-07-04T14:00:00Z', icon: 'search' },
  { id: 'va-009', type: 'certification_issued', message: 'Enterprise certification renewed', productName: 'Home Cultivation Masterclass', productId: 'prod-010', user: 'Enterprise Governance', timestamp: '2026-07-03T09:00:00Z', icon: 'award' },
  { id: 'va-010', type: 'compliance_check', message: 'GST/HSN compliance re-verified', productName: 'Premium Basmati Rice 5kg', productId: 'prod-003', user: 'Compliance Manager', timestamp: '2026-07-02T15:00:00Z', icon: 'shield' },
  { id: 'va-011', type: 'bulk_validated', message: 'Certification audit completed for gold products', productName: 'Multiple', productId: 'bulk-002', user: 'QA Team', timestamp: '2026-07-01T11:00:00Z', icon: 'layers' },
  { id: 'va-012', type: 'report_generated', message: 'Marketplace readiness report generated', productName: 'All Products', productId: 'all', user: 'Marketing Manager', timestamp: '2026-06-30T14:00:00Z', icon: 'shopping-cart' },
  { id: 'va-013', type: 'approval_granted', message: 'AgriBegri listing approved', productName: 'Button Mushroom', productId: 'prod-015', user: 'Marketing Manager', timestamp: '2026-06-29T10:00:00Z', icon: 'thumbs-up' },
  { id: 'va-014', type: 'validation_run', message: 'Full validation after bulk metadata update', productName: 'All Products', productId: 'all', user: 'QA Engine', timestamp: '2026-06-28T12:00:00Z', icon: 'check-circle' },
  { id: 'va-015', type: 'rejection', message: 'Packaging validation failed — dimensions missing', productName: 'Hydroponic Starter Kit', productId: 'prod-009', user: 'QA Engineer', timestamp: '2026-06-27T15:00:00Z', icon: 'x-circle' },
];
