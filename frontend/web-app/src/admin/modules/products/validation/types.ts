export type ValidationSectionId =
  | 'overview'
  | 'validation'
  | 'compliance'
  | 'completeness'
  | 'seo'
  | 'media'
  | 'pricing'
  | 'variants'
  | 'packaging'
  | 'marketplace'
  | 'accessibility'
  | 'publishing'
  | 'history'
  | 'reports'
  | 'settings'
  | 'help';

export const VALIDATION_SECTION_LABELS: Record<ValidationSectionId, string> = {
  overview: 'Overview',
  validation: 'Product Validation',
  compliance: 'Compliance',
  completeness: 'Completeness',
  seo: 'SEO Validation',
  media: 'Media Validation',
  pricing: 'Pricing Validation',
  variants: 'Variant Validation',
  packaging: 'Packaging Validation',
  marketplace: 'Marketplace Validation',
  accessibility: 'Accessibility',
  publishing: 'Publishing',
  history: 'History',
  reports: 'Reports',
  settings: 'Settings',
  help: 'Help',
};

export const VALIDATION_SECTION_ICONS: Record<ValidationSectionId, string> = {
  overview: 'dashboard',
  validation: 'check-circle',
  compliance: 'shield',
  completeness: 'percent',
  seo: 'search',
  media: 'image',
  pricing: 'dollar',
  variants: 'layers',
  packaging: 'box',
  marketplace: 'shopping-cart',
  accessibility: 'accessibility',
  publishing: 'send',
  history: 'clock',
  reports: 'file-text',
  settings: 'gear',
  help: 'help-circle',
};

export interface ValidationResult {
  field: string;
  label: string;
  status: 'pass' | 'fail' | 'warning' | 'info';
  message: string;
  severity?: 'error' | 'warning' | 'info';
}

export interface ProductValidationScore {
  productId: string;
  productName: string;
  overall: number;
  completeness: number;
  seo: number;
  compliance: number;
  marketplace: number;
  accessibility: number;
  aiReadiness: number;
  publishing: number;
  riskLevel: 'low' | 'medium' | 'high' | 'critical';
  certificationStatus: CertificationLevel;
  validatedAt: string;
}

export type CertificationLevel = 'none' | 'bronze' | 'silver' | 'gold' | 'enterprise' | 'marketplace_ready' | 'export_ready';

export interface Certification {
  id: string;
  productId: string;
  productName: string;
  level: CertificationLevel;
  badge: string;
  completionDate: string;
  notes: string;
  validUntil: string;
  issuedBy: string;
}

export interface PublishingReadinessStatus {
  productId: string;
  productName: string;
  status: 'ready' | 'needs_review' | 'blocked' | 'incomplete' | 'compliance_failure' | 'awaiting_approval';
  readinessScore: number;
  blockers: string[];
  warnings: string[];
}

export interface CompletenessResult {
  productId: string;
  productName: string;
  completionPct: number;
  totalFields: number;
  completedFields: number;
  missingFields: string[];
  criticalErrors: string[];
  suggestions: string[];
  sections: CompletenessSection[];
}

export interface CompletenessSection {
  name: string;
  total: number;
  completed: number;
  weight: number;
}

export interface QualityCheckResult {
  productId: string;
  productName: string;
  checks: QualityCheck[];
  passed: number;
  failed: number;
  total: number;
  score: number;
}

export interface QualityCheck {
  id: string;
  label: string;
  category: 'required' | 'length' | 'duplicate' | 'invalid' | 'broken_ref' | 'formatting' | 'naming' | 'images' | 'videos' | 'specs' | 'variants' | 'docs';
  status: 'pass' | 'fail' | 'warning';
  message: string;
}

export interface ComplianceResult {
  productId: string;
  productName: string;
  overallScore: number;
  checks: ComplianceCheck[];
  requiredApprovals: string[];
  warnings: string[];
}

export interface ComplianceCheck {
  id: string;
  label: string;
  category: 'label' | 'manufacturer' | 'country' | 'mrp' | 'gst' | 'hsn' | 'expiry' | 'batch' | 'legal' | 'consumer' | 'agriculture' | 'food_safety' | 'fssai' | 'agri';
  status: 'pass' | 'fail' | 'warning' | 'na';
  message: string;
}

export interface MediaValidationResult {
  productId: string;
  productName: string;
  primaryImage: boolean;
  galleryCount: number;
  minGalleryRequired: number;
  resolutionOk: boolean;
  aspectRatioOk: boolean;
  namingOk: boolean;
  altTextPresent: boolean;
  videoPresent: boolean;
  documentsPresent: boolean;
  qualityOk: boolean;
  score: number;
  issues: string[];
}

export interface SeoValidationResult {
  productId: string;
  productName: string;
  titleOk: boolean;
  descriptionOk: boolean;
  slugOk: boolean;
  keywordsOk: boolean;
  canonicalOk: boolean;
  structuredDataOk: boolean;
  schemaOk: boolean;
  ogOk: boolean;
  twitterOk: boolean;
  contentLengthOk: boolean;
  headingStructureOk: boolean;
  geoOk: boolean;
  aeoOk: boolean;
  score: number;
  issues: string[];
}

export interface PricingValidationResult {
  productId: string;
  productName: string;
  mrpOk: boolean;
  sellingPriceOk: boolean;
  wholesaleOk: boolean;
  discountOk: boolean;
  gstOk: boolean;
  hsnOk: boolean;
  currencyOk: boolean;
  pricingRulesOk: boolean;
  commercialRulesOk: boolean;
  score: number;
  issues: string[];
}

export interface VariantValidationResult {
  productId: string;
  productName: string;
  variantCountOk: boolean;
  noDuplicateVariants: boolean;
  noDuplicateSku: boolean;
  attributesPresent: boolean;
  packagingOk: boolean;
  specificationsOk: boolean;
  inventoryRefOk: boolean;
  score: number;
  issues: string[];
}

export interface PackagingValidationResult {
  productId: string;
  productName: string;
  weightOk: boolean;
  dimensionsOk: boolean;
  materialOk: boolean;
  storageOk: boolean;
  handlingOk: boolean;
  packagingNotesOk: boolean;
  shippingNotesOk: boolean;
  labelComplete: boolean;
  score: number;
  issues: string[];
}

export interface MarketplaceValidationResult {
  productId: string;
  productName: string;
  amazonReady: boolean;
  flipkartReady: boolean;
  agriBegriReady: boolean;
  googleShoppingReady: boolean;
  indiaMARTReady: boolean;
  exportReady: boolean;
  overallScore: number;
  missingFields: string[];
  warnings: string[];
  recommendations: string[];
}

export interface AccessibilityValidationResult {
  productId: string;
  productName: string;
  altTextOk: boolean;
  headingHierarchyOk: boolean;
  ariaOk: boolean;
  contrastOk: boolean;
  keyboardOk: boolean;
  score: number;
  issues: string[];
}

export interface AiReadinessValidationResult {
  productId: string;
  productName: string;
  entityCoverageOk: boolean;
  structuredContentOk: boolean;
  faqOk: boolean;
  semanticStructureOk: boolean;
  knowledgeGraphOk: boolean;
  contextQualityOk: boolean;
  contentRichnessOk: boolean;
  score: number;
  issues: string[];
}

export interface ProductHealthSummary {
  overallHealth: number;
  validationScore: number;
  seoScore: number;
  complianceScore: number;
  marketplaceScore: number;
  accessibilityScore: number;
  aiReadinessScore: number;
  publishingScore: number;
  riskLevel: 'low' | 'medium' | 'high' | 'critical';
  certificationStatus: CertificationLevel;
  totalProducts: number;
  validatedProducts: number;
  failedProducts: number;
}

export interface ValidationActivityEvent {
  id: string;
  type: 'validation_run' | 'compliance_check' | 'certification_issued' | 'approval_granted' | 'rejection' | 'report_generated' | 'bulk_validated';
  message: string;
  productName: string;
  productId: string;
  user: string;
  timestamp: string;
  icon: string;
}

export interface ValidationReport {
  id: string;
  title: string;
  type: 'validation_summary' | 'compliance' | 'marketplace' | 'seo' | 'accessibility' | 'executive';
  generatedAt: string;
  generatedBy: string;
  productCount: number;
  overallScore: number;
  summary: string;
}

export type ValidationRole = 'viewer' | 'qa_engineer' | 'compliance_manager' | 'seo_manager' | 'administrator';

export const VALIDATION_ROLE_HIERARCHY: ValidationRole[] = ['viewer', 'qa_engineer', 'compliance_manager', 'seo_manager', 'administrator'];

export const CURRENT_VALIDATION_ROLE: ValidationRole = 'compliance_manager';

export const VALIDATION_PERMISSIONS: Record<ValidationRole, string[]> = {
  viewer: ['view'],
  qa_engineer: ['view', 'validate', 'generate_reports'],
  compliance_manager: ['view', 'validate', 'approve', 'reject', 'generate_reports'],
  seo_manager: ['view', 'validate', 'generate_reports'],
  administrator: ['view', 'validate', 'approve', 'reject', 'generate_reports', 'archive'],
};

export interface ValidationFilters {
  search: string;
  validationScore: [number, number] | null;
  marketplaceReady: boolean;
  compliant: boolean;
  published: boolean;
  draft: boolean;
  archived: boolean;
  needsReview: boolean;
  certified: boolean;
  savedFilters: ValidationSavedFilter[];
  activeSavedFilter?: string;
}

export interface ValidationSavedFilter {
  id: string;
  name: string;
  filters: Partial<ValidationFilters>;
}

export const EMPTY_VALIDATION_FILTERS: ValidationFilters = {
  search: '',
  validationScore: null,
  marketplaceReady: false,
  compliant: false,
  published: false,
  draft: false,
  archived: false,
  needsReview: false,
  certified: false,
  savedFilters: [],
};

export type ValidationSortOption = 'product' | 'score' | 'risk' | 'compliance' | 'marketplace' | 'updated' | 'certification' | 'alphabetical';
