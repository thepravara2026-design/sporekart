import { useState, useCallback, useMemo, useEffect, useRef } from 'react';
import { useSearchParams } from 'react-router-dom';
import type { SortConfig, FilterConfig, ColumnConfig, QueryState } from '../components/data-grid/types';

const PAGE_SIZE_KEY = 'dg_pageSize';

function useStableCallback<T extends (...args: any[]) => any>(fn: T): T {
  const ref = useRef(fn);
  ref.current = fn;
  return useCallback((...args: any[]) => ref.current(...args), []) as T;
}

function serializeFilters(filters: FilterConfig[]): string {
  return JSON.stringify(filters.map((f) => ({ id: f.id, value: f.value })));
}

function deserializeFilters(raw: string | null, defaults: FilterConfig[]): FilterConfig[] {
  if (!raw) return defaults;
  try {
    const parsed = JSON.parse(raw);
    return defaults.map((d) => {
      const found = parsed.find((p: any) => p.id === d.id);
      return found ? { ...d, value: found.value } : d;
    });
  } catch {
    return defaults;
  }
}

function serializeSort(sort: SortConfig[]): string {
  return sort.map((s) => `${s.key}:${s.direction}`).join(',');
}

function deserializeSort(raw: string | null): SortConfig[] {
  if (!raw) return [];
  return raw.split(',').map((part) => {
    const [key, direction] = part.split(':');
    return { key, direction: direction === 'desc' ? 'desc' : 'asc' };
  });
}

function serializeColumns(columns: ColumnConfig[]): string {
  return JSON.stringify(
    columns.map((c) => ({
      k: c.key,
      v: c.visible,
      o: c.order,
      w: c.width,
      p: c.pinned,
    }))
  );
}

function deserializeColumns(raw: string | null, defaults: ColumnConfig[]): ColumnConfig[] {
  if (!raw) return defaults;
  try {
    const parsed = JSON.parse(raw);
    return defaults.map((d) => {
      const found = parsed.find((p: any) => p.k === d.key);
      return found ? { ...d, visible: found.v, order: found.o, width: found.w ?? d.width, pinned: found.p ?? d.pinned } : d;
    });
  } catch {
    return defaults;
  }
}

function queryStateToParams(state: QueryState): Record<string, string> {
  const params: Record<string, string> = {};
  if (state.search) params.q = state.search;
  if (state.filters.some((f) => f.value !== null && f.value !== '' && !(Array.isArray(f.value) && f.value.length === 0))) {
    params.f = serializeFilters(state.filters);
  }
  if (state.sort.length > 0) params.s = serializeSort(state.sort);
  if (state.page > 1) params.p = String(state.page);
  if (state.pageSize !== 25) params.ps = String(state.pageSize);
  const colStr = serializeColumns(state.columnConfig);
  const defaultColStr = serializeColumns(getDefaultColumnConfig(state.columnConfig.map(() => ({ key: '', header: '' }))));
  if (colStr !== defaultColStr) params.c = colStr;
  return params;
}

function getDefaultColumnConfig(columns: { key: string }[]): ColumnConfig[] {
  return columns.map((col, i) => ({
    key: col.key,
    header: '',
    visible: true,
    order: i,
    pinned: false,
    sortable: true,
    filterable: true,
    resizable: true,
  }));
}

export function useQueryState(
  columnDefs: { key: string; header: string; sortable?: boolean; filterable?: boolean; resizable?: boolean }[],
  options?: {
    defaultPageSize?: number;
    defaultSort?: SortConfig[];
    defaultFilters?: FilterConfig[];
    storageKey?: string;
  }
) {
  const [searchParams, setSearchParams] = useSearchParams();

  const defaults = useMemo(() => {
    const savedPageSize = typeof localStorage !== 'undefined' ? localStorage.getItem(PAGE_SIZE_KEY) : null;
    return {
      pageSize: savedPageSize ? parseInt(savedPageSize, 10) : (options?.defaultPageSize ?? 25),
      sort: options?.defaultSort ?? [],
      filters: options?.defaultFilters ?? [],
    };
  }, [options?.defaultPageSize, options?.defaultSort, options?.defaultFilters]);

  const [search, setSearchInternal] = useState(() => searchParams.get('q') ?? '');
  const [filters, setFiltersInternal] = useState<FilterConfig[]>(() =>
    deserializeFilters(searchParams.get('f'), defaults.filters)
  );
  const [sort, setSortInternal] = useState<SortConfig[]>(() =>
    searchParams.has('s') ? deserializeSort(searchParams.get('s')) : defaults.sort
  );
  const [page, setPageInternal] = useState(() => {
    const p = searchParams.get('p');
    return p ? Math.max(1, parseInt(p, 10)) : 1;
  });
  const [pageSize, setPageSizeInternal] = useState(() => {
    const ps = searchParams.get('ps');
    return ps ? parseInt(ps, 10) : defaults.pageSize;
  });
  const [columnConfig, setColumnConfigInternal] = useState<ColumnConfig[]>(() => {
    const columnDefaults = columnDefs.map((col, i) => ({
      key: col.key,
      header: col.header,
      visible: true,
      order: i,
      pinned: false as const,
      sortable: col.sortable ?? true,
      filterable: col.filterable ?? true,
      resizable: col.resizable ?? true,
    }));
    return searchParams.has('c') ? deserializeColumns(searchParams.get('c'), columnDefaults) : columnDefaults;
  });
  const [selectedRows, setSelectedRows] = useState<Set<string>>(new Set());
  const [viewId, setViewId] = useState<string | null>(null);

  const syncTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const syncToUrl = useCallback((state: QueryState) => {
    if (syncTimeoutRef.current) clearTimeout(syncTimeoutRef.current);
    syncTimeoutRef.current = setTimeout(() => {
      const params = queryStateToParams(state);
      setSearchParams(params, { replace: true });
    }, 150);
  }, [setSearchParams]);

  const queryState: QueryState = useMemo(
    () => ({ search, filters, sort, page, pageSize, columnConfig, selectedRows, viewId }),
    [search, filters, sort, page, pageSize, columnConfig, selectedRows, viewId]
  );

  useEffect(() => {
    syncToUrl(queryState);
  }, [search, filters, sort, page, pageSize, columnConfig, viewId]);

  const setSearch = useStableCallback((value: string) => {
    setSearchInternal(value);
    setPageInternal(1);
  });

  const setFilters = useStableCallback((updater: FilterConfig[] | ((prev: FilterConfig[]) => FilterConfig[])) => {
    setFiltersInternal(updater);
    setPageInternal(1);
  });

  const setSort = useStableCallback((updater: SortConfig[] | ((prev: SortConfig[]) => SortConfig[])) => {
    setSortInternal(updater);
    setPageInternal(1);
  });

  const setPage = useStableCallback((p: number) => setPageInternal(Math.max(1, p)));

  const setPageSize = useStableCallback((size: number) => {
    setPageSizeInternal(size);
    setPageInternal(1);
    localStorage.setItem(PAGE_SIZE_KEY, String(size));
  });

  const setColumnConfig = useStableCallback((updater: ColumnConfig[] | ((prev: ColumnConfig[]) => ColumnConfig[])) => {
    setColumnConfigInternal(updater);
  });

  const toggleRowSelection = useStableCallback((rowKey: string) => {
    setSelectedRows((prev) => {
      const next = new Set(prev);
      if (next.has(rowKey)) next.delete(rowKey);
      else next.add(rowKey);
      return next;
    });
  });

  const selectAllRows = useStableCallback((keys: string[]) => {
    setSelectedRows(new Set(keys));
  });

  const clearSelection = useStableCallback(() => {
    setSelectedRows(new Set());
  });

  const resetAll = useStableCallback(() => {
    setSearchInternal('');
    setFiltersInternal(options?.defaultFilters ?? []);
    setSortInternal(options?.defaultSort ?? []);
    setPageInternal(1);
    setPageSizeInternal(defaults.pageSize);
    setSelectedRows(new Set());
    setViewId(null);
    const resetColumnDefaults = columnDefs.map((col, i) => ({
      key: col.key,
      header: col.header,
      visible: true,
      order: i,
      pinned: false as const,
      sortable: col.sortable ?? true,
      filterable: col.filterable ?? true,
      resizable: col.resizable ?? true,
    }));
    setColumnConfigInternal(resetColumnDefaults);
  });

  const visibleColumns = useMemo(
    () => columnConfig.filter((c) => c.visible).sort((a, b) => a.order - b.order),
    [columnConfig]
  );

  const orderedColumns = useMemo(
    () => [...columnConfig].sort((a, b) => a.order - b.order),
    [columnConfig]
  );

  const paginatedData = useMemo(() => {
    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    return { start, end };
  }, [page, pageSize]);

  return {
    queryState,
    search,
    setSearch,
    filters,
    setFilters,
    sort,
    setSort,
    page,
    setPage,
    pageSize,
    setPageSize,
    columnConfig,
    setColumnConfig,
    visibleColumns,
    orderedColumns,
    selectedRows,
    toggleRowSelection,
    selectAllRows,
    clearSelection,
    viewId,
    setViewId,
    resetAll,
    paginatedData,
  };
}

export type QueryStateAPI = ReturnType<typeof useQueryState>;
