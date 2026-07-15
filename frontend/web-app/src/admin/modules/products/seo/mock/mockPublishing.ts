import type { PublishingStatus } from '../types';

export interface PublishingEvent {
  id: string;
  productId: string;
  productName: string;
  fromStatus: PublishingStatus;
  toStatus: PublishingStatus;
  changedBy: string;
  changedAt: string;
  notes?: string;
}

export const MOCK_PUBLISHING_EVENTS: PublishingEvent[] = [
  { id: 'pub-001', productId: 'prod-001', productName: 'Fresh Organic Tomatoes', fromStatus: 'draft', toStatus: 'ready_seo', changedBy: 'Content Writer', changedAt: '2026-07-10T09:00:00Z', notes: 'Initial content created' },
  { id: 'pub-002', productId: 'prod-001', productName: 'Fresh Organic Tomatoes', fromStatus: 'ready_seo', toStatus: 'ready_publish', changedBy: 'SEO Manager', changedAt: '2026-07-11T14:00:00Z', notes: 'SEO optimized' },
  { id: 'pub-003', productId: 'prod-001', productName: 'Fresh Organic Tomatoes', fromStatus: 'ready_publish', toStatus: 'published', changedBy: 'Marketing Manager', changedAt: '2026-07-12T10:00:00Z', notes: 'Published to all channels' },
  { id: 'pub-004', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', fromStatus: 'draft', toStatus: 'ready_seo', changedBy: 'Content Writer', changedAt: '2026-07-08T11:00:00Z' },
  { id: 'pub-005', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', fromStatus: 'ready_seo', toStatus: 'ready_publish', changedBy: 'SEO Manager', changedAt: '2026-07-09T15:00:00Z', notes: 'Schema added' },
  { id: 'pub-006', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', fromStatus: 'ready_publish', toStatus: 'published', changedBy: 'Marketing Manager', changedAt: '2026-07-10T09:00:00Z' },
  { id: 'pub-007', productId: 'prod-009', productName: 'Hydroponic Starter Kit', fromStatus: 'draft', toStatus: 'ready_seo', changedBy: 'Content Writer', changedAt: '2026-07-05T10:00:00Z' },
  { id: 'pub-008', productId: 'prod-009', productName: 'Hydroponic Starter Kit', fromStatus: 'ready_seo', toStatus: 'ready_publish', changedBy: 'SEO Manager', changedAt: '2026-07-07T14:00:00Z', notes: 'Meta updated' },
  { id: 'pub-009', productId: 'prod-015', productName: 'Button Mushroom', fromStatus: 'draft', toStatus: 'ready_seo', changedBy: 'Content Writer', changedAt: '2026-07-06T09:00:00Z' },
  { id: 'pub-010', productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk', fromStatus: 'draft', toStatus: 'review_required', changedBy: 'SEO Manager', changedAt: '2026-07-04T16:00:00Z', notes: 'Missing meta description and images' },
];

export const PUBLISHING_STATUS_FLOW: PublishingStatus[] = ['draft', 'ready_seo', 'ready_publish', 'published', 'archived'];

export const PUBLISHING_STATUS_COLORS: Record<PublishingStatus, string> = {
  draft: 'var(--color-accent-yellow)',
  ready_seo: 'var(--color-accent-blue)',
  ready_publish: 'var(--color-accent-purple)',
  published: 'var(--color-accent-green)',
  scheduled: 'var(--color-accent-cyan)',
  archived: 'var(--color-accent-red)',
  rejected: 'var(--color-accent-orange)',
  review_required: 'var(--color-accent-orange)',
};
