import React from 'react';

export interface ShimmerLoaderProps {
  width?: string | number;
  height?: string | number;
  borderRadius?: string;
  count?: number;
  gap?: string;
  variant?: 'text' | 'circle' | 'rect' | 'card';
  className?: string;
  style?: React.CSSProperties;
}

const keyframes = `
  @keyframes shimmerSlide {
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
  }
`;

function toPx(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const ShimmerLoader: React.FC<ShimmerLoaderProps> = ({
  width,
  height,
  borderRadius,
  count = 1,
  gap,
  variant = 'text',
  className = '',
  style,
}) => {
  const baseShimmerStyle = (): React.CSSProperties => ({
    background: `linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 75%)`,
    backgroundSize: '200% 100%',
    animation: `shimmerSlide var(--duration-slower) var(--easing-standard) infinite`,
  });

  const variantStyle = (): React.CSSProperties => {
    switch (variant) {
      case 'text':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '14px'),
          borderRadius: borderRadius || 'var(--radius-sm)',
        };
      case 'circle':
        return {
          width: toPx(width, '40px'),
          height: toPx(height, '40px'),
          borderRadius: borderRadius || 'var(--radius-full)',
          flexShrink: 0,
        };
      case 'rect':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '100px'),
          borderRadius: borderRadius || 'var(--radius-sm)',
        };
      case 'card':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '160px'),
          borderRadius: borderRadius || 'var(--radius-card)',
          border: '1px solid var(--color-border-default)',
        };
    }
  };

  const items = Array.from({ length: count });
  const gapValue = gap || 'var(--space-stack-sm)';

  if (count === 1) {
    const mergedStyle: React.CSSProperties = {
      ...baseShimmerStyle(),
      ...variantStyle(),
      ...style,
    };
    return (
      <div
        className={`sk-shimmer-loader sk-shimmer-loader--${variant} ${className}`.trim()}
        style={mergedStyle}
        aria-hidden="true"
      >
        <style>{keyframes}</style>
      </div>
    );
  }

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: gapValue,
    ...style,
  };

  return (
    <div className={`sk-shimmer-loader sk-shimmer-loader--${variant} ${className}`.trim()} style={containerStyle} aria-hidden="true">
      <style>{keyframes}</style>
      {items.map((_, i) => (
        <div
          key={i}
          style={{
            ...baseShimmerStyle(),
            ...variantStyle(),
          }}
        />
      ))}
    </div>
  );
};

ShimmerLoader.displayName = 'ShimmerLoader';
export default ShimmerLoader;
