import React from 'react';

export interface LinearProgressProps {
  value?: number;
  size?: 'sm' | 'md' | 'lg';
  color?: 'primary' | 'success' | 'warning' | 'danger';
  label?: string;
  showValue?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap = { sm: 4, md: 8, lg: 12 };
const colorMap = {
  primary: 'var(--color-bg-primary-default)',
  success: 'var(--color-success-500)',
  warning: 'var(--color-warning-500)',
  danger: 'var(--color-danger-500)',
};

const keyframes = `
  @keyframes linearProgressIndeterminate {
    0% { transform: translateX(-100%); }
    50% { transform: translateX(0%); }
    100% { transform: translateX(100%); }
  }
  @keyframes linearProgressBuffer {
    0% { transform: translateX(-100%); }
    100% { transform: translateX(400%); }
  }
`;

export const LinearProgress: React.FC<LinearProgressProps> = ({
  value,
  size = 'md',
  color = 'primary',
  label,
  showValue = false,
  className = '',
  style,
}) => {
  const isIndeterminate = value === undefined || value === null;
  const height = sizeMap[size];
  const fillColor = colorMap[color];

  const trackStyle: React.CSSProperties = {
    width: '100%',
    height,
    backgroundColor: 'var(--color-bg-skeleton-base)',
    borderRadius: 'var(--radius-xs)',
    overflow: 'hidden',
    position: 'relative',
    ...style,
  };

  const fillBase: React.CSSProperties = {
    height: '100%',
    borderRadius: 'var(--radius-xs)',
    backgroundColor: fillColor,
    transition: `width var(--duration-slower) var(--easing-standard)`,
  };

  const fillStyle: React.CSSProperties = isIndeterminate
    ? {
        ...fillBase,
        width: '50%',
        animation: `linearProgressIndeterminate var(--duration-slower) var(--easing-standard) infinite`,
      }
    : {
        ...fillBase,
        width: `${Math.max(0, Math.min(100, value))}%`,
      };

  const labelRowStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 'var(--space-stack-xs)',
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
  };

  return (
    <div
      className={`sk-linear-progress ${className}`.trim()}
      role="progressbar"
      aria-valuenow={isIndeterminate ? undefined : value}
      aria-valuemin={0}
      aria-valuemax={100}
      aria-label={label || 'Progress'}
      aria-valuetext={isIndeterminate ? 'indeterminate' : undefined}
    >
      <style>{keyframes}</style>
      {(label || showValue) && !isIndeterminate && (
        <div style={labelRowStyle}>
          {label && <span>{label}</span>}
          {showValue && <span>{Math.round(value!)}%</span>}
        </div>
      )}
      <div style={trackStyle}>
        <div style={fillStyle} />
      </div>
    </div>
  );
};

LinearProgress.displayName = 'LinearProgress';
export default LinearProgress;
