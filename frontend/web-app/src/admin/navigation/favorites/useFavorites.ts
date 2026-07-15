import { useState, useCallback, useEffect } from 'react';
import type { FavoriteItem } from '../types';

const STORAGE_KEY = 'nav_favorites';

function loadFavorites(): FavoriteItem[] {
  try { return JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '[]'); } catch { return []; }
}

function saveFavorites(items: FavoriteItem[]) {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(items)); } catch {}
}

export function useFavorites() {
  const [favorites, setFavorites] = useState<FavoriteItem[]>(loadFavorites);

  useEffect(() => { saveFavorites(favorites); }, [favorites]);

  const addFavorite = useCallback((item: Omit<FavoriteItem, 'timestamp'>) => {
    setFavorites((prev) => {
      if (prev.some((f) => f.id === item.id)) return prev;
      return [...prev, { ...item, timestamp: Date.now() }];
    });
  }, []);

  const removeFavorite = useCallback((id: string) => {
    setFavorites((prev) => prev.filter((f) => f.id !== id));
  }, []);

  const togglePin = useCallback((id: string) => {
    setFavorites((prev) => prev.map((f) => (f.id === id ? { ...f, pinned: !f.pinned } : f)));
  }, []);

  const isFavorite = useCallback((id: string) => favorites.some((f) => f.id === id), [favorites]);

  const pinnedFavorites = favorites.filter((f) => f.pinned);
  const allFavorites = favorites;

  return { favorites: allFavorites, pinnedFavorites, addFavorite, removeFavorite, togglePin, isFavorite };
}
