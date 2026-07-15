import type { OrgTag } from '../types';

export const MOCK_TAGS: OrgTag[] = [
  { id: 'tag-001', name: 'organic', slug: 'organic', tagType: 'keyword', description: 'Organically certified products.', usageCount: 25, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-002', name: 'fresh', slug: 'fresh', tagType: 'keyword', description: 'Freshly harvested products.', usageCount: 30, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-14T00:00:00Z' },
  { id: 'tag-003', name: 'dried', slug: 'dried', tagType: 'keyword', description: 'Dehydrated mushroom products.', usageCount: 18, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-13T00:00:00Z' },
  { id: 'tag-004', name: 'wild', slug: 'wild', tagType: 'keyword', description: 'Wild-foraged mushroom varieties.', usageCount: 8, isSystem: false, createdAt: '2026-01-02T00:00:00Z', updatedAt: '2026-06-12T00:00:00Z' },
  { id: 'tag-005', name: 'cultivated', slug: 'cultivated', tagType: 'keyword', description: 'Commercially cultivated mushrooms.', usageCount: 35, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-006', name: 'premium', slug: 'premium', tagType: 'keyword', description: 'Premium grade products.', usageCount: 15, isSystem: false, createdAt: '2026-01-05T00:00:00Z', updatedAt: '2026-06-11T00:00:00Z' },
  { id: 'tag-007', name: 'bulk', slug: 'bulk', tagType: 'keyword', description: 'Bulk quantity products for commercial buyers.', usageCount: 10, isSystem: false, createdAt: '2026-01-10T00:00:00Z', updatedAt: '2026-06-10T00:00:00Z' },
  { id: 'tag-008', name: 'mushroom', slug: 'mushroom', tagType: 'search', description: 'General mushroom search tag.', usageCount: 50, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-009', name: 'edible', slug: 'edible', tagType: 'search', description: 'Edible mushroom varieties.', usageCount: 40, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-14T00:00:00Z' },
  { id: 'tag-010', name: 'gourmet', slug: 'gourmet', tagType: 'search', description: 'Gourmet and specialty mushrooms.', usageCount: 20, isSystem: false, createdAt: '2026-01-03T00:00:00Z', updatedAt: '2026-06-13T00:00:00Z' },
  { id: 'tag-011', name: 'best-mushrooms', slug: 'best-mushrooms', tagType: 'seo', description: 'SEO tag for best mushroom sources.', usageCount: 5, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'tag-012', name: 'buy-online', slug: 'buy-online', tagType: 'seo', description: 'SEO tag for online mushroom purchases.', usageCount: 8, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-01T00:00:00Z' },
  { id: 'tag-013', name: 'new-arrival', slug: 'new-arrival', tagType: 'marketing', description: 'Newly added products.', usageCount: 6, isSystem: false, createdAt: '2026-06-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-014', name: 'best-seller', slug: 'best-seller', tagType: 'marketing', description: 'Top selling products.', usageCount: 12, isSystem: false, createdAt: '2026-03-01T00:00:00Z', updatedAt: '2026-06-14T00:00:00Z' },
  { id: 'tag-015', name: 'limited-edition', slug: 'limited-edition', tagType: 'marketing', description: 'Limited availability products.', usageCount: 4, isSystem: false, createdAt: '2026-04-01T00:00:00Z', updatedAt: '2026-06-10T00:00:00Z' },
  { id: 'tag-016', name: 'summer-sale', slug: 'summer-sale', tagType: 'campaign', description: 'Summer sale campaign products.', usageCount: 8, isSystem: false, createdAt: '2026-05-01T00:00:00Z', updatedAt: '2026-06-12T00:00:00Z' },
  { id: 'tag-017', name: 'festive-offer', slug: 'festive-offer', tagType: 'campaign', description: 'Festive season promotional products.', usageCount: 10, isSystem: false, createdAt: '2026-09-01T00:00:00Z', updatedAt: '2026-06-11T00:00:00Z' },
  { id: 'tag-018', name: 'needs-review', slug: 'needs-review', tagType: 'internal', description: 'Products pending internal review.', usageCount: 3, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-05T00:00:00Z' },
  { id: 'tag-019', name: 'qc-passed', slug: 'qc-passed', tagType: 'internal', description: 'Products that passed quality control.', usageCount: 30, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-020', name: 'discontinued', slug: 'discontinued', tagType: 'internal', description: 'Discontinued products.', usageCount: 2, isSystem: true, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-05-01T00:00:00Z' },
  { id: 'tag-021', name: 'vegetarian', slug: 'vegetarian', tagType: 'product', description: 'Suitable for vegetarians.', usageCount: 40, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-15T00:00:00Z' },
  { id: 'tag-022', name: 'vegan', slug: 'vegan', tagType: 'product', description: 'Suitable for vegans.', usageCount: 35, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-14T00:00:00Z' },
  { id: 'tag-023', name: 'gluten-free', slug: 'gluten-free', tagType: 'product', description: 'Naturally gluten-free products.', usageCount: 28, isSystem: false, createdAt: '2026-01-02T00:00:00Z', updatedAt: '2026-06-13T00:00:00Z' },
  { id: 'tag-024', name: 'keto-friendly', slug: 'keto-friendly', tagType: 'product', description: 'Keto diet compatible products.', usageCount: 15, isSystem: false, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-06-12T00:00:00Z' },
  { id: 'tag-025', name: 'protein-rich', slug: 'protein-rich', tagType: 'product', description: 'High protein mushroom products.', usageCount: 22, isSystem: false, createdAt: '2026-01-05T00:00:00Z', updatedAt: '2026-06-11T00:00:00Z' },
  { id: 'tag-026', name: 'white', slug: 'white', tagType: 'color', color: '#F5F5F5', description: 'White colored mushrooms.', usageCount: 15, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-10T00:00:00Z' },
  { id: 'tag-027', name: 'brown', slug: 'brown', tagType: 'color', color: '#8B4513', description: 'Brown colored mushrooms.', usageCount: 12, isSystem: false, createdAt: '2026-01-01T00:00:00Z', updatedAt: '2026-06-09T00:00:00Z' },
  { id: 'tag-028', name: 'golden', slug: 'golden', tagType: 'color', color: '#DAA520', description: 'Golden colored mushrooms.', usageCount: 5, isSystem: false, createdAt: '2026-01-03T00:00:00Z', updatedAt: '2026-06-08T00:00:00Z' },
  { id: 'tag-029', name: 'pink', slug: 'pink', tagType: 'color', color: '#FF69B4', description: 'Pink colored oyster mushrooms.', usageCount: 3, isSystem: false, createdAt: '2026-02-01T00:00:00Z', updatedAt: '2026-06-07T00:00:00Z' },
];

export function getTagById(id: string): OrgTag | undefined {
  return MOCK_TAGS.find((t) => t.id === id);
}

export function getTagsByType(type: string): OrgTag[] {
  return MOCK_TAGS.filter((t) => t.tagType === type);
}
