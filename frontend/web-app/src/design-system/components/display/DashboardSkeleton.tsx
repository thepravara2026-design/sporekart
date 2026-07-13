import React from 'react';
import { Skeleton } from './Skeleton';
import { CardSkeleton } from './CardSkeleton';

export interface DashboardSkeletonProps {
  cards?: number;
  className?: string;
}

export const DashboardSkeleton: React.FC<DashboardSkeletonProps> = ({
  cards = 6,
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-section-gap)',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-component-gap)',
    alignItems: 'center',
  };

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(3, 1fr)',
    gap: 'var(--space-component-gap)',
  };

  return (
    <div className={`sk-dashboard-skeleton ${className}`.trim()} style={containerStyle} role="status" aria-label="Loading dashboard">
      <div style={headerStyle}>
        <Skeleton variant="text" width="240px" height="1.5em" />
        <Skeleton variant="text" width="120px" height="1em" />
      </div>
      <div style={gridStyle}>
        {Array.from({ length: cards }).map((_, i) => (
          <CardSkeleton key={i} lines={3} hasImage />
        ))}
      </div>
    </div>
  );
};

DashboardSkeleton.displayName = 'DashboardSkeleton';
export default DashboardSkeleton;
