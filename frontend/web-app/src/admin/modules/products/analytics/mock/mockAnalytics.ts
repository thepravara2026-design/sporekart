import type { KpiCard, CatalogHealth, ProductQualityMetric, CategoryAnalytic, BrandAnalytic, VariantAnalytic, SeoAnalytic, MarketplaceAnalytic, PublishingAnalytic, ProductKpi, ChartConfig } from '../types';

export const MOCK_KPI_CARDS: KpiCard[] = [
  { id: 'kpi-1', label: 'Total Products', value: 12, icon: '📦', color: 'var(--color-accent-blue)' },
  { id: 'kpi-2', label: 'Published', value: 7, icon: '✅', color: 'var(--color-accent-green)' },
  { id: 'kpi-3', label: 'Draft', value: 3, icon: '📝', color: 'var(--color-accent-yellow)' },
  { id: 'kpi-4', label: 'Archived', value: 2, icon: '📁', color: 'var(--color-accent-red)' },
  { id: 'kpi-5', label: 'Categories', value: 6, icon: '📂', color: 'var(--color-accent-purple)' },
  { id: 'kpi-6', label: 'Brands', value: 11, icon: '🏷️', color: 'var(--color-accent-orange)' },
  { id: 'kpi-7', label: 'Variants', value: 15, icon: '🧩', color: 'var(--color-accent-cyan)' },
  { id: 'kpi-8', label: 'Avg Score', value: 76, unit: '%', icon: '📊', color: 'var(--color-accent-green)' },
  { id: 'kpi-9', label: 'Marketplace Ready', value: 5, icon: '🛒', color: 'var(--color-accent-green)' },
  { id: 'kpi-10', label: 'SEO Ready', value: 6, icon: '🔍', color: 'var(--color-accent-blue)' },
  { id: 'kpi-11', label: 'Compliance Ready', value: 4, icon: '🛡️', color: 'var(--color-accent-purple)' },
  { id: 'kpi-12', label: 'Recently Updated', value: 3, icon: '🕐', color: 'var(--color-accent-orange)', trend: 'up', trendValue: '+2' },
];

export const MOCK_CATALOG_HEALTH: CatalogHealth = {
  overall: 76, completion: 78, validation: 75, publishing: 78,
  seo: 76, media: 65, compliance: 73, marketplace: 68, accessibility: 72, aiReadiness: 70,
};

export const MOCK_QUALITY_METRICS: ProductQualityMetric[] = [
  { label: 'Missing Information', count: 3, severity: 'critical', icon: '⚠️' },
  { label: 'Products without Images', count: 2, severity: 'warning', icon: '🖼️' },
  { label: 'Products without Variants', count: 4, severity: 'warning', icon: '🧩' },
  { label: 'Products without SEO', count: 3, severity: 'critical', icon: '🔍' },
  { label: 'Products without Pricing', count: 1, severity: 'warning', icon: '💰' },
  { label: 'Products without Categories', count: 0, severity: 'info', icon: '📂' },
  { label: 'Products Requiring Review', count: 5, severity: 'warning', icon: '📋' },
  { label: 'Products Requiring Compliance', count: 3, severity: 'critical', icon: '🛡️' },
];

export const MOCK_CATEGORY_ANALYTICS: CategoryAnalytic[] = [
  { id: 'cat-1', name: 'Fresh Produce', productCount: 4, completion: 88, health: 85, depth: 2 },
  { id: 'cat-2', name: 'Dried Goods', productCount: 3, completion: 82, health: 80, depth: 1 },
  { id: 'cat-3', name: 'Grow Kits', productCount: 3, completion: 75, health: 72, depth: 2 },
  { id: 'cat-4', name: 'Spawn & Substrate', productCount: 2, completion: 45, health: 50, depth: 1 },
  { id: 'cat-5', name: 'Equipment', productCount: 1, completion: 65, health: 62, depth: 2 },
  { id: 'cat-6', name: 'Training', productCount: 1, completion: 88, health: 90, depth: 1 },
];

export const MOCK_BRAND_ANALYTICS: BrandAnalytic[] = [
  { id: 'br-1', name: 'GreenLeaf Farms', productCount: 3, coverage: 25, active: true, health: 90 },
  { id: 'br-2', name: 'SporeKart Essentials', productCount: 2, coverage: 17, active: true, health: 85 },
  { id: 'br-3', name: 'Mushroom Masters', productCount: 2, coverage: 17, active: true, health: 78 },
  { id: 'br-4', name: 'AgriGrow', productCount: 1, coverage: 8, active: true, health: 72 },
  { id: 'br-5', name: 'HydroWorld', productCount: 1, coverage: 8, active: true, health: 65 },
  { id: 'br-6', name: 'Organic Earth', productCount: 1, coverage: 8, active: true, health: 82 },
  { id: 'br-7', name: 'FarmDirect', productCount: 1, coverage: 8, active: false, health: 45 },
  { id: 'br-8', name: 'PureHarvest', productCount: 1, coverage: 8, active: true, health: 70 },
];

export const MOCK_VARIANT_ANALYTICS: VariantAnalytic = {
  productsWithVariants: 8, totalProducts: 12, avgVariants: 1.9, missingVariants: 4,
  variantDistribution: [
    { label: 'Size', count: 6 }, { label: 'Weight', count: 4 }, { label: 'Pack Size', count: 3 },
    { label: 'Color', count: 1 }, { label: 'Grade', count: 1 },
  ],
  packagingDistribution: [
    { label: 'Pouch', count: 5 }, { label: 'Box', count: 4 }, { label: 'Bag', count: 3 },
    { label: 'Kit', count: 2 }, { label: 'Tray', count: 1 },
  ],
  attributeUsage: [
    { label: 'Weight', count: 8 }, { label: 'Dimensions', count: 6 }, { label: 'Material', count: 5 },
    { label: 'Storage', count: 7 }, { label: 'Shelf Life', count: 4 },
  ],
  skuCoverage: 85,
};

export const MOCK_SEO_ANALYTICS: SeoAnalytic = {
  avgScore: 76, missingSeo: 3, metaCoverage: 82, schemaCoverage: 68,
  slugCoverage: 95, canonicalCoverage: 88, ogCoverage: 72, aiReadiness: 70,
  totalProducts: 12,
};

export const MOCK_MARKETPLACE_ANALYTICS: MarketplaceAnalytic = {
  amazon: 72, flipkart: 65, googleShopping: 78, agriBegri: 88, indiaMART: 68, avgScore: 74,
  missingRequirements: ['High-res images (5 products)', 'GTIN/EAN (7 products)', 'Product descriptions (3 products)'],
  warnings: ['Price adjustments may be needed for Amazon', 'Export documentation incomplete for 4 products'],
};

export const MOCK_PUBLISHING_ANALYTICS: PublishingAnalytic = {
  draft: 3, review: 1, scheduled: 1, published: 7, archived: 2, rejected: 1, pending: 2, health: 78,
};

export const MOCK_PRODUCT_KPI: ProductKpi = {
  catalogGrowth: 12, productCompletion: 78, avgSeo: 76,
  avgValidation: 75, avgCompliance: 73, avgMarketplace: 68,
  avgAccessibility: 72, avgQuality: 74,
};

export const MOCK_CHART_GROWTH: ChartConfig = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
  series: [{ name: 'Products', data: [5, 6, 7, 8, 9, 10, 12], color: 'var(--color-accent-blue)' }],
};

export const MOCK_CHART_COMPLETION: ChartConfig = {
  labels: ['Fresh Produce', 'Dried Goods', 'Grow Kits', 'Spawn', 'Equipment', 'Training'],
  series: [{ name: 'Completion %', data: [88, 82, 75, 45, 65, 88], color: 'var(--color-accent-green)' }],
};

export const MOCK_CHART_VARIANTS: ChartConfig = {
  labels: ['Size', 'Weight', 'Pack Size', 'Color', 'Grade'],
  series: [{ name: 'Variants', data: [6, 4, 3, 1, 1], color: 'var(--color-accent-purple)' }],
};

export const MOCK_CHART_HEALTH: ChartConfig = {
  labels: ['Completion', 'Validation', 'SEO', 'Media', 'Compliance', 'Marketplace', 'Accessibility', 'AI'],
  series: [{ name: 'Score', data: [78, 75, 76, 65, 73, 68, 72, 70], color: 'var(--color-accent-blue)' }],
};

export const MOCK_CHART_PUBLISHING: ChartConfig = {
  labels: ['Published', 'Draft', 'Review', 'Scheduled', 'Archived', 'Rejected'],
  series: [{ name: 'Products', data: [7, 3, 1, 1, 2, 1], color: 'var(--color-accent-green)' }],
};

export const MOCK_CHART_MARKETPLACE: ChartConfig = {
  labels: ['Amazon', 'Flipkart', 'Google Shopping', 'AgriBegri', 'IndiaMART'],
  series: [{ name: 'Readiness %', data: [72, 65, 78, 88, 68], color: 'var(--color-accent-orange)' }],
};
