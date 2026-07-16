import { memo, useCallback } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

interface StudentPaginationProps {
  page: number;
  totalPages: number;
  totalFiltered: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
  pageSizeOptions: number[];
}

export const StudentPagination = memo(function StudentPagination({
  page,
  totalPages,
  totalFiltered,
  pageSize,
  onPageChange,
  onPageSizeChange,
  pageSizeOptions,
}: StudentPaginationProps) {
  const from = (page - 1) * pageSize + 1;
  const to = Math.min(page * pageSize, totalFiltered);

  const handlePrev = useCallback(() => {
    if (page > 1) onPageChange(page - 1);
  }, [page, onPageChange]);

  const handleNext = useCallback(() => {
    if (page < totalPages) onPageChange(page + 1);
  }, [page, totalPages, onPageChange]);

  const canPrev = page > 1;
  const canNext = page < totalPages;

  return (
    <nav
      aria-label="Pagination"
      style={{
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        padding: '8px 0', flexWrap: 'wrap', gap: 8,
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {from}–{to} of {totalFiltered}
        </span>
        <select
          value={pageSize}
          onChange={(e) => onPageSizeChange(Number(e.target.value))}
          aria-label="Page size"
          style={{
            fontSize: 'var(--text-caption)', padding: '2px 4px',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: 'var(--color-bg-surface-default)',
            color: 'var(--color-text-secondary)',
          }}
        >
          {pageSizeOptions.map((opt) => (
            <option key={opt} value={opt}>{opt} / page</option>
          ))}
        </select>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
        <button
          type="button"
          onClick={() => onPageChange(1)}
          disabled={!canPrev}
          aria-label="First page"
          style={{
            padding: '4px 8px', border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: canPrev ? 'var(--color-bg-surface-default)' : 'var(--color-bg-disabled)',
            cursor: canPrev ? 'pointer' : 'default',
            opacity: canPrev ? 1 : 0.5,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-caption)',
          }}
        >
          <Icon name="skip-back" size={12} color="currentColor" />
        </button>
        <button
          type="button"
          onClick={handlePrev}
          disabled={!canPrev}
          aria-label="Previous page"
          style={{
            padding: '4px 8px', border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: canPrev ? 'var(--color-bg-surface-default)' : 'var(--color-bg-disabled)',
            cursor: canPrev ? 'pointer' : 'default',
            opacity: canPrev ? 1 : 0.5,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-caption)',
          }}
        >
          <Icon name="chevron-left" size={12} color="currentColor" />
        </button>

        <span style={{
          fontSize: 'var(--text-caption)', padding: '4px 8px',
          color: 'var(--color-text-secondary)',
          fontWeight: 'var(--weight-medium)',
        }}>
          Page {page} of {totalPages}
        </span>

        <button
          type="button"
          onClick={handleNext}
          disabled={!canNext}
          aria-label="Next page"
          style={{
            padding: '4px 8px', border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: canNext ? 'var(--color-bg-surface-default)' : 'var(--color-bg-disabled)',
            cursor: canNext ? 'pointer' : 'default',
            opacity: canNext ? 1 : 0.5,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-caption)',
          }}
        >
          <Icon name="chevron-right" size={12} color="currentColor" />
        </button>
        <button
          type="button"
          onClick={() => onPageChange(totalPages)}
          disabled={!canNext}
          aria-label="Last page"
          style={{
            padding: '4px 8px', border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            background: canNext ? 'var(--color-bg-surface-default)' : 'var(--color-bg-disabled)',
            cursor: canNext ? 'pointer' : 'default',
            opacity: canNext ? 1 : 0.5,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-caption)',
          }}
        >
          <Icon name="skip-forward" size={12} color="currentColor" />
        </button>
      </div>
    </nav>
  );
});
