import { useCallback, useEffect, useMemo, useState } from 'react';
import { MOCK_ASSETS, mockDelay, getAssetById } from '../mock/mockAssets';
import { MOCK_COLLECTIONS, getCollectionById } from '../mock/mockCollections';
import type { Asset, MediaFilters, SortOption, ViewMode } from '../types';
import { EMPTY_FILTERS } from '../types';

const VIEW_KEY = 'sk_media_view';
const SIDEBAR_KEY = 'sk_media_sidebar';

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

function matchesSearch(a: Asset, q: string): boolean {
  if (!q) return true;
  const needle = q.toLowerCase();
  const haystack = [a.name, a.fileName, a.title, a.description, a.alt, ...a.tags].join(' ').toLowerCase();
  return haystack.includes(needle);
}

function matchesFilters(a: Asset, f: MediaFilters): boolean {
  if (f.types.length && !f.types.includes(a.type)) return false;
  if (f.statuses.length && !f.statuses.includes(a.status)) return false;
  if (f.collections.length && !a.collectionIds.some((cid: string) => f.collections.includes(cid))) return false;
  if (f.tags.length && !f.tags.some((t) => f.tags.includes(t))) return false;
  if (f.dateFrom && new Date(a.createdAt).getTime() < new Date(f.dateFrom).getTime()) return false;
  if (f.dateTo) {
    const end = new Date(f.dateTo).getTime() + 86400000;
    if (new Date(a.createdAt).getTime() > end) return false;
  }
  if (f.fileSizeMin != null && a.fileSize < f.fileSizeMin) return false;
  if (f.fileSizeMax != null && a.fileSize > f.fileSizeMax) return false;
  if (f.widthMin != null && (a.width ?? 0) < f.widthMin) return false;
  if (f.widthMax != null && (a.width ?? 0) > f.widthMax) return false;
  if (f.heightMin != null && (a.height ?? 0) < f.heightMin) return false;
  if (f.heightMax != null && (a.height ?? 0) > f.heightMax) return false;
  return true;
}

function sortAssets(list: Asset[], sort: SortOption): Asset[] {
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
    case 'size_asc':
      return arr.sort((a, b) => a.fileSize - b.fileSize);
    case 'size_desc':
      return arr.sort((a, b) => b.fileSize - a.fileSize);
    case 'type':
      return arr.sort((a, b) => a.type.localeCompare(b.type));
    default:
      return arr;
  }
}

function getAllTags(): string[] {
  const tagSet = new Set<string>();
  MOCK_ASSETS.forEach((a) => a.tags.forEach((t) => tagSet.add(t)));
  return Array.from(tagSet).sort();
}

export function useMediaLibrary() {
  const [search, setSearchState] = useState('');
  const [filters, setFilters] = useState<MediaFilters>({ ...EMPTY_FILTERS });
  const [sort, setSort] = useState<SortOption>('newest');
  const [viewMode, setViewModeState] = useState<ViewMode>(
    () => readSession<ViewMode>(VIEW_KEY, 'grid'),
  );
  const [selectedIds, setSelectedIds] = useState<Set<string>>(new Set());
  const [activeCollectionId, setActiveCollectionId] = useState<string | null>(null);
  const [sidebarOpen, setSidebarOpenState] = useState<boolean>(
    () => readSession<boolean>(SIDEBAR_KEY, true),
  );
  const [previewAssetId, setPreviewAssetId] = useState<string | null>(null);
  const [uploadOpen, setUploadOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    setLoading(true);
    mockDelay(350).then(() => {
      if (active) setLoading(false);
    });
    return () => { active = false; };
  }, []);

  const setViewMode = useCallback((mode: ViewMode) => {
    setViewModeState(mode);
    writeSession(VIEW_KEY, mode);
  }, []);

  const setSidebarOpen = useCallback((open: boolean) => {
    setSidebarOpenState(open);
    writeSession(SIDEBAR_KEY, open);
  }, []);

  const setSearch = useCallback((value: string) => {
    setSearchState(value);
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
    setActiveCollectionId(null);
  }, []);

  const setCollectionFilter = useCallback((collectionId: string | null) => {
    setActiveCollectionId(collectionId);
    if (collectionId) {
      setFilters((prev) => ({ ...prev, collections: [collectionId] }));
    } else {
      setFilters((prev) => ({ ...prev, collections: [] }));
    }
  }, []);

  const openPreview = useCallback((id: string) => setPreviewAssetId(id), []);
  const closePreview = useCallback(() => setPreviewAssetId(null), []);
  const openUpload = useCallback(() => setUploadOpen(true), []);
  const closeUpload = useCallback(() => setUploadOpen(false), []);

  const allTags = useMemo(() => getAllTags(), []);

  const baseAssets = useMemo(() => {
    return MOCK_ASSETS;
  }, []);

  const filteredAssets = useMemo(() => {
    let result = baseAssets;
    if (activeCollectionId && activeCollectionId !== 'col-all') {
      result = result.filter((a) => a.collectionIds.includes(activeCollectionId));
    }
    result = result.filter((a) => matchesSearch(a, search) && matchesFilters(a, filters));
    return sortAssets(result, sort);
  }, [baseAssets, activeCollectionId, search, filters, sort]);

  const selectAll = useCallback(() => {
    setSelectedIds((prev) => {
      if (prev.size === filteredAssets.length) return new Set();
      return new Set(filteredAssets.map((a) => a.id));
    });
  }, [filteredAssets]);

  const previewAsset = useMemo(() => {
    if (!previewAssetId) return null;
    return getAssetById(previewAssetId) ?? null;
  }, [previewAssetId]);

  const collections = useMemo(() => MOCK_COLLECTIONS, []);

  const activeCollection = useMemo(() => {
    if (!activeCollectionId) return null;
    return getCollectionById(activeCollectionId) ?? null;
  }, [activeCollectionId]);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    count += filters.types.length;
    count += filters.statuses.length;
    count += filters.collections.length;
    count += filters.tags.length;
    if (filters.dateFrom || filters.dateTo) count += 1;
    if (filters.fileSizeMin != null || filters.fileSizeMax != null) count += 1;
    if (filters.widthMin != null || filters.widthMax != null) count += 1;
    if (filters.heightMin != null || filters.heightMax != null) count += 1;
    return count;
  }, [filters]);

  return {
    search,
    setSearch,
    filters,
    setFilters,
    sort,
    setSort,
    viewMode,
    setViewMode,
    selectedIds,
    toggleSelect,
    selectAll,
    clearSelection,
    activeCollectionId,
    setActiveCollectionId: setCollectionFilter,
    activeCollection,
    sidebarOpen,
    setSidebarOpen,
    previewAssetId,
    previewAsset,
    openPreview,
    closePreview,
    uploadOpen,
    openUpload,
    closeUpload,
    loading,
    filteredAssets,
    collections,
    allTags,
    clearAllFilters,
    activeFilterCount,
  };
}

export type MediaLibraryState = ReturnType<typeof useMediaLibrary>;
