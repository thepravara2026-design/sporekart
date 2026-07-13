import React from 'react';
import { Skeleton } from './Skeleton';

export interface ListSkeletonProps {
  count?: number;
  hasIcon?: boolean;
  hasDescription?: boolean;
  className?: string;
}

export const ListSkeleton: React.FC<ListSkeletonProps> = ({
  count = 5,
  hasIcon = false,
  hasDescription = false,
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
  };

  const itemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-sm) 0',
  };

  const textBlockStyle: React.CSSProperties = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-inline-xs)',
  };

  return (
    <div className={`sk-list-skeleton ${className}`.trim()} style={containerStyle} role="status" aria-label="Loading list">
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} style={itemStyle}>
          {hasIcon && <Skeleton variant="circular" width={40} height={40} />}
          <div style={textBlockStyle}>
            <Skeleton variant="text" width={hasDescription ? '80%' : '60%'} />
            {hasDescription && <Skeleton variant="text" width="50%" height="0.75em" />}
          </div>
        </div>
      ))}
    </div>
  );
};

ListSkeleton.displayName = 'ListSkeleton';
export default ListSkeleton;
