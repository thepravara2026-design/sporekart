import React from 'react';
import { Skeleton } from './Skeleton';

export interface FormSkeletonProps {
  fields?: number;
  className?: string;
}

export const FormSkeleton: React.FC<FormSkeletonProps> = ({
  fields = 4,
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-section-gap)',
  };

  const fieldStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-inline-sm)',
  };

  return (
    <div className={`sk-form-skeleton ${className}`.trim()} style={containerStyle} role="status" aria-label="Loading form">
      {Array.from({ length: fields }).map((_, i) => (
        <div key={i} style={fieldStyle}>
          <Skeleton variant="text" width="30%" height="0.75em" />
          <Skeleton variant="rounded" height={40} />
        </div>
      ))}
    </div>
  );
};

FormSkeleton.displayName = 'FormSkeleton';
export default FormSkeleton;
