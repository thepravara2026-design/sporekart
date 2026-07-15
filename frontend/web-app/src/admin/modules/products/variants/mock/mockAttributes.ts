import type { AttributeDefinition } from '../types';

export const MOCK_ATTRIBUTE_DEFINITIONS: AttributeDefinition[] = [
  // General
  { id: 'attr-def-weight', name: 'weight', label: 'Weight', type: 'weight', group: 'general', options: ['200 g', '400 g', '500 g', '1 kg', '2 kg', '5 kg'], required: true, filterable: true, comparable: true, displayOrder: 1, description: 'Product net weight' },
  { id: 'attr-def-size', name: 'size', label: 'Size', type: 'select', group: 'general', options: ['Small', 'Medium', 'Large', 'Extra Large'], required: false, filterable: true, comparable: true, displayOrder: 2 },
  { id: 'attr-def-kit-type', name: 'kitType', label: 'Kit Type', type: 'select', group: 'general', options: ['Beginner', 'Standard', 'Premium', 'Professional'], required: false, filterable: true, comparable: true, displayOrder: 3 },
  { id: 'attr-def-difficulty', name: 'difficulty', label: 'Difficulty', type: 'select', group: 'general', options: ['Easy', 'Medium', 'Advanced'], required: false, filterable: true, comparable: false, displayOrder: 4 },
  // Agriculture
  { id: 'attr-def-organic', name: 'organic', label: 'Organic', type: 'boolean', group: 'agriculture', required: true, filterable: true, comparable: true, displayOrder: 10, description: 'Certified organic product' },
  { id: 'attr-def-grade', name: 'grade', label: 'Grade', type: 'select', group: 'agriculture', options: ['Premium', 'A', 'B', 'Standard'], required: true, filterable: true, comparable: true, displayOrder: 11, description: 'Product quality grade' },
  { id: 'attr-def-cultivation', name: 'cultivationMethod', label: 'Cultivation Method', type: 'select', group: 'agriculture', options: ['Organic', 'Conventional', 'Hydroponic', 'Straw Based', 'Indoor', 'Greenhouse'], required: false, filterable: true, comparable: true, displayOrder: 12 },
  { id: 'attr-def-yield', name: 'yield', label: 'Expected Yield', type: 'text', group: 'agriculture', required: false, filterable: false, comparable: true, displayOrder: 13 },
  // Packaging
  { id: 'attr-def-package-type', name: 'packageType', label: 'Package Type', type: 'select', group: 'packaging', options: ['Pouch', 'Bag', 'Box', 'Tray', 'Jar', 'Bottle', 'Carton', 'Crate', 'Bundle'], required: true, filterable: true, comparable: true, displayOrder: 20 },
  { id: 'attr-def-material', name: 'material', label: 'Packaging Material', type: 'select', group: 'packaging', options: ['Plastic', 'Paper', 'Glass', 'Metal', 'Wood', 'Fabric', 'Biodegradable'], required: false, filterable: true, comparable: false, displayOrder: 21 },
  // Storage
  { id: 'attr-def-shelf-life', name: 'shelfLife', label: 'Shelf Life', type: 'text', group: 'storage', required: true, filterable: false, comparable: true, displayOrder: 30 },
  { id: 'attr-def-storage', name: 'storage', label: 'Storage Instructions', type: 'text', group: 'storage', required: true, filterable: false, comparable: false, displayOrder: 31 },
  // Technical
  { id: 'attr-def-plants-capacity', name: 'plantsCapacity', label: 'Plants Capacity', type: 'text', group: 'technical', required: false, filterable: true, comparable: true, displayOrder: 40 },
  { id: 'attr-def-warranty', name: 'warranty', label: 'Warranty', type: 'text', group: 'technical', required: false, filterable: false, comparable: true, displayOrder: 41 },
  // Marketing
  { id: 'attr-def-includes', name: 'includes', label: 'What\'s Included', type: 'text', group: 'marketing', required: false, filterable: false, comparable: false, displayOrder: 50, description: 'Items included in the package' },
];

export const ATTRIBUTE_GROUPS = ['general', 'agriculture', 'packaging', 'storage', 'technical', 'marketing', 'usage', 'safety', 'compliance'];

export function getAttributesByGroup(): Record<string, AttributeDefinition[]> {
  const groups: Record<string, AttributeDefinition[]> = {};
  for (const attr of MOCK_ATTRIBUTE_DEFINITIONS) {
    if (!groups[attr.group]) groups[attr.group] = [];
    groups[attr.group].push(attr);
  }
  return groups;
}
