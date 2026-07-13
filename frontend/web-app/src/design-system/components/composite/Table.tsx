import React, { useCallback, useMemo } from 'react';

export interface TableColumn<T = any> {
  key: string;
  header: string;
  render?: (row: T, index: number) => React.ReactNode;
  sortable?: boolean;
  sortFn?: (a: T, b: T) => number;
  width?: string;
  minWidth?: string;
  align?: 'left' | 'center' | 'right';
  hidden?: boolean;
  cellStyle?: React.CSSProperties;
}

export interface TableProps<T = any> {
  columns: TableColumn<T>[];
  data: T[];
  rowKey?: string | ((row: T) => string);
  variant?: 'default' | 'striped' | 'bordered' | 'borderless';
  size?: 'compact' | 'comfortable';
  stickyHeader?: boolean;
  selectable?: boolean;
  selectedRows?: Set<string>;
  onSelectionChange?: (selected: Set<string>) => void;
  sortColumn?: string;
  sortDirection?: 'asc' | 'desc';
  onSort?: (column: string, direction: 'asc' | 'desc') => void;
  loading?: boolean;
  loadingRows?: number;
  empty?: boolean;
  emptyMessage?: string;
  error?: string;
  onRetry?: () => void;
  expandable?: boolean;
  expandedRows?: Set<string>;
  onToggleExpand?: (rowId: string) => void;
  renderExpanded?: (row: T) => React.ReactNode;
  pagination?: {
    page: number;
    pageSize: number;
    total: number;
    onPageChange: (page: number) => void;
    onPageSizeChange?: (size: number) => void;
  };
  className?: string;
  'aria-label'?: string;
  caption?: string;
}

function getRowKey<T>(row: T, rowKey?: string | ((row: T) => string)): string {
  if (typeof rowKey === 'function') return rowKey(row);
  if (typeof rowKey === 'string') return String((row as any)[rowKey]);
  return String((row as any).id ?? '');
}

function getPageNumbers(current: number, total: number): (number | 'ellipsis')[] {
  if (total <= 7) {
    return Array.from({ length: total }, (_, i) => i + 1);
  }
  const pages: (number | 'ellipsis')[] = [1];
  if (current > 3) pages.push('ellipsis');
  const start = Math.max(2, current - 1);
  const end = Math.min(total - 1, current + 1);
  for (let i = start; i <= end; i++) pages.push(i);
  if (current < total - 2) pages.push('ellipsis');
  pages.push(total);
  return pages;
}

const skeletonKeyframes = `
@keyframes sk-table-pulse {
  0% { background-color: var(--color-bg-skeleton-base); }
  50% { background-color: var(--color-bg-skeleton-highlight); }
  100% { background-color: var(--color-bg-skeleton-base); }
}
`;

function skeletonBarWidth(row: number, col: number): number {
  return ((row * 7 + col * 13) % 35) + 50;
}

const SortAscIcon: React.FC = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" aria-hidden="true">
    <path d="M6 2L10 8H2L6 2Z" fill="currentColor" />
  </svg>
);

const SortDescIcon: React.FC = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" aria-hidden="true">
    <path d="M6 10L2 4H10L6 10Z" fill="currentColor" />
  </svg>
);

const SortBothIcon: React.FC = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" aria-hidden="true">
    <path d="M6 2L10 8H2L6 2Z" fill="currentColor" opacity="0.25" />
    <path d="M6 10L2 4H10L6 10Z" fill="currentColor" opacity="0.25" />
  </svg>
);

const ChevronRightIcon: React.FC = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
    <path d="M6 4L10 8L6 12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const CheckboxCheckedIcon: React.FC = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
    <rect x="0.5" y="0.5" width="15" height="15" rx="2" fill="var(--color-primary)" stroke="var(--color-primary)" />
    <path d="M4 8L7 11L12 5" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const CheckboxUncheckedIcon: React.FC = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
    <rect x="0.5" y="0.5" width="15" height="15" rx="2" fill="transparent" stroke="var(--color-border-default)" />
  </svg>
);

const CheckboxIndeterminateIcon: React.FC = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
    <rect x="0.5" y="0.5" width="15" height="15" rx="2" fill="var(--color-primary)" stroke="var(--color-primary)" />
    <rect x="4" y="7" width="8" height="2" rx="1" fill="white" />
  </svg>
);

const pageSizeOptions = [10, 25, 50, 100];

function Table<T = any>(props: TableProps<T>) {
  const {
    columns,
    data,
    rowKey,
    variant = 'default',
    size = 'comfortable',
    stickyHeader = false,
    selectable = false,
    selectedRows,
    onSelectionChange,
    sortColumn,
    sortDirection,
    onSort,
    loading = false,
    loadingRows = 5,
    empty: emptyProp,
    emptyMessage = 'No data',
    error,
    onRetry,
    expandable = false,
    expandedRows,
    onToggleExpand,
    renderExpanded,
    pagination,
    className = '',
    'aria-label': ariaLabel = 'Table',
    caption,
  } = props;

  const visibleColumns = useMemo(
    () => columns.filter((col) => !col.hidden),
    [columns]
  );

  const columnCount = useMemo(
    () => visibleColumns.length + (selectable ? 1 : 0) + (expandable ? 1 : 0),
    [visibleColumns.length, selectable, expandable]
  );

  const rowHeight = size === 'compact'
    ? 'var(--table-row-height-compact)'
    : 'var(--table-row-height-comfortable)';

  const cellPaddingY = size === 'compact' ? 'var(--space-1)' : 'var(--space-3)';
  const cellPaddingX = size === 'compact' ? 'var(--space-inline-sm)' : 'var(--space-inline-md)';

  const handleSort = useCallback(
    (key: string) => {
      if (!onSort) return;
      if (sortColumn === key) {
        onSort(key, sortDirection === 'asc' ? 'desc' : 'asc');
      } else {
        onSort(key, 'asc');
      }
    },
    [onSort, sortColumn, sortDirection]
  );

  const handleSelectAll = useCallback(() => {
    if (!onSelectionChange) return;
    const allKeys = new Set(data.map((row) => getRowKey(row, rowKey)));
    if (selectedRows && allKeys.size > 0 && Array.from(allKeys).every((k) => selectedRows.has(k))) {
      onSelectionChange(new Set());
    } else {
      onSelectionChange(allKeys);
    }
  }, [onSelectionChange, data, rowKey, selectedRows]);

  const handleRowSelect = useCallback(
    (rowId: string) => {
      if (!onSelectionChange) return;
      const next = new Set(selectedRows || []);
      if (next.has(rowId)) {
        next.delete(rowId);
      } else {
        next.add(rowId);
      }
      onSelectionChange(next);
    },
    [onSelectionChange, selectedRows]
  );

  const totalPages = pagination ? Math.ceil(pagination.total / pagination.pageSize) : 0;
  const pageNumbers = useMemo(
    () => getPageNumbers(pagination?.page ?? 1, totalPages),
    [pagination?.page, totalPages]
  );

  const showEmpty = useMemo(
    () => emptyProp === true || (data.length === 0 && emptyProp !== false),
    [emptyProp, data.length]
  );

  const renderBody = () => {
    if (loading) {
      return renderSkeletonRows();
    }
    if (error) {
      return renderErrorRow();
    }
    if (showEmpty) {
      return renderEmptyRow();
    }
    return renderDataRows();
  };

  const renderSkeletonRows = () => {
    const rows = Array.from({ length: loadingRows });
    return (
      <>
        <style>{skeletonKeyframes}</style>
        {rows.map((_, rowIdx) => (
          <tr key={`sk-${rowIdx}`} style={skeletonRowStyle}>
            {selectable && <td style={getCellStyle(true)} />}
            {expandable && <td style={getCellStyle(true)} />}
            {visibleColumns.map((_, colIdx) => (
              <td key={colIdx} style={getCellStyle()}>
                <div style={{
                  height: '12px',
                  borderRadius: 'var(--radius-base)',
                  animation: 'sk-table-pulse 1.5s ease-in-out infinite',
                  backgroundColor: 'var(--color-bg-skeleton-base)',
                  width: `${skeletonBarWidth(rowIdx, colIdx)}%`,
                  maxWidth: '100%',
                }} />
              </td>
            ))}
          </tr>
        ))}
      </>
    );
  };

  const renderErrorRow = () => (
    <tr>
      <td colSpan={columnCount} style={errorCellStyle}>
        <div style={errorContentStyle}>
          <span style={errorTextStyle}>{error}</span>
          {onRetry && (
            <button
              type="button"
              onClick={onRetry}
              style={retryButtonStyle}
            >
              Retry
            </button>
          )}
        </div>
      </td>
    </tr>
  );

  const renderEmptyRow = () => (
    <tr>
      <td colSpan={columnCount} style={emptyCellStyle}>
        <span style={emptyTextStyle}>{emptyMessage}</span>
      </td>
    </tr>
  );

  const renderDataRows = () =>
    data.map((row, rowIndex) => {
      const rowId = getRowKey(row, rowKey);
      const isSelected = selectedRows?.has(rowId) ?? false;
      const isExpanded = expandedRows?.has(rowId) ?? false;

      return (
        <React.Fragment key={rowId}>
          <tr
            style={getRowStyle(isSelected, rowIndex)}
            aria-selected={selectable ? isSelected : undefined}
          >
            {selectable && (
              <td style={getCellStyle(true)}>
                <button
                  type="button"
                  onClick={() => handleRowSelect(rowId)}
                  style={checkboxButtonStyle}
                  aria-label={`Select row ${rowIndex + 1}`}
                  tabIndex={0}
                >
                  {isSelected ? <CheckboxCheckedIcon /> : <CheckboxUncheckedIcon />}
                </button>
              </td>
            )}
            {expandable && (
              <td style={getCellStyle(true)}>
                <button
                  type="button"
                  onClick={() => onToggleExpand?.(rowId)}
                  style={expandButtonStyle}
                  aria-expanded={isExpanded}
                  aria-label={isExpanded ? 'Collapse row' : 'Expand row'}
                  tabIndex={0}
                >
                  <span style={{
                    display: 'inline-flex',
                    transition: `transform var(--duration-fast) var(--easing-standard)`,
                    transform: isExpanded ? 'rotate(90deg)' : 'rotate(0deg)',
                  }}>
                    <ChevronRightIcon />
                  </span>
                </button>
              </td>
            )}
            {visibleColumns.map((col) => {
              const cellValue = col.render ? col.render(row, rowIndex) : (row as any)[col.key];
              return (
                <td
                  key={col.key}
                  style={getCellStyle(false, col)}
                >
                  {cellValue}
                </td>
              );
            })}
          </tr>
          {expandable && isExpanded && renderExpanded && (
            <tr key={`exp-${rowId}`}>
              <td colSpan={columnCount} style={expandedCellStyle}>
                <div style={{
                  overflow: 'hidden',
                  transition: `max-height var(--duration-normal) var(--easing-standard)`,
                }}>
                  {renderExpanded(row)}
                </div>
              </td>
            </tr>
          )}
        </React.Fragment>
      );
    });

  const getRowStyle = (isSelected: boolean, index: number): React.CSSProperties => ({
    background: isSelected
      ? 'var(--color-bg-primary-weak)'
      : variant === 'striped' && index % 2 === 1
        ? 'var(--color-bg-background)'
        : 'var(--color-bg-surface-default)',
    transition: `background var(--duration-fast) var(--easing-standard)`,
  });

  const getCellStyle = (isInteractive: boolean = false, col?: TableColumn): React.CSSProperties => {
    const base: React.CSSProperties = {
      padding: `${cellPaddingY} ${cellPaddingX}`,
      height: rowHeight,
      fontSize: 'var(--text-table)',
      color: 'var(--color-text-primary)',
      verticalAlign: 'middle',
      textAlign: col?.align || 'left',
      ...(col?.cellStyle || {}),
    };

    if (variant === 'bordered') {
      base.border = '1px solid var(--color-border-default)';
    } else if (variant === 'default') {
      base.borderBottom = '1px solid var(--color-border-default)';
    } else if (variant === 'borderless') {
      base.border = 'none';
    }

    if (col?.width) base.width = col.width;
    if (col?.minWidth) base.minWidth = col.minWidth;

    if (isInteractive) {
      base.width = '48px';
      base.minWidth = '48px';
      base.textAlign = 'center';
      base.padding = '0';
    }

    return base;
  };

  const skeletonRowStyle: React.CSSProperties = {
    background: 'var(--color-bg-surface-default)',
  };

  const errorCellStyle: React.CSSProperties = {
    padding: 'var(--space-6)',
    textAlign: 'center',
    background: 'var(--color-danger-50)',
    borderTop: '2px solid var(--color-danger-500)',
  };

  const errorContentStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: 'var(--space-3)',
  };

  const errorTextStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-danger)',
    fontWeight: 'var(--weight-medium)',
  };

  const retryButtonStyle: React.CSSProperties = {
    padding: 'var(--space-1) var(--space-4)',
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-on-primary)',
    background: 'var(--color-primary)',
    border: 'none',
    borderRadius: 'var(--radius-btn)',
    cursor: 'pointer',
    outline: 'none',
  };

  const emptyCellStyle: React.CSSProperties = {
    padding: 'var(--space-10) var(--space-4)',
    textAlign: 'center',
  };

  const emptyTextStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-secondary)',
  };

  const expandedCellStyle: React.CSSProperties = {
    padding: `${cellPaddingY} ${cellPaddingX}`,
    background: 'var(--color-bg-background)',
    borderBottom: '1px solid var(--color-border-default)',
  };

  const checkboxButtonStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: '48px',
    height: '48px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 0,
    outline: 'none',
  };

  const expandButtonStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: '48px',
    height: '48px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 0,
    outline: 'none',
    color: 'var(--color-text-secondary)',
  };

  const wrapperStyle: React.CSSProperties = {
    width: '100%',
    overflowX: 'auto',
    borderRadius: 'var(--radius-table)',
    border: variant === 'bordered' ? '1px solid var(--color-border-default)' : 'none',
  };

  const tableStyle: React.CSSProperties = {
    width: '100%',
    borderCollapse: 'collapse',
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-table)',
    borderRadius: 'var(--radius-table)',
  };

  const theadStyle: React.CSSProperties = {
    position: stickyHeader ? 'sticky' : undefined,
    top: stickyHeader ? 0 : undefined,
    zIndex: stickyHeader ? 2 : undefined,
    boxShadow: stickyHeader ? 'var(--shadow-1)' : undefined,
    background: 'var(--color-bg-surface-default)',
  };

  const thBaseStyle: React.CSSProperties = {
    padding: `${cellPaddingY} ${cellPaddingX}`,
    height: rowHeight,
    fontSize: 'var(--text-table-h)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-secondary)',
    textTransform: 'uppercase',
    letterSpacing: 'var(--tracking-wide)',
    whiteSpace: 'nowrap',
    verticalAlign: 'middle',
    background: 'var(--color-bg-surface-default)',
  };

  const getThStyle = (col?: TableColumn): React.CSSProperties => {
    const style: React.CSSProperties = { ...thBaseStyle };
    if (variant === 'bordered') {
      style.border = '1px solid var(--color-border-default)';
    } else if (variant === 'default') {
      style.borderBottom = '1px solid var(--color-border-default)';
    } else if (variant === 'borderless') {
      style.border = 'none';
    }
    style.textAlign = col?.align || 'left';
    if (col?.width) style.width = col.width;
    if (col?.minWidth) style.minWidth = col.minWidth;
    return style;
  };

  const renderPagination = () => {
    if (!pagination) return null;
    const { page, pageSize, total, onPageChange, onPageSizeChange } = pagination;
    const startItem = (page - 1) * pageSize + 1;
    const endItem = Math.min(page * pageSize, total);

    return (
      <div style={paginationWrapperStyle}>
        <div style={paginationInfoStyle}>
          {startItem}–{endItem} of {total}
        </div>
        <div style={paginationControlsStyle}>
          {onPageSizeChange && (
            <select
              value={pageSize}
              onChange={(e) => onPageSizeChange(Number(e.target.value))}
              style={pageSizeSelectStyle}
              aria-label="Rows per page"
            >
              {pageSizeOptions.map((opt) => (
                <option key={opt} value={opt}>{opt} / page</option>
              ))}
            </select>
          )}
          <div style={paginationButtonsStyle}>
            <button
              type="button"
              disabled={page <= 1}
              onClick={() => onPageChange(page - 1)}
              style={getPageButtonStyle(page <= 1)}
              aria-label="Previous page"
            >
              Previous
            </button>
            {pageNumbers.map((p, idx) =>
              p === 'ellipsis' ? (
                <span key={`e-${idx}`} style={pageEllipsisStyle}>…</span>
              ) : (
                <button
                  key={p}
                  type="button"
                  disabled={p === page}
                  onClick={() => onPageChange(p)}
                  style={getPageButtonStyle(false, p === page)}
                  aria-current={p === page ? 'page' : undefined}
                  aria-label={`Page ${p}`}
                >
                  {p}
                </button>
              )
            )}
            <button
              type="button"
              disabled={page >= totalPages}
              onClick={() => onPageChange(page + 1)}
              style={getPageButtonStyle(page >= totalPages)}
              aria-label="Next page"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    );
  };

  const paginationWrapperStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 'var(--space-3) var(--space-inline-md)',
    borderTop: '1px solid var(--color-border-default)',
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    flexWrap: 'wrap',
    gap: 'var(--space-2)',
  };

  const paginationInfoStyle: React.CSSProperties = {
    whiteSpace: 'nowrap',
  };

  const paginationControlsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-3)',
  };

  const paginationButtonsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-1)',
  };

  const pageSizeSelectStyle: React.CSSProperties = {
    padding: 'var(--space-1) var(--space-2)',
    fontSize: 'var(--text-body-sm)',
    border: '1px solid var(--color-border-default)',
    borderRadius: 'var(--radius-base)',
    background: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    outline: 'none',
    cursor: 'pointer',
  };

  const getPageButtonStyle = (disabled: boolean, isActive?: boolean): React.CSSProperties => ({
    padding: 'var(--space-1) var(--space-2)',
    minWidth: '32px',
    height: '32px',
    fontSize: 'var(--text-body-sm)',
    fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-normal)',
    color: isActive ? 'var(--color-text-on-primary)' : disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    background: isActive ? 'var(--color-primary)' : 'transparent',
    border: '1px solid',
    borderColor: isActive ? 'var(--color-primary)' : disabled ? 'var(--color-border-default)' : 'var(--color-border-default)',
    borderRadius: 'var(--radius-base)',
    cursor: disabled ? 'default' : 'pointer',
    outline: 'none',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
  });

  const pageEllipsisStyle: React.CSSProperties = {
    padding: 'var(--space-1) var(--space-1)',
    color: 'var(--color-text-disabled)',
    userSelect: 'none',
  };

  const getAriaSort = (col: TableColumn): 'ascending' | 'descending' | 'none' | undefined => {
    if (!col.sortable) return undefined;
    if (sortColumn !== col.key) return 'none';
    return sortDirection === 'asc' ? 'ascending' : 'descending';
  };

  return (
    <div
      style={wrapperStyle}
      className={`sk-table-wrapper ${className}`}
      role="region"
      aria-label={ariaLabel}
    >
      <table style={tableStyle} role="table">
        {caption && <caption style={captionStyle}>{caption}</caption>}
        <thead style={theadStyle}>
          <tr>
            {selectable && (
              <th scope="col" style={{ ...getThStyle(), width: '48px', minWidth: '48px', textAlign: 'center' }}>
                <button
                  type="button"
                  onClick={handleSelectAll}
                  style={checkboxButtonStyle}
                  aria-label={selectedRows && selectedRows.size > 0 ? 'Deselect all rows' : 'Select all rows'}
                  tabIndex={0}
                >
                  {selectedRows && selectedRows.size === data.length && data.length > 0
                    ? <CheckboxCheckedIcon />
                    : selectedRows && selectedRows.size > 0
                      ? <CheckboxIndeterminateIcon />
                      : <CheckboxUncheckedIcon />
                  }
                </button>
              </th>
            )}
            {expandable && (
              <th scope="col" style={{ ...getThStyle(), width: '48px', minWidth: '48px', textAlign: 'center' }} aria-hidden="true" />
            )}
            {visibleColumns.map((col) => {
              const isSorted = sortColumn === col.key;
              const sortIndicator = col.sortable ? (
                isSorted ? (
                  sortDirection === 'asc' ? <SortAscIcon /> : <SortDescIcon />
                ) : (
                  <SortBothIcon />
                )
              ) : null;

              return (
                <th
                  key={col.key}
                  scope="col"
                  role="columnheader"
                  aria-sort={getAriaSort(col)}
                  style={{
                    ...getThStyle(col),
                    cursor: col.sortable ? 'pointer' : undefined,
                    userSelect: col.sortable ? 'none' : undefined,
                  }}
                  onClick={col.sortable ? () => handleSort(col.key) : undefined}
                  onKeyDown={col.sortable ? (e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); handleSort(col.key); } } : undefined}
                  tabIndex={col.sortable ? 0 : undefined}
                >
                  <span style={headerContentStyle}>
                    {col.header}
                    {sortIndicator && <span style={sortIconStyle}>{sortIndicator}</span>}
                  </span>
                </th>
              );
            })}
          </tr>
        </thead>
        <tbody>
          {renderBody()}
        </tbody>
      </table>
      {renderPagination()}
    </div>
  );
};

const captionStyle: React.CSSProperties = {
  captionSide: 'top',
  textAlign: 'left',
  padding: 'var(--space-2) var(--space-inline-md)',
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-secondary)',
};

const headerContentStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
};

const sortIconStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  flexShrink: 0,
};

Table.displayName = 'Table';

export { Table };
export default Table;
