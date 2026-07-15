import { useState, useCallback, useMemo } from 'react';
import type { VariantSectionId, VariantFilters, VariantSortOption } from '../types';
import { EMPTY_VARIANT_FILTERS } from '../types';
import { MOCK_VARIANTS, MOCK_VARIANT_GROUPS } from '../mock/mockVariants';
import { MOCK_ATTRIBUTE_DEFINITIONS } from '../mock/mockAttributes';
import { MOCK_SKU_ENTRIES } from '../mock/mockSku';
import { MOCK_PACKAGING } from '../mock/mockPackaging';
import { MOCK_BARCODES } from '../mock/mockBarcode';
import { MOCK_VARIANT_ACTIVITY } from '../mock/mockActivity';

export function useVariantState() {
  const [section, setSection] = useState<VariantSectionId>('overview');
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState<VariantFilters>({ ...EMPTY_VARIANT_FILTERS });
  const [sort, setSort] = useState<VariantSortOption>('updated');
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.variantType.length > 0) count++;
    if (filters.attributes.length > 0) count++;
    if (filters.packaging.length > 0) count++;
    if (filters.status.length > 0) count++;
    if (filters.category.length > 0) count++;
    if (filters.brand.length > 0) count++;
    if (filters.recentlyUpdated) count++;
    if (filters.recentlyCreated) count++;
    if (filters.archived) count++;
    if (filters.featured) count++;
    return count;
  }, [filters]);

  const filteredVariants = useMemo(() => {
    let result = [...MOCK_VARIANTS];

    if (search) {
      const q = search.toLowerCase();
      result = result.filter(
        (v) =>
          v.name.toLowerCase().includes(q) ||
          v.sku.toLowerCase().includes(q) ||
          v.productName.toLowerCase().includes(q) ||
          v.attributes.some((a) => a.value.toLowerCase().includes(q))
      );
    }

    if (filters.status.length > 0) {
      result = result.filter((v) => filters.status.includes(v.status));
    }

    result.sort((a, b) => {
      switch (sort) {
        case 'name': return a.name.localeCompare(b.name);
        case 'sku': return a.sku.localeCompare(b.sku);
        case 'weight': {
          const aW = a.attributes.find((at) => at.name === 'Weight')?.value ?? '';
          const bW = b.attributes.find((at) => at.name === 'Weight')?.value ?? '';
          return aW.localeCompare(bW);
        }
        case 'package': {
          const aP = a.attributes.find((at) => at.name === 'Package Type')?.value ?? '';
          const bP = b.attributes.find((at) => at.name === 'Package Type')?.value ?? '';
          return aP.localeCompare(bP);
        }
        case 'status': return a.status.localeCompare(b.status);
        case 'created': return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
        case 'alphabetical': return a.name.localeCompare(b.name);
        default: return new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime();
      }
    });

    return result;
  }, [search, filters, sort]);

  const selectedVariant = useMemo(
    () => MOCK_VARIANTS.find((v) => v.id === selectedId) ?? null,
    [selectedId]
  );

  const clearAllFilters = useCallback(() => setFilters({ ...EMPTY_VARIANT_FILTERS }), []);
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
    filteredVariants,
    selectedVariant,
    variants: MOCK_VARIANTS,
    variantGroups: MOCK_VARIANT_GROUPS,
    attributeDefinitions: MOCK_ATTRIBUTE_DEFINITIONS,
    skuEntries: MOCK_SKU_ENTRIES,
    packaging: MOCK_PACKAGING,
    barcodes: MOCK_BARCODES,
    activity: MOCK_VARIANT_ACTIVITY,
  };
}
