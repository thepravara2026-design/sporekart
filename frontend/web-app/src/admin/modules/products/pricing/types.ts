export type PriceTier =
  | 'mrp'
  | 'selling'
  | 'wholesale'
  | 'distributor'
  | 'dealer'
  | 'retail'
  | 'farmer'
  | 'training'
  | 'bundle';

export const PRICE_TIER_LABELS: Record<PriceTier, string> = {
  mrp: 'MRP',
  selling: 'Selling Price',
  wholesale: 'Wholesale Price',
  distributor: 'Distributor Price',
  dealer: 'Dealer Price',
  retail: 'Retail Price',
  farmer: 'Farmer Price',
  training: 'Training Price',
  bundle: 'Bundle Price',
};

export interface PriceEntry {
  tier: PriceTier;
  amount: number;
  currency: string;
  effectiveFrom?: string;
  effectiveTo?: string;
}

export interface PricingEntity {
  id: string;
  productId: string;
  productName: string;
  sku: string;
  category: string;
  categoryId: string;
  brand: string;
  brandId: string;
  image?: string;
  prices: PriceEntry[];
  gstPercentage: number;
  gstCategory: string;
  hsnCode: string;
  status: PricingStatus;
  approved: boolean;
  updatedAt: string;
  updatedBy: string;
  notes?: string;
}

export type PricingStatus = 'active' | 'draft' | 'pending' | 'archived';

export interface DiscountRule {
  id: string;
  name: string;
  type: 'percentage' | 'fixed' | 'volume' | 'wholesale' | 'coupon';
  value: number;
  minQuantity?: number;
  maxQuantity?: number;
  minAmount?: number;
  maxAmount?: number;
  maxDiscount?: number;
  applicableTo: DiscountApplicableTo;
  applicableIds: string[];
  startDate: string;
  endDate: string;
  status: DiscountStatus;
  priority: number;
  stackable: boolean;
  description?: string;
  createdAt: string;
  createdBy: string;
}

export type DiscountApplicableTo = 'all' | 'category' | 'brand' | 'product' | 'collection' | 'customer';

export type DiscountStatus = 'active' | 'inactive' | 'scheduled' | 'expired' | 'archived';

export interface CommercialRule {
  id: string;
  name: string;
  type: 'min_price' | 'max_price' | 'suggested_price' | 'margin' | 'cost' | 'approval';
  value: number;
  currency: string;
  applicableTo: DiscountApplicableTo;
  applicableIds: string[];
  status: 'active' | 'inactive';
  priority: number;
  notes?: string;
  createdAt: string;
  createdBy: string;
}

export interface TaxRule {
  id: string;
  name: string;
  percentage: number;
  type: 'gst' | 'vat' | 'sales_tax' | 'service_tax' | 'cess';
  category: string;
  applicableTo: DiscountApplicableTo;
  applicableIds: string[];
  status: 'active' | 'inactive';
  description?: string;
  createdAt: string;
  createdBy: string;
}

export interface GSTEntry {
  id: string;
  code: string;
  percentage: number;
  category: string;
  type: GSTType;
  description: string;
  status: 'active' | 'inactive';
  cgst: number;
  sgst: number;
  igst: number;
  cess?: number;
  effectiveFrom: string;
  createdAt: string;
}

export type GSTType = 'goods' | 'services' | 'both';

export interface HSNEntry {
  id: string;
  code: string;
  description: string;
  taxCategory: string;
  commodityType: string;
  gstPercentage: number;
  status: 'active' | 'inactive';
  createdAt: string;
  updatedAt: string;
}

export interface CurrencySetting {
  code: string;
  symbol: string;
  name: string;
  precision: number;
  format: string;
  isDefault: boolean;
  exchangeRate: number;
  status: 'active' | 'inactive';
}

export interface SchedulePricingData {
  entityIds: string[];
  entityType: 'product' | 'category' | 'brand' | 'collection';
  prices: PriceEntry[];
  gstPercentage: number;
  gstCategory: string;
  hsnCode: string;
  approved: boolean;
  updatedAt: string;
  updatedBy: string;
  notes?: string;
}

export interface PriceSchedule {
  id: string;
  name: string;
  type: ScheduleType;
  pricing: SchedulePricingData;
  startDate: string;
  endDate: string;
  priority: number;
  status: ScheduleStatus;
  campaign?: string;
  notes?: string;
  createdAt: string;
  createdBy: string;
}

export type ScheduleType = 'campaign' | 'seasonal' | 'limited_time' | 'flash_sale' | 'weekend_offer' | 'festival' | 'launch';

export type ScheduleStatus = 'draft' | 'active' | 'scheduled' | 'expired' | 'cancelled';

export interface PriceHistoryEntry {
  id: string;
  entityId: string;
  entityName: string;
  previousPrice: number;
  newPrice: number;
  tier: PriceTier;
  changedBy: string;
  changeDate: string;
  reason: string;
  type: 'price_change' | 'discount_applied' | 'bulk_update' | 'scheduled' | 'approved' | 'reverted' | 'archived' | 'published';
}

export interface BulkPricingOperation {
  id: string;
  name: string;
  type: BulkOperationType;
  description: string;
  icon: string;
}

export type BulkOperationType = 'update_price' | 'apply_discount' | 'update_gst' | 'update_hsn' | 'approve' | 'archive' | 'publish' | 'schedule';

export interface PromotionalCampaign {
  id: string;
  name: string;
  type: PromoType;
  discountType: 'percentage' | 'fixed';
  discountValue: number;
  applicableTo: DiscountApplicableTo;
  applicableIds: string[];
  minAmount?: number;
  maxDiscount?: number;
  minQuantity?: number;
  startDate: string;
  endDate: string;
  status: DiscountStatus;
  priority: number;
  description?: string;
  createdAt: string;
  createdBy: string;
}

export type PromoType = 'campaign' | 'limited_time' | 'weekend' | 'seasonal' | 'featured' | 'collection' | 'category' | 'brand' | 'launch';

export type PricingSectionId =
  | 'overview'
  | 'product-pricing'
  | 'discount-rules'
  | 'promotions'
  | 'tax-rules'
  | 'gst'
  | 'hsn'
  | 'scheduled-pricing'
  | 'price-history'
  | 'bulk-pricing'
  | 'price-preview'
  | 'settings'
  | 'help';

export const PRICING_SECTION_LABELS: Record<PricingSectionId, string> = {
  overview: 'Overview',
  'product-pricing': 'Product Pricing',
  'discount-rules': 'Discount Rules',
  promotions: 'Promotions',
  'tax-rules': 'Tax Rules',
  gst: 'GST',
  hsn: 'HSN',
  'scheduled-pricing': 'Scheduled Pricing',
  'price-history': 'Price History',
  'bulk-pricing': 'Bulk Pricing',
  'price-preview': 'Price Preview',
  settings: 'Settings',
  help: 'Help',
};

export const PRICING_SECTION_ICONS: Record<PricingSectionId, string> = {
  overview: 'dashboard',
  'product-pricing': 'tag',
  'discount-rules': 'percent',
  promotions: 'megaphone',
  'tax-rules': 'receipt',
  gst: 'currency-rupee',
  hsn: 'code',
  'scheduled-pricing': 'calendar',
  'price-history': 'clock',
  'bulk-pricing': 'multiple',
  'price-preview': 'eye',
  settings: 'settings',
  help: 'help',
};

export type PricingRole = 'viewer' | 'pricing_editor' | 'manager' | 'finance' | 'administrator';

export const PRICING_ROLE_HIERARCHY: PricingRole[] = ['viewer', 'pricing_editor', 'manager', 'finance', 'administrator'];

export const CURRENT_PRICING_ROLE: PricingRole = 'manager';

export const PRICING_PERMISSIONS: Record<PricingRole, string[]> = {
  viewer: ['view'],
  pricing_editor: ['view', 'edit', 'schedule'],
  manager: ['view', 'edit', 'approve', 'schedule', 'bulk_update'],
  finance: ['view', 'edit', 'approve', 'archive', 'restore'],
  administrator: ['view', 'edit', 'approve', 'schedule', 'bulk_update', 'archive', 'restore'],
};

export function canPricing(role: PricingRole, ...permissions: string[]): boolean {
  const userPerms = PRICING_PERMISSIONS[role];
  return permissions.every((p) => userPerms.includes(p));
}

export interface PricingFilters {
  search: string;
  priceRange: [number, number] | null;
  tiers: PriceTier[];
  gst: number[];
  hsn: string[];
  status: PricingStatus[];
  discountStatus: DiscountStatus[];
  scheduleStatus: ScheduleStatus[];
  campaignType: PromoType[];
  recentlyUpdated: boolean;
  scheduled: boolean;
  featured: boolean;
  wholesale: boolean;
  retail: boolean;
  savedFilters: SavedFilter[];
  activeSavedFilter?: string;
}

export interface SavedFilter {
  id: string;
  name: string;
  filters: Partial<PricingFilters>;
}

export const EMPTY_PRICING_FILTERS: PricingFilters = {
  search: '',
  priceRange: null,
  tiers: [],
  gst: [],
  hsn: [],
  status: [],
  discountStatus: [],
  scheduleStatus: [],
  campaignType: [],
  recentlyUpdated: false,
  scheduled: false,
  featured: false,
  wholesale: false,
  retail: false,
  savedFilters: [],
};

export type PricingSortOption = 'mrp' | 'selling_price' | 'wholesale_price' | 'updated' | 'discount' | 'gst' | 'category' | 'brand' | 'alphabetical';

export interface PricePreviewData {
  mrp: number;
  sellingPrice: number;
  wholesalePrice: number;
  gst: number;
  gstAmount: number;
  discount: number;
  discountPercent: number;
  savings: number;
  finalPrice: number;
  customerPrice: number;
  adminPrice: number;
  marketplacePrice: number;
  priceLabels: Record<string, string>;
}
