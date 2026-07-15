import type { SeoHealthScore, AiReadinessScore } from '../types';

export const MOCK_SEO_HEALTH: SeoHealthScore = {
  overall: 76,
  title: 82,
  description: 68,
  slug: 90,
  images: 55,
  keywords: 72,
  structuredData: 85,
  schema: 80,
  url: 88,
  canonical: 95,
  accessibility: 70,
  performance: 75,
  contentQuality: 65,
  recommendations: [
    'Add meta descriptions to all unpublished products',
    'Increase image count to minimum 3 per product',
    'Add FAQ schema for better rich result eligibility',
    'Improve content quality score with more detailed descriptions',
  ],
  warnings: [
    'Some products missing Twitter card images',
    'Keyword density below recommended threshold',
    'Consider adding video content for higher engagement',
  ],
};

export const MOCK_AI_READINESS: AiReadinessScore = {
  overall: 68,
  geoReady: 62,
  aeoReady: 58,
  structuredContent: 75,
  semanticHeadings: 70,
  entityCoverage: 65,
  faqQuality: 60,
  kgReady: 72,
  contentCompleteness: 68,
  recommendations: [
    'Add structured FAQ content targeting voice search queries',
    'Improve semantic heading hierarchy (H1→H2→H3)',
    'Increase entity coverage with more specific product attributes',
    'Create comprehensive product guides for featured snippet targeting',
    'Add schema.org markup for enhanced knowledge graph presence',
  ],
};

export const VALIDATION_RULES = [
  { field: 'metaTitle', label: 'Meta Title', min: 30, max: 60, severity: 'error' as const },
  { field: 'metaDescription', label: 'Meta Description', min: 120, max: 160, severity: 'error' as const },
  { field: 'slug', label: 'URL Slug', min: 3, max: 80, severity: 'error' as const },
  { field: 'keywords', label: 'Keywords', min: 3, max: 10, severity: 'warning' as const },
  { field: 'images', label: 'Product Images', min: 3, max: 10, severity: 'warning' as const },
];
