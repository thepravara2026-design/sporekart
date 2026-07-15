import React from 'react';

const pulseKeyframes = '@keyframes analyticsPulse { 0% { opacity: 0.6; } 50% { opacity: 1; } 100% { opacity: 0.6; } }';

const skeletonBase: React.CSSProperties = {
  background: 'var(--color-bg-surface-raised)',
  borderRadius: 'var(--radius-sm)',
  animation: 'analyticsPulse 1.5s ease-in-out infinite',
};

const SkeletonBar: React.FC<{ width?: string | number; height?: number }> = ({ width = '100%', height = 14 }) => (
  <div style={{ ...skeletonBase, width, height }} />
);

export const AnalyticsDashboardSkeleton: React.FC = React.memo(() => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="status" aria-label="Loading dashboard">
    <style>{pulseKeyframes}</style>
    <SkeletonBar width="220px" height={24} />
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
      {Array.from({ length: 12 }).map((_, i) => (
        <div key={i} style={{ padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', display: 'flex', flexDirection: 'column', gap: 10 }}>
          <SkeletonBar width={36} height={36} />
          <SkeletonBar width="60%" height={12} />
          <SkeletonBar width="40%" height={20} />
        </div>
      ))}
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
      <div style={{ padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', display: 'flex', flexDirection: 'column', gap: 12 }}>
        <SkeletonBar width="140px" height={18} />
        <SkeletonBar width="100%" height={200} />
      </div>
      <div style={{ padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', display: 'flex', flexDirection: 'column', gap: 12 }}>
        <SkeletonBar width="140px" height={18} />
        {Array.from({ length: 5 }).map((_, i) => (
          <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <SkeletonBar width={28} height={28} />
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <SkeletonBar width="70%" height={12} />
              <SkeletonBar width="40%" height={10} />
            </div>
          </div>
        ))}
      </div>
    </div>
  </div>
));

export const AnalyticsChartSkeleton: React.FC = React.memo(() => (
  <div style={{ padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', display: 'flex', flexDirection: 'column', gap: 12 }} role="status" aria-label="Loading chart">
    <style>{pulseKeyframes}</style>
    <SkeletonBar width="160px" height={18} />
    <SkeletonBar width="100%" height={220} />
  </div>
));

export const AnalyticsKpiSkeleton: React.FC<{ count?: number }> = React.memo(({ count = 4 }) => (
  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }} role="status" aria-label="Loading KPI cards">
    <style>{pulseKeyframes}</style>
    {Array.from({ length: count }).map((_, i) => (
      <div key={i} style={{ padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', display: 'flex', flexDirection: 'column', gap: 10 }}>
        <SkeletonBar width={36} height={36} />
        <SkeletonBar width="70%" height={12} />
        <SkeletonBar width="40%" height={20} />
      </div>
    ))}
  </div>
));

export default { AnalyticsDashboardSkeleton, AnalyticsChartSkeleton, AnalyticsKpiSkeleton };
