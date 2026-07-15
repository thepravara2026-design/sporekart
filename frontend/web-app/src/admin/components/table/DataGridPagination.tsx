import React, { useCallback, useMemo, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { useDataGrid } from '../data-grid/DataGridProvider';

export const DataGridPagination = memo(function DataGridPagination() {
  const { page, setPage, pageSize, setPageSize, total, rawData } = useDataGrid();
  const totalRecords = total ?? rawData.length;
  const totalPages = Math.max(1, Math.ceil(totalRecords / pageSize));

  const startRecord = totalRecords === 0 ? 0 : (page - 1) * pageSize + 1;
  const endRecord = Math.min(page * pageSize, totalRecords);

  const pageNumbers = useMemo(() => {
    const pages: (number | 'ellipsis')[] = [];
    if (totalPages <= 7) {
      for (let i = 1; i <= totalPages; i++) pages.push(i);
    } else {
      pages.push(1);
      if (page > 3) pages.push('ellipsis');
      const start = Math.max(2, page - 1);
      const end = Math.min(totalPages - 1, page + 1);
      for (let i = start; i <= end; i++) pages.push(i);
      if (page < totalPages - 2) pages.push('ellipsis');
      pages.push(totalPages);
    }
    return pages;
  }, [page, totalPages]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'ArrowLeft' && page > 1) setPage(page - 1);
      if (e.key === 'ArrowRight' && page < totalPages) setPage(page + 1);
      if (e.key === 'Home') setPage(1);
      if (e.key === 'End') setPage(totalPages);
    },
    [page, totalPages, setPage]
  );

  if (totalRecords === 0) return null;

  return (
    <div
      onKeyDown={handleKeyDown}
      tabIndex={0}
      role="navigation"
      aria-label="Pagination"
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '12px 16px',
        borderTop: '1px solid var(--color-border)',
        background: 'var(--color-surface)',
        gap: 12,
        flexWrap: 'wrap',

      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
        <span>
          Showing {startRecord}&ndash;{endRecord} of {totalRecords} records
        </span>
        <select
          value={pageSize}
          onChange={(e) => setPageSize(Number(e.target.value))}
          aria-label="Records per page"
          style={{
            padding: '4px 8px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            cursor: 'pointer',
          }}
        >
          {[10, 20, 50, 100].map((size) => (
            <option key={size} value={size}>
              {size} / page
            </option>
          ))}
        </select>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <PageButton onClick={() => setPage(1)} disabled={page === 1} ariaLabel="First page">
          <Icon name="chevrons-left" size={14} />
        </PageButton>
        <PageButton onClick={() => setPage(page - 1)} disabled={page === 1} ariaLabel="Previous page">
          <Icon name="chevron-left" size={14} />
        </PageButton>
        {pageNumbers.map((p, i) =>
          p === 'ellipsis' ? (
            <span
              key={`ellipsis-${i}`}
              style={{
                padding: '4px 8px',
                color: 'var(--color-text-tertiary)',
                fontSize: 'var(--text-body)',
              }}
            >
              &hellip;
            </span>
          ) : (
            <button
              key={p}
              onClick={() => setPage(p)}
              aria-label={`Page ${p}`}
              aria-current={p === page ? 'page' : undefined}
              style={{
                minWidth: 32,
                height: 32,
                padding: '4px 8px',
                border: p === page ? '1px solid var(--color-primary)' : '1px solid transparent',
                borderRadius: 'var(--radius-md)',
                background: p === page ? 'var(--color-primary-alpha)' : 'transparent',
                color: p === page ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontWeight: p === page ? 600 : 400,
                fontSize: 'var(--text-body)',
                cursor: 'pointer',
              }}
            >
              {p}
            </button>
          )
        )}
        <PageButton onClick={() => setPage(page + 1)} disabled={page === totalPages} ariaLabel="Next page">
          <Icon name="chevron-right" size={14} />
        </PageButton>
        <PageButton onClick={() => setPage(totalPages)} disabled={page === totalPages} ariaLabel="Last page">
          <Icon name="chevrons-right" size={14} />
        </PageButton>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Go to</span>
        <input
          type="number"
          min={1}
          max={totalPages}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              const val = parseInt((e.target as HTMLInputElement).value, 10);
              if (val >= 1 && val <= totalPages) setPage(val);
            }
          }}
          aria-label="Go to page"
          style={{
            width: 56,
            padding: '4px 8px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            textAlign: 'center',
          }}
        />
      </div>
    </div>
  );
});

function PageButton({
  onClick,
  disabled,
  ariaLabel,
  children,
}: {
  onClick: () => void;
  disabled: boolean;
  ariaLabel: string;
  children: React.ReactNode;
}) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      aria-label={ariaLabel}
      style={{
        minWidth: 32,
        height: 32,
        padding: '4px 8px',
        border: '1px solid transparent',
        borderRadius: 'var(--radius-md)',
        background: 'transparent',
        color: disabled ? 'var(--color-text-disabled)' : 'var(--color-text-secondary)',
        cursor: disabled ? 'not-allowed' : 'pointer',
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        opacity: disabled ? 0.4 : 1,
      }}
    >
      {children}
    </button>
  );
}
