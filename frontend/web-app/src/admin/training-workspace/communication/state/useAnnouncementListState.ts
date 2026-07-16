// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Announcement list state
// Sprint 26 · Part 10. Client-side filter/search/sort/paginate over mock data.
// ---------------------------------------------------------------------------

import { useCallback, useMemo, useState } from 'react';
import { MOCK_ANNOUNCEMENTS } from '../data/communicationMockData';
import {
  DEFAULT_PAGE_SIZE,
  PRIORITY_WEIGHT,
  type AnnouncementSortKey,
} from '../data/communicationOptions';
import type {
  Announcement,
  CommunicationCategory,
  CommunicationPriority,
  CommunicationStatus,
} from '../data/communicationTypes';

export interface AnnouncementFilters {
  search: string;
  status: CommunicationStatus | '';
  category: CommunicationCategory | '';
  priority: CommunicationPriority | '';
  pinnedOnly: boolean;
  sort: AnnouncementSortKey;
}

export const DEFAULT_ANNOUNCEMENT_FILTERS: AnnouncementFilters = {
  search: '',
  status: '',
  category: '',
  priority: '',
  pinnedOnly: false,
  sort: 'newest',
};

export interface UseAnnouncementListState {
  filters: AnnouncementFilters;
  setFilter: <K extends keyof AnnouncementFilters>(key: K, value: AnnouncementFilters[K]) => void;
  resetFilters: () => void;
  activeFilterCount: number;
  page: number;
  pageSize: number;
  setPage: (p: number) => void;
  setPageSize: (s: number) => void;
  total: number;
  pageCount: number;
  filtered: Announcement[];
  paged: Announcement[];
}

function sortAnnouncements(list: Announcement[], sort: AnnouncementSortKey): Announcement[] {
  const copy = [...list];
  switch (sort) {
    case 'oldest':
      return copy.sort((a, b) => (a.createdAt > b.createdAt ? 1 : -1));
    case 'priority':
      return copy.sort((a, b) => PRIORITY_WEIGHT[b.priority] - PRIORITY_WEIGHT[a.priority]);
    case 'title':
      return copy.sort((a, b) => a.title.localeCompare(b.title));
    case 'views':
      return copy.sort((a, b) => b.viewsPlaceholder - a.viewsPlaceholder);
    case 'newest':
    default:
      return copy.sort((a, b) => (a.createdAt < b.createdAt ? 1 : -1));
  }
}

export function useAnnouncementListState(): UseAnnouncementListState {
  const [filters, setFilters] = useState<AnnouncementFilters>(DEFAULT_ANNOUNCEMENT_FILTERS);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSizeRaw] = useState(DEFAULT_PAGE_SIZE);

  const setFilter = useCallback(
    <K extends keyof AnnouncementFilters>(key: K, value: AnnouncementFilters[K]) => {
      setFilters((prev) => ({ ...prev, [key]: value }));
      setPage(1);
    },
    [],
  );

  const setPageSize = useCallback((s: number) => {
    setPageSizeRaw(s);
    setPage(1);
  }, []);

  const resetFilters = useCallback(() => {
    setFilters(DEFAULT_ANNOUNCEMENT_FILTERS);
    setPage(1);
  }, []);

  const filtered = useMemo(() => {
    const q = filters.search.trim().toLowerCase();
    const result = MOCK_ANNOUNCEMENTS.filter((a) => {
      if (q && !`${a.title} ${a.summary} ${a.author}`.toLowerCase().includes(q)) return false;
      if (filters.status && a.status !== filters.status) return false;
      if (filters.category && a.category !== filters.category) return false;
      if (filters.priority && a.priority !== filters.priority) return false;
      if (filters.pinnedOnly && !a.pinned) return false;
      return true;
    });
    return sortAnnouncements(result, filters.sort);
  }, [filters]);

  const activeFilterCount = useMemo(() => {
    let n = 0;
    if (filters.search.trim()) n += 1;
    if (filters.status) n += 1;
    if (filters.category) n += 1;
    if (filters.priority) n += 1;
    if (filters.pinnedOnly) n += 1;
    return n;
  }, [filters]);

  const total = filtered.length;
  const pageCount = Math.max(1, Math.ceil(total / pageSize));
  const safePage = Math.min(page, pageCount);

  const paged = useMemo(() => {
    const start = (safePage - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, safePage, pageSize]);

  return {
    filters,
    setFilter,
    resetFilters,
    activeFilterCount,
    page: safePage,
    pageSize,
    setPage,
    setPageSize,
    total,
    pageCount,
    filtered,
    paged,
  };
}
