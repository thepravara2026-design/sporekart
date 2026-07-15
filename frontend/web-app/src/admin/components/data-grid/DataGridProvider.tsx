import React, { createContext, useContext, useMemo, useCallback, useState } from 'react';
import { useQueryState, QueryStateAPI } from '../../hooks/useQueryState';
import type { DataGridColumn, DataGridProps } from './types';

interface DataGridContextValue<T> extends QueryStateAPI {
  columns: DataGridColumn<T>[];
  rawData: T[];
  total: number;
  loading: boolean;
  emptyMessage: string;
  emptyDescription: string;
  selectable: boolean;
  sortable: boolean;
  filterable: boolean;
  searchable: boolean;
  exportable: boolean;
  resizableColumns: boolean;
  stickyHeader: boolean;
  stickyColumns: number;
  rowKeyFn: (row: T) => string;
  onRowClick?: (row: T) => void;
  cardViewBreakpoint: number;
  renderCard?: (row: T) => React.ReactNode;
  viewMode: 'table' | 'cards';
  setViewMode: (mode: 'table' | 'cards') => void;
}

const DataGridContext = createContext<DataGridContextValue<any> | null>(null);

export function useDataGrid<T = any>(): DataGridContextValue<T> {
  const ctx = useContext(DataGridContext);
  if (!ctx) throw new Error('useDataGrid must be used within a DataGridProvider');
  return ctx;
}

export function DataGridProvider<T extends Record<string, any>>({
  columns,
  data,
  total: totalProp,
  loading = false,
  emptyMessage = 'No data found',
  emptyDescription = 'There are no records to display.',
  pageSize: defaultPageSize,
  sortable = true,
  filterable = true,
  selectable = true,
  searchable = true,
  exportable = true,
  resizableColumns = true,
  stickyHeader = true,
  stickyColumns = 0,
  rowKey = 'id',
  onRowClick,
  cardViewBreakpoint = 768,
  renderCard,
  children,
}: DataGridProps<T>) {
  const columnDefs = useMemo(
    () =>
      columns.map((col) => ({
        key: col.key,
        header: col.header,
        sortable: col.sortable ?? sortable,
        filterable: col.filterable ?? filterable,
        resizable: col.resizable ?? resizableColumns,
      })),
    [columns, sortable, filterable, resizableColumns]
  );

  const queryState = useQueryState(columnDefs, { defaultPageSize });

  const total = totalProp ?? data.length;

  const rowKeyFn = useCallback(
    (row: T): string => {
      if (typeof rowKey === 'function') return rowKey(row);
      return String(row[rowKey as keyof T] ?? '');
    },
    [rowKey]
  );

  const [viewMode, setViewMode] = useState<'table' | 'cards'>('table');

  const value = useMemo<DataGridContextValue<T>>(
    () => ({
      ...queryState,
      columns,
      rawData: data,
      total,
      loading,
      emptyMessage,
      emptyDescription,
      selectable,
      sortable,
      filterable,
      searchable,
      exportable,
      resizableColumns,
      stickyHeader,
      stickyColumns,
      rowKeyFn,
      onRowClick: onRowClick ?? undefined,
      cardViewBreakpoint,
      renderCard,
      viewMode,
      setViewMode,
    }),
    [
      queryState,
      columns,
      data,
      total,
      loading,
      emptyMessage,
      emptyDescription,
      selectable,
      sortable,
      filterable,
      searchable,
      exportable,
      resizableColumns,
      stickyHeader,
      stickyColumns,
      rowKeyFn,
      onRowClick,
      cardViewBreakpoint,
      renderCard,
      viewMode,
    ]
  );

  return <DataGridContext.Provider value={value}>{children}</DataGridContext.Provider>;
}
