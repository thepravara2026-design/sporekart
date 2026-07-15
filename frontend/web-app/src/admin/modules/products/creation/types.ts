export type WizardStepId =
  | 'basic'
  | 'classification'
  | 'packaging'
  | 'pricing'
  | 'seo'
  | 'review'
  | 'confirmation';

export type WeightUnit = 'g' | 'kg' | 'lb' | 'oz';
export type DimensionUnit = 'cm' | 'mm' | 'in';
export type PackagingType = 'box' | 'pouch' | 'bottle' | 'jar' | 'bag' | 'tube' | 'bulk' | 'other';
export type CurrencyCode = 'USD' | 'INR' | 'EUR' | 'GBP';
export type TaxClass = 'standard' | 'reduced' | 'zero' | 'exempt';
export type ProductNature = 'retail' | 'wholesale' | 'training' | 'equipment' | 'consumable';
export type ProductStatus = 'draft' | 'active' | 'archived';
export type Season = 'spring' | 'summer' | 'monsoon' | 'autumn' | 'winter' | 'all';

export interface ProductWizardDimensions {
  length: number;
  width: number;
  height: number;
  unit: DimensionUnit;
}

export interface ProductWizardData {
  // Basic Info
  name: string;
  description: string;
  shortDescription: string;
  productType: string;
  brand: string;
  manufacturer: string;
  sku: string;
  barcode: string;
  category: string;
  collection: string[];
  tags: string[];
  status: ProductStatus;

  // Classification
  productFamily: string;
  productGroup: string;
  mushroomType: string;
  growingMethod: string;
  season: Season;
  productNature: ProductNature;
  attributes: string[];

  // Packaging & Physical
  packagingType: PackagingType;
  packageSize: string;
  unitsPerPack: number;
  weight: number;
  weightUnit: WeightUnit;
  dimensions: ProductWizardDimensions;
  packageWeight: number;
  packageWeightUnit: WeightUnit;
  shelfLife: string;
  storageConditions: string;
  countryOfOrigin: string;
  packagingNotes: string;

  // Pricing (Mock)
  mrp: number;
  price: number;
  wholesalePrice: number;
  discount: number;
  cost: number;
  currency: CurrencyCode;
  taxClass: TaxClass;
  stockKeepingUnit: string;
  hsnCode: string;
  gst: number;
  priceNotes: string;

  // SEO
  metaTitle: string;
  metaDescription: string;
  slug: string;
  keywords: string[];
  canonicalUrl: string;
  ogTitle: string;
  ogDescription: string;
}

export type WizardErrors = Record<string, string>;

export interface DraftMeta {
  draftId: string;
  savedAt: string;
  step: WizardStepId;
}

export interface SubmittedProduct {
  id: string;
  name: string;
  sku: string;
  status: 'draft';
  createdAt: string;
}
