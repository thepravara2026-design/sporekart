import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  CATALOG_PRODUCTS,
  brandNameById,
  categoryNameById,
  collectionNameById,
  mockDelay,
  type CatalogProduct,
} from '../mock/catalogMock';
import type { CatalogFilters, CatalogViewMode, SavedCatalogView, SortOption } from './types';
import { EMPTY_FILTERS } from './types';

const VIEW_KEY = 'sk_catalog_view';
const SAVED_VIEWS_KEY = 'sk_catalog_saved_views';
const RECENT_KEY = 'sk_catalog_recent_searches';

function readSession<T>(key: string, fallback: T): T {
  if (typeof window === 'undefined') return fallback;
  try {
    const raw = window.sessionStorage.getItem(key);
    if (!raw) return fallback;
    return JSON.parse(raw) as T;
  } catch {
    return fallback;
  }
}

function writeSession(key: string, value: unknown): void {
  if (typeof window === 'undefined') return;
  try {
    window.sessionStorage.setItem(key, JSON.stringify(value));
  } catch {
    /* ignore */
  }
}

function inRange(value: number, min: number | null, max: number | null): boolean {
  if (min != null && value < min) return false;
  if (max != null && value > max) return false;
  return true;
}

function inDateRange(iso: string, from: string | null, to: string | null): boolean {
  const t = new Date(iso).getTime();
  if (from) {
    const f = new Date(from).getTime();
    if (t < f) return false;
  }
  if (to) {
    const e = new Date(to).getTime() + 24 * 60 * 60 * 1000;
    if (t > e) return false;
  }
  return true;
}

function matchesSearch(p: CatalogProduct, q: string): boolean {
  if (!q) return true;
  const needle = q.toLowerCase();
  const category = categoryNameById[p.categoryId] ?? '';
  const brand = brandNameById[p.brandId] ?? '';
  const haystack = [
    p.name,
    p.sku,
    p.barcode,
    category,
    brand,
    p.productType,
    p.lifecycleState,
    ...p.tags,
  ]
    .join(' ')
    .toLowerCase();
  return haystack.includes(needle);
}

function matchesFilters(p: CatalogProduct, f: CatalogFilters): boolean {
  const category = categoryNameById[p.categoryId] ?? '';
  const brand = brandNameById[p.brandId] ?? '';
  const collection = collectionNameById[p.collectionId] ?? '';

  if (f.categories.length && !f.categories.includes(category)) return false;
  if (f.brands.length && !f.brands.includes(brand)) return false;
  if (f.collections.length && !f.collections.includes(collection)) return false;
  if (f.statuses.length && !f.statuses.includes(p.lifecycleState)) return false;
  if (f.types.length && !f.types.includes(p.productType)) return false;
  if (!inRange(p.pricing.sellingPrice, f.priceMin, f.priceMax)) return false;
  if (!inDateRange(p.createdAt, f.createdFrom, f.createdTo)) return false;
  if (!inDateRange(p.updatedAt, f.updatedFrom, f.updatedTo)) return false;
  if (f.hasImages != null && p.hasImages !== f.hasImages) return false;
  if (f.featured != null && p.featured !== f.featured) return false;
  if (f.draft === true && p.lifecycleState !== 'draft') return false;
  if (f.published === true && p.lifecycleState !== 'published') return false;
  if (f.archived === true && p.lifecycleState !== 'archived') return false;
  return true;
}

function sortProducts(list: CatalogProduct[], sort: SortOption): CatalogProduct[] {
  const arr = [...list];
  switch (sort) {
    case 'newest':
      return arr.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    case 'oldest':
      return arr.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
    case 'name_asc':
      return arr.sort((a, b) => a.name.localeCompare(b.name));
    case 'name_desc':
      return arr.sort((a, b) => b.name.localeCompare(a.name));
    case 'price_asc':
      return arr.sort((a, b) => a.pricing.sellingPrice - b.pricing.sellingPrice);
    case 'price_desc':
      return arr.sort((a, b) => b.pricing.sellingPrice - a.pricing.sellingPrice);
    case 'updated':
      return arr.sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime());
    case 'status':
      return arr.sort((a, b) => a.lifecycleState.localeCompare(b.lifecycleState));
    case 'category':
      return arr.sort((a, b) =>
        (categoryNameById[a.categoryId] ?? '').localeCompare(categoryNameById[b.categoryId] ?? ''),
      );
    case 'brand':
      return arr.sort((a, b) =>
        (brandNameById[a.brandId] ?? '').localeCompare(brandNameById[b.brandId] ?? ''),
      );
    default:
      return arr;
  }
}

function countActiveFilters(f: CatalogFilters): number {
  let count = 0;
  count += f.categories.length;
  count += f.brands.length;
  count += f.collections.length;
  count += f.statuses.length;
  count += f.types.length;
  if (f.priceMin != null || f.priceMax != null) count += 1;
  if (f.createdFrom || f.createdTo) count += 1;
  if (f.updatedFrom || f.updatedTo) count += 1;
  if (f.hasImages != null) count += 1;
  if (f.featured != null) count += 1;
  if (f.draft) count += 1;
  if (f.published) count += 1;
  if (f.archived) count += 1;
  return count;
}

export interface UseCatalogStateOptions {
  initialView?: CatalogViewMode;
  initialPageSize?: number;
}

export function useCatalogState(options: UseCatalogStateOptions = {}) {
  const [search, setSearchState] = useState('');
  const [filters, setFilters] = useState<CatalogFilters>({ ...EMPTY_FILTERS });
  const [sort, setSort] = useState<SortOption>('newest');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(options.initialPageSize ?? 20);
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());
  const [viewMode, setViewModeState] = useState<CatalogViewMode>(
    () => options.initialView ?? readSession<CatalogViewMode>(VIEW_KEY, 'table'),
  );
  const [savedViews, setSavedViews] = useState<SavedCatalogView[]>(() =>
    readSession<SavedCatalogView[]>(SAVED_VIEWS_KEY, []),
  );
  const [recentSearches, setRecentSearches] = useState<string[]>(() =>
    readSession<string[]>(RECENT_KEY, []),
  );
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    setLoading(true);
    mockDelay().then(() => {
      if (active) setLoading(false);
    });
    return () => {
      active = false;
    };
  }, []);

  const setViewMode = useCallback((mode: CatalogViewMode) => {
    setViewModeState(mode);
    writeSession(VIEW_KEY, mode);
  }, []);

  const setSearch = useCallback((value: string) => {
    setSearchState(value);
    setPage(1);
  }, []);

  const addRecentSearch = useCallback((q: string) => {
    const trimmed = q.trim();
    if (!trimmed) return;
    setRecentSearches((prev) => {
      const next = [trimmed, ...prev.filter((s) => s.toLowerCase() !== trimmed.toLowerCase())].slice(0, 5);
      writeSession(RECENT_KEY, next);
      return next;
    });
  }, []);

  const toggleSelect = useCallback((id: string) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  const clearSelection = useCallback(() => {
    setSelectedIds(new Set());
  }, []);

  const clearAllFilters = useCallback(() => {
    setFilters({ ...EMPTY_FILTERS });
    setPage(1);
  }, []);

  const filteredProducts = useMemo(() => {
    const result = CATALOG_PRODUCTS.filter(
      (p) => matchesSearch(p, search) && matchesFilters(p, filters),
    );
    return sortProducts(result, sort);
  }, [search, filters, sort]);

  const totalResults = filteredProducts.length;

  const totalPages = Math.max(1, Math.ceil(totalResults / pageSize));
  const safePage = Math.min(page, totalPages);

  const pagedProducts = useMemo(() => {
    const start = (safePage - 1) * pageSize;
    return filteredProducts.slice(start, start + pageSize);
  }, [filteredProducts, safePage, pageSize]);

  const toggleSelectAll = useCallback(() => {
    setSelectedIds((prev) => {
      const pageIds = pagedProducts.map((p) => p.id);
      const allSelected = pageIds.every((id) => prev.has(id));
      const next = new Set(prev);
      if (allSelected) {
        pageIds.forEach((id) => next.delete(id));
      } else {
        pageIds.forEach((id) => next.add(id));
      }
      return next;
    });
  }, [pagedProducts]);

  const saveView = useCallback(
    (name: string) => {
      const view: SavedCatalogView = {
        id: `view-${Date.now()}`,
        name,
        queryState: { search, page, pageSize },
        filters: { ...filters },
        sort,
        viewMode,
      };
      setSavedViews((prev) => {
        const next = [...prev, view];
        writeSession(SAVED_VIEWS_KEY, next);
        return next;
      });
    },
    [search, page, pageSize, filters, sort, viewMode],
  );

  const loadView = useCallback(
    (id: string) => {
      const view = savedViews.find((v) => v.id === id);
      if (!view) return;
      setFilters({ ...view.filters });
      setSort(view.sort);
      setViewMode(view.viewMode);
      setSearchState(view.queryState.search ?? '');
      setPage(1);
    },
    [savedViews, setViewMode],
  );

  const deleteView = useCallback((id: string) => {
    setSavedViews((prev) => {
      const next = prev.filter((v) => v.id !== id);
      writeSession(SAVED_VIEWS_KEY, next);
      return next;
    });
  }, []);

  const activeFilterCount = useMemo(() => countActiveFilters(filters), [filters]);

  return {
    search,
    setSearch,
    filters,
    setFilters,
    sort,
    setSort,
    page: safePage,
    setPage,
    pageSize,
    setPageSize,
    selectedIds,
    toggleSelect,
    toggleSelectAll,
    clearSelection,
    viewMode,
    setViewMode,
    savedViews,
    saveView,
    loadView,
    deleteView,
    loading,
    setLoading,
    filteredProducts,
    pagedProducts,
    totalResults,
    clearAllFilters,
    activeFilterCount,
    recentSearches,
    addRecentSearch,
  };
}

export type CatalogState = ReturnType<typeof useCatalogState>;
