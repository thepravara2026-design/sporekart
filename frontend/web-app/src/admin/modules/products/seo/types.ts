export interface SocialProfileLink {
  platform: string;
  url: string;
  icon: string;
  label: string;
  enabled: boolean;
}

export interface SeoEntry {
  id: string;
  productId: string;
  productName: string;
  slug: string;
  metaTitle: string;
  metaDescription: string;
  keywords: string[];
  focusKeyword: string;
  secondaryKeywords: string[];
  productSummary: string;
  seoNotes?: string;
  ogTitle: string;
  ogDescription: string;
  ogImage?: string;
  twitterTitle: string;
  twitterDescription: string;
  twitterImage?: string;
  instagramTitle: string;
  instagramDescription: string;
  instagramImage?: string;
  canonicalUrl: string;
  indexable: boolean;
  followLinks: boolean;
  structuredData: StructuredDataEntry;
  publishingStatus: PublishingStatus;
  marketplaceStatus: MarketplaceStatus;
  seoScore: number;
  aiReadinessScore: number;
  marketplaceScore: number;
  validationErrors: ValidationIssue[];
  validationWarnings: ValidationIssue[];
  createdAt: string;
  updatedAt: string;
  updatedBy: string;
}

export interface StructuredDataEntry {
  product: boolean;
  brand: boolean;
  offer: boolean;
  organization: boolean;
  breadcrumb: boolean;
  faq: boolean;
  review: boolean;
  image: boolean;
  video: boolean;
  aggregateRating: boolean;
  availability: boolean;
  price: boolean;
  jsonLd?: string;
}

export type PublishingStatus = 'draft' | 'ready_seo' | 'ready_publish' | 'published' | 'scheduled' | 'archived' | 'rejected' | 'review_required';

export interface MarketplaceStatus {
  amazon: MarketplaceItem;
  flipkart: MarketplaceItem;
  agriBegri: MarketplaceItem;
  indiaMART: MarketplaceItem;
  googleShopping: MarketplaceItem;
  export: MarketplaceItem;
}

export interface MarketplaceItem {
  ready: boolean;
  score: number;
  missing: string[];
  warnings: string[];
}

export interface ValidationIssue {
  field: string;
  message: string;
  severity: 'error' | 'warning' | 'info';
}

export interface SitemapEntry {
  id: string;
  type: 'product' | 'category' | 'image' | 'video';
  url: string;
  lastModified: string;
  priority: number;
  changeFreq: 'always' | 'hourly' | 'daily' | 'weekly' | 'monthly' | 'yearly' | 'never';
}

export interface SeoHealthScore {
  overall: number;
  title: number;
  description: number;
  slug: number;
  images: number;
  keywords: number;
  structuredData: number;
  schema: number;
  url: number;
  canonical: number;
  accessibility: number;
  performance: number;
  contentQuality: number;
  recommendations: string[];
  warnings: string[];
}

export interface AiReadinessScore {
  overall: number;
  geoReady: number;
  aeoReady: number;
  structuredContent: number;
  semanticHeadings: number;
  entityCoverage: number;
  faqQuality: number;
  kgReady: number;
  contentCompleteness: number;
  recommendations: string[];
}

export type SeoSectionId =
  | 'overview'
  | 'seo'
  | 'meta'
  | 'structured-data'
  | 'urls'
  | 'publishing'
  | 'marketplace'
  | 'search-preview'
  | 'social'
  | 'ai-readiness'
  | 'validation'
  | 'history'
  | 'settings'
  | 'help';

export const SEO_SECTION_LABELS: Record<SeoSectionId, string> = {
  overview: 'Overview',
  seo: 'SEO',
  meta: 'Meta Information',
  'structured-data': 'Structured Data',
  urls: 'URLs',
  publishing: 'Publishing',
  marketplace: 'Marketplace',
  'search-preview': 'Search Preview',
  social: 'Social',
  'ai-readiness': 'AI Readiness',
  validation: 'Validation',
  history: 'History',
  settings: 'Settings',
  help: 'Help',
};

export const SEO_SECTION_ICONS: Record<SeoSectionId, string> = {
  overview: 'dashboard',
  seo: 'search',
  meta: 'file-text',
  'structured-data': 'code',
  urls: 'link',
  publishing: 'send',
  marketplace: 'shopping-cart',
  'search-preview': 'eye',
  social: 'share',
  'ai-readiness': 'cpu',
  validation: 'check-circle',
  history: 'clock',
  settings: 'gear',
  help: 'help-circle',
};

export type SeoRole = 'viewer' | 'seo_editor' | 'marketing_manager' | 'administrator';

export const SEO_ROLE_HIERARCHY: SeoRole[] = ['viewer', 'seo_editor', 'marketing_manager', 'administrator'];

export const CURRENT_SEO_ROLE: SeoRole = 'marketing_manager';

export const SEO_PERMISSIONS: Record<SeoRole, string[]> = {
  viewer: ['view'],
  seo_editor: ['view', 'edit_seo'],
  marketing_manager: ['view', 'edit_seo', 'publish', 'archive'],
  administrator: ['view', 'edit_seo', 'publish', 'archive', 'validate', 'preview'],
};

export interface SeoFilters {
  search: string;
  seoScore: [number, number] | null;
  marketplaceReady: boolean;
  published: boolean;
  draft: boolean;
  needsReview: boolean;
  missingMeta: boolean;
  missingSchema: boolean;
  missingImages: boolean;
  archived: boolean;
  savedFilters: SeoSavedFilter[];
  activeSavedFilter?: string;
}

export interface SeoSavedFilter {
  id: string;
  name: string;
  filters: Partial<SeoFilters>;
}

export const EMPTY_SEO_FILTERS: SeoFilters = {
  search: '',
  seoScore: null,
  marketplaceReady: false,
  published: false,
  draft: false,
  needsReview: false,
  missingMeta: false,
  missingSchema: false,
  missingImages: false,
  archived: false,
  savedFilters: [],
};

export type SeoSortOption = 'product' | 'slug' | 'score' | 'marketplace' | 'updated' | 'title' | 'status' | 'alphabetical';
