export type AnalyticsSectionId =
  | 'overview' | 'catalog' | 'products' | 'categories' | 'brands' | 'variants'
  | 'pricing' | 'seo' | 'marketplace' | 'publishing' | 'validation' | 'compliance'
  | 'reports' | 'insights' | 'settings' | 'help';

export const ANALYTICS_SECTION_LABELS: Record<AnalyticsSectionId, string> = {
  overview: 'Executive Overview', catalog: 'Catalog Overview', products: 'Products',
  categories: 'Categories', brands: 'Brands', variants: 'Variants', pricing: 'Pricing',
  seo: 'SEO', marketplace: 'Marketplace', publishing: 'Publishing',
  validation: 'Validation', compliance: 'Compliance', reports: 'Reports',
  insights: 'Insights', settings: 'Settings', help: 'Help',
};

export const ANALYTICS_SECTION_ICONS: Record<AnalyticsSectionId, string> = {
  overview: 'dashboard', catalog: 'database', products: 'package', categories: 'folder',
  brands: 'tag', variants: 'layers', pricing: 'dollar', seo: 'search',
  marketplace: 'shopping-cart', publishing: 'send', validation: 'check-circle',
  compliance: 'shield', reports: 'file-text', insights: 'lightbulb', settings: 'gear', help: 'help-circle',
};

export interface KpiCard {
  id: string; label: string; value: number | string; unit?: string;
  trend?: 'up' | 'down' | 'neutral'; trendValue?: string; icon: string; color: string;
}

export interface CatalogHealth {
  overall: number; completion: number; validation: number; publishing: number;
  seo: number; media: number; compliance: number; marketplace: number;
  accessibility: number; aiReadiness: number;
}

export interface ProductQualityMetric {
  label: string; count: number; severity: 'critical' | 'warning' | 'info'; icon: string;
}

export interface CategoryAnalytic {
  id: string; name: string; productCount: number; completion: number; health: number; depth: number;
}

export interface BrandAnalytic {
  id: string; name: string; productCount: number; coverage: number; active: boolean; health: number;
}

export interface VariantAnalytic {
  productsWithVariants: number; totalProducts: number; avgVariants: number;
  missingVariants: number; variantDistribution: { label: string; count: number }[];
  packagingDistribution: { label: string; count: number }[];
  attributeUsage: { label: string; count: number }[];
  skuCoverage: number;
}

export interface SeoAnalytic {
  avgScore: number; missingSeo: number; metaCoverage: number; schemaCoverage: number;
  slugCoverage: number; canonicalCoverage: number; ogCoverage: number; aiReadiness: number;
  totalProducts: number;
}

export interface MarketplaceAnalytic {
  amazon: number; flipkart: number; googleShopping: number;
  agriBegri: number; indiaMART: number; avgScore: number;
  missingRequirements: string[]; warnings: string[];
}

export interface PublishingAnalytic {
  draft: number; review: number; scheduled: number; published: number;
  archived: number; rejected: number; pending: number; health: number;
}

export interface ProductKpi {
  catalogGrowth: number; productCompletion: number; avgSeo: number;
  avgValidation: number; avgCompliance: number; avgMarketplace: number;
  avgAccessibility: number; avgQuality: number;
}

export interface InsightRecommendation {
  id: string; type: 'seo' | 'images' | 'duplicate' | 'category' | 'publishing' | 'marketplace' | 'quality';
  severity: 'critical' | 'warning' | 'info'; message: string; productCount: number; action: string;
}

export interface AnalyticsReport {
  id: string; title: string; type: string; icon: string;
  generatedAt: string; generatedBy: string; description: string;
}

export interface AnalyticsActivityEvent {
  id: string; type: string; message: string; user: string; timestamp: string; icon: string;
}

export type AnalyticsRole = 'viewer' | 'manager' | 'business_analyst' | 'administrator';

export const ANALYTICS_ROLE_HIERARCHY: AnalyticsRole[] = ['viewer', 'manager', 'business_analyst', 'administrator'];
export const CURRENT_ANALYTICS_ROLE: AnalyticsRole = 'manager';

export const ANALYTICS_PERMISSIONS: Record<AnalyticsRole, string[]> = {
  viewer: ['view_analytics'],
  manager: ['view_analytics', 'generate_reports'],
  business_analyst: ['view_analytics', 'generate_reports', 'export_reports'],
  administrator: ['view_analytics', 'generate_reports', 'export_reports', 'customize_dashboard'],
};

export interface AnalyticsFilters {
  search: string; dateRange: [string, string] | null; category: string | null;
  brand: string | null; status: string | null; savedFilters: AnalyticsSavedFilter[];
  activeSavedFilter?: string;
}

export interface AnalyticsSavedFilter { id: string; name: string; filters: Partial<AnalyticsFilters>; }

export const EMPTY_ANALYTICS_FILTERS: AnalyticsFilters = {
  search: '', dateRange: null, category: null, brand: null, status: null, savedFilters: [],
};

export interface ChartDataPoint { label: string; value: number; color?: string; }
export interface ChartSeries { name: string; data: number[]; color: string; }
export interface ChartConfig { labels: string[]; series: ChartSeries[]; }
