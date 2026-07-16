import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { CommunicationMessage, Announcement, InboxItem, TimelineEvent, CommunicationPreference, NotificationTemplate, CommunicationAnalytics, EngagementDashboard, CommunicationType, Priority, MessageStatus } from '../types';
import {
  getMessages, getAnnouncements, getInbox, getTimeline, getPreferences, getTemplates, getAnalytics, getDashboard,
} from '../data/mockData';

interface CommunicationFilters {
  search: string;
  type: CommunicationType | 'all';
  priority: Priority | 'all';
  status: MessageStatus | 'all';
  course: string;
  batch: string;
  category: string;
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
}

interface CommunicationContextValue extends CommunicationState {
  setSearch: (s: string) => void;
  setTypeFilter: (t: CommunicationType | 'all') => void;
  setPriorityFilter: (p: Priority | 'all') => void;
  setStatusFilter: (s: MessageStatus | 'all') => void;
  setCourseFilter: (c: string) => void;
  setBatchFilter: (b: string) => void;
  getFilteredMessages: () => CommunicationMessage[];
  getFilteredAnnouncements: () => Announcement[];
  getFilteredInbox: () => InboxItem[];
  toggleStar: (id: string) => void;
  toggleRead: (id: string) => void;
  updatePreference: (id: string, updates: Partial<CommunicationPreference>) => void;
}

const Ctx = createContext<CommunicationContextValue | undefined>(undefined);

function filterMessages<T extends { type: string; priority: string; courseId: string; batchId: string; title: string }>(items: T[], filters: CommunicationFilters): T[] {
  let result = items;
  if (filters.type !== 'all') result = result.filter((m) => m.type === filters.type);
  if (filters.priority !== 'all') result = result.filter((m) => m.priority === filters.priority);
  if (filters.course !== 'all') result = result.filter((m) => m.courseId === filters.course);
  if (filters.batch !== 'all') result = result.filter((m) => m.batchId === filters.batch);
  if (filters.search) {
    const t = filters.search.toLowerCase();
    result = result.filter((m) => m.title.toLowerCase().includes(t));
  }
  return result;
}

export function CommunicationProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<CommunicationState>(() => ({
    messages: getMessages(), announcements: getAnnouncements(), inbox: getInbox(),
    timeline: getTimeline(), preferences: getPreferences(), templates: getTemplates(),
    analytics: getAnalytics(), dashboard: getDashboard(),
    filters: { search: '', type: 'all', priority: 'all', status: 'all', course: 'all', batch: 'all', category: 'all' },
  }));

  const updFilter = useCallback((updates: Partial<CommunicationFilters>) => setState((p) => ({ ...p, filters: { ...p.filters, ...updates } })), []);
  const setSearch = useCallback((s: string) => updFilter({ search: s }), [updFilter]);
  const setTypeFilter = useCallback((t: CommunicationType | 'all') => updFilter({ type: t }), [updFilter]);
  const setPriorityFilter = useCallback((p: Priority | 'all') => updFilter({ priority: p }), [updFilter]);
  const setStatusFilter = useCallback((s: MessageStatus | 'all') => updFilter({ status: s }), [updFilter]);
  const setCourseFilter = useCallback((c: string) => updFilter({ course: c }), [updFilter]);
  const setBatchFilter = useCallback((b: string) => updFilter({ batch: b }), [updFilter]);

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
    return result;
  }, [state.inbox, state.filters]);

  const toggleStar = useCallback((id: string) => setState((p) => ({ ...p, inbox: p.inbox.map((item) => item.id === id ? { ...item, isStarred: !item.isStarred } : item) })), []);
  const toggleRead = useCallback((id: string) => setState((p) => ({ ...p, inbox: p.inbox.map((item) => item.id === id ? { ...item, isRead: !item.isRead, readDate: !item.isRead ? new Date().toISOString() : null } : item) })), []);
  const updatePreference = useCallback((id: string, updates: Partial<CommunicationPreference>) => setState((p) => ({ ...p, preferences: p.preferences.map((pref) => pref.id === id ? { ...pref, ...updates, lastUpdated: new Date().toISOString() } : pref) })), []);

  const value = useMemo(() => ({
    ...state, setSearch, setTypeFilter, setPriorityFilter, setStatusFilter, setCourseFilter, setBatchFilter,
    getFilteredMessages, getFilteredAnnouncements, getFilteredInbox, toggleStar, toggleRead, updatePreference,
  }), [state, setSearch, setTypeFilter, setPriorityFilter, setStatusFilter, setCourseFilter, setBatchFilter,
      getFilteredMessages, getFilteredAnnouncements, getFilteredInbox, toggleStar, toggleRead, updatePreference]);

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useCommunication(): CommunicationContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useCommunication must be used within CommunicationProvider');
  return ctx;
}
