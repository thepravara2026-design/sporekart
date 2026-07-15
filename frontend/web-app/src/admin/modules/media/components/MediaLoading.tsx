import React from 'react';
import type { ViewMode } from '../types';

interface MediaLoadingProps {
  viewMode: ViewMode;
}

const skeletonStyle: React.CSSProperties = {
  borderRadius: 'var(--radius-card)',
  background: 'var(--color-bg-surface-raised)',
  animation: 'sk-pulse 1.5s ease-in-out infinite',
  aspectRatio: '1',
};

const rowSkeletonStyle: React.CSSProperties = {
  height: 56,
  borderRadius: 'var(--radius-sm)',
  background: 'var(--color-bg-surface-raised)',
  animation: 'sk-pulse 1.5s ease-in-out infinite',
};

export const MediaLoading = React.memo(function MediaLoading({ viewMode }: MediaLoadingProps) {
  return (
    <>
      <style>{`
        @keyframes sk-pulse {
          0%, 100% { opacity: 0.4; }
          50% { opacity: 0.8; }
        }
      `}</style>
      {viewMode === 'list' ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={i} style={rowSkeletonStyle} />
          ))}
        </div>
      ) : (
        <div
          style={{
            display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
            gap: 'var(--space-component-gap)',
          }}
        >
          {Array.from({ length: 12 }).map((_, i) => (
            <div key={i} style={skeletonStyle} />
          ))}
        </div>
      )}
    </>
  );
});

export default MediaLoading;
