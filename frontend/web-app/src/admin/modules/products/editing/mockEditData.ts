import type { ProductWizardData } from '../creation/types';
import type { ProductVersion, EditActivityEvent, EditingSettings } from './types';

export const MOCK_PRODUCT_ID = 'SK-PROD-1001';
export const MOCK_PRODUCT_SKU = 'SKU-GOK-001';
export const MOCK_MODIFIED_BY = 'a.manager@sporekart.com';

export const mockCurrentProduct: ProductWizardData = {
  name: 'Organic Pink Oyster Mushroom Grow Kit',
  description:
    'A complete at-home grow kit for vibrant pink oyster mushrooms. Includes substrate bag, misting bottle, and step-by-step guide. Harvest in 3–4 weeks.',
  shortDescription: 'Beginner-friendly pink oyster mushroom grow kit.',
  productType: 'growing_kit',
  brand: 'SporeKart',
  manufacturer: 'SporeKart Cultivation Labs',
  sku: MOCK_PRODUCT_SKU,
  barcode: '8901234560011',
  category: 'Grow Kits',
  collection: ['Bestsellers', 'New Arrivals'],
  tags: ['organic', 'beginner', 'indoorgrow'],
  status: 'active',
  productFamily: 'Mushroom Kits',
  productGroup: 'Gourmet',
  mushroomType: 'Pink Oyster',
  growingMethod: 'Substrate Bag',
  season: 'all',
  productNature: 'retail',
  attributes: ['heirloom', 'high-yield'],
  packagingType: 'box',
  packageSize: '24 × 18 × 12 cm',
  unitsPerPack: 1,
  weight: 1200,
  weightUnit: 'g',
  dimensions: { length: 24, width: 18, height: 12, unit: 'cm' },
  packageWeight: 1350,
  packageWeightUnit: 'g',
  shelfLife: '12 months',
  storageConditions: 'Cool, dry place away from direct sunlight',
  countryOfOrigin: 'India',
  packagingNotes: 'Fragile — handle with care. Include humidity pack.',
  mrp: 1499,
  price: 1199,
  wholesalePrice: 999,
  discount: 20,
  cost: 720,
  currency: 'INR',
  taxClass: 'standard',
  stockKeepingUnit: 'INV-GOK-001',
  hsnCode: '1212',
  gst: 5,
  priceNotes: 'Festive promo active until month-end.',
  metaTitle: 'Organic Pink Oyster Mushroom Grow Kit | SporeKart',
  metaDescription:
    'Buy the beginner-friendly Organic Pink Oyster Mushroom Grow Kit. Harvest fresh mushrooms at home in 3–4 weeks.',
  slug: 'pink-oyster-grow-kit',
  keywords: ['pink oyster', 'mushroom grow kit', 'organic'],
  canonicalUrl: 'https://sporekart.com/products/pink-oyster-grow-kit',
  ogTitle: 'Grow Pink Oysters at Home',
  ogDescription: 'Everything you need to cultivate gourmet pink oyster mushrooms.',
};

export const mockVersions: ProductVersion[] = [
  {
    id: 'ver-1',
    version: 1,
    status: 'published',
    modifiedBy: 'r.editor@sporekart.com',
    createdAt: '2026-04-02T09:12:00Z',
    reason: 'Initial creation',
    summary: 'Created product with core details and pricing.',
    data: {
      ...mockCurrentProduct,
      name: 'Pink Oyster Mushroom Grow Kit',
      price: 1299,
      mrp: 1599,
      discount: 18,
      metaTitle: 'Pink Oyster Mushroom Grow Kit',
      metaDescription: 'A mushroom grow kit.',
      keywords: ['mushroom', 'grow kit'],
      collection: ['New Arrivals'],
      tags: ['organic'],
    },
  },
  {
    id: 'ver-2',
    version: 2,
    status: 'published',
    modifiedBy: 'a.manager@sporekart.com',
    createdAt: '2026-05-18T14:40:00Z',
    reason: 'Pricing & SEO refresh',
    summary: 'Updated pricing, added SEO metadata and collections.',
    data: {
      ...mockCurrentProduct,
      price: 1249,
      mrp: 1499,
      discount: 16,
      metaTitle: 'Organic Pink Oyster Mushroom Grow Kit | SporeKart',
    },
  },
  {
    id: 'ver-3',
    version: 3,
    status: 'published',
    modifiedBy: 'a.manager@sporekart.com',
    createdAt: '2026-06-30T11:05:00Z',
    reason: 'Current working version',
    summary: 'Latest edits: packaging notes, wholesale price, OG tags.',
    data: mockCurrentProduct,
  },
];

export const mockActivity: EditActivityEvent[] = [
  { id: 'a1', type: 'created', message: 'Product created', actor: 'r.editor@sporekart.com', timestamp: '2026-04-02T09:12:00Z' },
  { id: 'a2', type: 'published', message: 'Product published', actor: 'a.manager@sporekart.com', timestamp: '2026-04-02T15:30:00Z' },
  { id: 'a3', type: 'edited', message: 'Updated pricing and SEO', actor: 'a.manager@sporekart.com', timestamp: '2026-05-18T14:40:00Z' },
  { id: 'a4', type: 'approved', message: 'Changes approved', actor: 'a.manager@sporekart.com', timestamp: '2026-05-18T14:45:00Z' },
  { id: 'a5', type: 'edited', message: 'Updated packaging notes and wholesale price', actor: 'a.manager@sporekart.com', timestamp: '2026-06-30T11:05:00Z' },
  { id: 'a6', type: 'draft_saved', message: 'Draft saved', actor: 'a.manager@sporekart.com', timestamp: '2026-06-30T11:10:00Z' },
  { id: 'a7', type: 'viewed', message: 'Product viewed in workspace', actor: 'viewer@sporekart.com', timestamp: '2026-07-10T08:20:00Z' },
];

export const mockSettings: EditingSettings = {
  requireApproval: true,
  autoPublish: false,
  lockOnReview: true,
  notifyOnPublish: true,
};
