import { useState, useCallback, useMemo } from 'react';
import { getItems } from '../services/inventoryItemMockService';
import { filterInventoryItems, sortInventoryItems, paginateItems } from '../utils';
import type { InventoryItemFilterState } from '../types';
import { DEFAULT_INVENTORY_ITEM_FILTER_STATE } from '../constants';

export function useInventoryItemData(filterState: InventoryItemFilterState = DEFAULT_INVENTORY_ITEM_FILTER_STATE) {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortKey, setSortKey] = useState<'code' | 'name' | 'productName' | 'sku' | 'category' | 'status' | 'lifecycle' | 'updatedAt'>('updatedAt');
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('desc');
  const [page, setPage] = useState(1);
  const perPage = 10;

  const allItems = useMemo(() => getItems(), []);

  const filtered = useMemo(
    () => filterInventoryItems(allItems, searchQuery, filterState),
    [allItems, searchQuery, filterState],
  );

  const sorted = useMemo(() => sortInventoryItems(filtered, sortKey, sortDir), [filtered, sortKey, sortDir]);

  const paged = useMemo(() => paginateItems(sorted, page, perPage), [sorted, page]);

  const totalPages = Math.max(1, Math.ceil(sorted.length / perPage));

  const toggleSort = useCallback((key: typeof sortKey) => {
    setSortKey((prev) => {
      if (prev === key) {
        setSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));
        return prev;
      }
      setSortDir('asc');
      return key;
    });
  }, []);

  const handleSearch = useCallback((q: string) => {
    setSearchQuery(q);
    setPage(1);
  }, []);

  const goToPage = useCallback((p: number) => {
    setPage(Math.max(1, Math.min(p, totalPages)));
  }, [totalPages]);

  return { items: paged, allItems, totalCount: sorted.length, totalPages, page, sortKey, sortDir, toggleSort, searchQuery, setSearchQuery: handleSearch, goToPage, loading: false, error: undefined };
}
