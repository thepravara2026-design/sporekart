import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { CommunicationMessage, Announcement, InboxItem, TimelineEvent, CommunicationPreference, NotificationTemplate, CommunicationAnalytics, EngagementDashboard, CommunicationType, Priority, MessageStatus } from '../types';
import {
  getMessages, getAnnouncements, getInbox, getTimeline, getPreferences, getTemplates, getAnalytics, getDashboard,
} from '../data/mockData';

export type SortKey = 'createdDate' | 'priority' | 'title' | 'type' | 'studentName' | 'receivedDate' | 'readDate';
export type SortDirection = 'asc' | 'desc';

interface CommunicationFilters {
  search: string;
  type: CommunicationType | 'all';
  priority: Priority | 'all';
  status: MessageStatus | 'all';
  course: string;
  batch: string;
  category: string;
  dateFrom: string;
  dateTo: string;
  sortKey: SortKey;
  sortDirection: SortDirection;
}

interface CommunicationState {
  messages: CommunicationMessage[];
  announcements: Announcement[];
  inbox: InboxItem[];
  timeline: TimelineEvent[];
  preferences: CommunicationPreference[];
  templates: NotificationTemplate[];
  analytics: CommunicationAnalytics;
  dashboard: EngagementDashboard;
  filters: CommunicationFilters;
  page: number;
  pageSize: number;
}

interface CommunicationContextValue extends CommunicationState {
  setSearch: (s: string) => void;
  setTypeFilter: (t: CommunicationType | 'all') => void;
  setPriorityFilter: (p: Priority | 'all') => void;
  setStatusFilter: (s: MessageStatus | 'all') => void;
  setCourseFilter: (c: string) => void;
  setBatchFilter: (b: string) => void;
  setCategoryFilter: (c: string) => void;
  setDateFrom: (d: string) => void;
  setDateTo: (d: string) => void;
  setSort: (key: SortKey) => void;
  setPage: (p: number) => void;
  setPageSize: (s: number) => void;
  getFilteredMessages: () => CommunicationMessage[];
  getFilteredAnnouncements: () => Announcement[];
  getFilteredInbox: () => InboxItem[];
  toggleStar: (id: string) => void;
  toggleRead: (id: string) => void;
  updatePreference: (id: string, updates: Partial<CommunicationPreference>) => void;
}

const Ctx = createContext<CommunicationContextValue | undefined>(undefined);

const PRIORITY_ORDER: Record<string, number> = { critical: 0, high: 1, medium: 2, low: 3, informational: 4 };

function sortItems<T>(items: T[], key: SortKey, dir: SortDirection): T[] {
  return [...items].sort((a, b) => {
    const aVal = String((a as Record<string, unknown>)[key] ?? '');
    const bVal = String((b as Record<string, unknown>)[key] ?? '');
    let cmp: number;
    if (key === 'priority') {
      cmp = (PRIORITY_ORDER[aVal] ?? 99) - (PRIORITY_ORDER[bVal] ?? 99);
    } else {
      cmp = aVal.localeCompare(bVal);
    }
    return dir === 'asc' ? cmp : -cmp;
  });
}

function filterMessages<T extends { type: string; priority: string; courseId: string; batchId: string; title: string; createdDate: string }>(items: T[], filters: CommunicationFilters): T[] {
  let result = items;
  if (filters.type !== 'all') result = result.filter((m) => m.type === filters.type);
  if (filters.priority !== 'all') result = result.filter((m) => m.priority === filters.priority);
  if (filters.course !== 'all') result = result.filter((m) => m.courseId === filters.course);
  if (filters.batch !== 'all') result = result.filter((m) => m.batchId === filters.batch);
  if (filters.dateFrom) result = result.filter((m) => m.createdDate >= filters.dateFrom);
  if (filters.dateTo) result = result.filter((m) => m.createdDate <= filters.dateTo);
  if (filters.search) {
    const t = filters.search.toLowerCase();
    result = result.filter((m) => m.title.toLowerCase().includes(t));
  }
  return sortItems(result, filters.sortKey, filters.sortDirection);
}

export function CommunicationProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<CommunicationState>(() => ({
    messages: getMessages(), announcements: getAnnouncements(), inbox: getInbox(),
    timeline: getTimeline(), preferences: getPreferences(), templates: getTemplates(),
    analytics: getAnalytics(), dashboard: getDashboard(),
    filters: { search: '', type: 'all', priority: 'all', status: 'all', course: 'all', batch: 'all', category: 'all', dateFrom: '', dateTo: '', sortKey: 'createdDate', sortDirection: 'desc' },
    page: 1, pageSize: 10,
  }));

  const updFilter = useCallback((updates: Partial<CommunicationFilters>) => setState((p) => ({ ...p, filters: { ...p.filters, ...updates }, page: 1 })), []);
  const setSearch = useCallback((s: string) => updFilter({ search: s }), [updFilter]);
  const setTypeFilter = useCallback((t: CommunicationType | 'all') => updFilter({ type: t }), [updFilter]);
  const setPriorityFilter = useCallback((p: Priority | 'all') => updFilter({ priority: p }), [updFilter]);
  const setStatusFilter = useCallback((s: MessageStatus | 'all') => updFilter({ status: s }), [updFilter]);
  const setCourseFilter = useCallback((c: string) => updFilter({ course: c }), [updFilter]);
  const setBatchFilter = useCallback((b: string) => updFilter({ batch: b }), [updFilter]);
  const setCategoryFilter = useCallback((c: string) => updFilter({ category: c }), [updFilter]);
  const setDateFrom = useCallback((d: string) => updFilter({ dateFrom: d }), [updFilter]);
  const setDateTo = useCallback((d: string) => updFilter({ dateTo: d }), [updFilter]);
  const setSort = useCallback((sortKey: SortKey) => setState((p) => ({ ...p, filters: { ...p.filters, sortKey, sortDirection: p.filters.sortKey === sortKey && p.filters.sortDirection === 'asc' ? 'desc' : 'asc' }, page: 1 })), []);
  const setPage = useCallback((page: number) => setState((p) => ({ ...p, page })), []);
  const setPageSize = useCallback((pageSize: number) => setState((p) => ({ ...p, pageSize, page: 1 })), []);

  const getFilteredMessages = useCallback(() => filterMessages(state.messages, state.filters), [state.messages, state.filters]);
  const getFilteredAnnouncements = useCallback(() => filterMessages(state.announcements, state.filters), [state.announcements, state.filters]);
  const getFilteredInbox = useCallback(() => {
    let result = state.inbox;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((m) => m.title.toLowerCase().includes(t) || m.studentName.toLowerCase().includes(t));
    }
    if (state.filters.type !== 'all') result = result.filter((m) => m.type === state.filters.type);
    if (state.filters.priority !== 'all') result = result.filter((m) => m.priority === state.filters.priority);
    if (state.filters.category !== 'all') result = result.filter((m) => m.category === state.filters.category);
    if (state.filters.dateFrom) result = result.filter((m) => m.receivedDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((m) => m.receivedDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.inbox, state.filters]);

  const toggleStar = useCallback((id: string) => setState((p) => ({ ...p, inbox: p.inbox.map((item) => item.id === id ? { ...item, isStarred: !item.isStarred } : item) })), []);
  const toggleRead = useCallback((id: string) => setState((p) => ({ ...p, inbox: p.inbox.map((item) => item.id === id ? { ...item, isRead: !item.isRead, readDate: !item.isRead ? new Date().toISOString() : null } : item) })), []);
  const updatePreference = useCallback((id: string, updates: Partial<CommunicationPreference>) => setState((p) => ({ ...p, preferences: p.preferences.map((pref) => pref.id === id ? { ...pref, ...updates, lastUpdated: new Date().toISOString() } : pref) })), []);

  const value = useMemo(() => ({
    ...state, setSearch, setTypeFilter, setPriorityFilter, setStatusFilter, setCourseFilter, setBatchFilter,
    setCategoryFilter, setDateFrom, setDateTo, setSort, setPage, setPageSize,
    getFilteredMessages, getFilteredAnnouncements, getFilteredInbox, toggleStar, toggleRead, updatePreference,
  }), [state, setSearch, setTypeFilter, setPriorityFilter, setStatusFilter, setCourseFilter, setBatchFilter,
      setCategoryFilter, setDateFrom, setDateTo, setSort, setPage, setPageSize,
      getFilteredMessages, getFilteredAnnouncements, getFilteredInbox, toggleStar, toggleRead, updatePreference]);

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useCommunication(): CommunicationContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useCommunication must be used within CommunicationProvider');
  return ctx;
}
