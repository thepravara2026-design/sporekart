import { useState, useCallback, useMemo } from 'react';
import type { SeoSectionId, SeoFilters, SeoSortOption } from '../types';
import { EMPTY_SEO_FILTERS } from '../types';
import { MOCK_SEO_ENTRIES } from '../mock/mockSeo';
import { MOCK_PUBLISHING_EVENTS } from '../mock/mockPublishing';
import { MARKETPLACE_CONFIGS } from '../mock/mockMarketplace';
import { MOCK_SEO_ACTIVITY } from '../mock/mockActivity';
import { MOCK_SEO_HEALTH, MOCK_AI_READINESS } from '../mock/mockValidation';
import { MOCK_STRUCTURED_DATA_EXAMPLES } from '../mock/mockStructuredData';
import { MOCK_SOCIAL_PROFILES, MOCK_SOCIAL_ACTIVITY } from '../mock/mockSocial';

export function useSeoState() {
  const [section, setSection] = useState<SeoSectionId>('overview');
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState<SeoFilters>({ ...EMPTY_SEO_FILTERS });
  const [sort, setSort] = useState<SeoSortOption>('updated');
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.seoScore) count++;
    if (filters.marketplaceReady) count++;
    if (filters.published) count++;
    if (filters.draft) count++;
    if (filters.needsReview) count++;
    if (filters.missingMeta) count++;
    if (filters.missingSchema) count++;
    if (filters.missingImages) count++;
    if (filters.archived) count++;
    return count;
  }, [filters]);

  const filteredEntries = useMemo(() => {
    let result = [...MOCK_SEO_ENTRIES];
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(
        (e) =>
          e.productName.toLowerCase().includes(q) ||
          e.slug.toLowerCase().includes(q) ||
          e.metaTitle.toLowerCase().includes(q) ||
          e.keywords.some((k) => k.toLowerCase().includes(q))
      );
    }
    result.sort((a, b) => {
      switch (sort) {
        case 'product': return a.productName.localeCompare(b.productName);
        case 'slug': return a.slug.localeCompare(b.slug);
        case 'title': return a.metaTitle.localeCompare(b.metaTitle);
        case 'score': return b.seoScore - a.seoScore;
        case 'marketplace': return b.marketplaceScore - a.marketplaceScore;
        case 'status': return a.publishingStatus.localeCompare(b.publishingStatus);
        case 'alphabetical': return a.productName.localeCompare(b.productName);
        default: return new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime();
      }
    });
    return result;
  }, [search, filters, sort]);

  const selectedEntry = useMemo(() => MOCK_SEO_ENTRIES.find((e) => e.id === selectedId) ?? null, [selectedId]);

  const clearAllFilters = useCallback(() => setFilters({ ...EMPTY_SEO_FILTERS }), []);
  const toggleSelect = useCallback((id: string) => setSelectedId((prev) => (prev === id ? null : id)), []);

  return {
    section, setSection, loading, setLoading,
    search, setSearch, filters, setFilters, sort, setSort,
    selectedId, toggleSelect, activeFilterCount, clearAllFilters,
    filteredEntries, selectedEntry,
    entries: MOCK_SEO_ENTRIES,
    publishingEvents: MOCK_PUBLISHING_EVENTS,
    marketplaceConfigs: MARKETPLACE_CONFIGS,
    activity: MOCK_SEO_ACTIVITY,
    seoHealth: MOCK_SEO_HEALTH,
    aiReadiness: MOCK_AI_READINESS,
    structuredDataExamples: MOCK_STRUCTURED_DATA_EXAMPLES,
    socialProfiles: MOCK_SOCIAL_PROFILES,
    socialActivity: MOCK_SOCIAL_ACTIVITY,
  };
}
