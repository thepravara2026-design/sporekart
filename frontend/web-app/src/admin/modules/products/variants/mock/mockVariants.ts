import type { ProductVariant, VariantAttribute } from '../types';

function attr(name: string, value: string, group?: string): VariantAttribute {
  return { id: `attr-${name}`, name, value, type: name === 'Organic' ? 'boolean' : name === 'Weight' ? 'weight' : name === 'Shelf Life' ? 'text' : 'select', group: group ?? 'general', displayOrder: 0 };
}

export const MOCK_VARIANTS: ProductVariant[] = [
  // Fresh Mushroom variants
  {
    id: 'var-001', name: 'Fresh Button Mushroom 200g', sku: 'FR-MSH-200', productId: 'prod-015', productName: 'Button Mushroom',
    parentId: 'var-group-fresh-mushroom', attributes: [attr('Weight', '200 g', 'general'), attr('Package Type', 'Tray', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'A', 'agriculture'), attr('Shelf Life', '7 days', 'storage'), attr('Storage', 'Refrigerated 2-4°C', 'storage')],
    status: 'active', isDefault: true, priority: 1, price: 160, stock: 500, createdAt: '2026-01-15T00:00:00Z', updatedAt: '2026-07-10T00:00:00Z',
  },
  {
    id: 'var-002', name: 'Fresh Button Mushroom 400g', sku: 'FR-MSH-400', productId: 'prod-015', productName: 'Button Mushroom',
    parentId: 'var-group-fresh-mushroom', attributes: [attr('Weight', '400 g', 'general'), attr('Package Type', 'Tray', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'A', 'agriculture'), attr('Shelf Life', '7 days', 'storage'), attr('Storage', 'Refrigerated 2-4°C', 'storage')],
    status: 'active', isDefault: false, priority: 2, price: 290, stock: 300, createdAt: '2026-01-15T00:00:00Z', updatedAt: '2026-07-08T00:00:00Z',
  },
  {
    id: 'var-003', name: 'Fresh Button Mushroom 1kg', sku: 'FR-MSH-1KG', productId: 'prod-015', productName: 'Button Mushroom',
    parentId: 'var-group-fresh-mushroom', attributes: [attr('Weight', '1 kg', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'B', 'agriculture'), attr('Shelf Life', '5 days', 'storage'), attr('Storage', 'Refrigerated 2-4°C', 'storage')],
    status: 'active', isDefault: false, priority: 3, price: 580, stock: 150, createdAt: '2026-01-15T00:00:00Z', updatedAt: '2026-07-05T00:00:00Z',
  },
  // Dry Mushroom variants
  {
    id: 'var-004', name: 'Dried Shiitake Mushroom 100g', sku: 'DR-SHT-100', productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk',
    parentId: 'var-group-dry-mushroom', attributes: [attr('Weight', '100 g', 'general'), attr('Package Type', 'Pouch', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'Premium', 'agriculture'), attr('Shelf Life', '12 months', 'storage'), attr('Storage', 'Cool dry place', 'storage')],
    status: 'active', isDefault: true, priority: 1, price: 350, stock: 200, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-07-12T00:00:00Z',
  },
  {
    id: 'var-005', name: 'Dried Shiitake Mushroom 250g', sku: 'DR-SHT-250', productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk',
    parentId: 'var-group-dry-mushroom', attributes: [attr('Weight', '250 g', 'general'), attr('Package Type', 'Pouch', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'Premium', 'agriculture'), attr('Shelf Life', '12 months', 'storage'), attr('Storage', 'Cool dry place', 'storage')],
    status: 'active', isDefault: false, priority: 2, price: 750, stock: 120, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-07-11T00:00:00Z',
  },
  {
    id: 'var-006', name: 'Dried Shiitake Mushroom 500g', sku: 'DR-SHT-500', productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk',
    parentId: 'var-group-dry-mushroom', attributes: [attr('Weight', '500 g', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'Standard', 'agriculture'), attr('Shelf Life', '12 months', 'storage'), attr('Storage', 'Cool dry place', 'storage')],
    status: 'inactive', isDefault: false, priority: 3, price: 1300, stock: 60, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-06-30T00:00:00Z',
  },
  // Spawn Seeds variants
  {
    id: 'var-007', name: 'Oyster Mushroom Spawn 350g', sku: 'SP-OST-350', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit',
    parentId: 'var-group-spawn', attributes: [attr('Weight', '350 g', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Cultivation Method', 'Straw Based', 'agriculture'), attr('Yield', '2-3 kg', 'agriculture'), attr('Shelf Life', '6 months', 'storage'), attr('Storage', 'Refrigerated 2-8°C', 'storage')],
    status: 'active', isDefault: true, priority: 1, price: 290, stock: 400, createdAt: '2026-03-01T00:00:00Z', updatedAt: '2026-07-09T00:00:00Z',
  },
  {
    id: 'var-008', name: 'Oyster Mushroom Spawn 1kg', sku: 'SP-OST-1KG', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit',
    parentId: 'var-group-spawn', attributes: [attr('Weight', '1 kg', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Cultivation Method', 'Straw Based', 'agriculture'), attr('Yield', '5-8 kg', 'agriculture'), attr('Shelf Life', '6 months', 'storage'), attr('Storage', 'Refrigerated 2-8°C', 'storage')],
    status: 'active', isDefault: false, priority: 2, price: 650, stock: 200, createdAt: '2026-03-01T00:00:00Z', updatedAt: '2026-07-07T00:00:00Z',
  },
  {
    id: 'var-009', name: 'Oyster Mushroom Spawn 5kg', sku: 'SP-OST-5KG', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit',
    parentId: 'var-group-spawn', attributes: [attr('Weight', '5 kg', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Cultivation Method', 'Straw Based', 'agriculture'), attr('Yield', '20-30 kg', 'agriculture'), attr('Shelf Life', '4 months', 'storage'), attr('Storage', 'Refrigerated 2-8°C', 'storage')],
    status: 'draft', isDefault: false, priority: 3, price: 2500, stock: 0, createdAt: '2026-06-01T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z',
  },
  // Growing Kit variants
  {
    id: 'var-010', name: 'Hydroponic Kit - Beginner', sku: 'KIT-HYD-BEG', productId: 'prod-009', productName: 'Hydroponic Starter Kit',
    parentId: 'var-group-kit', attributes: [attr('Kit Type', 'Beginner', 'general'), attr('Package Type', 'Box', 'packaging'), attr('Plants Capacity', '6 plants', 'technical'), attr('Difficulty', 'Easy', 'general'), attr('Includes', 'Basic nutrients, net pots, grow medium', 'marketing'), attr('Warranty', '6 months', 'technical')],
    status: 'active', isDefault: true, priority: 1, price: 1499, stock: 100, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-07-06T00:00:00Z',
  },
  {
    id: 'var-011', name: 'Hydroponic Kit - Standard', sku: 'KIT-HYD-STD', productId: 'prod-009', productName: 'Hydroponic Starter Kit',
    parentId: 'var-group-kit', attributes: [attr('Kit Type', 'Standard', 'general'), attr('Package Type', 'Box', 'packaging'), attr('Plants Capacity', '12 plants', 'technical'), attr('Difficulty', 'Medium', 'general'), attr('Includes', 'Advanced nutrients, pH kit, air pump, net pots', 'marketing'), attr('Warranty', '12 months', 'technical')],
    status: 'active', isDefault: false, priority: 2, price: 2499, stock: 75, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-07-04T00:00:00Z',
  },
  {
    id: 'var-012', name: 'Hydroponic Kit - Premium', sku: 'KIT-HYD-PRM', productId: 'prod-009', productName: 'Hydroponic Starter Kit',
    parentId: 'var-group-kit', attributes: [attr('Kit Type', 'Premium', 'general'), attr('Package Type', 'Box', 'packaging'), attr('Plants Capacity', '24 plants', 'technical'), attr('Difficulty', 'Advanced', 'general'), attr('Includes', 'Full nutrient set, pH/EC meter, auto timer, air pump, LED grow light', 'marketing'), attr('Warranty', '24 months', 'technical')],
    status: 'active', isDefault: false, priority: 3, price: 4499, stock: 30, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-07-01T00:00:00Z',
  },
  // Tomatoes variant
  {
    id: 'var-013', name: 'Fresh Organic Tomatoes 500g', sku: 'FR-TOM-500', productId: 'prod-001', productName: 'Fresh Organic Tomatoes',
    attributes: [attr('Weight', '500 g', 'general'), attr('Package Type', 'Pouch', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'A', 'agriculture'), attr('Shelf Life', '5 days', 'storage'), attr('Storage', 'Ambient 18-25°C', 'storage')],
    status: 'active', isDefault: true, priority: 1, price: 40, stock: 1000, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-07-13T00:00:00Z',
  },
  {
    id: 'var-014', name: 'Fresh Organic Tomatoes 1kg', sku: 'FR-TOM-1KG', productId: 'prod-001', productName: 'Fresh Organic Tomatoes',
    attributes: [attr('Weight', '1 kg', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'A', 'agriculture'), attr('Shelf Life', '5 days', 'storage'), attr('Storage', 'Ambient 18-25°C', 'storage')],
    status: 'active', isDefault: false, priority: 2, price: 75, stock: 600, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-07-12T00:00:00Z',
  },
  {
    id: 'var-015', name: 'Fresh Organic Tomatoes 2kg', sku: 'FR-TOM-2KG', productId: 'prod-001', productName: 'Fresh Organic Tomatoes',
    attributes: [attr('Weight', '2 kg', 'general'), attr('Package Type', 'Bag', 'packaging'), attr('Organic', 'Yes', 'agriculture'), attr('Grade', 'B', 'agriculture'), attr('Shelf Life', '4 days', 'storage'), attr('Storage', 'Ambient 18-25°C', 'storage')],
    status: 'active', isDefault: false, priority: 3, price: 130, stock: 300, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-07-10T00:00:00Z',
  },
];

export const MOCK_VARIANT_GROUPS = [
  { id: 'var-group-fresh-mushroom', name: 'Fresh Button Mushroom', description: 'Fresh mushroom variants by weight', variants: ['var-001', 'var-002', 'var-003'] },
  { id: 'var-group-dry-mushroom', name: 'Dried Shiitake Mushroom', description: 'Dried mushroom variants by weight', variants: ['var-004', 'var-005', 'var-006'] },
  { id: 'var-group-spawn', name: 'Oyster Mushroom Spawn', description: 'Spawn seeds by quantity', variants: ['var-007', 'var-008', 'var-009'] },
  { id: 'var-group-kit', name: 'Hydroponic Growing Kit', description: 'Kit variants by skill level', variants: ['var-010', 'var-011', 'var-012'] },
];
