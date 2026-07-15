export type ProductLifecycleState =
  | 'draft'
  | 'under_review'
  | 'approved'
  | 'published'
  | 'scheduled'
  | 'active'
  | 'inactive'
  | 'archived'
  | 'deleted';

export type ProductType =
  | 'fresh_mushroom'
  | 'dry_mushroom'
  | 'spawn_seed'
  | 'growing_kit'
  | 'training_material'
  | 'agricultural'
  | string;

export interface ProductDimensions {
  length: number;
  width: number;
  height: number;
  unit: 'cm' | 'mm' | 'in';
}

export interface ProductPricing {
  mrp: number;
  sellingPrice: number;
  wholesalePrice: number;
  discount: number;
}

export interface ProductMedia {
  images: string[];
  videos: string[];
  thumbnail?: string;
}

export interface ProductSeo {
  title: string;
  description: string;
  keywords: string[];
  slug: string;
}

export type ProductPublishingStatus =
  | 'draft'
  | 'pending'
  | 'live'
  | 'scheduled'
  | 'unpublished';

export interface Product {
  id: string;
  sku: string;
  barcode: string;
  name: string;
  shortDescription: string;
  longDescription: string;
  categoryId: string;
  collectionId: string;
  brandId: string;
  tags: string[];
  productType: ProductType;
  variantType: 'single' | 'variable' | 'bundle';
  packaging: string;
  unit: string;
  weight: number;
  dimensions: ProductDimensions;
  shelfLife: string;
  storageConditions: string;
  countryOfOrigin: string;
  manufacturer: string;
  gst: number;
  hsnCode: string;
  pricing: ProductPricing;
  media: ProductMedia;
  seo: ProductSeo;
  publishingStatus: ProductPublishingStatus;
  lifecycleState: ProductLifecycleState;
  createdBy: string;
  updatedBy: string;
  createdAt: string;
  updatedAt: string;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  parentId?: string;
  description?: string;
  isActive: boolean;
}

export interface Collection {
  id: string;
  name: string;
  slug: string;
  description?: string;
  coverImage?: string;
  isActive: boolean;
}

export interface Brand {
  id: string;
  name: string;
  slug: string;
  logo?: string;
  description?: string;
  isActive: boolean;
}

export type ProductActivityType =
  | 'create'
  | 'update'
  | 'publish'
  | 'archive'
  | 'delete'
  | 'import'
  | 'export'
  | 'review';

export interface ProductActivity {
  id: string;
  type: ProductActivityType;
  message: string;
  actor: string;
  timestamp: string;
}

export interface ProductSettings {
  lowStockThreshold: number;
  autoPublish: boolean;
  requireApproval: boolean;
  defaultTaxRegion: string;
  mediaCompression: boolean;
}

export type ProductPermissions =
  | 'view'
  | 'create'
  | 'update'
  | 'delete'
  | 'publish'
  | 'archive'
  | 'approve'
  | 'restore'
  | 'export'
  | 'import'
  | 'bulk_actions';
