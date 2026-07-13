import React from 'react';
import { Skeleton } from './Skeleton';

export interface TableSkeletonProps {
  rows?: number;
  columns?: number;
  className?: string;
}

export const TableSkeleton: React.FC<TableSkeletonProps> = ({
  rows = 5,
  columns = 4,
  className = '',
}) => {
  const tableStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: `repeat(${columns}, 1fr)`,
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-card-padding)',
    backgroundColor: 'var(--color-bg-surface-default)',
    borderRadius: 'var(--radius-card)',
    border: '1px solid var(--color-border-default)',
  };

  const headerStyle: React.CSSProperties = {
    display: 'contents',
  };

  const cellStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    padding: 'var(--space-inline-sm) 0',
  };

  return (
    <div className={`sk-table-skeleton ${className}`.trim()} style={tableStyle} role="status" aria-label="Loading table">
      <div style={headerStyle}>
        {Array.from({ length: columns }).map((_, ci) => (
          <div key={`h-${ci}`} style={cellStyle}>
            <Skeleton variant="text" height="0.875em" width="80%" />
          </div>
        ))}
      </div>
      {Array.from({ length: rows }).map((_, ri) => (
        <div key={`r-${ri}`} style={headerStyle}>
          {Array.from({ length: columns }).map((_, ci) => (
            <div key={`c-${ri}-${ci}`} style={cellStyle}>
              <Skeleton variant="text" height="0.75em" width={ci === 0 ? '60%' : '100%'} />
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

TableSkeleton.displayName = 'TableSkeleton';
export default TableSkeleton;
