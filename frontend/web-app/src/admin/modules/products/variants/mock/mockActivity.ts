export interface VariantActivityEvent {
  id: string;
  type: 'variant_created' | 'variant_updated' | 'variant_archived' | 'sku_generated' | 'attribute_added' | 'packaging_assigned' | 'bulk_operation' | 'variant_duplicated';
  message: string;
  entityName: string;
  entityId: string;
  user: string;
  timestamp: string;
  icon: string;
}

export const MOCK_VARIANT_ACTIVITY: VariantActivityEvent[] = [
  { id: 'va-001', type: 'variant_created', message: 'Created 3 new Fresh Mushroom variants', entityName: 'Button Mushroom', entityId: 'prod-015', user: 'Product Manager', timestamp: '2026-07-14T10:00:00Z', icon: 'layers' },
  { id: 'va-002', type: 'variant_updated', message: 'Updated pricing for Dried Shiitake 250g', entityName: 'Dried Shiitake Mushroom 250g', entityId: 'var-005', user: 'Pricing Manager', timestamp: '2026-07-13T14:30:00Z', icon: 'edit' },
  { id: 'va-003', type: 'sku_generated', message: 'Auto-generated SKU for 5 spawn variants', entityName: 'Oyster Mushroom Spawn', entityId: 'var-group-spawn', user: 'System', timestamp: '2026-07-12T09:00:00Z', icon: 'tag' },
  { id: 'va-004', type: 'attribute_added', message: 'Added "Cultivation Method" attribute to spawn group', entityName: 'Spawn Attributes', entityId: 'attr-def-cultivation', user: 'Admin User', timestamp: '2026-07-11T11:00:00Z', icon: 'list' },
  { id: 'va-005', type: 'packaging_assigned', message: 'Assigned Master Carton packaging to all fresh variants', entityName: 'Fresh Mushroom Group', entityId: 'var-group-fresh-mushroom', user: 'Product Manager', timestamp: '2026-07-10T16:00:00Z', icon: 'box' },
  { id: 'va-006', type: 'variant_archived', message: 'Archived Dried Shiitake 500g variant', entityName: 'Dried Shiitake Mushroom 500g', entityId: 'var-006', user: 'Product Manager', timestamp: '2026-07-09T10:00:00Z', icon: 'archive' },
  { id: 'va-007', type: 'bulk_operation', message: 'Bulk assigned packaging to 6 spawn variants', entityName: 'Spawn Products', entityId: 'var-group-spawn', user: 'Admin User', timestamp: '2026-07-08T15:00:00Z', icon: 'multiple' },
  { id: 'va-008', type: 'variant_created', message: 'Added Premium Kit variant (24 plant system)', entityName: 'Hydroponic Kit - Premium', entityId: 'var-012', user: 'Product Manager', timestamp: '2026-07-07T09:30:00Z', icon: 'layers' },
  { id: 'va-009', type: 'variant_duplicated', message: 'Duplicated Oyster 1kg to create 5kg variant', entityName: 'Oyster Mushroom Spawn 5kg', entityId: 'var-009', user: 'Product Manager', timestamp: '2026-07-06T14:00:00Z', icon: 'copy' },
  { id: 'va-010', type: 'variant_updated', message: 'Updated specifications for Beginner Kit', entityName: 'Hydroponic Kit - Beginner', entityId: 'var-010', user: 'Product Manager', timestamp: '2026-07-05T11:00:00Z', icon: 'edit' },
  { id: 'va-011', type: 'sku_generated', message: 'Manual SKU assigned for KIT-HYD-BEG', entityName: 'Hydroponic Kit - Beginner', entityId: 'var-010', user: 'Admin User', timestamp: '2026-07-04T10:00:00Z', icon: 'tag' },
  { id: 'va-012', type: 'attribute_added', message: 'Created "Warranty" attribute definition', entityName: 'Attribute Definitions', entityId: 'attr-def-warranty', user: 'Admin User', timestamp: '2026-07-03T15:00:00Z', icon: 'list' },
  { id: 'va-013', type: 'bulk_operation', message: 'Generated barcodes for 5 fresh variants', entityName: 'Fresh Mushroom Group', entityId: 'var-group-fresh-mushroom', user: 'System', timestamp: '2026-07-02T09:00:00Z', icon: 'multiple' },
  { id: 'va-014', type: 'packaging_assigned', message: 'Updated storage instructions for Kit packaging', entityName: 'Kit Box Standard', entityId: 'pkg-011', user: 'Compliance Team', timestamp: '2026-07-01T14:00:00Z', icon: 'box' },
  { id: 'va-015', type: 'variant_created', message: 'Created Tomato variants (500g, 1kg, 2kg)', entityName: 'Fresh Organic Tomatoes', entityId: 'prod-001', user: 'Product Manager', timestamp: '2026-06-30T10:00:00Z', icon: 'layers' },
];
