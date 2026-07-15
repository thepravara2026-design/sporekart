import { useCallback, useRef, useState, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { useDataGrid } from '../data-grid/DataGridProvider';
import { ColumnHeader } from './ColumnHeader';
import { BulkActions } from './BulkActions';
import { CardView } from './CardView';
import { DataGridPagination } from './DataGridPagination';
import type { DataGridColumn } from '../data-grid/types';

export const EnterpriseTable = memo(function EnterpriseTable<T extends Record<string, any>>() {
  const {
    rawData,
    loading,
    emptyMessage,
    emptyDescription,
    selectable,
    sortable: gridSortable,
    stickyHeader,
    rowKeyFn,
    onRowClick,
    visibleColumns,
    sort,
    setSort,
    selectedRows,
    toggleRowSelection,
    selectAllRows,
    clearSelection,
    setColumnConfig,
    resizableColumns,
    viewMode,
    renderCard,
  } = useDataGrid<T>();

  const tableRef = useRef<HTMLDivElement>(null);
  const [contextMenu, setContextMenu] = useState<{ x: number; y: number; row: T } | null>(null);

  const allSelected = rawData.length > 0 && rawData.every((row) => selectedRows.has(rowKeyFn(row)));
  const someSelected = rawData.some((row) => selectedRows.has(rowKeyFn(row)));

  const handleSelectAll = useCallback(() => {
    if (allSelected) {
      clearSelection();
    } else {
      selectAllRows(rawData.map(rowKeyFn));
    }
  }, [allSelected, clearSelection, selectAllRows, rawData, rowKeyFn]);

  const handleSort = useCallback(
    (key: string) => {
      if (!gridSortable) return;
      setSort((prev) => {
        const existing = prev.find((s) => s.key === key);
        if (!existing) return [{ key, direction: 'asc' }];
        if (existing.direction === 'asc') return [{ key, direction: 'desc' }];
        return [];
      });
    },
    [gridSortable, setSort]
  );

  const handleColumnResize = useCallback(
    (key: string, width: number) => {
      if (!resizableColumns) return;
      setColumnConfig((prev) => prev.map((c) => (c.key === key ? { ...c, width: `${width}px` } : c)));
    },
    [resizableColumns, setColumnConfig]
  );

  if (viewMode === 'cards' && renderCard) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
        {selectable && <BulkActions />}
        <CardView data={rawData} renderCard={renderCard} rowKeyFn={rowKeyFn} />
        <DataGridPagination />
      </div>
    );
  }

  if (loading) {
    return (
      <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
        {selectable && <BulkActions />}
        <div style={{ overflowX: 'auto' }} ref={tableRef}>
        <table
          role="grid"
          style={{
            width: '100%',
            borderCollapse: 'collapse',
            fontSize: 'var(--text-body)',
          }}
        >
          <thead>
            <tr>
              {selectable && <th style={{ width: 40, padding: 12 }} />}
                {visibleColumns.map((col) => (
                  <th
                    key={col.key}
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      borderBottom: '2px solid var(--color-border)',
                      fontWeight: 600,
                      fontSize: 'var(--text-caption)',
                      color: 'var(--color-text-tertiary)',
                      textTransform: 'uppercase',
                      letterSpacing: '0.05em',
                      whiteSpace: 'nowrap',
                    }}
                  >
                    {col.header}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {Array.from({ length: 8 }).map((_, i) => (
                <tr key={i}>
                  {selectable && (
                    <td style={{ padding: 12 }}>
                      <div style={{ width: 16, height: 16, background: 'var(--color-surface-hover)', borderRadius: 4 }} />
                    </td>
                  )}
                  {visibleColumns.map((col) => (
                    <td key={col.key} style={{ padding: '10px 16px' }}>
                      <div
                        style={{
                          height: 14,
                          width: `${60 + (i * 7) % 30}%`,
                          background: 'var(--color-surface-hover)',
                          borderRadius: 4,
                          animation: 'skeletonPulse 1.5s ease-in-out infinite',
                        }}
                      />
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <DataGridPagination />
      </div>
    );
  }

  if (rawData.length === 0) {
    return (
      <div
        style={{
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-lg)',
          padding: '48px 24px',
          textAlign: 'center',
          color: 'var(--color-text-tertiary)',
        }}
      >
        <Icon name="inbox" size={48} />
        <h3 style={{ margin: '16px 0 8px', color: 'var(--color-text-secondary)' }}>{emptyMessage}</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body)' }}>{emptyDescription}</p>
      </div>
    );
  }

  return (
    <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
      {selectable && <BulkActions />}
      <div style={{ overflowX: 'auto' }} ref={tableRef}>
        <table
          role="grid"
          style={{
            width: '100%',
            borderCollapse: 'collapse',
            fontSize: 'var(--text-body)',
          }}
        >
          <thead>
            <tr>
              {selectable && (
                <th
                  style={{
                    width: 40,
                    padding: '12px',
                    borderBottom: '2px solid var(--color-border)',
                    ...(stickyHeader ? { position: 'sticky', top: 0, zIndex: 3, background: 'var(--color-surface)' } : {}),
                  }}
                >
                  <input
                    type="checkbox"
                    checked={allSelected}
                    ref={(el) => { if (el) el.indeterminate = someSelected && !allSelected; }}
                    onChange={handleSelectAll}
                    aria-label="Select all rows"
                    style={{ accentColor: 'var(--color-primary)', cursor: 'pointer' }}
                  />
                </th>
              )}
              {visibleColumns.map((col) => {
                const colDef = col as unknown as DataGridColumn<T>;
                return (
                  <ColumnHeader
                    key={col.key}
                    column={col}
                    onSort={colDef.sortable ? () => handleSort(col.key) : undefined}
                    sortDirection={sort.find((s) => s.key === col.key)?.direction ?? null}
                    onResize={colDef.resizable ? (w) => handleColumnResize(col.key, w) : undefined}
                    sticky={stickyHeader}
                  />
                );
              })}
            </tr>
          </thead>
          <tbody>
            {rawData.map((row, rowIndex) => {
              const key = rowKeyFn(row);
              const isSelected = selectedRows.has(key);
              return (
                <tr
                  key={key}
                  onClick={() => onRowClick?.(row)}
                  onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onRowClick) { e.preventDefault(); onRowClick(row); } }}
                  tabIndex={onRowClick ? 0 : undefined}
                  onContextMenu={(e) => {
                    e.preventDefault();
                    setContextMenu({ x: e.clientX, y: e.clientY, row });
                  }}
                  style={{
                    background: isSelected ? 'var(--color-primary-alpha)' : rowIndex % 2 === 0 ? 'var(--color-surface)' : 'var(--color-surface-hover)',
                    cursor: onRowClick ? 'pointer' : undefined,
                    transition: 'background 0.15s',
                  }}
                  onMouseEnter={(e) => {
                    if (!isSelected) e.currentTarget.style.background = 'var(--color-surface-hover)';
                  }}
                  onMouseLeave={(e) => {
                    if (!isSelected) e.currentTarget.style.background = rowIndex % 2 === 0 ? 'var(--color-surface)' : 'var(--color-surface-hover)';
                  }}
                >
                  {selectable && (
                    <td
                      style={{ padding: '10px 12px', borderBottom: '1px solid var(--color-border)' }}
                      onClick={(e) => e.stopPropagation()}
                    >
                      <input
                        type="checkbox"
                        checked={isSelected}
                        onChange={() => toggleRowSelection(key)}
                        aria-label={`Select row ${rowIndex + 1}`}
                        style={{ accentColor: 'var(--color-primary)', cursor: 'pointer' }}
                      />
                    </td>
                  )}
                  {visibleColumns.map((col) => {
                    const colDef = col as unknown as DataGridColumn<T>;
                    const cellContent = colDef.render ? colDef.render(row, rowIndex) : String(row[col.key] ?? '');
                    return (
                      <td
                        key={col.key}
                        style={{
                          padding: '10px 16px',
                          borderBottom: '1px solid var(--color-border)',
                          textAlign: colDef.align ?? 'left',
                          ...colDef.cellStyle,
                        }}
                      >
                        {cellContent}
                      </td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <DataGridPagination />
      {contextMenu && (
        <div
          role="menu"
          aria-label="Row context menu"
          style={{
            position: 'fixed',
            top: contextMenu.y,
            left: contextMenu.x,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            boxShadow: 'var(--elevation-lg)',
            zIndex: 1000,
            minWidth: 160,
            padding: 4,
          }}
          onClick={() => setContextMenu(null)}
          onMouseLeave={() => setContextMenu(null)}
        >
          <button role="menuitem" style={contextMenuBtnStyle}>Edit</button>
          <button role="menuitem" style={contextMenuBtnStyle}>Duplicate</button>
          <button role="menuitem" style={contextMenuBtnStyle}>View Details</button>
          <div style={{ height: 1, background: 'var(--color-border)', margin: '4px 0' }} />
          <button role="menuitem" style={{ ...contextMenuBtnStyle, color: 'var(--color-error)' }}>Delete</button>
        </div>
      )}
    </div>
  );
});

const contextMenuBtnStyle: React.CSSProperties = {
  display: 'block',
  width: '100%',
  padding: '8px 12px',
  border: 'none',
  background: 'transparent',
  cursor: 'pointer',
  textAlign: 'left',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body)',
  borderRadius: 'var(--radius-sm)',
};
