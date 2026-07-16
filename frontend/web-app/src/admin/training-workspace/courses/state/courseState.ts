import { useState, useCallback, useMemo } from 'react';
import { MOCK_COURSES, getFilteredCourses, computeCourseStats } from '../data/courseMockData';
import type { CourseDashboardStats } from '../data/courseMockData';

export type ViewMode = 'grid' | 'list' | 'compact' | 'table';

export interface CourseFilters {
  search: string;
  status: string;
  category: string;
  level: string;
  language: string;
  deliveryMode: string;
}

export interface CourseSelection {
  selectedIds: Set<string>;
  lastSelectedId: string | null;
}

export interface CoursePageState {
  currentPage: number;
  pageSize: number;
  totalItems: number;
}

export function useCourseState() {
  const allCourses = useMemo(() => MOCK_COURSES, []);

  const [viewMode, setViewMode] = useState<ViewMode>('grid');
  const [filters, setFilters] = useState<CourseFilters>({
    search: '',
    status: '',
    category: '',
    level: '',
    language: '',
    deliveryMode: '',
  });
  const [sortBy, setSortBy] = useState<'updatedAt' | 'name' | 'createdAt' | 'rating'>('updatedAt');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');
  const [pinnedOnly, setPinnedOnly] = useState(false);
  const [favoritesOnly, setFavoritesOnly] = useState(false);
  const [selection, setSelection] = useState<CourseSelection>({
    selectedIds: new Set(),
    lastSelectedId: null,
  });
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 12;
  const [loading, setLoading] = useState(false);

  const stats: CourseDashboardStats = useMemo(() => computeCourseStats(allCourses), [allCourses]);

  const filtered = useMemo(() => {
    let result = getFilteredCourses(allCourses, filters);
    if (pinnedOnly) result = result.filter((c) => c.pinned);
    if (favoritesOnly) result = result.filter((c) => c.favorite);
    result.sort((a, b) => {
      const aVal = a[sortBy];
      const bVal = b[sortBy];
      if (typeof aVal === 'string' && typeof bVal === 'string') {
        return sortOrder === 'desc'
          ? bVal.localeCompare(aVal)
          : aVal.localeCompare(bVal);
      }
      return sortOrder === 'desc'
        ? (bVal as number) - (aVal as number)
        : (aVal as number) - (bVal as number);
    });
    return result;
  }, [allCourses, filters, sortBy, sortOrder, pinnedOnly, favoritesOnly]);

  const totalItems = filtered.length;
  const totalPages = Math.max(1, Math.ceil(totalItems / pageSize));
  const safePage = Math.min(currentPage, totalPages);

  const paginatedCourses = useMemo(() => {
    const start = (safePage - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, safePage, pageSize]);

  const updateFilter = useCallback((key: keyof CourseFilters, value: string) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
    setCurrentPage(1);
  }, []);

  const clearFilters = useCallback(() => {
    setFilters({ search: '', status: '', category: '', level: '', language: '', deliveryMode: '' });
    setCurrentPage(1);
  }, []);

  const togglePinned = useCallback((courseId: string) => {
    const course = MOCK_COURSES.find((c) => c.id === courseId);
    if (course) course.pinned = !course.pinned;
    setCurrentPage((p) => p);
  }, []);

  const toggleFavorite = useCallback((courseId: string) => {
    const course = MOCK_COURSES.find((c) => c.id === courseId);
    if (course) course.favorite = !course.favorite;
    setCurrentPage((p) => p);
  }, []);

  const toggleSelect = useCallback((courseId: string) => {
    setSelection((prev) => {
      const next = new Set(prev.selectedIds);
      if (next.has(courseId)) next.delete(courseId);
      else next.add(courseId);
      return { selectedIds: next, lastSelectedId: courseId };
    });
  }, []);

  const selectAll = useCallback(() => {
    setSelection({
      selectedIds: new Set(paginatedCourses.map((c) => c.id)),
      lastSelectedId: null,
    });
  }, [paginatedCourses]);

  const clearSelection = useCallback(() => {
    setSelection({ selectedIds: new Set(), lastSelectedId: null });
  }, []);

  const setPage = useCallback((page: number) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
  }, [totalPages]);

  const setSort = useCallback((field: 'updatedAt' | 'name' | 'createdAt' | 'rating') => {
    setSortBy((prev) => {
      if (prev === field) {
        setSortOrder((o) => (o === 'desc' ? 'asc' : 'desc'));
        return prev;
      }
      setSortOrder('desc');
      return field;
    });
  }, []);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.status) count++;
    if (filters.category) count++;
    if (filters.level) count++;
    if (filters.language) count++;
    if (filters.deliveryMode) count++;
    if (pinnedOnly) count++;
    if (favoritesOnly) count++;
    return count;
  }, [filters, pinnedOnly, favoritesOnly]);

  return {
    allCourses,
    paginatedCourses,
    stats,
    viewMode,
    setViewMode,
    filters,
    updateFilter,
    clearFilters,
    activeFilterCount,
    sortBy,
    sortOrder,
    setSort,
    pinnedOnly,
    setPinnedOnly,
    favoritesOnly,
    setFavoritesOnly,
    selection,
    toggleSelect,
    selectAll,
    clearSelection,
    currentPage: safePage,
    totalPages,
    totalItems,
    pageSize,
    setPage,
    togglePinned,
    toggleFavorite,
    loading,
    setLoading,
  };
}

export type CourseStateValue = ReturnType<typeof useCourseState>;
