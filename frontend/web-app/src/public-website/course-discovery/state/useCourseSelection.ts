import { useCallback, useMemo, useState } from 'react';
import type { DiscoveryCourse } from '../data/discoveryMockData';

const COMPARE_LIMIT = 3;

export function useCourseComparison(all: DiscoveryCourse[]) {
  const [compareIds, setCompareIds] = useState<string[]>([]);

  const toggleCompare = useCallback((id: string) => {
    setCompareIds((prev) => {
      if (prev.includes(id)) return prev.filter((x) => x !== id);
      if (prev.length >= COMPARE_LIMIT) return prev;
      return [...prev, id];
    });
  }, []);

  const clearCompare = useCallback(() => setCompareIds([]), []);

  const compareCourses = useMemo(
    () => compareIds.map((id) => all.find((c) => c.course.id === id)).filter(Boolean) as DiscoveryCourse[],
    [compareIds, all],
  );

  return {
    compareIds,
    compareCourses,
    toggleCompare,
    clearCompare,
    isComparing: (id: string) => compareIds.includes(id),
    canAddMore: compareIds.length < COMPARE_LIMIT,
    limit: COMPARE_LIMIT,
  };
}

export interface DiscoverySelectionState {
  bookmarks: Set<string>;
  wishlist: Set<string>;
  toggleBookmark: (id: string) => void;
  toggleWishlist: (id: string) => void;
  isBookmarked: (id: string) => boolean;
  isWishlisted: (id: string) => boolean;
}

export function useDiscoverySelection(): DiscoverySelectionState {
  const [bookmarks, setBookmarks] = useState<Set<string>>(new Set());
  const [wishlist, setWishlist] = useState<Set<string>>(new Set());

  const toggleBookmark = useCallback((id: string) => {
    setBookmarks((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  const toggleWishlist = useCallback((id: string) => {
    setWishlist((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }, []);

  return {
    bookmarks,
    wishlist,
    toggleBookmark,
    toggleWishlist,
    isBookmarked: (id) => bookmarks.has(id),
    isWishlisted: (id) => wishlist.has(id),
  };
}
