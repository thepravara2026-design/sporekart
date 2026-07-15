import React from 'react';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { Card } from '../../../../design-system/components/composite/Card';

const FieldSkeleton: React.FC = () => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
    <Skeleton variant="text" width={140} height={14} />
    <Skeleton variant="rounded" width="100%" height={40} />
  </div>
);

export const WizardLoadingSkeleton: React.FC = () => {
  return (
    <div>
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-inline-sm)',
          padding: 'var(--space-stack-md) 0',
        }}
      >
        {[0, 1, 2, 3, 4, 5, 6].map((i) => (
          <React.Fragment key={i}>
            <Skeleton variant="circular" width={32} height={32} />
            {i < 6 && <Skeleton variant="text" width={48} height={12} />}
          </React.Fragment>
        ))}
      </div>

      <Card variant="outlined" padding="md">
        <Skeleton variant="text" width={200} height={20} />
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr',
            gap: 'var(--space-component-gap)',
            marginTop: 'var(--space-stack-md)',
          }}
        >
          <FieldSkeleton />
          <FieldSkeleton />
          <FieldSkeleton />
          <FieldSkeleton />
          <div style={{ gridColumn: '1 / -1' }}>
            <FieldSkeleton />
          </div>
        </div>
      </Card>
    </div>
  );
};

export default React.memo(WizardLoadingSkeleton);
