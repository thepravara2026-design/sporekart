import { memo } from 'react';

interface EnrollmentPaginationProps {
  page: number;
  totalPages: number;
  totalFiltered: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
  pageSizeOptions?: number[];
}

export const EnrollmentPagination = memo(function EnrollmentPagination({
  page, totalPages, totalFiltered, pageSize, onPageChange, onPageSizeChange,
  pageSizeOptions = [10, 20, 50],
}: EnrollmentPaginationProps) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 8, padding: '8px 0' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{totalFiltered} total</span>
        <select
          value={pageSize}
          onChange={(e) => onPageSizeChange(Number(e.target.value))}
          aria-label="Results per page"
          style={{
            padding: '2px 4px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-caption)',
          }}
        >
          {pageSizeOptions.map((s) => (
            <option key={s} value={s}>{s} / page</option>
          ))}
        </select>
      </div>
      <div style={{ display: 'flex', gap: 4 }} role="navigation" aria-label="Pagination">
        <button
          onClick={() => onPageChange(page - 1)}
          disabled={page <= 1}
          aria-label="Previous page"
          style={{
            padding: '4px 10px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)', cursor: page <= 1 ? 'default' : 'pointer',
            opacity: page <= 1 ? 0.5 : 1, fontSize: 'var(--text-caption)',
          }}
        >
          Prev
        </button>
        {Array.from({ length: Math.min(totalPages, 5) }, (_, i) => {
          const start = Math.max(1, Math.min(page - 2, totalPages - 4));
          const p = start + i;
          if (p > totalPages) return null;
          return (
            <button
              key={p}
              onClick={() => onPageChange(p)}
              aria-label={`Page ${p}`}
              aria-current={p === page ? 'page' : undefined}
              style={{
                padding: '4px 10px', borderRadius: 'var(--radius-sm)',
                border: `1px solid ${p === page ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
                background: p === page ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                color: p === page ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                cursor: 'pointer', fontWeight: p === page ? 'var(--weight-semibold)' : 'var(--weight-normal)',
                fontSize: 'var(--text-caption)',
              }}
            >
              {p}
            </button>
          );
        })}
        <button
          onClick={() => onPageChange(page + 1)}
          disabled={page >= totalPages}
          aria-label="Next page"
          style={{
            padding: '4px 10px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)', cursor: page >= totalPages ? 'default' : 'pointer',
            opacity: page >= totalPages ? 0.5 : 1, fontSize: 'var(--text-caption)',
          }}
        >
          Next
        </button>
      </div>
    </div>
  );
});
