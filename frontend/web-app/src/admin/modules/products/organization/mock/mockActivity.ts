import type { OrgActivityEvent } from '../types';

export const MOCK_ACTIVITY_EVENTS: OrgActivityEvent[] = [
  { id: 'act-001', type: 'created', entityType: 'category', entityId: 'cat-fresh-king', entityName: 'King Oyster', message: 'Created category "King Oyster" under Fresh Mushrooms', actor: 'Anita Sharma', timestamp: '2026-06-15T10:30:00Z' },
  { id: 'act-002', type: 'edited', entityType: 'brand', entityId: 'brd-001', entityName: 'SporeKart Originals', message: 'Updated description and website URL for SporeKart Originals', actor: 'Rahul Verma', timestamp: '2026-06-15T09:15:00Z' },
  { id: 'act-003', type: 'assigned', entityType: 'tag', entityId: 'tag-001', entityName: 'organic', message: 'Assigned "organic" tag to 5 products in Fresh Mushrooms', actor: 'Priya Patel', timestamp: '2026-06-14T16:45:00Z' },
  { id: 'act-004', type: 'moved', entityType: 'category', entityId: 'cat-fresh-beech', entityName: 'Beech', message: 'Moved "Beech" category to display order 7 under Fresh Mushrooms', actor: 'Anita Sharma', timestamp: '2026-06-14T14:20:00Z' },
  { id: 'act-005', type: 'created', entityType: 'collection', entityId: 'org-col-005', entityName: 'New Arrivals', message: 'Created "New Arrivals" featured collection', actor: 'Rahul Verma', timestamp: '2026-06-14T11:00:00Z' },
  { id: 'act-006', type: 'edited', entityType: 'category', entityId: 'cat-dried-porcini', entityName: 'Porcini', message: 'Updated description and featured status for Porcini category', actor: 'Priya Patel', timestamp: '2026-06-13T15:30:00Z' },
  { id: 'act-007', type: 'archived', entityType: 'brand', entityId: 'brd-009', entityName: 'WildTrails', message: 'Archived brand "WildTrails" due to forest clearance pending', actor: 'Suresh Kumar', timestamp: '2026-06-13T10:00:00Z' },
  { id: 'act-008', type: 'restored', entityType: 'category', entityId: 'cat-archived-sample', entityName: 'Archived Sample Category', message: 'Restored "Archived Sample Category" from archive', actor: 'Anita Sharma', timestamp: '2026-06-12T09:45:00Z' },
  { id: 'act-009', type: 'merged', entityType: 'tag', entityId: 'tag-004', entityName: 'wild', message: 'Merged "wild-foraged" tag into "wild" tag', actor: 'System', timestamp: '2026-06-12T08:00:00Z' },
  { id: 'act-010', type: 'created', entityType: 'brand', entityId: 'brd-010', entityName: 'BioCultivate', message: 'Created "BioCultivate" brand for training products', actor: 'Suresh Kumar', timestamp: '2026-06-11T14:30:00Z' },
  { id: 'act-011', type: 'removed', entityType: 'collection', entityId: 'org-col-016', entityName: 'Archived Products', message: 'Removed 2 products from "Archived Products" collection', actor: 'Rahul Verma', timestamp: '2026-06-11T11:15:00Z' },
  { id: 'act-012', type: 'edited', entityType: 'tag', entityId: 'tag-013', entityName: 'new-arrival', message: 'Changed tag type from "marketing" to "product" for "new-arrival"', actor: 'Priya Patel', timestamp: '2026-06-10T16:00:00Z' },
  { id: 'act-013', type: 'viewed', entityType: 'category', entityId: 'cat-fresh', entityName: 'Fresh Mushrooms', message: 'Category "Fresh Mushrooms" details viewed by manager', actor: 'Rahul Verma', timestamp: '2026-06-10T10:30:00Z' },
  { id: 'act-014', type: 'assigned', entityType: 'category', entityId: 'cat-spawn-kit', entityName: 'Growing Kits', message: 'Assigned 3 new products to "Growing Kits" category', actor: 'Priya Patel', timestamp: '2026-06-09T15:00:00Z' },
  { id: 'act-015', type: 'created', entityType: 'tag', entityId: 'tag-029', entityName: 'pink', message: 'Created "pink" color tag for pink oyster mushrooms', actor: 'Anita Sharma', timestamp: '2026-06-09T09:30:00Z' },
];
