import { useState, useCallback, useMemo } from 'react';
import type { ValidationSectionId, ValidationFilters, ValidationSortOption, CompletenessResult, QualityCheckResult, PublishingReadinessStatus } from '../types';
import { EMPTY_VALIDATION_FILTERS } from '../types';
import { MOCK_VALIDATION_SCORES, MOCK_PRODUCT_HEALTH } from '../mock/mockValidation';
import { MOCK_COMPLIANCE_RESULTS } from '../mock/mockCompliance';
import { MOCK_CERTIFICATIONS } from '../mock/mockCertification';
import { MOCK_REPORTS } from '../mock/mockReports';
import { MOCK_VALIDATION_ACTIVITY } from '../mock/mockActivity';
import { MOCK_PUBLISHING_EVENTS } from '../../seo/mock/mockPublishing';

const MOCK_COMPLETENESS: CompletenessResult[] = MOCK_VALIDATION_SCORES.map((v) => ({
  productId: v.productId, productName: v.productName,
  completionPct: v.completeness,
  totalFields: 24,
  completedFields: Math.round(24 * v.completeness / 100),
  missingFields: v.completeness < 70 ? ['Product description', 'High-res images', 'Specifications', 'SEO metadata'] : v.completeness < 90 ? ['Video content', 'Extended description'] : [],
  criticalErrors: v.completeness < 50 ? ['Missing mandatory fields', 'No pricing configured'] : v.completeness < 70 ? ['Missing SEO metadata'] : [],
  suggestions: v.completeness < 90 ? ['Add video demonstrations', 'Write detailed specifications', 'Include customer testimonials'] : ['Consider adding 360° product view'],
  sections: [
    { name: 'Basic Info', total: 4, completed: 4, weight: 15 },
    { name: 'Category & Brand', total: 3, completed: v.completeness >= 70 ? 3 : 2, weight: 10 },
    { name: 'Variants', total: 3, completed: v.completeness >= 60 ? 3 : 1, weight: 15 },
    { name: 'Pricing', total: 4, completed: v.completeness >= 70 ? 4 : 2, weight: 15 },
    { name: 'Packaging', total: 3, completed: v.completeness >= 60 ? 3 : 1, weight: 10 },
    { name: 'Media', total: 3, completed: v.completeness >= 70 ? 2 : 1, weight: 15 },
    { name: 'SEO', total: 4, completed: v.completeness >= 80 ? 4 : 2, weight: 20 },
  ],
}));

const MOCK_QUALITY_CHECKS: QualityCheckResult[] = MOCK_VALIDATION_SCORES.map((v) => ({
  productId: v.productId, productName: v.productName,
  checks: [
    { id: 'qc-1', label: 'Required fields', category: 'required', status: v.completeness >= 80 ? 'pass' : v.completeness >= 50 ? 'warning' : 'fail', message: v.completeness >= 80 ? 'All required fields present' : v.completeness >= 50 ? 'Some optional fields missing' : 'Critical required fields missing' },
    { id: 'qc-2', label: 'Field length limits', category: 'length', status: v.completeness >= 70 ? 'pass' : 'warning', message: v.completeness >= 70 ? 'All fields within length limits' : 'Title or description may exceed limits' },
    { id: 'qc-3', label: 'Duplicate data check', category: 'duplicate', status: 'pass', message: 'No duplicate data detected' },
    { id: 'qc-4', label: 'Invalid data check', category: 'invalid', status: v.completeness >= 60 ? 'pass' : 'fail', message: v.completeness >= 60 ? 'No invalid data found' : 'Invalid pricing data detected' },
    { id: 'qc-5', label: 'Broken references', category: 'broken_ref', status: 'pass', message: 'All references valid' },
    { id: 'qc-6', label: 'Formatting standards', category: 'formatting', status: v.completeness >= 70 ? 'pass' : 'warning', message: v.completeness >= 70 ? 'Formatting follows standards' : 'Some formatting inconsistencies' },
    { id: 'qc-7', label: 'Naming conventions', category: 'naming', status: 'pass', message: 'Naming conventions followed' },
    { id: 'qc-8', label: 'Image count', category: 'images', status: v.completeness >= 70 ? 'pass' : 'warning', message: v.completeness >= 70 ? 'Minimum 3 images present' : 'Fewer than 3 images' },
    { id: 'qc-9', label: 'Video count', category: 'videos', status: 'warning', message: 'Video content recommended but not present' },
    { id: 'qc-10', label: 'Specifications complete', category: 'specs', status: v.completeness >= 60 ? 'pass' : 'fail', message: v.completeness >= 60 ? 'Specifications documented' : 'Specifications missing' },
    { id: 'qc-11', label: 'Variant structure', category: 'variants', status: 'pass', message: 'Variant structure valid' },
    { id: 'qc-12', label: 'Documentation', category: 'docs', status: v.completeness >= 60 ? 'pass' : 'warning', message: v.completeness >= 60 ? 'Product documentation available' : 'Documentation incomplete' },
  ],
  passed: 0, failed: 0, total: 12, score: 0,
}));

MOCK_QUALITY_CHECKS.forEach((q) => {
  q.passed = q.checks.filter((c) => c.status === 'pass').length;
  q.failed = q.checks.filter((c) => c.status === 'fail').length;
  q.score = Math.round((q.passed / q.total) * 100);
});

const MOCK_PUBLISHING_READINESS: PublishingReadinessStatus[] = MOCK_VALIDATION_SCORES.map((v) => ({
  productId: v.productId, productName: v.productName,
  status: v.publishing >= 85 ? 'ready' : v.publishing >= 70 ? 'needs_review' : v.publishing >= 50 ? 'blocked' : 'incomplete',
  readinessScore: v.publishing,
  blockers: v.publishing < 70 ? ['Low validation score', 'Marketplace gaps', 'Compliance issues'] : v.publishing < 85 ? ['Minor SEO improvements needed'] : [],
  warnings: v.publishing < 85 ? ['Review before publishing'] : [],
}));

export function useValidationState() {
  const [section, setSection] = useState<ValidationSectionId>('overview');
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState<ValidationFilters>({ ...EMPTY_VALIDATION_FILTERS });
  const [sort, setSort] = useState<ValidationSortOption>('score');
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.validationScore) count++;
    if (filters.marketplaceReady) count++;
    if (filters.compliant) count++;
    if (filters.published) count++;
    if (filters.draft) count++;
    if (filters.archived) count++;
    if (filters.needsReview) count++;
    if (filters.certified) count++;
    return count;
  }, [filters]);

  const filteredScores = useMemo(() => {
    let result = [...MOCK_VALIDATION_SCORES];
    if (search) {
      const q = search.toLowerCase();
      result = result.filter((v) => v.productName.toLowerCase().includes(q));
    }
    result.sort((a, b) => {
      switch (sort) {
        case 'product': return a.productName.localeCompare(b.productName);
        case 'score': return b.overall - a.overall;
        case 'risk': return ['critical', 'high', 'medium', 'low'].indexOf(a.riskLevel) - ['critical', 'high', 'medium', 'low'].indexOf(b.riskLevel);
        case 'compliance': return b.compliance - a.compliance;
        case 'marketplace': return b.marketplace - a.marketplace;
        case 'certification': return b.certificationStatus.localeCompare(a.certificationStatus);
        case 'alphabetical': return a.productName.localeCompare(b.productName);
        default: return new Date(b.validatedAt).getTime() - new Date(a.validatedAt).getTime();
      }
    });
    return result;
  }, [search, filters, sort]);

  const selectedScore = useMemo(() => MOCK_VALIDATION_SCORES.find((v) => v.productId === selectedId) ?? null, [selectedId]);
  const selectedCompliance = useMemo(() => MOCK_COMPLIANCE_RESULTS.find((c) => c.productId === selectedId) ?? null, [selectedId]);
  const selectedCompleteness = useMemo(() => MOCK_COMPLETENESS.find((c) => c.productId === selectedId) ?? null, [selectedId]);
  const selectedQuality = useMemo(() => MOCK_QUALITY_CHECKS.find((c) => c.productId === selectedId) ?? null, [selectedId]);
  const selectedPublishing = useMemo(() => MOCK_PUBLISHING_READINESS.find((p) => p.productId === selectedId) ?? null, [selectedId]);

  const clearAllFilters = useCallback(() => setFilters({ ...EMPTY_VALIDATION_FILTERS }), []);
  const toggleSelect = useCallback((id: string) => setSelectedId((prev) => (prev === id ? null : id)), []);

  return {
    section, setSection, loading, setLoading,
    search, setSearch, filters, setFilters, sort, setSort,
    selectedId, toggleSelect, activeFilterCount, clearAllFilters,
    filteredScores, selectedScore,
    scores: MOCK_VALIDATION_SCORES,
    productHealth: MOCK_PRODUCT_HEALTH,
    complianceResults: MOCK_COMPLIANCE_RESULTS,
    selectedCompliance,
    completenessResults: MOCK_COMPLETENESS,
    selectedCompleteness,
    qualityChecks: MOCK_QUALITY_CHECKS,
    selectedQuality,
    certifications: MOCK_CERTIFICATIONS,
    reports: MOCK_REPORTS,
    activity: MOCK_VALIDATION_ACTIVITY,
    publishingEvents: MOCK_PUBLISHING_EVENTS,
    publishingReadiness: MOCK_PUBLISHING_READINESS,
    selectedPublishing,
  };
}
