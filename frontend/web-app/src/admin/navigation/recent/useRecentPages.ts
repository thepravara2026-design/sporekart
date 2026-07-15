import { useState, useCallback, useEffect } from 'react';
import type { RecentPage } from '../types';

const STORAGE_KEY = 'nav_recent';
const MAX_ITEMS = 20;

function loadRecent(): RecentPage[] {
  try { return JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '[]'); } catch { return []; }
}

function saveRecent(items: RecentPage[]) {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(items)); } catch {}
}

export function useRecentPages() {
  const [recent, setRecent] = useState<RecentPage[]>(loadRecent);

  useEffect(() => { saveRecent(recent); }, [recent]);

  const addRecent = useCallback((item: Omit<RecentPage, 'timestamp'>) => {
    setRecent((prev) => {
      const filtered = prev.filter((r) => r.id !== item.id);
      return [{ ...item, timestamp: Date.now() }, ...filtered].slice(0, MAX_ITEMS);
    });
  }, []);

  const removeRecent = useCallback((id: string) => {
    setRecent((prev) => prev.filter((r) => r.id !== id));
  }, []);

  const clearRecent = useCallback(() => {
    setRecent([]);
  }, []);

  return { recent, addRecent, removeRecent, clearRecent };
}
