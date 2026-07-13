import React from 'react';

export type ChartSkeletonType = 'bar' | 'line' | 'pie' | 'radial';

export interface ChartSkeletonProps {
  type?: ChartSkeletonType;
  width?: string | number;
  height?: string | number;
  className?: string;
  style?: React.CSSProperties;
}

const shimmerStyle: React.CSSProperties = {
  background: 'linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 75%)',
  backgroundSize: '200% 100%',
  animation: 'sk-shimmer 1.5s ease-in-out infinite',
  borderRadius: 'var(--radius-xs)',
};

const barSkeleton = (count: number): React.ReactNode[] =>
  Array.from({ length: count }, (_, i) => (
    <div
      key={i}
      style={{
        ...shimmerStyle,
        width: `${60 + Math.random() * 40}%`,
        height: `${30 + Math.random() * 60}%`,
        alignSelf: 'flex-end',
      }}
    />
  ));

const lineSkeleton = (): React.ReactNode => (
  <svg width="100%" height="100%" viewBox="0 0 300 150" preserveAspectRatio="none" style={{ position: 'absolute', inset: 0 }}>
    <defs>
      <linearGradient id="sk-line-shimmer" x1="0" x2="1" y1="0" y2="0">
        <stop offset="0%" stopColor="var(--color-bg-skeleton-base)" />
        <stop offset="50%" stopColor="var(--color-bg-skeleton-highlight)" />
        <stop offset="100%" stopColor="var(--color-bg-skeleton-base)" />
        <animateTransform attributeName="gradientTransform" type="translate" from="-1 0" to="1 0" dur="1.5s" repeatCount="indefinite" />
      </linearGradient>
    </defs>
    <polyline
      points="0,120 40,100 80,110 120,60 160,80 200,40 240,70 280,50 300,55"
      fill="none"
      stroke="url(#sk-line-shimmer)"
      strokeWidth="4"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <circle cx="40" cy="100" r="4" fill="url(#sk-line-shimmer)" />
    <circle cx="120" cy="60" r="4" fill="url(#sk-line-shimmer)" />
    <circle cx="200" cy="40" r="4" fill="url(#sk-line-shimmer)" />
  </svg>
);

const pieSkeleton = (): React.ReactNode => (
  <svg width="60%" height="60%" viewBox="0 0 100 100" style={{ position: 'absolute' }}>
    <defs>
      <linearGradient id="sk-pie-shimmer" x1="0" x2="1" y1="0" y2="0">
        <stop offset="0%" stopColor="var(--color-bg-skeleton-base)" />
        <stop offset="50%" stopColor="var(--color-bg-skeleton-highlight)" />
        <stop offset="100%" stopColor="var(--color-bg-skeleton-base)" />
        <animateTransform attributeName="gradientTransform" type="translate" from="-1 0" to="1 0" dur="1.5s" repeatCount="indefinite" />
      </linearGradient>
    </defs>
    <circle cx="50" cy="50" r="40" fill="none" stroke="url(#sk-pie-shimmer)" strokeWidth="8" />
    <line x1="50" y1="50" x2="50" y2="10" stroke="url(#sk-pie-shimmer)" strokeWidth="3" />
    <line x1="50" y1="50" x2="90" y2="50" stroke="url(#sk-pie-shimmer)" strokeWidth="3" />
    <line x1="50" y1="50" x2="30" y2="78" stroke="url(#sk-pie-shimmer)" strokeWidth="3" />
  </svg>
);

const radialSkeleton = (): React.ReactNode => (
  <svg width="60%" height="60%" viewBox="0 0 120 120" style={{ position: 'absolute' }}>
    <defs>
      <linearGradient id="sk-radial-shimmer" x1="0" x2="1" y1="0" y2="0">
        <stop offset="0%" stopColor="var(--color-bg-skeleton-base)" />
        <stop offset="50%" stopColor="var(--color-bg-skeleton-highlight)" />
        <stop offset="100%" stopColor="var(--color-bg-skeleton-base)" />
        <animateTransform attributeName="gradientTransform" type="translate" from="-1 0" to="1 0" dur="1.5s" repeatCount="indefinite" />
      </linearGradient>
    </defs>
    <circle cx="60" cy="60" r="50" fill="none" stroke="url(#sk-radial-shimmer)" strokeWidth="8" />
    <path d="M60 10 A 50 50 0 1 1 30 90" fill="none" stroke="url(#sk-radial-shimmer)" strokeWidth="6" strokeLinecap="round" />
    <text x="60" y="65" textAnchor="middle" fontSize="18" fontWeight="bold" fill="var(--color-bg-skeleton-base)">
      75%
    </text>
  </svg>
);

export const ChartSkeleton: React.FC<ChartSkeletonProps> = ({
  type = 'bar',
  width = '100%',
  height = 400,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    position: 'relative',
    display: 'flex',
    alignItems: 'flex-end',
    justifyContent: 'space-evenly',
    gap: 'var(--space-2)',
    width,
    height,
    padding: 'var(--space-4)',
    overflow: 'hidden',
    ...style,
  };

  const renderSkeleton = () => {
    switch (type) {
      case 'bar':
        return barSkeleton(8);
      case 'line':
        return lineSkeleton();
      case 'pie':
        return pieSkeleton();
      case 'radial':
        return radialSkeleton();
    }
  };

  return (
    <div className={className} style={containerStyle} role="status" aria-label="Loading chart">
      <style>{`@keyframes sk-shimmer { 0% { background-position: -200% 0; } 100% { background-position: 200% 0; } }`}</style>
      {renderSkeleton()}
    </div>
  );
};

ChartSkeleton.displayName = 'ChartSkeleton';
export default ChartSkeleton;
