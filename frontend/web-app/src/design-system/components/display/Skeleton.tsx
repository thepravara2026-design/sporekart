import React from 'react';

export interface SkeletonProps {
  variant?: 'text' | 'circular' | 'rectangular' | 'rounded';
  width?: string | number;
  height?: string | number;
  className?: string;
  animation?: 'pulse' | 'wave';
}

const keyframes = `
  @keyframes skeletonPulse {
    0%, 100% { opacity: 0.4; }
    50% { opacity: 0.8; }
  }
  @keyframes skeletonWave {
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
  }
`;

function toPx(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const Skeleton: React.FC<SkeletonProps> = ({
  variant = 'text',
  width,
  height,
  className = '',
  animation = 'pulse',
}) => {
  const baseStyle: React.CSSProperties = {
    backgroundColor: 'var(--color-bg-skeleton-base)',
    display: 'inline-block',
    lineHeight: 0,
    overflow: 'hidden',
  };

  const variantStyle = (): React.CSSProperties => {
    switch (variant) {
      case 'text':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '1em'),
          borderRadius: 'var(--radius-sm)',
          verticalAlign: 'middle',
        };
      case 'circular':
        return {
          width: toPx(width, '40px'),
          height: toPx(height, '40px'),
          borderRadius: 'var(--radius-full)',
          flexShrink: 0,
        };
      case 'rectangular':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '100px'),
          borderRadius: 0,
        };
      case 'rounded':
        return {
          width: toPx(width, '100%'),
          height: toPx(height, '40px'),
          borderRadius: 'var(--radius-md)',
        };
    }
  };

  const animStyle: React.CSSProperties =
    animation === 'pulse'
      ? { animation: 'skeletonPulse 1.5s ease-in-out infinite' }
      : {
          animation: 'skeletonWave 1.5s ease-in-out infinite',
          backgroundImage: 'linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-shine) 50%, var(--color-bg-skeleton-base) 75%)',
          backgroundSize: '200% 100%',
        };

  const style: React.CSSProperties = { ...baseStyle, ...variantStyle(), ...animStyle };

  return (
    <span
      className={`sk-skeleton sk-skeleton--${variant} ${className}`.trim()}
      style={style}
      aria-hidden="true"
    >
      <style dangerouslySetInnerHTML={{ __html: keyframes }} />
    </span>
  );
};

Skeleton.displayName = 'Skeleton';
export default Skeleton;
