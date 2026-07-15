import { useCallback, useMemo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { DataGridProvider, useDataGrid } from './DataGridProvider';
import { SearchBar } from '../search/SearchBar';
import { FilterBar } from '../filters/FilterBar';
import { EnterpriseTable } from '../table/EnterpriseTable';
import { ColumnManager } from '../table/ColumnManager';
import { ExportButton } from '../export/ExportButton';
import { SavedViews } from '../saved-views/SavedViews';
import type { DataGridProps, SavedView } from './types';

function DataGridInner<T extends Record<string, any>>() {
  const {
    search,
    setSearch,
    searchable,
    exportable,
    resetAll,
    columnConfig,
    setColumnConfig,
    sort,
    setSort,
    page,
    setPage,
    pageSize,
    setPageSize,
    viewMode,
    setViewMode,
    renderCard,
  } = useDataGrid<T>();

  const handleSearchChange = useCallback(
    (value: string) => {
      setSearch(value);
    },
    [setSearch]
  );

  const handleSaveView = useCallback(
    (name: string) => {
      const view: SavedView = {
        id: `view-${Date.now()}`,
        name,
        queryState: {
          search,
          sort,
          page,
          pageSize,
          columnConfig,
        },
        recent: true,
      };
      try {
        const existing = JSON.parse(localStorage.getItem('dg_saved_views') ?? '[]');
        existing.push(view);
        localStorage.setItem('dg_saved_views', JSON.stringify(existing));
      } catch { /* ignore */ }
    },
    [search, sort, page, pageSize, columnConfig]
  );

  const handleLoadView = useCallback(
    (view: SavedView) => {
      if (view.queryState.search !== undefined) setSearch(view.queryState.search);
      if (view.queryState.sort) setSort(view.queryState.sort);
      if (view.queryState.page) setPage(view.queryState.page);
      if (view.queryState.pageSize) setPageSize(view.queryState.pageSize);
      if (view.queryState.columnConfig) setColumnConfig(view.queryState.columnConfig);
    },
    [setSearch, setSort, setPage, setPageSize, setColumnConfig]
  );

  const handleDeleteView = useCallback(
    (id: string) => {
      try {
        const existing = JSON.parse(localStorage.getItem('dg_saved_views') ?? '[]');
        localStorage.setItem('dg_saved_views', JSON.stringify(existing.filter((v: SavedView) => v.id !== id)));
      } catch { /* ignore */ }
    },
    []
  );

  const savedViews: SavedView[] = useMemo(() => {
    try {
      return JSON.parse(localStorage.getItem('dg_saved_views') ?? '[]');
    } catch {
      return [];
    }
  }, []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 12,
          flexWrap: 'wrap',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, flex: 1 }}>
          {searchable && (
            <SearchBar
              value={search}
              onChange={handleSearchChange}
              placeholder="Search records..."
              debounceMs={300}
            />
          )}
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          <ColumnManager />
          {exportable && <ExportButton />}
          <SavedViews
            views={savedViews}
            onSave={handleSaveView}
            onLoad={handleLoadView}
            onDelete={handleDeleteView}
          />
          {renderCard && (
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setViewMode(viewMode === 'table' ? 'cards' : 'table')}
              aria-label={viewMode === 'table' ? 'Switch to card view' : 'Switch to table view'}
            >
              <Icon name={viewMode === 'table' ? 'grid' : 'list'} size={14} />
            </Button>
          )}
          <Button variant="ghost" size="sm" onClick={resetAll} aria-label="Reset all filters">
            <Icon name="refresh-cw" size={14} />
          </Button>
        </div>
      </div>
      <FilterBar />
      <EnterpriseTable />
    </div>
  );
}

export function DataGrid<T extends Record<string, any>>(props: DataGridProps<T>) {
  return (
    <DataGridProvider {...props}>
      <DataGridInner />
    </DataGridProvider>
  );
}
