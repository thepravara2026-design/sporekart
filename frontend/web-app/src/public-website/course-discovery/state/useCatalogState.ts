import { useCallback, useMemo, useState } from 'react';
import type { Course } from '../../../admin/training-workspace/courses/data/courseMockData';
import { MOCK_COURSES } from '../../../admin/training-workspace/courses/data/courseMockData';
import {
  buildDiscoveryCatalog,
  type DiscoveryCourse,
} from '../data/discoveryMockData';
import {
  DEFAULT_CATALOG_FILTERS,
  DIFFICULTY_RANK,
  type CatalogFilters,
  type CatalogSortField,
  type CatalogViewMode,
} from '../data/catalogOptions';

export const CATALOG_PAGE_SIZE = 9;

function durationWeeks(course: Course): number {
  const match = course.duration.match(/(\d+)/);
  return match ? parseInt(match[1], 10) : course.durationHours / 10;
}

function applyFilters(courses: DiscoveryCourse[], filters: CatalogFilters): DiscoveryCourse[] {
  const q = filters.search.trim().toLowerCase();
  return courses.filter((dc) => {
    const c = dc.course;
    if (q) {
      const haystack = [
        c.name,
        c.code,
        c.shortDescription,
        c.longDescription,
        c.category,
        c.level,
        c.deliveryMode,
        c.language,
        ...c.learningObjectives,
        ...c.targetAudience,
      ]
        .join(' ')
        .toLowerCase();
      if (!haystack.includes(q)) return false;
    }
    if (filters.category && c.category !== filters.category) return false;
    if (filters.level && c.level !== filters.level) return false;
    if (filters.language && c.language !== filters.language) return false;
    if (filters.deliveryMode && c.deliveryMode !== filters.deliveryMode) return false;
    if (filters.trainingType && c.level !== filters.trainingType && c.deliveryMode !== filters.trainingType) return false;
    if (filters.skillLevel && c.level !== filters.skillLevel) return false;
    if (filters.status === 'published' && c.lifecycle !== 'published') return false;
    if (filters.status === 'scheduled' && c.lifecycle !== 'scheduled') return false;

    if (filters.priceBand !== 'all') {
      if (filters.priceBand === 'free' && dc.price > 0) return false;
      if (filters.priceBand === 'low' && !(dc.price > 0 && dc.price < 2500)) return false;
      if (filters.priceBand === 'mid' && !(dc.price >= 2500 && dc.price <= 5000)) return false;
      if (filters.priceBand === 'high' && dc.price < 5000) return false;
    }

    if (filters.durationBand !== 'all') {
      const weeks = durationWeeks(c);
      if (filters.durationBand === 'short' && weeks >= 4) return false;
      if (filters.durationBand === 'medium' && (weeks < 4 || weeks > 6)) return false;
      if (filters.durationBand === 'long' && weeks <= 6) return false;
    }

    if (filters.availability !== 'all') {
      if (filters.availability === 'open' && dc.availableSeats <= 0) return false;
      if (filters.availability === 'limited' && (dc.availableSeats <= 0 || dc.availableSeats > 5)) return false;
      if (filters.availability === 'full' && dc.availableSeats > 0) return false;
    }

    return true;
  });
}

function applySort(courses: DiscoveryCourse[], sort: CatalogSortField): DiscoveryCourse[] {
  const sorted = [...courses];
  switch (sort) {
    case 'popular':
      sorted.sort((a, b) => b.enrollmentCountPlaceholder - a.enrollmentCountPlaceholder);
      break;
    case 'newest':
      sorted.sort((a, b) => b.course.createdAt.localeCompare(a.course.createdAt));
      break;
    case 'updated':
      sorted.sort((a, b) => b.course.updatedAt.localeCompare(a.course.updatedAt));
      break;
    case 'trending':
      sorted.sort((a, b) => Number(b.trending) - Number(a.trending) || b.enrollmentCountPlaceholder - a.enrollmentCountPlaceholder);
      break;
    case 'featured':
      sorted.sort((a, b) => Number(b.featured) - Number(a.featured) || b.enrollmentCountPlaceholder - a.enrollmentCountPlaceholder);
      break;
    case 'alphabetical':
      sorted.sort((a, b) => a.course.name.localeCompare(b.course.name));
      break;
    case 'duration':
      sorted.sort((a, b) => durationWeeks(a.course) - durationWeeks(b.course));
      break;
    case 'price':
      sorted.sort((a, b) => a.price - b.price);
      break;
    case 'difficulty':
      sorted.sort((a, b) => DIFFICULTY_RANK[a.course.level] - DIFFICULTY_RANK[b.course.level]);
      break;
  }
  return sorted;
}

export function useCatalogState(initial?: DiscoveryCourse[]) {
  const all = useMemo<DiscoveryCourse[]>(
    () => initial ?? buildDiscoveryCatalog(MOCK_COURSES),
    [initial],
  );

  const [viewMode, setViewMode] = useState<CatalogViewMode>('grid');
  const [filters, setFilters] = useState<CatalogFilters>({ ...DEFAULT_CATALOG_FILTERS });
  const [sort, setSort] = useState<CatalogSortField>('popular');
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);

  const updateFilter = useCallback((key: keyof CatalogFilters, value: string) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
    setCurrentPage(1);
  }, []);

  const clearFilters = useCallback(() => {
    setFilters({ ...DEFAULT_CATALOG_FILTERS });
    setCurrentPage(1);
  }, []);

  const setSortField = useCallback((field: CatalogSortField) => {
    setSort(field);
    setCurrentPage(1);
  }, []);

  const filtered = useMemo(() => applyFilters(all, filters), [all, filters]);
  const sorted = useMemo(() => applySort(filtered, sort), [filtered, sort]);
  const totalItems = sorted.length;
  const totalPages = Math.max(1, Math.ceil(totalItems / CATALOG_PAGE_SIZE));
  const safePage = Math.min(currentPage, totalPages);

  const paginated = useMemo(
    () => sorted.slice((safePage - 1) * CATALOG_PAGE_SIZE, safePage * CATALOG_PAGE_SIZE),
    [sorted, safePage],
  );

  const setPage = useCallback((page: number) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
  }, [totalPages]);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.search) count++;
    if (filters.category) count++;
    if (filters.level) count++;
    if (filters.language) count++;
    if (filters.deliveryMode) count++;
    if (filters.trainingType) count++;
    if (filters.priceBand !== 'all') count++;
    if (filters.durationBand !== 'all') count++;
    if (filters.availability !== 'all') count++;
    if (filters.skillLevel) count++;
    if (filters.status !== 'all') count++;
    return count;
  }, [filters]);

  return {
    all,
    viewMode,
    setViewMode,
    filters,
    updateFilter,
    clearFilters,
    activeFilterCount,
    sort,
    setSortField,
    paginated,
    totalItems,
    totalPages,
    currentPage: safePage,
    setPage,
    loading,
    setLoading,
  };
}

export type CatalogState = ReturnType<typeof useCatalogState>;
