import { useState, useCallback, useMemo } from 'react';
import type { OrgSectionId, OrgFilters, OrgSortOption, CategoryNode } from '../types';
import { EMPTY_ORG_FILTERS } from '../types';
import { MOCK_CATEGORIES, getRootCategories, getChildCategories } from '../mock/mockCategories';
import { MOCK_COLLECTIONS } from '../mock/mockCollections';
import { MOCK_BRANDS } from '../mock/mockBrands';
import { MOCK_TAGS } from '../mock/mockTags';
import { MOCK_ACTIVITY_EVENTS } from '../mock/mockActivity';

export function useOrganizationState() {
  const [section, setSection] = useState<OrgSectionId>('overview');
  const [search, setSearchState] = useState('');
  const [filters, setFilters] = useState<OrgFilters>({ ...EMPTY_ORG_FILTERS });
  const [sort, setSort] = useState<OrgSortOption>('name_asc');
  const [loading, setLoading] = useState(true);
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set(['cat-fresh', 'cat-dried', 'cat-spawn']));

  const [previewEntity, setPreviewEntity] = useState<{ type: string; id: string } | null>(null);
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());

  const categories = useMemo(() => MOCK_CATEGORIES, []);
  const collections = useMemo(() => MOCK_COLLECTIONS, []);
  const brands = useMemo(() => MOCK_BRANDS, []);
  const tags = useMemo(() => MOCK_TAGS, []);
  const activity = useMemo(() => MOCK_ACTIVITY_EVENTS, []);

  const toggleExpand = useCallback((id: string) => {
    setExpandedCategories((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  const expandAll = useCallback(() => {
    const all = new Set(MOCK_CATEGORIES.filter((c) => getChildCategories(c.id).length > 0).map((c) => c.id));
    setExpandedCategories(all);
  }, []);

  const collapseAll = useCallback(() => {
    setExpandedCategories(new Set());
  }, []);

  const categoryTree = useMemo((): CategoryNode[] => {
    function build(ids: string[]): CategoryNode[] {
      return ids.map((id) => {
        const cat = MOCK_CATEGORIES.find((c) => c.id === id)!;
        const children = getChildCategories(id);
        return {
          ...cat,
          children: children.length > 0 ? build(children.map((c) => c.id)) : [],
          depth: cat.parentId ? (MOCK_CATEGORIES.find((c) => c.id === cat.parentId)?.parentId ? 2 : 1) : 0,
          isExpanded: expandedCategories.has(id),
        };
      });
    }
    return build(getRootCategories().map((c) => c.id));
  }, [expandedCategories]);

  const filteredCategories = useMemo(() => {
    let result = categories;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter((c) => c.name.toLowerCase().includes(q) || c.code.toLowerCase().includes(q) || c.description.toLowerCase().includes(q));
    }
    if (filters.status.length > 0) result = result.filter((c) => filters.status.includes(c.status));
    if (filters.isFeatured != null) result = result.filter((c) => c.isFeatured === filters.isFeatured);
    return result;
  }, [categories, search, filters]);

  const filteredCollections = useMemo(() => {
    let result = collections;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter((c) => c.name.toLowerCase().includes(q) || c.description.toLowerCase().includes(q));
    }
    if (filters.status.length > 0) result = result.filter((c) => filters.status.includes(c.status));
    if (filters.types.length > 0) result = result.filter((c) => filters.types.includes(c.collectionType));
    return result;
  }, [collections, search, filters]);

  const filteredBrands = useMemo(() => {
    let result = brands;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter((b) => b.name.toLowerCase().includes(q) || b.description.toLowerCase().includes(q) || b.country.toLowerCase().includes(q));
    }
    if (filters.status.length > 0) result = result.filter((b) => filters.status.includes(b.status));
    if (filters.isFeatured != null) result = result.filter((b) => b.isFeatured === filters.isFeatured);
    return result;
  }, [brands, search, filters]);

  const filteredTags = useMemo(() => {
    let result = tags;
    if (search) {
      const q = search.toLowerCase();
      result = result.filter((t) => t.name.toLowerCase().includes(q) || t.description.toLowerCase().includes(q));
    }
    if (filters.tags.length > 0) result = result.filter((t) => filters.tags.includes(t.tagType));
    return result;
  }, [tags, search, filters]);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    count += filters.status.length;
    if (filters.isFeatured != null) count += 1;
    count += filters.types.length;
    count += filters.tags.length;
    count += filters.brands.length;
    if (filters.dateFrom || filters.dateTo) count += 1;
    return count;
  }, [filters]);

  const filterContext = { clearAllFilters: useCallback(() => setFilters({ ...EMPTY_ORG_FILTERS }), []), activeFilterCount };
  const selectionContext = { toggleSelect: useCallback((id: string) => setSelectedIds((prev) => { const n = new Set(prev); if (n.has(id)) n.delete(id); else n.add(id); return n; }), []), clearSelection: useCallback(() => setSelectedIds(new Set()), []) };
  const previewContext = { openPreview: useCallback((type: string, id: string) => setPreviewEntity({ type, id }), []), closePreview: useCallback(() => setPreviewEntity(null), []) };

  return {
    section, setSection,
    loading, setLoading,
    search, setSearch: setSearchState,
    filters, setFilters, clearAllFilters: filterContext.clearAllFilters,
    sort, setSort,
    activeFilterCount,
    selectedIds, toggleSelect: selectionContext.toggleSelect, clearSelection: selectionContext.clearSelection,
    previewEntity, openPreview: previewContext.openPreview, closePreview: previewContext.closePreview,
    expandedCategories, toggleExpand, expandAll, collapseAll,
    categoryTree,
    categories: filteredCategories,
    collections: filteredCollections,
    brands: filteredBrands,
    tags: filteredTags,
    allCategories: categories,
    allCollections: collections,
    allBrands: brands,
    allTags: tags,
    activity,
  };
}

export type OrganizationState = ReturnType<typeof useOrganizationState>;
