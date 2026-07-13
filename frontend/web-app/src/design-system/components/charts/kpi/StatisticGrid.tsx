import React from 'react';

export interface StatisticGridItem {
  title: string;
  value: string | number;
  subtitle?: string;
  icon?: React.ReactNode;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  color?: string;
}

export interface StatisticGridProps {
  items: StatisticGridItem[];
  columns?: 2 | 3 | 4;
  className?: string;
  style?: React.CSSProperties;
}

const gridColsMap: Record<number, string> = {
  2: 'repeat(2, 1fr)',
  3: 'repeat(3, 1fr)',
  4: 'repeat(4, 1fr)',
};

const accentColorMap: Record<string, string> = {
  up: 'var(--color-icon-success)',
  down: 'var(--color-icon-danger)',
  flat: 'var(--color-icon-default)',
};

export const StatisticGrid: React.FC<StatisticGridProps> = ({
  items,
  columns = 3,
  className = '',
  style,
}) => {
  return (
    <div
      className={`sk-statistic-grid ${className}`.trim()}
      style={{
        display: 'grid',
        gridTemplateColumns: gridColsMap[columns],
        gap: 'var(--space-component-gap)',
        ...style,
      }}
    >
      {items.map((item, index) => {
        const accentColor = item.color || (item.trend ? accentColorMap[item.trend] : 'var(--color-bg-primary-default)');

        return (
          <div
            key={index}
            className="sk-statistic-grid__item"
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: 'var(--space-2)',
              padding: 'var(--space-4)',
              background: 'var(--color-bg-surface-default)',
              borderRadius: 'var(--radius-card)',
              borderLeft: `4px solid ${accentColor}`,
              boxShadow: 'var(--shadow-1)',
              transition: 'box-shadow var(--duration-fast) var(--easing-ease-out), transform var(--duration-fast) var(--easing-ease-out)',
            }}
          >
            <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
                {item.title}
              </span>
              {item.icon && (
                <span style={{ color: 'var(--color-icon-default)', flexShrink: 0, lineHeight: 0 }}>
                  {item.icon}
                </span>
              )}
            </div>
            <div style={{ display: 'flex', alignItems: 'baseline', gap: 'var(--space-2)' }}>
              <span
                style={{
                  fontSize: 'var(--text-h2)',
                  fontWeight: 'var(--weight-bold)',
                  color: 'var(--color-text-primary)',
                  lineHeight: 1.15,
                  fontVariantNumeric: 'tabular-nums',
                }}
              >
                {item.value}
              </span>
              {item.trend && (
                <span
                  style={{
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: 2,
                    color: accentColor,
                    fontSize: 'var(--text-body-sm)',
                    fontWeight: 'var(--weight-medium)',
                  }}
                >
                  {item.trend === 'up' && (
                    <svg width={14} height={14} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                      <path d="M8 2l6 6h-4v6H6V8H2l6-6z" />
                    </svg>
                  )}
                  {item.trend === 'down' && (
                    <svg width={14} height={14} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                      <path d="M8 14l6-6h-4V2H6v6H2l6 6z" />
                    </svg>
                  )}
                  {item.trend === 'flat' && (
                    <svg width={14} height={14} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                      <path d="M2 7h12v2H2V7z" />
                    </svg>
                  )}
                  {item.trendValue && <span>{item.trendValue}</span>}
                </span>
              )}
            </div>
            {item.subtitle && (
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: 1.35 }}>
                {item.subtitle}
              </span>
            )}
          </div>
        );
      })}
    </div>
  );
};

StatisticGrid.displayName = 'StatisticGrid';
export default StatisticGrid;
