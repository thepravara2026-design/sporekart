import { useState, useCallback, useMemo } from 'react';
import type { Student, StudentFilters, StudentSortConfig, StudentViewMode, StudentStatus, LearningMode } from '../types';
import { MOCK_STUDENTS, MOCK_STUDENT_STATS } from '../data/mockData';

export interface StudentWorkspaceState {
  students: Student[];
  stats: typeof MOCK_STUDENT_STATS;
  searchQuery: string;
  filters: StudentFilters;
  sort: StudentSortConfig;
  viewMode: StudentViewMode;
  page: number;
  pageSize: number;
  selectedIds: Set<string>;
  loading: boolean;
  error: string | null;
}

const DEFAULT_SORT: StudentSortConfig = { field: 'registrationDate', direction: 'desc' };

export function useStudentWorkspaceState() {
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState<StudentFilters>({});
  const [sort, setSort] = useState<StudentSortConfig>(DEFAULT_SORT);
  const [viewMode, setViewMode] = useState<StudentViewMode>('table');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const data = useMemo(() => MOCK_STUDENTS, []);
  const stats = useMemo(() => MOCK_STUDENT_STATS, []);

  const filteredStudents = useMemo(() => {
    let result = [...data];

    if (searchQuery) {
      const q = searchQuery.toLowerCase();
      result = result.filter(
        (s) =>
          s.studentId.toLowerCase().includes(q) ||
          s.fullName.toLowerCase().includes(q) ||
          s.email.toLowerCase().includes(q) ||
          s.phone.includes(q) ||
          (s.course && s.course.toLowerCase().includes(q)) ||
          s.language.toLowerCase().includes(q) ||
          s.category.toLowerCase().includes(q) ||
          s.tags.some((t) => t.toLowerCase().includes(q))
      );
    }

    if (filters.status && filters.status.length > 0) {
      result = result.filter((s) => filters.status!.includes(s.status));
    }
    if (filters.course && filters.course.length > 0) {
      result = result.filter((s) => s.course && filters.course!.includes(s.course));
    }
    if (filters.language && filters.language.length > 0) {
      result = result.filter((s) => filters.language!.includes(s.language));
    }
    if (filters.learningMode && filters.learningMode.length > 0) {
      result = result.filter((s) => filters.learningMode!.includes(s.learningMode));
    }
    if (filters.category && filters.category.length > 0) {
      result = result.filter((s) => filters.category!.includes(s.category));
    }
    if (filters.state && filters.state.length > 0) {
      result = result.filter((s) => filters.state!.includes(s.state));
    }
    if (filters.district && filters.district.length > 0) {
      result = result.filter((s) => filters.district!.includes(s.district));
    }
    if (filters.tags && filters.tags.length > 0) {
      result = result.filter((s) => filters.tags!.some((t) => s.tags.includes(t)));
    }
    if (filters.registrationDateFrom) {
      const from = new Date(filters.registrationDateFrom);
      result = result.filter((s) => new Date(s.registrationDate) >= from);
    }
    if (filters.registrationDateTo) {
      const to = new Date(filters.registrationDateTo);
      result = result.filter((s) => new Date(s.registrationDate) <= to);
    }

    result.sort((a, b) => {
      let cmp = 0;
      switch (sort.field) {
        case 'name':
          cmp = a.fullName.localeCompare(b.fullName);
          break;
        case 'email':
          cmp = a.email.localeCompare(b.email);
          break;
        case 'status':
          cmp = a.status.localeCompare(b.status);
          break;
        case 'course':
          cmp = (a.course || '').localeCompare(b.course || '');
          break;
        case 'registrationDate':
          cmp = new Date(a.registrationDate).getTime() - new Date(b.registrationDate).getTime();
          break;
        case 'language':
          cmp = a.language.localeCompare(b.language);
          break;
        case 'category':
          cmp = a.category.localeCompare(b.category);
          break;
      }
      return sort.direction === 'desc' ? -cmp : cmp;
    });

    return result;
  }, [data, searchQuery, filters, sort]);

  const totalFiltered = filteredStudents.length;
  const totalPages = Math.max(1, Math.ceil(totalFiltered / pageSize));

  const paginatedStudents = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filteredStudents.slice(start, start + pageSize);
  }, [filteredStudents, page, pageSize]);

  const updateFilter = useCallback(<K extends keyof StudentFilters>(key: K, value: StudentFilters[K]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
    setPage(1);
  }, []);

  const clearFilters = useCallback(() => {
    setFilters({});
    setSearchQuery('');
    setSort(DEFAULT_SORT);
    setPage(1);
    setSelectedIds(new Set());
  }, []);

  const setFilterStatus = useCallback((status: StudentStatus[]) => {
    updateFilter('status', status);
  }, [updateFilter]);

  const setFilterCourse = useCallback((course: string[]) => {
    updateFilter('course', course);
  }, [updateFilter]);

  const setFilterLanguage = useCallback((language: string[]) => {
    updateFilter('language', language);
  }, [updateFilter]);

  const setFilterLearningMode = useCallback((mode: LearningMode[]) => {
    updateFilter('learningMode', mode);
  }, [updateFilter]);

  const setFilterCategory = useCallback((category: string[]) => {
    updateFilter('category', category);
  }, [updateFilter]);

  const handleSort = useCallback((field: StudentSortConfig['field']) => {
    setSort((prev) => ({
      field,
      direction: prev.field === field && prev.direction === 'asc' ? 'desc' : 'asc',
    }));
  }, []);

  const toggleSelection = useCallback((id: string) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  const toggleSelectAll = useCallback(() => {
    setSelectedIds((prev) => {
      if (prev.size === paginatedStudents.length) return new Set();
      return new Set(paginatedStudents.map((s) => s.id));
    });
  }, [paginatedStudents]);

  const clearSelection = useCallback(() => {
    setSelectedIds(new Set());
  }, []);

  const handlePageChange = useCallback((newPage: number) => {
    setPage(Math.max(1, Math.min(newPage, totalPages)));
  }, [totalPages]);

  const handlePageSizeChange = useCallback((newSize: number) => {
    setPageSize(newSize);
    setPage(1);
  }, []);

  return {
    students: data,
    stats,
    searchQuery,
    setSearchQuery: useCallback((q: string) => { setSearchQuery(q); setPage(1); }, []),
    filters,
    updateFilter,
    clearFilters,
    setFilterStatus,
    setFilterCourse,
    setFilterLanguage,
    setFilterLearningMode,
    setFilterCategory,
    sort,
    handleSort,
    setSort,
    viewMode,
    setViewMode,
    page,
    totalPages,
    totalFiltered,
    pageSize,
    handlePageChange,
    handlePageSizeChange,
    pageSizeOptions: [10, 25, 50, 100],
    selectedIds,
    toggleSelection,
    toggleSelectAll,
    clearSelection,
    paginatedStudents,
    filteredStudents,
    loading,
    setLoading,
    error,
    setError,
  };
}

export type StudentWorkspaceContextValue = ReturnType<typeof useStudentWorkspaceState>;
