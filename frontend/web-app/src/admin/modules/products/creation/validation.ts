import type {
  ProductWizardData,
  WizardErrors,
  WizardStepId,
  DimensionUnit,
  WeightUnit,
} from './types';

export const STEP_ORDER: WizardStepId[] = [
  'basic',
  'classification',
  'packaging',
  'pricing',
  'seo',
  'review',
  'confirmation',
];

export const STEP_LABELS: Record<WizardStepId, string> = {
  basic: 'Basic Info',
  classification: 'Classification',
  packaging: 'Packaging',
  pricing: 'Pricing',
  seo: 'SEO',
  review: 'Review',
  confirmation: 'Confirmation',
};

export const FIELD_LABELS: Record<keyof ProductWizardData, string> = {
  name: 'Product Name',
  description: 'Long Description',
  shortDescription: 'Short Description',
  productType: 'Product Type',
  brand: 'Brand',
  manufacturer: 'Manufacturer',
  sku: 'SKU',
  barcode: 'Barcode',
  category: 'Category',
  collection: 'Collection',
  tags: 'Tags',
  status: 'Status',
  productFamily: 'Product Family',
  productGroup: 'Product Group',
  mushroomType: 'Mushroom Type',
  growingMethod: 'Growing Method',
  season: 'Season',
  productNature: 'Product Nature',
  attributes: 'Attributes',
  packagingType: 'Packaging Type',
  packageSize: 'Package Size',
  unitsPerPack: 'Units Per Pack',
  weight: 'Weight',
  weightUnit: 'Weight Unit',
  dimensions: 'Dimensions',
  packageWeight: 'Package Weight',
  packageWeightUnit: 'Package Weight Unit',
  shelfLife: 'Shelf Life',
  storageConditions: 'Storage Conditions',
  countryOfOrigin: 'Country of Origin',
  packagingNotes: 'Packaging Notes',
  mrp: 'MRP',
  price: 'Selling Price',
  wholesalePrice: 'Wholesale Price',
  discount: 'Discount %',
  cost: 'Cost',
  currency: 'Currency',
  taxClass: 'Tax Class',
  stockKeepingUnit: 'Stock Keeping Unit',
  hsnCode: 'HSN Code',
  gst: 'GST %',
  priceNotes: 'Price Notes',
  metaTitle: 'Meta Title',
  metaDescription: 'Meta Description',
  slug: 'URL Slug',
  keywords: 'Keywords',
  canonicalUrl: 'Canonical URL',
  ogTitle: 'Open Graph Title',
  ogDescription: 'Open Graph Description',
};

export const CHARACTER_LIMITS: Record<string, number> = {
  name: 120,
  shortDescription: 160,
  description: 2000,
  metaTitle: 70,
  metaDescription: 320,
  canonicalUrl: 255,
  ogTitle: 70,
  ogDescription: 320,
};

export const DRAFT_STORAGE_KEY = 'sporekart:product-draft';

// Mock catalog of existing product names used only for the duplicate-name warning.
export const DUPLICATE_NAMES: string[] = [
  'organic pink oyster mushroom grow kit',
  "lion's mane supplement",
  'reishi extract tincture',
  'cordyceps miliary grow bag',
  'shiitake fruiting block',
  'oyster mushroom spawn',
];

export function checkDuplicateName(value: string): string | undefined {
  const v = value.trim().toLowerCase();
  if (!v) return undefined;
  if (DUPLICATE_NAMES.includes(v)) {
    return 'A product with this name already exists in the mock catalog. Consider a unique name.';
  }
  return undefined;
}

// ---- Pure validators -------------------------------------------------------

export function required(value: unknown, label = 'This field'): string | undefined {
  if (value === null || value === undefined) return `${label} is required.`;
  if (typeof value === 'string' && value.trim().length === 0) return `${label} is required.`;
  if (typeof value === 'number' && Number.isNaN(value)) return `${label} is required.`;
  return undefined;
}

export function hasValue(value: unknown): boolean {
  if (value === null || value === undefined) return false;
  if (typeof value === 'string') return value.trim().length > 0;
  if (typeof value === 'number') return !Number.isNaN(value) && value !== 0;
  if (Array.isArray(value)) return value.length > 0;
  return true;
}

export function maxLength(value: string, max: number, label = 'This field'): string | undefined {
  if (value && value.length > max) return `${label} must be ${max} characters or fewer (currently ${value.length}).`;
  return undefined;
}

export function minLength(value: string, min: number, label = 'This field'): string | undefined {
  if (value && value.length > 0 && value.length < min) return `${label} must be at least ${min} characters.`;
  return undefined;
}

export function numericMin(value: number, min: number, label = 'This value'): string | undefined {
  if (value === undefined || value === null || Number.isNaN(value)) return undefined;
  if (value < min) return `${label} must be ${min} or greater.`;
  return undefined;
}

export function numericMax(value: number, max: number, label = 'This value'): string | undefined {
  if (value === undefined || value === null || Number.isNaN(value)) return undefined;
  if (value > max) return `${label} must be ${max} or fewer.`;
  return undefined;
}

export function currencyValid(value: number, label = 'Price'): string | undefined {
  if (value === undefined || value === null || Number.isNaN(value)) return undefined;
  if (value < 0) return `${label} cannot be negative.`;
  return undefined;
}

export function slugify(value: string): string {
  return value
    .toLowerCase()
    .trim()
    .replace(/[^a-z0-9\s-]/g, '')
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-')
    .replace(/^-|-$/g, '');
}

// ---- Field-level validation -----------------------------------------------

export function validateField(name: keyof ProductWizardData, value: unknown, data?: ProductWizardData): string | undefined {
  switch (name) {
    case 'name':
      return required(value, 'Product name') ?? maxLength(String(value ?? ''), CHARACTER_LIMITS.name, 'Product name');
    case 'shortDescription':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.shortDescription, 'Short description');
    case 'description':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.description, 'Description');
    case 'productType':
      return required(value, 'Product type');
    case 'sku':
      return required(value, 'SKU');
    case 'barcode':
      return maxLength(String(value ?? ''), 32, 'Barcode');
    case 'category':
      return required(value, 'Category');
    case 'packagingType':
      return required(value, 'Packaging type');
    case 'packageSize':
      return maxLength(String(value ?? ''), 60, 'Package size');
    case 'unitsPerPack':
      return required(value, 'Units per pack') ?? numericMin(Number(value) || 0, 1, 'Units per pack');
    case 'weight':
      return required(value, 'Weight') ?? numericMin(Number(value) || 0, 0, 'Weight');
    case 'packageWeight':
      return numericMin(Number(value) || 0, 0, 'Package weight');
    case 'mrp':
      return currencyValid(Number(value) || 0, 'MRP');
    case 'price':
      return required(value, 'Selling price') ?? currencyValid(Number(value) || 0, 'Selling price');
    case 'wholesalePrice':
      return currencyValid(Number(value) || 0, 'Wholesale price');
    case 'discount':
      return numericMin(Number(value) || 0, 0, 'Discount') ?? numericMax(Number(value) || 0, 100, 'Discount');
    case 'cost':
      return currencyValid(Number(value) || 0, 'Cost');
    case 'stockKeepingUnit':
      return undefined;
    case 'gst':
      return numericMin(Number(value) || 0, 0, 'GST') ?? numericMax(Number(value) || 0, 100, 'GST');
    case 'metaTitle':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.metaTitle, 'Meta title');
    case 'metaDescription':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.metaDescription, 'Meta description');
    case 'slug':
      if (data && data.slug && !/^[a-z0-9-]+$/.test(data.slug)) {
        return 'Slug may only contain lowercase letters, numbers, and hyphens.';
      }
      return undefined;
    case 'canonicalUrl':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.canonicalUrl, 'Canonical URL');
    case 'ogTitle':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.ogTitle, 'Open Graph title');
    case 'ogDescription':
      return maxLength(String(value ?? ''), CHARACTER_LIMITS.ogDescription, 'Open Graph description');
    default:
      return undefined;
  }
}

// ---- Step field maps -------------------------------------------------------

export function getStepFields(step: WizardStepId): (keyof ProductWizardData)[] {
  switch (step) {
    case 'basic':
      return ['name', 'shortDescription', 'description', 'productType', 'sku', 'barcode', 'brand', 'category', 'collection', 'tags', 'status'];
    case 'classification':
      return ['productFamily', 'productGroup', 'mushroomType', 'growingMethod', 'season', 'productNature', 'attributes'];
    case 'packaging':
      return ['packagingType', 'packageSize', 'unitsPerPack', 'weight', 'packageWeight', 'gst', 'hsnCode'];
    case 'pricing':
      return ['mrp', 'price', 'wholesalePrice', 'discount', 'taxClass', 'currency', 'cost', 'priceNotes', 'stockKeepingUnit', 'hsnCode'];
    case 'seo':
      return ['metaTitle', 'metaDescription', 'slug', 'keywords', 'canonicalUrl', 'ogTitle', 'ogDescription'];
    case 'review':
      return [];
    case 'confirmation':
      return [];
    default:
      return [];
  }
}

// ---- Step / full validation ------------------------------------------------

export function validateStep(step: WizardStepId, data: ProductWizardData): WizardErrors {
  const errors: WizardErrors = {};
  for (const field of getStepFields(step)) {
    const err = validateField(field, data[field], data);
    if (err) errors[field] = err;
  }
  return errors;
}

export function validateAll(data: ProductWizardData): WizardErrors {
  const errors: WizardErrors = {};
  for (const step of STEP_ORDER) {
    const stepErrors = validateStep(step, data);
    Object.assign(errors, stepErrors);
  }
  return errors;
}

// ---- Optional-field warnings (for review step) -----------------------------

export interface WizardWarning {
  field: keyof ProductWizardData;
  message: string;
}

export function computeWarnings(data: ProductWizardData): WizardWarning[] {
  const warnings: WizardWarning[] = [];
  const optionalChecks: { field: keyof ProductWizardData; label: string; empty: boolean }[] = [
    { field: 'shortDescription', label: 'Short description', empty: !data.shortDescription },
    { field: 'description', label: 'Long description', empty: !data.description },
    { field: 'brand', label: 'Brand', empty: !data.brand },
    { field: 'manufacturer', label: 'Manufacturer', empty: !data.manufacturer },
    { field: 'barcode', label: 'Barcode', empty: !data.barcode },
    { field: 'tags', label: 'Tags', empty: data.tags.length === 0 },
    { field: 'collection', label: 'Collection', empty: data.collection.length === 0 },
    { field: 'productFamily', label: 'Product family', empty: !data.productFamily },
    { field: 'productGroup', label: 'Product group', empty: !data.productGroup },
    { field: 'mushroomType', label: 'Mushroom type', empty: !data.mushroomType },
    { field: 'growingMethod', label: 'Growing method', empty: !data.growingMethod },
    { field: 'attributes', label: 'Attributes', empty: data.attributes.length === 0 },
    { field: 'packageSize', label: 'Package size', empty: !data.packageSize },
    { field: 'shelfLife', label: 'Shelf life', empty: !data.shelfLife },
    { field: 'storageConditions', label: 'Storage conditions', empty: !data.storageConditions },
    { field: 'countryOfOrigin', label: 'Country of origin', empty: !data.countryOfOrigin },
    { field: 'packagingNotes', label: 'Packaging notes', empty: !data.packagingNotes },
    { field: 'mrp', label: 'MRP', empty: !(data.mrp > 0) },
    { field: 'wholesalePrice', label: 'Wholesale price', empty: !(data.wholesalePrice > 0) },
    { field: 'cost', label: 'Cost', empty: !(data.cost > 0) },
    { field: 'priceNotes', label: 'Price notes', empty: !data.priceNotes },
    { field: 'metaTitle', label: 'Meta title', empty: !data.metaTitle },
    { field: 'metaDescription', label: 'Meta description', empty: !data.metaDescription },
    { field: 'keywords', label: 'SEO keywords', empty: data.keywords.length === 0 },
    { field: 'canonicalUrl', label: 'Canonical URL', empty: !data.canonicalUrl },
    { field: 'ogTitle', label: 'Open Graph title', empty: !data.ogTitle },
    { field: 'ogDescription', label: 'Open Graph description', empty: !data.ogDescription },
  ];
  for (const check of optionalChecks) {
    if (check.empty) warnings.push({ field: check.field, message: `${check.label} is not set.` });
  }
  if (data.mrp > 0 && data.price > 0 && data.mrp < data.price) {
    warnings.push({ field: 'mrp', message: 'MRP is lower than the selling price.' });
  }
  if (data.discount > 0 && !(data.wholesalePrice > 0)) {
    warnings.push({ field: 'discount', message: 'Discount set without a wholesale price reference.' });
  }
  return warnings;
}

// ---- Mock SEO score (placeholder) -----------------------------------------

export function computeSeoScore(data: ProductWizardData): number {
  const checks: boolean[] = [
    hasValue(data.metaTitle) && data.metaTitle.length >= 30 && data.metaTitle.length <= CHARACTER_LIMITS.metaTitle,
    hasValue(data.metaDescription) && data.metaDescription.length >= 70 && data.metaDescription.length <= CHARACTER_LIMITS.metaDescription,
    hasValue(data.slug),
    data.keywords.length > 0,
    hasValue(data.canonicalUrl),
    hasValue(data.ogTitle),
    hasValue(data.ogDescription),
  ];
  const passed = checks.filter(Boolean).length;
  return Math.round((passed / checks.length) * 100);
}

// ---- Formatters ------------------------------------------------------------

export function formatCurrency(amount: number, currency: string): string {
  try {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency }).format(amount || 0);
  } catch {
    return `${currency} ${(amount || 0).toFixed(2)}`;
  }
}

export function formatWeight(value: number, unit: WeightUnit): string {
  return `${value || 0} ${unit}`;
}

export function formatDimensions(d: { length: number; width: number; height: number; unit: DimensionUnit }): string {
  return `${d.length || 0} × ${d.width || 0} × ${d.height || 0} ${d.unit}`;
}
