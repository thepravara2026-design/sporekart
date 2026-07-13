import React from 'react';
import { Skeleton } from './Skeleton';

export interface ProductGridSkeletonProps {
  count?: number;
  columns?: number;
  className?: string;
}

export const ProductGridSkeleton: React.FC<ProductGridSkeletonProps> = ({
  count = 8,
  columns = 4,
  className = '',
}) => {
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: `repeat(${columns}, 1fr)`,
    gap: 'var(--space-component-gap)',
  };

  const cardStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-card-padding)',
    borderRadius: 'var(--radius-card)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
  };

  return (
    <div className={`sk-product-grid-skeleton ${className}`.trim()} style={gridStyle} role="status" aria-label="Loading products">
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} style={cardStyle}>
          <Skeleton variant="rectangular" height={200} />
          <Skeleton variant="text" width="80%" />
          <Skeleton variant="text" width="40%" height="0.875em" />
        </div>
      ))}
    </div>
  );
};

ProductGridSkeleton.displayName = 'ProductGridSkeleton';
export default ProductGridSkeleton;
