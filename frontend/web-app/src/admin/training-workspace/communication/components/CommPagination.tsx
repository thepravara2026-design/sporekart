import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { PAGE_SIZE_OPTIONS } from '../data/communicationOptions';

export interface CommPaginationProps {
  page: number;
  pageCount: number;
  pageSize: number;
  total: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
}

const btnStyle = (disabled: boolean): React.CSSProperties => ({
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 32,
  height: 32,
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)',
  color: disabled ? 'var(--color-text-disabled)' : 'var(--color-text-secondary)',
  cursor: disabled ? 'not-allowed' : 'pointer',
});

const CommPagination = memo(function CommPagination({
  page,
  pageCount,
  pageSize,
  total,
  onPageChange,
  onPageSizeChange,
}: CommPaginationProps) {
  const from = total === 0 ? 0 : (page - 1) * pageSize + 1;
  const to = Math.min(page * pageSize, total);

  return (
    <nav
      aria-label="Pagination"
      style={{
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 'var(--space-3)',
        padding: 'var(--space-3) 0',
      }}
    >
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Showing {from}–{to} of {total}
      </span>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)' }}>
        <label style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          Rows
          <select
            value={pageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            aria-label="Rows per page"
            style={{
              padding: '4px 8px',
              borderRadius: 'var(--radius-sm)',
              border: '1px solid var(--color-border-default)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)',
              fontSize: 'var(--text-body-sm)',
            }}
          >
            {PAGE_SIZE_OPTIONS.map((size) => (
              <option key={size} value={size}>{size}</option>
            ))}
          </select>
        </label>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-1)' }}>
          <button type="button" style={btnStyle(page <= 1)} disabled={page <= 1} onClick={() => onPageChange(page - 1)} aria-label="Previous page">
            <Icon name="chevron-left" size={16} />
          </button>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', minWidth: 68, textAlign: 'center' }}>
            {page} / {pageCount}
          </span>
          <button type="button" style={btnStyle(page >= pageCount)} disabled={page >= pageCount} onClick={() => onPageChange(page + 1)} aria-label="Next page">
            <Icon name="chevron-right" size={16} />
          </button>
        </div>
      </div>
    </nav>
  );
});

export default CommPagination;
