import { useState, useCallback, useMemo } from 'react';
import type { PricingSectionId, PricingFilters, PricingSortOption, PricingEntity } from '../types';
import { EMPTY_PRICING_FILTERS } from '../types';
import { MOCK_PRICING_ENTITIES } from '../mock/mockPrices';
import { MOCK_DISCOUNT_RULES } from '../mock/mockDiscounts';
import { MOCK_TAX_RULES } from '../mock/mockTaxes';
import { MOCK_GST_ENTRIES } from '../mock/mockGst';
import { MOCK_HSN_ENTRIES } from '../mock/mockHsn';
import { MOCK_PROMOTIONAL_CAMPAIGNS } from '../mock/mockCampaigns';
import { MOCK_PRICE_HISTORY } from '../mock/mockHistory';
import { MOCK_PRICE_SCHEDULES } from '../mock/mockScheduledPricing';
import { MOCK_COMMERCIAL_RULES } from '../mock/mockCommercialRules';
import { MOCK_CURRENCIES } from '../mock/mockCurrency';

export function usePricingState() {
  const [section, setSection] = useState<PricingSectionId>('overview');
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState<PricingFilters>({ ...EMPTY_PRICING_FILTERS });
  const [sort, setSort] = useState<PricingSortOption>('updated');
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.priceRange) count++;
    if (filters.tiers.length > 0) count++;
    if (filters.gst.length > 0) count++;
    if (filters.hsn.length > 0) count++;
    if (filters.status.length > 0) count++;
    if (filters.discountStatus.length > 0) count++;
    if (filters.scheduleStatus.length > 0) count++;
    if (filters.campaignType.length > 0) count++;
    if (filters.recentlyUpdated) count++;
    if (filters.scheduled) count++;
    if (filters.featured) count++;
    if (filters.wholesale) count++;
    if (filters.retail) count++;
    return count;
  }, [filters]);

  const filteredPricing = useMemo(() => {
    let result = [...MOCK_PRICING_ENTITIES];

    if (search) {
      const q = search.toLowerCase();
      result = result.filter(
        (e) =>
          e.productName.toLowerCase().includes(q) ||
          e.sku.toLowerCase().includes(q) ||
          e.category.toLowerCase().includes(q) ||
          e.brand.toLowerCase().includes(q)
      );
    }

    if (filters.status.length > 0) {
      result = result.filter((e) => filters.status.includes(e.status));
    }

    result.sort((a, b) => {
      const dateA = new Date(a.updatedAt).getTime();
      const dateB = new Date(b.updatedAt).getTime();
      switch (sort) {
        case 'mrp': {
          const aMrp = a.prices.find((p) => p.tier === 'mrp')?.amount ?? 0;
          const bMrp = b.prices.find((p) => p.tier === 'mrp')?.amount ?? 0;
          return bMrp - aMrp;
        }
        case 'selling_price': {
          const aSell = a.prices.find((p) => p.tier === 'selling')?.amount ?? 0;
          const bSell = b.prices.find((p) => p.tier === 'selling')?.amount ?? 0;
          return bSell - aSell;
        }
        case 'wholesale_price': {
          const aWs = a.prices.find((p) => p.tier === 'wholesale')?.amount ?? 0;
          const bWs = b.prices.find((p) => p.tier === 'wholesale')?.amount ?? 0;
          return bWs - aWs;
        }
        case 'alphabetical': return a.productName.localeCompare(b.productName);
        case 'category': return a.category.localeCompare(b.category);
        case 'brand': return a.brand.localeCompare(b.brand);
        case 'discount': {
          const getDisc = (e: PricingEntity) => {
            const mrp = e.prices.find((p) => p.tier === 'mrp')?.amount ?? 0;
            const sell = e.prices.find((p) => p.tier === 'selling')?.amount ?? 0;
            return mrp > 0 ? ((mrp - sell) / mrp) * 100 : 0;
          };
          return getDisc(b) - getDisc(a);
        }
        case 'gst': return b.gstPercentage - a.gstPercentage;
        default: return dateB - dateA;
      }
    });

    return result;
  }, [search, filters, sort]);

  const selectedPricing = useMemo(
    () => MOCK_PRICING_ENTITIES.find((e) => e.id === selectedId) ?? null,
    [selectedId]
  );

  const clearAllFilters = useCallback(() => setFilters({ ...EMPTY_PRICING_FILTERS }), []);
  const toggleSelect = useCallback((id: string) => setSelectedId((prev) => (prev === id ? null : id)), []);

  return {
    section, setSection,
    loading, setLoading,
    search, setSearch,
    filters, setFilters,
    sort, setSort,
    selectedId, toggleSelect,
    activeFilterCount,
    clearAllFilters,
    filteredPricing,
    selectedPricing,
    pricingEntities: MOCK_PRICING_ENTITIES,
    discountRules: MOCK_DISCOUNT_RULES,
    taxRules: MOCK_TAX_RULES,
    gstEntries: MOCK_GST_ENTRIES,
    hsnEntries: MOCK_HSN_ENTRIES,
    campaigns: MOCK_PROMOTIONAL_CAMPAIGNS,
    priceHistory: MOCK_PRICE_HISTORY,
    schedules: MOCK_PRICE_SCHEDULES,
    commercialRules: MOCK_COMMERCIAL_RULES,
    currencies: MOCK_CURRENCIES,
  };
}
