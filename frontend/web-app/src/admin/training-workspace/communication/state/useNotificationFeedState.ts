// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Notification feed state
// Sprint 26 · Part 10. Read/unread + type/priority filtering over mock data.
// ---------------------------------------------------------------------------

import { useCallback, useMemo, useState } from 'react';
import { MOCK_NOTIFICATIONS } from '../data/communicationMockData';
import type {
  CommunicationPriority,
  NotificationItem,
  NotificationType,
} from '../data/communicationTypes';

export type ReadFilter = 'all' | 'unread' | 'read';

export interface NotificationFilters {
  search: string;
  read: ReadFilter;
  type: NotificationType | '';
  priority: CommunicationPriority | '';
}

export const DEFAULT_NOTIFICATION_FILTERS: NotificationFilters = {
  search: '',
  read: 'all',
  type: '',
  priority: '',
};

export interface UseNotificationFeedState {
  filters: NotificationFilters;
  setFilter: <K extends keyof NotificationFilters>(key: K, value: NotificationFilters[K]) => void;
  resetFilters: () => void;
  readState: Record<string, boolean>;
  markRead: (id: string) => void;
  markUnread: (id: string) => void;
  markAllRead: () => void;
  unreadCount: number;
  filtered: NotificationItem[];
}

export function useNotificationFeedState(): UseNotificationFeedState {
  const [filters, setFilters] = useState<NotificationFilters>(DEFAULT_NOTIFICATION_FILTERS);
  const [overrides, setOverrides] = useState<Record<string, boolean>>({});

  const setFilter = useCallback(
    <K extends keyof NotificationFilters>(key: K, value: NotificationFilters[K]) => {
      setFilters((prev) => ({ ...prev, [key]: value }));
    },
    [],
  );

  const resetFilters = useCallback(() => setFilters(DEFAULT_NOTIFICATION_FILTERS), []);

  const readState = useMemo(() => {
    const map: Record<string, boolean> = {};
    for (const n of MOCK_NOTIFICATIONS) {
      map[n.id] = overrides[n.id] ?? n.read;
    }
    return map;
  }, [overrides]);

  const markRead = useCallback((id: string) => {
    setOverrides((prev) => ({ ...prev, [id]: true }));
  }, []);

  const markUnread = useCallback((id: string) => {
    setOverrides((prev) => ({ ...prev, [id]: false }));
  }, []);

  const markAllRead = useCallback(() => {
    setOverrides(() => {
      const next: Record<string, boolean> = {};
      for (const n of MOCK_NOTIFICATIONS) next[n.id] = true;
      return next;
    });
  }, []);

  const unreadCount = useMemo(
    () => MOCK_NOTIFICATIONS.reduce((n, item) => (readState[item.id] ? n : n + 1), 0),
    [readState],
  );

  const filtered = useMemo(() => {
    const q = filters.search.trim().toLowerCase();
    return MOCK_NOTIFICATIONS.filter((n) => {
      if (q && !`${n.title} ${n.message}`.toLowerCase().includes(q)) return false;
      if (filters.type && n.type !== filters.type) return false;
      if (filters.priority && n.priority !== filters.priority) return false;
      if (filters.read === 'unread' && readState[n.id]) return false;
      if (filters.read === 'read' && !readState[n.id]) return false;
      return true;
    });
  }, [filters, readState]);

  return {
    filters,
    setFilter,
    resetFilters,
    readState,
    markRead,
    markUnread,
    markAllRead,
    unreadCount,
    filtered,
  };
}
