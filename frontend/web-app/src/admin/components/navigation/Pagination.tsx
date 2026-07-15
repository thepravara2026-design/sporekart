import React, { useCallback, useMemo } from 'react';

export interface PaginationProps {
  page: number;
  pageSize: number;
  total: number;
  onPageChange: (page: number) => void;
  onPageSizeChange?: (pageSize: number) => void;
  pageSizeOptions?: number[];
  siblingCount?: number;
  className?: string;
  style?: React.CSSProperties;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  gap: 'var(--space-inline-md)',
  flexWrap: 'wrap',
  padding: 'var(--space-stack-sm) 0',
};

const infoStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

const btnGroup: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 2,
};

const pageBtn: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  minWidth: 32,
  height: 32,
  padding: '0 6px',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-sm)',
  background: 'transparent',
  color: 'var(--color-text-primary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-caption)',
  cursor: 'pointer',
  transition: 'background var(--duration-fast) var(--easing-standard), border-color var(--duration-fast) var(--easing-standard)',
};

const activeBtn: React.CSSProperties = {
  ...pageBtn,
  background: 'var(--color-primary)',
  borderColor: 'var(--color-primary)',
  color: 'var(--color-text-on-primary)',
  fontWeight: 'var(--weight-bold)',
};

const ellipsisStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  minWidth: 32,
  height: 32,
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  userSelect: 'none',
};

const selectStyle: React.CSSProperties = {
  padding: '4px 8px',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-background)',
  color: 'var(--color-text-primary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-caption)',
  cursor: 'pointer',
  outline: 'none',
};

function range(start: number, end: number): number[] {
  const length = end - start + 1;
  return Array.from({ length }, (_, i) => start + i);
}

export const Pagination: React.FC<PaginationProps> = ({
  page,
  pageSize,
  total,
  onPageChange,
  onPageSizeChange,
  pageSizeOptions = [10, 20, 50, 100],
  siblingCount = 1,
  className = '',
  style,
}) => {
  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const startItem = total === 0 ? 0 : (page - 1) * pageSize + 1;
  const endItem = Math.min(page * pageSize, total);

  const pageNumbers = useMemo(() => {
    const totalPageNumbers = siblingCount * 2 + 5;
    if (totalPages <= totalPageNumbers) return range(1, totalPages);
    const leftSiblingIndex = Math.max(page - siblingCount, 1);
    const rightSiblingIndex = Math.min(page + siblingCount, totalPages);
    const showLeftEllipsis = leftSiblingIndex > 2;
    const showRightEllipsis = rightSiblingIndex < totalPages - 1;
    if (!showLeftEllipsis && showRightEllipsis) {
      const leftCount = 3 + 2 * siblingCount;
      return [...range(1, leftCount), -1, totalPages];
    }
    if (showLeftEllipsis && !showRightEllipsis) {
      const rightCount = 3 + 2 * siblingCount;
      return [1, -1, ...range(totalPages - rightCount + 1, totalPages)];
    }
    return [1, -1, ...range(leftSiblingIndex, rightSiblingIndex), -1, totalPages];
  }, [page, totalPages, siblingCount]);

  const handlePrev = useCallback(() => {
    if (page > 1) onPageChange(page - 1);
  }, [page, onPageChange]);

  const handleNext = useCallback(() => {
    if (page < totalPages) onPageChange(page + 1);
  }, [page, totalPages, onPageChange]);

  if (totalPages <= 1) return null;

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      <div style={infoStyle}>
        {startItem}&ndash;{endItem} of {total}
      </div>
      <div style={btnGroup}>
        <button
          type="button"
          style={pageBtn}
          onClick={handlePrev}
          disabled={page <= 1}
          aria-label="Previous page"
        >
          &#9664;
        </button>
        {pageNumbers.map((p, idx) => {
          if (p === -1) {
            return <span key={`ellipsis-${idx}`} style={ellipsisStyle}>&hellip;</span>;
          }
          return (
            <button
              key={p}
              type="button"
              style={p === page ? activeBtn : pageBtn}
              onClick={() => onPageChange(p)}
              aria-current={p === page ? 'page' : undefined}
              aria-label={`Page ${p}`}
            >
              {p}
            </button>
          );
        })}
        <button
          type="button"
          style={pageBtn}
          onClick={handleNext}
          disabled={page >= totalPages}
          aria-label="Next page"
        >
          &#9654;
        </button>
      </div>
      {onPageSizeChange && (
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Show</span>
          <select
            style={selectStyle}
            value={pageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            aria-label="Page size"
          >
            {pageSizeOptions.map((opt) => (
              <option key={opt} value={opt}>{opt}</option>
            ))}
          </select>
        </div>
      )}
    </div>
  );
};

Pagination.displayName = 'Pagination';
export default Pagination;
