import React from 'react';
import { Skeleton } from './Skeleton';

export interface AvatarSkeletonProps {
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';
  className?: string;
}

const sizeMap: Record<string, number> = {
  xs: 24,
  sm: 32,
  md: 40,
  lg: 48,
  xl: 56,
  '2xl': 72,
};

export const AvatarSkeleton: React.FC<AvatarSkeletonProps> = ({
  size = 'md',
  className = '',
}) => {
  const dim = sizeMap[size];

  return (
    <span className={`sk-avatar-skeleton ${className}`.trim()} role="status" aria-label="Loading avatar">
      <Skeleton variant="circular" width={dim} height={dim} />
    </span>
  );
};

AvatarSkeleton.displayName = 'AvatarSkeleton';
export default AvatarSkeleton;
