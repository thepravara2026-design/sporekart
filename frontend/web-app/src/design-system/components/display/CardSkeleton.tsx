import React from 'react';
import { Skeleton } from './Skeleton';

export interface CardSkeletonProps {
  lines?: number;
  hasImage?: boolean;
  className?: string;
}

export const CardSkeleton: React.FC<CardSkeletonProps> = ({
  lines = 3,
  hasImage = false,
  className = '',
}) => {
  const cardStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
    padding: 'var(--space-card-padding)',
    borderRadius: 'var(--radius-card)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
  };

  return (
    <div className={`sk-card-skeleton ${className}`.trim()} style={cardStyle}>
      {hasImage && <Skeleton variant="rectangular" height={200} />}
      {Array.from({ length: lines }).map((_, i) => (
        <Skeleton key={i} variant="text" width={i === lines - 1 ? '60%' : '100%'} />
      ))}
    </div>
  );
};

CardSkeleton.displayName = 'CardSkeleton';
export default CardSkeleton;
