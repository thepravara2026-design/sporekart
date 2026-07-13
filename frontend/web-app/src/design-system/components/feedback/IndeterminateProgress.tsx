import React from 'react';

export interface IndeterminateProgressProps {
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap = { sm: 4, md: 8, lg: 12 };

const keyframes = `
  @keyframes indeterminateMarquee {
    0% { transform: translateX(-100%); }
    100% { transform: translateX(400%); }
  }
  @keyframes indeterminateSecondary {
    0% { transform: translateX(-400%); }
    100% { transform: translateX(100%); }
  }
`;

export const IndeterminateProgress: React.FC<IndeterminateProgressProps> = ({
  size = 'md',
  className = '',
  style,
}) => {
  const height = sizeMap[size];

  const trackStyle: React.CSSProperties = {
    width: '100%',
    height,
    backgroundColor: 'var(--color-bg-skeleton-base)',
    borderRadius: 'var(--radius-xs)',
    overflow: 'hidden',
    position: 'relative',
    ...style,
  };

  const barStyle: React.CSSProperties = {
    height: '100%',
    width: '25%',
    borderRadius: 'var(--radius-xs)',
    backgroundColor: 'var(--color-bg-primary-default)',
    animation: `indeterminateMarquee var(--duration-slower) var(--easing-standard) infinite`,
  };

  return (
    <div
      className={`sk-indeterminate-progress ${className}`.trim()}
      role="progressbar"
      aria-label="Loading"
      aria-valuetext="indeterminate"
      style={trackStyle}
    >
      <style>{keyframes}</style>
      <div style={barStyle} />
    </div>
  );
};

IndeterminateProgress.displayName = 'IndeterminateProgress';
export default IndeterminateProgress;
