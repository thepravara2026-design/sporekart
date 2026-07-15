import type { Product, ProductLifecycleState, ProductType } from '../types';
import { MOCK_BRANDS, MOCK_CATEGORIES, MOCK_COLLECTIONS } from './mockProducts';

export type StockStatus = 'in_stock' | 'low_stock' | 'out_of_stock' | 'preorder';

export type CatalogProduct = Product & {
  stockStatus: StockStatus;
  featured: boolean;
  hasImages: boolean;
  thumbUrl: string;
  rating?: number;
};

interface TypeDef {
  type: ProductType;
  category: string;
  categoryId: string;
  names: string[];
  publishingStatus: Product['publishingStatus'];
  hsnCode: string;
  gst: number;
  packaging: string;
  unit: string;
  weight: number;
}

const TYPE_DEFS: TypeDef[] = [
  {
    type: 'fresh_mushroom',
    category: 'Fresh Mushrooms',
    categoryId: 'cat-fresh',
    names: ['Oyster', 'King Oyster', 'Button', 'Shiitake', 'Enoki', 'Portobello', 'Cremini', "Lion's Mane"],
    publishingStatus: 'live',
    hsnCode: '0709',
    gst: 5,
    packaging: 'Punnet',
    unit: '250g',
    weight: 0.25,
  },
  {
    type: 'dry_mushroom',
    category: 'Dry Mushrooms',
    categoryId: 'cat-dry',
    names: ['Shiitake', 'Porcini', 'Morel', 'Truffle', 'Chanterelle', 'Wood Ear'],
    publishingStatus: 'live',
    hsnCode: '0712',
    gst: 5,
    packaging: 'Pouch',
    unit: '100g',
    weight: 0.1,
  },
  {
    type: 'spawn_seed',
    category: 'Spawn Seeds',
    categoryId: 'cat-spawn',
    names: ['Oyster', 'Shiitake', 'Milky', 'Button', 'Reishi'],
    publishingStatus: 'live',
    hsnCode: '0602',
    gst: 12,
    packaging: 'Vacuum bag',
    unit: '1kg',
    weight: 1,
  },
  {
    type: 'growing_kit',
    category: 'Growing Kits',
    categoryId: 'cat-kits',
    names: ['Beginner', 'Advanced', 'Tabletop', 'Family', 'Pro'],
    publishingStatus: 'live',
    hsnCode: '0602',
    gst: 12,
    packaging: 'Box',
    unit: '1 kit',
    weight: 2.2,
  },
  {
    type: 'training_material',
    category: 'Training Kits',
    categoryId: 'cat-training',
    names: ['Masterclass', 'Starter Guide', 'Pro Certification', 'Workshop'],
    publishingStatus: 'live',
    hsnCode: '9992',
    gst: 18,
    packaging: 'Digital',
    unit: '1 license',
    weight: 0,
  },
  {
    type: 'agricultural',
    category: 'Future Products',
    categoryId: 'cat-future',
    names: ['Substrate Block', 'Compost Mix', 'Nutrient Booster'],
    publishingStatus: 'draft',
    hsnCode: '3101',
    gst: 12,
    packaging: 'Sack',
    unit: '5kg',
    weight: 5,
  },
];

interface BrandDef {
  id: string;
  name: string;
}

const BRANDS: BrandDef[] = [
  ...MOCK_BRANDS.map((b) => ({ id: b.id, name: b.name })),
];

interface CollectionDef {
  id: string;
  name: string;
}

const COLLECTIONS: CollectionDef[] = [
  ...MOCK_COLLECTIONS.map((c) => ({ id: c.id, name: c.name })),
  { id: 'col-premium', name: 'Premium Selection' },
];

const LIFECYCLES: ProductLifecycleState[] = [
  'draft',
  'under_review',
  'approved',
  'published',
  'scheduled',
  'active',
  'inactive',
  'archived',
];

const STOCKS: StockStatus[] = ['in_stock', 'low_stock', 'out_of_stock', 'preorder'];

const TAG_POOL = ['organic', 'fresh', 'gourmet', 'vegan', 'bestseller', 'new', 'premium', 'value', 'limited', 'seasonal'];

const AUTHORS = ['arya@sporekart.com', 'meera@sporekart.com', 'rohan@sporekart.com', 'kavya@sporekart.com'];

function pad(n: number): string {
  return String(n).padStart(3, '0');
}

function two(n: number): string {
  return String(n).padStart(2, '0');
}

function skuCode(type: ProductType): string {
  switch (type) {
    case 'fresh_mushroom':
      return 'FM';
    case 'dry_mushroom':
      return 'DM';
    case 'spawn_seed':
      return 'SP';
    case 'growing_kit':
      return 'KIT';
    case 'training_material':
      return 'TM';
    default:
      return 'AG';
  }
}

function makeDate(base: number, offsetDays: number): string {
  const d = new Date(Date.UTC(2026, 0, 1, 9, 0, 0));
  d.setUTCDate(d.getUTCDate() + ((base + offsetDays) % 200));
  return d.toISOString();
}

function buildProductName(typeCategory: string, base: string): string {
  if (typeCategory === 'Fresh Mushrooms') return `Fresh ${base} Mushrooms`;
  if (typeCategory === 'Dry Mushrooms') return `Dried ${base} Mushrooms`;
  if (typeCategory === 'Spawn Seeds') return `${base} Mushroom Spawn`;
  if (typeCategory === 'Growing Kits') return `${base} Mushroom Grow Kit`;
  if (typeCategory === 'Training Kits') return `${base} Cultivation Course`;
  return `${base}`;
}

function generateCatalog(): CatalogProduct[] {
  const products: CatalogProduct[] = [];
  let counter = 0;

  for (let ti = 0; ti < TYPE_DEFS.length; ti++) {
    const td = TYPE_DEFS[ti];
    for (let ni = 0; ni < td.names.length; ni++) {
      for (let bi = 0; bi < BRANDS.length; bi++) {
        counter += 1;
        const idx = counter;
        const brand = BRANDS[bi];
        const collection = COLLECTIONS[(idx + ti) % COLLECTIONS.length];
        const lifecycleState = LIFECYCLES[idx % LIFECYCLES.length];
        const stockStatus = STOCKS[idx % STOCKS.length];
        const featured = idx % 6 === 0;
        const hasImages = idx % 5 !== 0;
        const baseName = td.names[ni];
        const name = `${buildProductName(td.category, baseName)}${bi > 0 ? ` (${brand.name})` : ''}`;

        const mrp = 149 + ((idx * 37) % 2400);
        const discountPct = 5 + (idx % 6) * 5;
        const sellingPrice = Math.round(mrp * (1 - discountPct / 100));
        const wholesalePrice = Math.round(sellingPrice * 0.82);

        const tags = [
          TAG_POOL[idx % TAG_POOL.length],
          TAG_POOL[(idx + 3) % TAG_POOL.length],
          TAG_POOL[(idx + 6) % TAG_POOL.length],
        ].filter((t, i, arr) => arr.indexOf(t) === i);

        const createdAt = makeDate(idx, 0);
        const updatedAt = makeDate(idx, 30 + (idx % 40));
        const thumbUrl = hasImages ? `/mock/product-${(idx % 12) + 1}.jpg` : '';
        const rating = idx % 4 === 0 ? undefined : Math.round((3 + ((idx % 20) / 10)) * 10) / 10;

        const product: CatalogProduct = {
          id: `catprod-${pad(idx)}`,
          sku: `SK-${skuCode(td.type)}-${baseName.slice(0, 3).toUpperCase().replace(/[^A-Z]/g, 'X')}-${two(idx)}`,
          barcode: `89010${pad(idx)}${two(bi)}`,
          name,
          shortDescription: `${name} — premium quality, curated by ${brand.name}.`,
          longDescription: `${name} sourced and quality-checked by ${brand.name}. Part of the ${collection.name} collection. Ideal for enthusiasts and professionals alike.`,
          categoryId: td.categoryId,
          collectionId: collection.id,
          brandId: brand.id,
          tags,
          productType: td.type,
          variantType: idx % 7 === 0 ? 'variable' : idx % 11 === 0 ? 'bundle' : 'single',
          packaging: td.packaging,
          unit: td.unit,
          weight: td.weight,
          dimensions: { length: 12 + (idx % 10), width: 10 + (idx % 6), height: 5 + (idx % 8), unit: 'cm' },
          shelfLife: td.type === 'fresh_mushroom' ? '5 days refrigerated' : '12 months',
          storageConditions: td.type === 'fresh_mushroom' ? 'Refrigerate at 4°C' : 'Cool, dry place',
          countryOfOrigin: idx % 9 === 0 ? 'Italy' : 'India',
          manufacturer: brand.name,
          gst: td.gst,
          hsnCode: td.hsnCode,
          pricing: { mrp, sellingPrice, wholesalePrice, discount: discountPct },
          media: {
            images: hasImages ? [thumbUrl] : [],
            videos: [],
            thumbnail: hasImages ? thumbUrl : undefined,
          },
          seo: {
            title: name,
            description: `Buy ${name} online at SporeKart.`,
            keywords: tags,
            slug: name.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, ''),
          },
          publishingStatus: td.publishingStatus,
          lifecycleState,
          createdBy: AUTHORS[idx % AUTHORS.length],
          updatedBy: AUTHORS[(idx + 1) % AUTHORS.length],
          createdAt,
          updatedAt,
          stockStatus,
          featured,
          hasImages,
          thumbUrl,
          rating,
        };

        products.push(product);
      }
    }
  }

  return products;
}

export const CATALOG_PRODUCTS: CatalogProduct[] = generateCatalog();

export const categoryNameById: Record<string, string> = (() => {
  const map: Record<string, string> = {};
  for (const c of MOCK_CATEGORIES) map[c.id] = c.name;
  for (const td of TYPE_DEFS) map[td.categoryId] = td.category;
  return map;
})();

export const brandNameById: Record<string, string> = (() => {
  const map: Record<string, string> = {};
  for (const b of BRANDS) map[b.id] = b.name;
  return map;
})();

export const collectionNameById: Record<string, string> = (() => {
  const map: Record<string, string> = {};
  for (const c of COLLECTIONS) map[c.id] = c.name;
  return map;
})();

export const TYPE_LABELS: Record<string, string> = {
  fresh_mushroom: 'Fresh Mushroom',
  dry_mushroom: 'Dry Mushroom',
  spawn_seed: 'Spawn Seed',
  growing_kit: 'Growing Kit',
  training_material: 'Training Material',
  agricultural: 'Agricultural',
};

export const STOCK_LABELS: Record<StockStatus, string> = {
  in_stock: 'In Stock',
  low_stock: 'Low Stock',
  out_of_stock: 'Out of Stock',
  preorder: 'Preorder',
};

export const ALL_CATEGORIES: string[] = TYPE_DEFS.map((t) => t.category);

export const ALL_BRANDS: string[] = BRANDS.map((b) => b.name);

export const ALL_COLLECTIONS: string[] = COLLECTIONS.map((c) => c.name);

export const ALL_TYPES: ProductType[] = TYPE_DEFS.map((t) => t.type);

export const ALL_STATUSES: ProductLifecycleState[] = [...LIFECYCLES];

export function mockDelay(ms = 600): Promise<void> {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}
