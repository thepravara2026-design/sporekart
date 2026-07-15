import React, { memo } from 'react';
import { Pagination } from '../../../components/navigation/Pagination';

interface PaginationBarProps {
  page: number;
  pageSize: number;
  total: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
}

export const PaginationBar: React.FC<PaginationBarProps> = memo(function PaginationBar({
  page,
  pageSize,
  total,
  onPageChange,
  onPageSizeChange,
}) {
  const totalPages = Math.max(1, Math.ceil(total / pageSize));
  const start = total === 0 ? 0 : (page - 1) * pageSize + 1;
  const end = Math.min(page * pageSize, total);

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 'var(--space-component-gap)',
        flexWrap: 'wrap',
        marginTop: 'var(--space-section-gap)',
        paddingTop: 'var(--space-component-gap)',
        borderTop: '1px solid var(--color-border)',
      }}
    >
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
        Showing {start}&ndash;{end} of {total} · Page {page} of {totalPages}
      </span>
      <div style={{ flex: 1, minWidth: 220, display: 'flex', justifyContent: 'flex-end' }}>
        <Pagination
          page={page}
          pageSize={pageSize}
          total={total}
          onPageChange={onPageChange}
          onPageSizeChange={onPageSizeChange}
          pageSizeOptions={[5, 10, 20, 50, 100]}
        />
      </div>
    </div>
  );
});

export default PaginationBar;
