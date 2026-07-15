export interface SeoActivityEvent {
  id: string;
  type: 'seo_updated' | 'meta_updated' | 'schema_added' | 'url_changed' | 'published' | 'archived' | 'score_changed' | 'marketplace_updated';
  message: string;
  entityName: string;
  entityId: string;
  user: string;
  timestamp: string;
  icon: string;
}

export const MOCK_SEO_ACTIVITY: SeoActivityEvent[] = [
  { id: 'sa-001', type: 'seo_updated', message: 'Updated SEO metadata for 3 products', entityName: 'Bulk Update', entityId: 'bulk-001', user: 'SEO Manager', timestamp: '2026-07-14T10:00:00Z', icon: 'search' },
  { id: 'sa-002', type: 'meta_updated', message: 'Optimized meta title and description', entityName: 'Premium Basmati Rice 5kg', entityId: 'seo-002', user: 'SEO Manager', timestamp: '2026-07-13T14:30:00Z', icon: 'file-text' },
  { id: 'sa-003', type: 'schema_added', message: 'Added FAQ schema to mushroom kit', entityName: 'Oyster Mushroom Grow Kit', entityId: 'seo-003', user: 'SEO Manager', timestamp: '2026-07-12T11:00:00Z', icon: 'code' },
  { id: 'sa-004', type: 'url_changed', message: 'Updated canonical URL', entityName: 'Hydroponic Starter Kit', entityId: 'seo-004', user: 'SEO Manager', timestamp: '2026-07-11T09:00:00Z', icon: 'link' },
  { id: 'sa-005', type: 'published', message: 'Published to all sales channels', entityName: 'Fresh Organic Tomatoes', entityId: 'seo-001', user: 'Marketing Manager', timestamp: '2026-07-12T10:00:00Z', icon: 'send' },
  { id: 'sa-006', type: 'score_changed', message: 'SEO Health Score improved from 72 to 88', entityName: 'Fresh Organic Tomatoes', entityId: 'seo-001', user: 'SEO Manager', timestamp: '2026-07-11T16:00:00Z', icon: 'trending-up' },
  { id: 'sa-007', type: 'marketplace_updated', message: 'Amazon listing updated with new images', entityName: 'Organic Compost 25kg', entityId: 'seo-007', user: 'Marketing Manager', timestamp: '2026-07-10T14:00:00Z', icon: 'shopping-cart' },
  { id: 'sa-008', type: 'schema_added', message: 'Generated JSON-LD for all product variants', entityName: 'Button Mushroom', entityId: 'seo-005', user: 'System', timestamp: '2026-07-09T10:00:00Z', icon: 'code' },
  { id: 'sa-009', type: 'archived', message: 'Archived outdated product listing', entityName: 'Shiitake Spawn Bags 10pk', entityId: 'seo-006', user: 'Marketing Manager', timestamp: '2026-07-08T15:00:00Z', icon: 'archive' },
  { id: 'sa-010', type: 'score_changed', message: 'AI Readiness Score updated', entityName: 'Hydroponic Starter Kit', entityId: 'seo-004', user: 'System', timestamp: '2026-07-07T09:00:00Z', icon: 'cpu' },
  { id: 'sa-011', type: 'meta_updated', message: 'Updated focus keyword strategy', entityName: 'Oyster Mushroom Grow Kit', entityId: 'seo-003', user: 'SEO Manager', timestamp: '2026-07-06T11:00:00Z', icon: 'file-text' },
  { id: 'sa-012', type: 'published', message: 'Scheduled publishing for next week', entityName: 'Hydroponic Starter Kit', entityId: 'seo-004', user: 'Marketing Manager', timestamp: '2026-07-05T14:00:00Z', icon: 'calendar' },
  { id: 'sa-013', type: 'marketplace_updated', message: 'AgriBegri listing approved', entityName: 'Organic Compost 25kg', entityId: 'seo-007', user: 'System', timestamp: '2026-07-04T12:00:00Z', icon: 'shopping-cart' },
  { id: 'sa-014', type: 'seo_updated', message: 'Bulk keyword update for 5 products', entityName: 'Bulk Operation', entityId: 'bulk-002', user: 'SEO Manager', timestamp: '2026-07-03T10:00:00Z', icon: 'search' },
  { id: 'sa-015', type: 'url_changed', message: 'Slug updated for SEO optimization', entityName: 'Oyster Mushroom Grow Kit', entityId: 'seo-003', user: 'SEO Manager', timestamp: '2026-07-02T16:00:00Z', icon: 'link' },
];
