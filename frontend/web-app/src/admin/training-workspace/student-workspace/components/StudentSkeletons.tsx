import { memo } from 'react';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { Card } from '../../../../design-system/components/composite/Card';

export const DashboardSkeleton = memo(function DashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {Array.from({ length: 6 }).map((_, i) => (
          <Card key={i} padding="md">
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              <Skeleton variant="circular" width={32} height={32} />
              <Skeleton variant="text" width="60%" height={14} />
              <Skeleton variant="text" width="40%" height={24} />
            </div>
          </Card>
        ))}
      </div>
      <Card padding="md">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <Skeleton variant="text" width="30%" height={20} />
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 16 }}>
            {Array.from({ length: 6 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
                <Skeleton variant="text" width="80%" height={12} />
                <Skeleton variant="rounded" width="100%" height={16} />
              </div>
            ))}
          </div>
        </div>
      </Card>
    </div>
  );
});

export const RegistrySkeleton = memo(function RegistrySkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
        <Skeleton variant="rounded" width={240} height={36} />
        <Skeleton variant="rounded" width={100} height={36} />
        <Skeleton variant="rounded" width={100} height={36} />
        <div style={{ flex: 1 }} />
        <Skeleton variant="rounded" width={32} height={32} />
      </div>
      <div style={{ display: 'flex', gap: 8 }}>
        <Skeleton variant="rounded" width={120} height={28} />
        <Skeleton variant="rounded" width={120} height={28} />
        <Skeleton variant="rounded" width={120} height={28} />
        <Skeleton variant="rounded" width={120} height={28} />
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        {Array.from({ length: 5 }).map((_, i) => (
          <div key={i} style={{ display: 'flex', gap: 12, alignItems: 'center', padding: '8px 0' }}>
            <Skeleton variant="circular" width={32} height={32} />
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <Skeleton variant="text" width="30%" height={14} />
              <Skeleton variant="text" width="50%" height={12} />
            </div>
            <Skeleton variant="rounded" width={80} height={22} />
            <Skeleton variant="text" width={120} height={12} />
          </div>
        ))}
      </div>
    </div>
  );
});

export const WidgetSkeleton = memo(function WidgetSkeleton({ count = 3 }: { count?: number }) {
  return (
    <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
      {Array.from({ length: count }).map((_, i) => (
        <Card key={i} padding="md">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, minWidth: 180 }}>
            <Skeleton variant="text" width="60%" height={12} />
            <Skeleton variant="text" width="40%" height={28} />
            <Skeleton variant="rounded" width="100%" height={4} />
          </div>
        </Card>
      ))}
    </div>
  );
});
