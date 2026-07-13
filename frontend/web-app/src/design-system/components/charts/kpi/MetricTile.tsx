import React from 'react';

export interface MetricTileProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon?: React.ReactNode;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  color?: string;
  background?: string;
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  onClick?: () => void;
  className?: string;
  style?: React.CSSProperties;
}

const sizeValueMap: Record<string, { fontSize: string }> = {
  sm: { fontSize: 'var(--text-h4)' },
  md: { fontSize: 'var(--text-h2)' },
  lg: { fontSize: 'var(--text-h1)' },
};

const sizeSubtitleMap: Record<string, { fontSize: string }> = {
  sm: { fontSize: 'var(--text-caption)' },
  md: { fontSize: 'var(--text-body-sm)' },
  lg: { fontSize: 'var(--text-body)' },
};

const TrendArrow: React.FC<{ direction: 'up' | 'down' | 'flat'; size: number }> = ({ direction, size }) => {
  if (direction === 'up') {
    return (
      <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
        <path d="M8 2l6 6h-4v6H6V8H2l6-6z" />
      </svg>
    );
  }
  if (direction === 'down') {
    return (
      <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
        <path d="M8 14l6-6h-4V2H6v6H2l6 6z" />
      </svg>
    );
  }
  return (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
      <path d="M2 7h12v2H2V7z" />
    </svg>
  );
};

export const MetricTile: React.FC<MetricTileProps> = ({
  title,
  value,
  subtitle,
  icon,
  trend,
  trendValue,
  color,
  background,
  size = 'md',
  loading = false,
  onClick,
  className = '',
  style,
}) => {
  const valueStyle = sizeValueMap[size];
  const subtitleStyle = sizeSubtitleMap[size];
  const accentColor = color || 'var(--color-bg-primary-default)';

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-2)',
    padding: size === 'sm' ? 'var(--space-3)' : 'var(--space-4)',
    background: background || 'var(--color-bg-surface-default)',
    borderRadius: 'var(--radius-card)',
    borderLeft: `4px solid ${accentColor}`,
    boxShadow: 'var(--shadow-1)',
    cursor: onClick ? 'pointer' : undefined,
    transition: 'box-shadow var(--duration-fast) var(--easing-ease-out), transform var(--duration-fast) var(--easing-ease-out)',
    position: 'relative',
    overflow: 'hidden',
    ...style,
  };

  const shimmerKeyframes = `
    @keyframes sk-tile-shimmer {
      0% { background-position: -200% 0; }
      100% { background-position: 200% 0; }
    }
  `;

  const skeletonStyle: React.CSSProperties = {
    background: 'linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 75%)',
    backgroundSize: '200% 100%',
    animation: 'sk-tile-shimmer 1.5s ease-in-out infinite',
    borderRadius: 'var(--radius-xs)',
  };

  if (loading) {
    return (
      <div
        className={`sk-metric-tile sk-metric-tile--loading ${className}`.trim()}
        style={containerStyle}
        role="status"
        aria-label={`Loading ${title}`}
      >
        <style>{shimmerKeyframes}</style>
        <div style={{ height: 12, width: '60%', ...skeletonStyle }} />
        <div style={{ height: 28, width: '40%', marginTop: 'var(--space-1)', ...skeletonStyle }} />
        <div style={{ height: 10, width: '80%', marginTop: 'var(--space-1)', ...skeletonStyle }} />
      </div>
    );
  }

  const trendColor = trend === 'up'
    ? 'var(--color-icon-success)'
    : trend === 'down'
      ? 'var(--color-icon-danger)'
      : 'var(--color-icon-default)';

  return (
    <div
      className={`sk-metric-tile ${className}`.trim()}
      style={containerStyle}
      onClick={onClick}
      role={onClick ? 'button' : undefined}
      tabIndex={onClick ? 0 : undefined}
      onKeyDown={onClick ? (e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); onClick(); } } : undefined}
    >
      <style>{`
        .sk-metric-tile:hover {
          box-shadow: var(--shadow-2);
          transform: translateY(-1px);
        }
      `}</style>
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between' }}>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
          {title}
        </span>
        {icon && (
          <span style={{ color: 'var(--color-icon-default)', flexShrink: 0, lineHeight: 0 }}>
            {icon}
          </span>
        )}
      </div>
      <div style={{ display: 'flex', alignItems: 'baseline', gap: 'var(--space-2)' }}>
        <span
          style={{
            ...valueStyle,
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
            lineHeight: 1.15,
            fontVariantNumeric: 'tabular-nums',
          }}
        >
          {value}
        </span>
        {trend && (
          <span
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 2,
              color: trendColor,
              fontSize: 'var(--text-body-sm)',
              fontWeight: 'var(--weight-medium)',
            }}
          >
            <TrendArrow direction={trend} size={14} />
            {trendValue && <span>{trendValue}</span>}
          </span>
        )}
      </div>
      {subtitle && (
        <span style={{ ...subtitleStyle, color: 'var(--color-text-secondary)', lineHeight: 1.35 }}>
          {subtitle}
        </span>
      )}
    </div>
  );
};

MetricTile.displayName = 'MetricTile';
export default MetricTile;
