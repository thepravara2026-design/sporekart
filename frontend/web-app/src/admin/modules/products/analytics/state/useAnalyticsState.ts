import { useState, useCallback, useMemo } from 'react';
import type { AnalyticsSectionId, AnalyticsFilters } from '../types';
import { EMPTY_ANALYTICS_FILTERS } from '../types';
import { MOCK_KPI_CARDS, MOCK_CATALOG_HEALTH, MOCK_QUALITY_METRICS, MOCK_CATEGORY_ANALYTICS, MOCK_BRAND_ANALYTICS, MOCK_VARIANT_ANALYTICS, MOCK_SEO_ANALYTICS, MOCK_MARKETPLACE_ANALYTICS, MOCK_PUBLISHING_ANALYTICS, MOCK_PRODUCT_KPI, MOCK_CHART_GROWTH, MOCK_CHART_COMPLETION, MOCK_CHART_VARIANTS, MOCK_CHART_HEALTH, MOCK_CHART_PUBLISHING, MOCK_CHART_MARKETPLACE } from '../mock/mockAnalytics';
import { MOCK_INSIGHTS } from '../mock/mockInsights';
import { MOCK_ANALYTICS_REPORTS } from '../mock/mockReports';
import { MOCK_ANALYTICS_ACTIVITY } from '../mock/mockActivity';

export function useAnalyticsState() {
  const [section, setSection] = useState<AnalyticsSectionId>('overview');
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState<AnalyticsFilters>({ ...EMPTY_ANALYTICS_FILTERS });

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.dateRange) count++;
    if (filters.category) count++;
    if (filters.brand) count++;
    if (filters.status) count++;
    return count;
  }, [filters]);

  const clearAllFilters = useCallback(() => setFilters({ ...EMPTY_ANALYTICS_FILTERS }), []);

  return {
    section, setSection, loading, setLoading,
    search, setSearch, filters, setFilters,
    activeFilterCount, clearAllFilters,
    kpiCards: MOCK_KPI_CARDS,
    catalogHealth: MOCK_CATALOG_HEALTH,
    qualityMetrics: MOCK_QUALITY_METRICS,
    categoryAnalytics: MOCK_CATEGORY_ANALYTICS,
    brandAnalytics: MOCK_BRAND_ANALYTICS,
    variantAnalytics: MOCK_VARIANT_ANALYTICS,
    seoAnalytics: MOCK_SEO_ANALYTICS,
    marketplaceAnalytics: MOCK_MARKETPLACE_ANALYTICS,
    publishingAnalytics: MOCK_PUBLISHING_ANALYTICS,
    productKpi: MOCK_PRODUCT_KPI,
    chartGrowth: MOCK_CHART_GROWTH,
    chartCompletion: MOCK_CHART_COMPLETION,
    chartVariants: MOCK_CHART_VARIANTS,
    chartHealth: MOCK_CHART_HEALTH,
    chartPublishing: MOCK_CHART_PUBLISHING,
    chartMarketplace: MOCK_CHART_MARKETPLACE,
    insights: MOCK_INSIGHTS,
    reports: MOCK_ANALYTICS_REPORTS,
    activity: MOCK_ANALYTICS_ACTIVITY,
  };
}
