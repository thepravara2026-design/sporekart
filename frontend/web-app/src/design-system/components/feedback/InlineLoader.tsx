import React from 'react';

export interface InlineLoaderProps {
  size?: 'sm' | 'md';
  label?: string;
  className?: string;
  style?: React.CSSProperties;
}

const iconSizeMap = { sm: 16, md: 24 };

const spinKeyframes = `
  @keyframes sk-spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
`;

export const InlineLoader: React.FC<InlineLoaderProps> = ({
  size = 'sm',
  label,
  className = '',
  style,
}) => {
  const iconSize = iconSizeMap[size];

  const containerStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    fontSize: size === 'sm' ? 'var(--text-body-sm)' : 'var(--text-body)',
    color: 'var(--color-text-secondary)',
    ...style,
  };

  const spinnerStyle: React.CSSProperties = {
    width: iconSize,
    height: iconSize,
    border: '2px solid var(--color-bg-skeleton-base)',
    borderTopColor: 'var(--color-bg-primary-default)',
    borderRadius: 'var(--radius-full)',
    animation: 'sk-spin 0.8s linear infinite',
    flexShrink: 0,
  };

  return (
    <span
      className={`sk-inline-loader sk-inline-loader--${size} ${className}`.trim()}
      style={containerStyle}
      role="status"
      aria-label={label || 'Loading'}
    >
      <style>{spinKeyframes}</style>
      <span style={spinnerStyle} aria-hidden="true" />
      {label && <span>{label}</span>}
    </span>
  );
};

InlineLoader.displayName = 'InlineLoader';
export default InlineLoader;
