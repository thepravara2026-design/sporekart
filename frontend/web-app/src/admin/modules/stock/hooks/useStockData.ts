import { useState, useCallback, useMemo } from 'react';
import { getStockRecords, getStockMetrics, getStockActivities } from '../services/stockMockService';
import { filterStockRecords, sortStockRecords, paginateItems } from '../utils';
import type { StockFilterState } from '../types';
import { DEFAULT_STOCK_FILTER_STATE } from '../constants';

export function useStockData(filterState: StockFilterState = DEFAULT_STOCK_FILTER_STATE) {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortKey, setSortKey] = useState<'code' | 'inventoryItemName' | 'sku' | 'warehouseName' | 'health' | 'status' | 'updatedAt'>('updatedAt');
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('desc');
  const [page, setPage] = useState(1);
  const perPage = 10;

  const allRecords = useMemo(() => getStockRecords(), []);

  const filtered = useMemo(
    () => filterStockRecords(allRecords, searchQuery, filterState),
    [allRecords, searchQuery, filterState],
  );

  const sorted = useMemo(() => sortStockRecords(filtered, sortKey, sortDir), [filtered, sortKey, sortDir]);
  const paged = useMemo(() => paginateItems(sorted, page, perPage), [sorted, page]);
  const totalPages = Math.max(1, Math.ceil(sorted.length / perPage));

  const toggleSort = useCallback((key: typeof sortKey) => {
    setSortKey((prev) => {
      if (prev === key) { setSortDir((d) => (d === 'asc' ? 'desc' : 'asc')); return prev; }
      setSortDir('asc'); return key;
    });
  }, []);

  const handleSearch = useCallback((q: string) => { setSearchQuery(q); setPage(1); }, []);
  const goToPage = useCallback((p: number) => { setPage(Math.max(1, Math.min(p, totalPages))); }, [totalPages]);

  return { records: paged, allRecords, totalCount: sorted.length, totalPages, page, sortKey, sortDir, toggleSort, searchQuery, setSearchQuery: handleSearch, goToPage, loading: false, error: undefined };
}

export function useStockDataContext() {
  const [records] = useState(() => getStockRecords());
  const [metrics] = useState(() => getStockMetrics());
  const [activities] = useState(() => getStockActivities());
  return { records, metrics, activities };
}
