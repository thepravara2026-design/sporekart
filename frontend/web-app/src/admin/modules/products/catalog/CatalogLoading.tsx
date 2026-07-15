import React, { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import type { CatalogViewMode } from './types';

interface CatalogLoadingProps {
  viewMode: CatalogViewMode;
  rows?: number;
}

const SkeletonTable = memo(function SkeletonTable({ rows }: { rows: number }) {
  return (
    <Card variant="outlined" padding="none">
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-component-gap)',
            padding: '12px 16px',
            borderBottom: '1px solid var(--color-border)',
            background: 'var(--color-bg-surface-raised)',
          }}
        >
          <Skeleton variant="rounded" width={20} height={20} />
          <Skeleton variant="text" width={160} height={14} />
          <div style={{ flex: 1 }} />
          <Skeleton variant="text" width={80} height={14} />
          <Skeleton variant="text" width={80} height={14} />
        </div>
        {Array.from({ length: rows }).map((_, i) => (
          <div
            key={i}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-component-gap)',
              padding: '12px 16px',
              borderBottom: '1px solid var(--color-border)',
            }}
          >
            <Skeleton variant="rounded" width={20} height={20} />
            <Skeleton variant="rounded" width={40} height={40} />
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6, flex: 1 }}>
              <Skeleton variant="text" width="60%" height={12} />
              <Skeleton variant="text" width="35%" height={10} />
            </div>
            <Skeleton variant="rounded" width={70} height={20} />
            <Skeleton variant="rounded" width={70} height={20} />
            <Skeleton variant="text" width={60} height={12} />
          </div>
        ))}
      </div>
    </Card>
  );
});

const SkeletonCards = memo(function SkeletonCards({ rows }: { rows: number }) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}
    >
      {Array.from({ length: rows }).map((_, i) => (
        <Card key={i} variant="outlined" padding="sm">
          <Skeleton variant="rounded" width="100%" height={140} />
          <div style={{ marginTop: 10, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <Skeleton variant="rounded" width={72} height={18} />
            <Skeleton variant="text" width="80%" height={14} />
            <Skeleton variant="text" width="40%" height={12} />
            <Skeleton variant="text" width="30%" height={14} />
          </div>
        </Card>
      ))}
    </div>
  );
});

export const CatalogLoading: React.FC<CatalogLoadingProps> = memo(function CatalogLoading({ viewMode, rows = 8 }) {
  if (viewMode === 'table' || viewMode === 'compact') {
    return <SkeletonTable rows={rows} />;
  }
  return <SkeletonCards rows={rows} />;
});

export default CatalogLoading;
